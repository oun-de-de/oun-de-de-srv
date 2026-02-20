package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.controllers.dto.inventory.CreateItemRequest;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.inventory.InventoryService;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.InventoryItemResult;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.StockTransactionResult;
import com.cdtphuhoi.oun_de_de.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/inventory/items")
public class InventoryController {

    private final InventoryService inventoryService;

    private final ControllerUtils controllerUtils;

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

    @PostMapping
    public ResponseEntity<InventoryItemResult> createItem(
        @Valid @RequestBody CreateItemRequest request
    ) {
        var usr = controllerUtils.getCurrentSignedInUser();
        var result = inventoryService.createItem(
            MapperHelpers.getInventoryMapper().toCreateItemData(request),
            usr
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }

    /*
     * update item via transaction
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable String itemId) {
        return null;
    }

    @GetMapping("/{itemId}/transactions")
    public ResponseEntity<List<StockTransactionResult>> listTransactionsByItemId(
        @PathVariable String itemId
    ) {
        return ResponseEntity.ok(inventoryService.findTransactionsByItem(itemId));
    }


    /*
     * list borrowings
     */
}
