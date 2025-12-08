package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.dto.response.StockProductResponse;
import iuh.fit.xstore.model.Stock;
import iuh.fit.xstore.model.StockItem;
import iuh.fit.xstore.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@AllArgsConstructor
public class StockController {

    private final StockService stockService;

    // Stock
    @GetMapping
    ApiResponse<List<Stock>> getStocks() {
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, stockService.findAll());
    }

    @GetMapping("/{id}")
    ApiResponse<Stock> getStock(@PathVariable("id") int id) {
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, stockService.findById(id));
    }

    @PostMapping
    ApiResponse<Stock> createStock(@RequestBody Stock stock) {
        return new ApiResponse<>(SuccessCode.STOCK_CREATED, stockService.create(stock));
    }

    @PutMapping("/{id}")
    ApiResponse<Stock> updateStock(@PathVariable("id") int id, @RequestBody Stock stock) {
        return new ApiResponse<>(SuccessCode.STOCK_UPDATED, stockService.update(id, stock));
    }

    @DeleteMapping("/{id}")
    ApiResponse<Integer> deleteStock(@PathVariable("id") int id) {
        return new ApiResponse<>(SuccessCode.STOCK_DELETED, stockService.delete(id));
    }

    // Xem danh sách sản phẩm trong kho
    @GetMapping("/{id}/items")
    ApiResponse<List<StockProductResponse>> getItems(@PathVariable("id") int id) {
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, stockService.getItemsOfStock(id));
    }

    // Tạo mới hoặc cập nhật số lượng
    @PostMapping("/{id}/items")
    ApiResponse<StockItem> setItemQuantity(@PathVariable("id") int id,
                                           @RequestParam("productInfoId") int productInfoId,
                                           @RequestParam("quantity") int quantity) {
        return new ApiResponse<>(SuccessCode.STOCK_ITEM_UPDATED,
                stockService.setItemQuantity(id, productInfoId, quantity));
    }

    // Tăng số lượng
    @PostMapping("/{id}/items/increase")
    ApiResponse<StockItem> increaseItemQuantity(@PathVariable("id") int id,
                                                @RequestParam("productInfoId") int productInfoId,
                                                @RequestParam("amount") int amount) {
        return new ApiResponse<>(SuccessCode.STOCK_ITEM_UPDATED,
                stockService.increaseItemQuantity(id, productInfoId, amount));
    }

    // Giảm số lượng
    @PostMapping("/{id}/items/decrease")
    ApiResponse<StockItem> decreaseItemQuantity(@PathVariable("id") int id,
                                                @RequestParam("productInfoId") int productInfoId,
                                                @RequestParam("amount") int amount) {
        return new ApiResponse<>(SuccessCode.STOCK_ITEM_UPDATED,
                stockService.decreaseItemQuantity(id, productInfoId, amount));
    }

    // Xóa sản phẩm khỏi kho
    @DeleteMapping("/{id}/items")
    ApiResponse<Void> deleteItem(@PathVariable("id") int id,
                                 @RequestParam("productInfoId") int productInfoId) {
        stockService.deleteItem(id, productInfoId);
        return new ApiResponse<>(SuccessCode.STOCK_ITEM_DELETED, null);
    }

    // Lấy tổng số lượng của tất cả variants của product từ tất cả kho
    @GetMapping("/products/{productId}/total-quantities")
    ApiResponse<Map<Integer, Integer>> getTotalQuantitiesForProduct(@PathVariable("productId") int productId) {
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, stockService.getTotalQuantitiesForProduct(productId));
    }
}
