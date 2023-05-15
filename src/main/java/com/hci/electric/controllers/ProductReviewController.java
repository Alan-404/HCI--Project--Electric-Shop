package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.hci.electric.dtos.productDetail.ProductDetailResponse;
import com.hci.electric.dtos.review.AddReviewRequest;
import com.hci.electric.dtos.review.ProductReviewResponse;
import com.hci.electric.dtos.review.ProductReviewsWithStats;
import com.hci.electric.dtos.review.RatingStat;
import com.hci.electric.dtos.review.ReviewerResponse;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Bill;
import com.hci.electric.models.Category;
import com.hci.electric.models.Discount;
import com.hci.electric.models.Distributor;
import com.hci.electric.models.Order;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductCategory;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.ProductImage;
import com.hci.electric.models.ProductReview;
import com.hci.electric.models.User;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.BillService;
import com.hci.electric.services.CategoryService;
import com.hci.electric.services.DiscountService;
import com.hci.electric.services.DistributorService;
import com.hci.electric.services.OrderService;
import com.hci.electric.services.ProductCategoryService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductImageService;
import com.hci.electric.services.ProductReviewService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.UserService;
import com.hci.electric.utils.Color;

@RestController
@RequestMapping("/review")
public class ProductReviewController {
    private final ProductReviewService productReviewService;
    private final AccountService accountService;
    private final OrderService orderService;
    private final BillService billService;
    private final ProductDetailService productDetailService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final DistributorService brandService;
    private final CategoryService categoryService;
    private final ProductImageService productImageService;
    private final ProductCategoryService productCategoryService;
    private final ProductService productService;
    private final DiscountService discountService;
    private final Auth auth;

    @Value("${algolia.app_id}")
    private String algoliaAppId;

    @Value("${algolia.api_key}")
    private String algoliaApiKey;

    public ProductReviewController(
        ProductReviewService productReviewService,
        AccountService accountService,
        OrderService orderService,
        BillService billService,
        ProductDetailService productDetailService,
        UserService userService,
        ProductService productService,
        DistributorService brandService,
        CategoryService categoryService,
        ProductImageService productImageService,
        ProductCategoryService productCategoryService,
        DiscountService discountService) {
        this.productReviewService = productReviewService;
        this.productDetailService = productDetailService;
        this.accountService = accountService;
        this.billService = billService;
        this.orderService = orderService;
        this.userService = userService;
        this.modelMapper = new ModelMapper();
        this.auth = new Auth(this.accountService);
        this.productService = productService;
        this.productImageService = productImageService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.productCategoryService = productCategoryService;
        this.discountService = discountService;
    }

    @PostMapping("/api")
    public ResponseEntity<ProductReviewResponse> addReview(HttpServletRequest httpServletRequest, @RequestBody AddReviewRequest review){
        String token = httpServletRequest.getHeader("Authorization");
        Account account = this.auth.checkToken(token);

        if (account == null){
            return ResponseEntity.status(400).body(null);
        }

        ProductDetail item;

        if (review.getOrderId() != null) {

            Order order = this.orderService.getById(review.getOrderId());
            if (order == null || order.isReviewed() == true){
                return ResponseEntity.status(400).body(null);
            }
    
    
            Bill bill = this.billService.getById(order.getBillId());
            if (bill ==null || bill.getUserId().equals(account.getUserId()) == false){
                return ResponseEntity.status(400).body(null);
            }
    
            item = this.productDetailService.getById(order.getProductId());

            if (item == null) {
                return ResponseEntity.status(400).body(null);
            }

            this.orderService.saveRecord(order, true);      
        } else {
            item = this.productDetailService.getById(review.getProductId());

            if (item == null) {
                return ResponseEntity.status(400).body(null);
            }

            List<Order> orders = this.orderService.getByProductId(item.getProductId());

            for (Order order : orders) {
                this.orderService.saveRecord(order, true);
            }

        }


        int numberOfReviews = item.getNumReviews() + 1;
        item.setAverageRating((item.getAverageRating() * item.getNumReviews()) / numberOfReviews + review.getStars() / numberOfReviews);
        item.setNumReviews(numberOfReviews);

        ProductReview record = new ProductReview();
        record.setUserId(account.getUserId());
        record.setContent(review.getContent());
        record.setProductId(item.getId());
        record.setStars(review.getStars());
        
        ProductReview savedReview = this.productReviewService.save(record);
        if (savedReview == null) {
            return ResponseEntity.status(500).body(null);
        }

        ProductReviewResponse response = this.modelMapper.map(savedReview, ProductReviewResponse.class);
        User reviewer = this.userService.getById(savedReview.getUserId());
        ReviewerResponse reviewerResponse = this.modelMapper.map(reviewer, ReviewerResponse.class);
        
        response.setReviewer(reviewerResponse);

        ProductDetailResponse algoliaResponse = this.modelMapper.map(item, ProductDetailResponse.class);
        Product productOrigin = this.productService.getById(item.getProductId());
        Discount discount = this.discountService.getByProductId(item.getId());
        Distributor brand = this.brandService.getById(productOrigin.getDistributorId());
        List<ProductCategory> pcList = this.productCategoryService.getByProductId(productOrigin.getId());
        List<String> categories = new ArrayList<>();
        List<ProductImage> images = this.productImageService.getMediaByProduct(item.getId());

        for (ProductCategory pc : pcList) {
            Category category = this.categoryService.getById(pc.getCategoryId());
            categories.add(category.getName());
        }

        algoliaResponse.setBrand(brand.getName());
        algoliaResponse.setCategories(categories);
        algoliaResponse.setDiscount(discount.getValue());
        algoliaResponse.setImage(images.get(0).getLink());
        algoliaResponse.setName(productOrigin.getName() + " " +
            item.getSpecifications() + " " + Color.COLORS[item.getColor()]);
        algoliaResponse.setObjectID(item.getId());
        algoliaResponse.setColor(Color.COLORS[item.getColor()]);

        SearchClient client = DefaultSearchClient.create(this.algoliaAppId, this.algoliaApiKey);
        SearchIndex<ProductDetailResponse> index = client.initIndex("hci_proj", ProductDetailResponse.class);
        index.saveObject(algoliaResponse);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductReviewsWithStats> getReviews(
        @PathVariable("id") String productId) {
        List<ProductReview> reviews = this.productReviewService.getByProduct(productId);
        List<ProductReviewResponse> responses = new ArrayList<>();
        List<RatingStat> stats = new ArrayList<>();

        for (ProductReview review : reviews) {
            ProductReviewResponse response = this.modelMapper.map(review, ProductReviewResponse.class);
            User reviewer = this.userService.getById(review.getUserId());
            ReviewerResponse reviewerResponse = this.modelMapper.map(reviewer, ReviewerResponse.class);
            System.out.println(response.getProductId());
            response.setReviewer(reviewerResponse);
            responses.add(response);
        }

        for (int i = 0; i < 5; i++) {
            int num = this.productReviewService.countRaingByValueAndProductId(i + 1, productId);
            RatingStat stat = new RatingStat(i + 1, num);

            stats.add(stat);
        }

        ProductReviewsWithStats productReviewsWithStats = new ProductReviewsWithStats(responses, stats);

        return ResponseEntity.status(200).body(productReviewsWithStats);
    }
}
