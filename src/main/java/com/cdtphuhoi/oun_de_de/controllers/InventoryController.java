package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.services.inventory.InventoryService;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.InventoryItemResult;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/inventory/items")
public class InventoryController {

    private final InventoryService inventoryService;


    @GetMapping
    public ResponseEntity<List<InventoryItemResult>> listItems() {
        return ResponseEntity.ok(inventoryService.findAllItems());
    }

    /*
     * @return item details include: transaction, borrowing (if exist)
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItemDetails(@PathVariable String itemId) {
        return null;
    }

    /*
     * Create item and StockTransaction(IN)
     */
    @PostMapping
    public ResponseEntity<?> createItem() {
        return null;
    }

    /*
     * update item via transaction
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable String itemId) {
        return null;
    }
}
