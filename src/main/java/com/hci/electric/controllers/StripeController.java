package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.hci.electric.models.Bill;
import com.hci.electric.models.Order;
import com.hci.electric.models.ProductDetail;
import com.hci.electric.models.Warehouse;
import com.hci.electric.models.WarehouseHistory;
import com.hci.electric.services.BillService;
import com.hci.electric.services.OrderService;
import com.hci.electric.services.ProductDetailService;
import com.hci.electric.services.WarehouseHistoryService;
import com.hci.electric.services.WarehouseService;
import com.hci.electric.utils.Enums;
import com.stripe.Stripe;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Value("${stripe.secret_key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook_key}")
    private String stripeWebhookKey;
    private final BillService billService;
    private final OrderService orderService;
    private final WarehouseService warehouseService;
    private final WarehouseHistoryService warehouseHistoryService;
    private final ProductDetailService productDetailService;

    public StripeController(
        BillService billService,
        OrderService orderService,
        WarehouseService warehouseService,
        WarehouseHistoryService warehouseHistoryService,
        ProductDetailService productDetailService) {
        this.billService = billService;
        this.orderService = orderService;
        this.warehouseService = warehouseService;
        this.warehouseHistoryService = warehouseHistoryService;
        this.productDetailService = productDetailService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(
        @RequestHeader Map<String, String> headers,
        @RequestBody String payload) {
        // String payload = httpEntity.getBody();

        Event event = null;
        
        Stripe.apiKey = stripeSecretKey;
        String sigHeader = headers.get("stripe-signature");
        System.out.println("Signature header...........: " + sigHeader);
        System.out.println("webhook key...............: " + stripeWebhookKey);
        if (stripeWebhookKey != null && sigHeader != null) {
            try {
                event = Webhook.constructEvent(payload, sigHeader, stripeWebhookKey);
            } catch (Exception ex) {
                ex.printStackTrace();
                return ResponseEntity.status(500).body(null);
            }
        }

        

        switch (event.getType()) {
            case "checkout.session.completed":
                System.out.println("Checkout completed event................................");
                EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
                StripeObject stripeObject = null;
                
                if (dataObjectDeserializer.getObject().isPresent()) {
                    stripeObject = dataObjectDeserializer.getObject().get();
                } else {
                    // Deserialization failed, probably due to an API version mismatch.
                    // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
                    // instructions on how to handle this case, or return an error here.
                }
                
                Session session = (Session) stripeObject;
                
                String billId = session.getMetadata().get("billId");
                
                Bill bill = this.billService.getById(billId);
                
                if (bill != null) {
                    List<Order> orders = this.orderService.getByBillId(billId);

                    for (Order order : orders) {
                        Warehouse warehouse = this.warehouseService.getByProductId(order.getProductId());
                        warehouseHandle(warehouse, order.getQuantity());
                        ProductDetail productDetail = this.productDetailService.getById(order.getProductId());

                        productDetail.setTotalSales(productDetail.getTotalSales() + order.getQuantity());
                        this.productDetailService.edit(productDetail);
                    }

                    bill.setStatus("Paid");
                    this.billService.edit(bill);
                }
            
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
            break;
        }
        
        return ResponseEntity.status(200).body("");
    }

    private void warehouseHandle(Warehouse warehouse, int quantityGotten) {
        warehouse.setQuantity(warehouse.getQuantity() - quantityGotten);
        this.warehouseService.edit(warehouse);

        WarehouseHistory record = new WarehouseHistory();
        record.setWarehouseId(warehouse.getId());
        record.setQuantity(quantityGotten);
        record.setType(Enums.TypeWarehouse.MINUS.toString());
        this.warehouseHistoryService.save(record);
    }
}
