package com.hci.electric.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.warehouse.EditQuantityWarehouseRequest;
import com.hci.electric.dtos.warehouse.EditWarehouseResponse;
import com.hci.electric.middlewares.Jwt;
import com.hci.electric.models.Account;
import com.hci.electric.models.Warehouse;
import com.hci.electric.models.WarehouseHistory;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.WarehouseHistoryService;
import com.hci.electric.services.WarehouseService;
import com.hci.electric.utils.Enums;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final WarehouseHistoryService warehouseHistoryService;
    private final Jwt jwt;
    private final AccountService accountService;

    public WarehouseController(WarehouseService warehouseService, WarehouseHistoryService warehouseHistoryService, AccountService accountService){
        this.warehouseHistoryService = warehouseHistoryService;
        this.warehouseService = warehouseService;
        this.accountService = accountService;
        this.jwt = new Jwt();
    }

    @PutMapping("/api")
    public ResponseEntity<EditWarehouseResponse> editQuantityWarehouse(@RequestBody EditQuantityWarehouseRequest request, HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization");
        if(accessToken.startsWith("Bearer ") == false){
            return ResponseEntity.status(400).body(new EditWarehouseResponse(false, "Invalid Token", null));
        }

        String accountId = this.jwt.extractAccountId(accessToken.split(" ")[1]);
        if (accountId == null){
            return ResponseEntity.status(400).body(new EditWarehouseResponse(false, "Invalid Token", null));
        }

        Account account = this.accountService.getById(accountId);
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
        this.warehouseHistoryService.save(record);
        return ResponseEntity.status(200).body(new EditWarehouseResponse(true, "Saved", savedWarehouse));
    }
}
