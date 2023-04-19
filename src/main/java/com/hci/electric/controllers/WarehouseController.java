package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.warehouse.EditQuantityWarehouseRequest;
import com.hci.electric.dtos.warehouse.EditWarehouseResponse;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.Warehouse;
import com.hci.electric.models.WarehouseHistory;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.WarehouseHistoryService;
import com.hci.electric.services.WarehouseService;
import com.hci.electric.utils.Enums;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final WarehouseHistoryService warehouseHistoryService;
    private final AccountService accountService;
    private final ProductDetailService productDetailService;
    private final Auth auth;

    public WarehouseController(WarehouseService warehouseService, WarehouseHistoryService warehouseHistoryService, AccountService accountService, ProductDetailService productDetailService){
        this.warehouseHistoryService = warehouseHistoryService;
        this.warehouseService = warehouseService;
        this.accountService = accountService;
        this.productDetailService = productDetailService;
        this.auth = new Auth(this.accountService);
    }

    @PutMapping("/api")
    public ResponseEntity<EditWarehouseResponse> editQuantityWarehouse(@RequestBody EditQuantityWarehouseRequest request, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        
        Account account = this.auth.checkToken(accessToken);
        if (account == null || account.getRole().equals(Enums.RoleAccount.ADMIN.toString().toLowerCase()) == false){
            return ResponseEntity.status(403).body(new EditWarehouseResponse(false, "Forbidden", null));
        }
        Warehouse warehouse = this.warehouseService.getByProductId(request.getProductId());
        WarehouseHistory record = new WarehouseHistory();
        record.setWarehouseId(warehouse.getId());

        String type = request.getType();

        if(type.equals(Enums.TypeWarehouse.PLUS.toString().toLowerCase())){
            warehouse.setQuantity(warehouse.getQuantity() + request.getQuantity());
        }
        else if (type.equals(Enums.TypeWarehouse.MINUS.toString().toLowerCase())){
            if (warehouse.getQuantity() < request.getQuantity()){
                return ResponseEntity.status(400).body(new EditWarehouseResponse(false, "Not Enough", null));
            }
            warehouse.setQuantity(warehouse.getQuantity() - request.getQuantity());
        }
        else if (type.equals(Enums.TypeWarehouse.EDIT.toString().toLowerCase())){
            if (warehouse.getQuantity() > request.getQuantity()){
                type = Enums.TypeWarehouse.MINUS.toString().toLowerCase();
            }
            else if (warehouse.getQuantity() < request.getQuantity()){
                type = Enums.TypeWarehouse.PLUS.toString().toLowerCase();
            }
            else{
                return ResponseEntity.status(400).body(new EditWarehouseResponse(false, "Not Edit", null));
            }
            warehouse.setQuantity(request.getQuantity());
            request.setQuantity(Math.abs(warehouse.getQuantity() - request.getQuantity()));
        }
        else{
            return ResponseEntity.status(400).body(new EditWarehouseResponse(false, "Invalid Method", null));
        }
        record.setType(type);
        record.setQuantity(request.getQuantity());
        Warehouse savedWarehouse = this.warehouseService.save(warehouse);
        if (savedWarehouse == null){
            return ResponseEntity.status(500).body(new EditWarehouseResponse(false, "Internel Error Server", savedWarehouse));
        }
        this.warehouseHistoryService.save(record);
        return ResponseEntity.status(200).body(new EditWarehouseResponse(true, "Saved", savedWarehouse));
    }

    @GetMapping("/show")
    public ResponseEntity<List<Warehouse>> paginageWarehouses(@RequestParam(required = false) Integer num, @RequestParam(required = false) Integer page){
        if(page == null){
            page = 0;
        }

        int totalItems = this.warehouseService.getAll().size();

        if (num == null){
            num = totalItems;
        }

        /* int totalPages = totalItems/num;
        if (totalItems%num != 0){
            totalPages++;
        } */

        List<Warehouse> warehouses = this.warehouseService.paginateWarehouse(page, num);

        if (warehouses == null){
            return ResponseEntity.status(500).body(new ArrayList<>());
        }

        return ResponseEntity.status(200).body(warehouses);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<List<WarehouseHistory>> paginateHistory(@PathVariable("id") String productId, @RequestParam(required = false) Boolean descending){
        ProductDetail item = this.productDetailService.getById(productId);

        if (item == null){
            return ResponseEntity.status(400).body(null);
        }

        if (descending == null){
            descending = true;
        }

        Warehouse warehouse = this.warehouseService.getByProductId(item.getId());
        List<WarehouseHistory> histories = this.warehouseHistoryService.getByWarehouse(warehouse.getId(), descending);
        if (histories == null){
            return ResponseEntity.status(500).body(new ArrayList<>());
        }
        return ResponseEntity.status(200).body(histories);
    }
}
