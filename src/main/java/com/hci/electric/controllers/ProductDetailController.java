package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.productDetail.AddProductItemRequest;
import com.hci.electric.dtos.productDetail.AddProductRequest;
import com.hci.electric.dtos.productDetail.DetailItem;
import com.hci.electric.dtos.productDetail.EditProductDetailRequest;
import com.hci.electric.dtos.productDetail.PaginateProductDetail;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Discount;
import com.hci.electric.models.Distributor;
import com.hci.electric.models.Product;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.ProductImage;
import com.hci.electric.models.Warehouse;
import com.hci.electric.models.WarehouseHistory;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.DiscountService;
import com.hci.electric.services.DistributorService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.ProductImageService;
import com.hci.electric.services.ProductService;
import com.hci.electric.services.WarehouseHistoryService;
import com.hci.electric.services.WarehouseService;

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

    public ProductDetailController(ProductDetailService productDetailService, AccountService accountService, DistributorService distributorService, DiscountService discountService, WarehouseService warehouseService, ProductService productService, ProductImageService productImageService, WarehouseHistoryService historyService){
        this.productDetailService = productDetailService;
        this.accountService = accountService;
        this.productService = productService;
        this.discountService = discountService;
        this.warehouseService = warehouseService;
        this.distributorService = distributorService;
        this.modelMapper = new ModelMapper();
        this.productImageService = productImageService;
        this.historyService = historyService;
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
    public ResponseEntity<List<ProductDetail>> saveDetailItem(@RequestBody AddProductItemRequest request){
        Product checkProduct = this.productService.getById(request.getProductId());
        if (checkProduct == null){
            return ResponseEntity.status(400).body(null);
        }
        ProductDetail productDetail = this.modelMapper.map(request, ProductDetail.class);
        List<ProductDetail> items = new ArrayList<>();
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
            
            items.add(savedItem);
        }
        return ResponseEntity.status(200).body(items);
    }


    @GetMapping("/show")
    public ResponseEntity<PaginateProductDetail> getProductDetails(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer num){
        if (page == null){
            page = 1;
        }

        int totalProducts = this.productDetailService.getAll().size();
        if(num == null){
            num = totalProducts;
        }

        int totalPage = totalProducts/num;
        if (totalProducts%num !=0){
            totalPage++;
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
    public ResponseEntity<ProductDetail> editProductDetail(@RequestBody EditProductDetailRequest request){
        ProductDetail item = this.productDetailService.getById(request.getProductId());
        if (item == null){
            return ResponseEntity.status(400).body(null);
        }

        for (String image : request.getImages()) {
            ProductImage imageItem = new ProductImage();
            imageItem.setLink(image);
            this.productImageService.save(imageItem);
        }

        Discount discount = this.discountService.getByProductId(item.getId());
        if (request.getDiscount() < 0 || request.getDiscount() > 100){
            return ResponseEntity.status(400).body(null);
        }

        discount.setValue(request.getDiscount());
        this.discountService.save(discount);

        Warehouse warehouse = this.warehouseService.getByProductId(item.getId());
        if (warehouse.getQuantity() != request.getQuantity()){
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
        
        return ResponseEntity.status(200).body(item);
    }
}
