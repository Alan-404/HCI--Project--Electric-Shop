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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algolia.search.DefaultSearchClient;
import com.algolia.search.SearchClient;
import com.algolia.search.SearchIndex;
import com.hci.electric.dtos.productDetail.AddProductItemRequest;
import com.hci.electric.dtos.productDetail.AddProductRequest;
import com.hci.electric.dtos.productDetail.DetailItem;
import com.hci.electric.dtos.productDetail.EditProductDetailRequest;
import com.hci.electric.dtos.productDetail.PaginateProductDetail;
import com.hci.electric.dtos.productDetail.ProductDetailResponse;
import com.hci.electric.dtos.productDetail.SameOriginProduct;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Category;
import com.hci.electric.models.Discount;
import com.hci.electric.models.Distributor;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductCategory;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.ProductImage;
import com.hci.electric.models.Warehouse;
import com.hci.electric.models.WarehouseHistory;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CategoryService;
import com.hci.electric.services.DiscountService;
import com.hci.electric.services.DistributorService;
import com.hci.electric.services.ProductCategoryService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductImageService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.WarehouseHistoryService;
import com.hci.electric.services.WarehouseService;
import com.hci.electric.utils.Color;

@RestController
@RequestMapping("/detail")
public class ProductDetailController {
    private final ProductDetailService productDetailService;
    private final AccountService accountService;
    private final DistributorService distributorService;
    private final DiscountService discountService;
    private final WarehouseService warehouseService;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final Auth auth;
    private final WarehouseHistoryService historyService;
    private final ProductImageService productImageService;
    private final CategoryService categoryService;
    private final ProductCategoryService productCategoryService;

    @Value("${algolia.app_id}")
    private String algoliaAppId;

    @Value("${algolia.api_key}")
    private String algoliaApiKey;

    public ProductDetailController(
        ProductDetailService productDetailService,
        AccountService accountService,
        DistributorService distributorService,
        DiscountService discountService,
        WarehouseService warehouseService,
        ProductService productService,
        ProductImageService productImageService,
        WarehouseHistoryService historyService,
        CategoryService categoryService,
        ProductCategoryService productCategoryService) {
        
        this.productDetailService = productDetailService;
        this.accountService = accountService;
        this.productService = productService;
        this.discountService = discountService;
        this.warehouseService = warehouseService;
        this.distributorService = distributorService;
        this.modelMapper = new ModelMapper();
        this.productImageService = productImageService;
        this.historyService = historyService;
        this.categoryService = categoryService;
        this.productCategoryService = productCategoryService;
        this.auth = new Auth(this.accountService);
    }

    @PostMapping("/add")
    public ResponseEntity<Product> addDetailOfProducts(@RequestBody AddProductRequest request, HttpServletRequest httpServletRequest){
        
        String token = httpServletRequest.getHeader("Authorization");

        Account account = this.auth.checkToken(token);
        if (account == null){
            return ResponseEntity.status(400).body(null);
        }

        Distributor distributor = this.distributorService.getByUserId(account.getUserId());
        if (distributor == null){
            return ResponseEntity.status(400).body(null);
        }


        Product product = this.productService.getById(request.getProductId());
        if (product == null){
            return ResponseEntity.status(400).body(null);
        }

        for (int i=0; i<request.getProducts().size(); i++){
            ProductDetail item = request.getProducts().get(i);
            item.setProductId(request.getProductId());
            ProductDetail savedItem = this.productDetailService.save(item);

            
            Warehouse warehouse = new Warehouse();
            warehouse.setProductId(savedItem.getId());
            warehouse.setQuantity(request.getQuantities().get(i));
            this.warehouseService.save(warehouse);


            Discount discount = new Discount();
            discount.setProductId(savedItem.getId());
            discount.setValue(request.getDiscounts().get(i));
            this.discountService.save(discount);
        }

        return ResponseEntity.status(200).body(product);
    }

    @PostMapping("/save")
    public ResponseEntity<List<DetailItem>> saveDetailItem(@RequestBody AddProductItemRequest request){
        Product checkProduct = this.productService.getById(request.getProductId());
        if (checkProduct == null){
            return ResponseEntity.status(400).body(null);
        }
        ProductDetail productDetail = this.modelMapper.map(request, ProductDetail.class);
        List<DetailItem> items = new ArrayList<>();
        for (Integer color : request.getColors()) {
            productDetail.setColor(color);
            ProductDetail savedItem =  this.productDetailService.save(productDetail);
            
            Warehouse warehouse = new Warehouse();
            warehouse.setProductId(savedItem.getId());
            warehouse.setQuantity(0);
            this.warehouseService.save(warehouse);

            Discount discount = new Discount();
            discount.setProductId(savedItem.getId());
            discount.setValue(0.0);
            this.discountService.save(discount);
            
            DetailItem detailItem = this.modelMapper.map(savedItem, DetailItem.class);
            detailItem.setName(checkProduct.getName() + " " + savedItem.getSpecifications());

            items.add(detailItem);
        }
        return ResponseEntity.status(200).body(items);
    }


    @GetMapping("/show")
    public ResponseEntity<PaginateProductDetail> getProductDetails(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer num){
        if (page == null){
            page = 1;
        }

        if(num == null) {
            num = 0;
        }

        int totalProducts = this.productDetailService.getAll().size();
        int totalPage = 0;

        if (totalProducts > 0) {
            num = totalProducts;

            totalPage = totalProducts / num;

            if (totalProducts%num !=0){
                totalPage++;
            }
        }


        List<ProductDetail> items = this.productDetailService.paginate(page, num);

        if (items == null){
            return ResponseEntity.status(500).body(new PaginateProductDetail(new ArrayList<>(), 0));
        }
        
        List<DetailItem> products = new ArrayList<>();
        for (ProductDetail product : items) {
            DetailItem item = this.modelMapper.map(product, DetailItem.class);
            Product orgin = this.productService.getById(product.getProductId());
            Discount discount = this.discountService.getByProductId(product.getId());
            Warehouse warehouse = this.warehouseService.getByProductId(product.getId());
            List<ProductImage> media = this.productImageService.getMediaByProduct(product.getId());
            List<String> links = new ArrayList<>();
            for (ProductImage media_item : media) {
                links.add(media_item.getLink());
            }

            item.setName(orgin.getName() + " " + product.getSpecifications());
            item.setDiscount(discount.getValue());
            item.setWarehouse(warehouse.getQuantity());
            item.setMedia(links);
            products.add(item);
        }

        PaginateProductDetail response = new PaginateProductDetail(products, totalPage);

        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<DetailItem> editProductDetail(@RequestBody EditProductDetailRequest request){
        ProductDetail item = this.productDetailService.getById(request.getId());

        if (item == null) {
            return ResponseEntity.status(400).body(null);
        }

        Product productOrigin = this.productService.getById(request.getProductId());

        if (productOrigin == null) {
            return ResponseEntity.status(400).body(null);
        }

        item.setColor(request.getColor());
        item.setSpecifications(request.getSpecifications());
        item.setPrice(request.getPrice());
        item.setProductId(request.getProductId());

        this.productDetailService.edit(item);

        this.productImageService.deleteAllByProduct(request.getId());

        for (String image : request.getImages()) {
            ProductImage imageItem = new ProductImage();
            imageItem.setLink(image);
            imageItem.setProductId(item.getId());

            this.productImageService.save(imageItem);
        }

        Discount discount = this.discountService.getByProductId(item.getId());

        if (request.getDiscount() < 0 || request.getDiscount() > 100) {
            return ResponseEntity.status(400).body(null);
        }

        discount.setValue(request.getDiscount());
        this.discountService.save(discount);

        Warehouse warehouse = this.warehouseService.getByProductId(item.getId());
        
        if (warehouse.getQuantity() != request.getQuantity()) {
            int delta = warehouse.getQuantity() - request.getQuantity();
            warehouse.setQuantity(request.getQuantity());
            this.warehouseService.edit(warehouse);
            WarehouseHistory history = new WarehouseHistory();
            if(delta<0){
                history.setQuantity(-delta);
                history.setType("PLUS");
            }
            else{
                history.setQuantity(delta);
                history.setType("MINUS");
            }

            this.historyService.save(history);
        }

        DetailItem detailItem = this.modelMapper.map(item, DetailItem.class);
        detailItem.setMedia(request.getImages());
        detailItem.setName(productOrigin.getName() + " " + request.getSpecifications());
        detailItem.setWarehouse(request.getQuantity());
        detailItem.setDiscount(request.getDiscount());


        ProductDetailResponse algoliaResponse = this.modelMapper.map(item, ProductDetailResponse.class);
        Distributor brand = this.distributorService.getById(productOrigin.getDistributorId());
        List<ProductCategory> pcList = this.productCategoryService.getByProductId(productOrigin.getId());
        List<String> categories = new ArrayList<>();

        for (ProductCategory pc : pcList) {
            Category category = this.categoryService.getById(pc.getCategoryId());
            categories.add(category.getName());
        }

        algoliaResponse.setBrand(brand.getName());
        algoliaResponse.setCategories(categories);
        algoliaResponse.setDiscount(discount.getValue());
        algoliaResponse.setImage(request.getImages().get(0));
        algoliaResponse.setName(productOrigin.getName() + " " +
            item.getSpecifications() + " " + Color.COLORS[request.getColor()]);
        algoliaResponse.setObjectID(item.getId());

        SearchClient client = DefaultSearchClient.create(this.algoliaAppId, this.algoliaApiKey);
        SearchIndex<ProductDetailResponse> index = client.initIndex("hci_proj", ProductDetailResponse.class);
        index.saveObject(algoliaResponse);
        
        return ResponseEntity.status(200).body(detailItem);
    }

    @GetMapping("/single/{id}")
    public ResponseEntity<DetailItem> getSingle(@PathVariable("id") String id) {
        if (id == null) {
            return ResponseEntity.status(400).body(null);
        }

        ProductDetail detail = this.productDetailService.getById(id);

        if (detail == null) {
            return ResponseEntity.status(400).body(null);
        }

        Product product = this.productService.getById(detail.getProductId());

        if (product == null) {
            return ResponseEntity.status(400).body(null);
        }

        DetailItem response = this.modelMapper.map(detail, DetailItem.class);
        List<ProductImage> images = this.productImageService.getMediaByProduct(detail.getId());
        Discount discount = this.discountService.getByProductId(detail.getId());
        Warehouse warehouse = this.warehouseService.getByProductId(detail.getId());
        List<ProductDetail> productDetails = this.productDetailService.getByProductId(product.getId());

        List<SameOriginProduct> sameOriginProducts = new ArrayList<>();

        for (ProductDetail pd : productDetails) {   
            List<ProductImage> pdImages = this.productImageService.getMediaByProduct(pd.getId());

            if (pdImages.size() == 0) {
                continue;
            }

            Discount pdDiscount = this.discountService.getByProductId(pd.getId());
            double realPrice = pd.getPrice() - (pd.getPrice() * pdDiscount.getValue() / 100);
            SameOriginProduct sop = this.modelMapper.map(pd, SameOriginProduct.class);

            sop.setRealPrice(realPrice);
            sop.setImage(pdImages.get(0).getLink());

            sameOriginProducts.add(sop);
        }

        List<String> media = new ArrayList<>();

        for (ProductImage image : images) {
            media.add(image.getLink());
        }

        response.setDiscount(discount.getValue());
        response.setWarehouse(warehouse.getQuantity());
        response.setName(product.getName() + " " + detail.getSpecifications());
        response.setMedia(media);
        response.setSameOriginProducts(sameOriginProducts);
        response.setDescription(product.getDescription());
        response.setInformation(product.getInformation());

        return ResponseEntity.status(200).body(response);
    }


    @GetMapping("/algolia")
    public ResponseEntity<String> addRecordsToAlgolia() {
        List<ProductDetail> productDetailList = this.productDetailService.getAll();
        List<ProductDetailResponse> responses = new ArrayList<>();

        for (ProductDetail productDetail : productDetailList) {
            ProductDetailResponse response = this.modelMapper.map(productDetail, ProductDetailResponse.class);
            List<ProductImage> images = this.productImageService.getMediaByProduct(productDetail.getId());

            if (images.size() == 0) {
                continue;
            }

            Product productOrigin = this.productService.getById(productDetail.getProductId());
            Distributor brand = this.distributorService.getById(productOrigin.getDistributorId());
            List<ProductCategory> pcList = this.productCategoryService.getByProductId(productOrigin.getId());
            Discount discount = this.discountService.getByProductId(productDetail.getId());
            
            List<String> categories = new ArrayList<>();

            for (ProductCategory pc : pcList) {
                Category category = this.categoryService.getById(pc.getCategoryId());
                categories.add(category.getName());
            }

            response.setBrand(brand.getName());
            response.setCategories(categories);
            response.setName(productOrigin.getName() + " " + productDetail.getSpecifications()
                + " " + Color.COLORS[productDetail.getColor()]);
            
            response.setDiscount(discount.getValue());
            response.setImage(images.get(0).getLink());
            response.setObjectID(productDetail.getId());
            response.setColor(Color.COLORS[productDetail.getColor()]);
            response.setModel(productOrigin.getName());

            responses.add(response);
        }

        SearchClient client = DefaultSearchClient.create(this.algoliaAppId, this.algoliaApiKey);
        SearchIndex<ProductDetailResponse> index = client.initIndex("hci_proj", ProductDetailResponse.class);
        index.saveObjects(responses);

        return ResponseEntity.status(200).body("Saved objects to algolia");
    }
}
