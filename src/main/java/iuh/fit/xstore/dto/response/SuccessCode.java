package iuh.fit.xstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode {
    LOGIN_SUCCESSFULLY(200, "Login successfully"),
    REGISTER_SUCCESSFULLY(200, "Register successfully"),
    RESET_PASSWORD_SUCCESSFULLY(200, "Reset password successfully"),

    OTP_SEND_SUCCESSFULLY(200, "Send otp successfully"),
    OTP_VALID(200, "Valid Otp"),

    USER_CREATED(201, "User created successfully"),
    USER_UPDATED(200, "User updated successfully"),
    USER_DELETED(200, "User deleted successfully"),

    ACCOUNT_CREATED(201, "Account created successfully"),
    ACCOUNT_UPDATED(200, "Account updated successfully"),
    ACCOUNT_DELETED(200, "Account deleted successfully"),

    PRODUCT_CREATED(201, "Product created successfully"),
    PRODUCT_UPDATED(200, "Product updated successfully"),
    PRODUCT_DELETED(200, "Product deleted successfully"),

    PRODUCT_TYPE_CREATED(201, "Product type created successfully"),
    PRODUCT_TYPE_UPDATED(200, "Product type updated successfully"),
    PRODUCT_TYPE_DELETED(200, "Product type deleted successfully"),

    ADDRESS_CREATED(201, "Address created successfully"),
    ADDRESS_UPDATED(200, "Address updated successfully"),
    ADDRESS_DELETED(200, "Address deleted successfully"),

    SHIPINFO_CREATED(201, "Ship info created successfully"),
    SHIPINFO_UPDATED(200, "Ship info updated successfully"),
    SHIPINFO_DELETED(200, "Ship info deleted successfully"),

    FAVOURITE_CREATED(201, "Favourite created successfully"),
    FAVOURITE_DELETED(200, "Favourite deleted successfully"),

    // ===== ORDER =====
    ORDER_CREATED(201, "Order created successfully"),
    ORDER_UPDATED(200, "Order updated successfully"),
    ORDER_DELETED(200, "Order deleted successfully"),

    // ===== ORDER ITEM =====
    ORDER_ITEM_CREATED(201, "Order item created successfully"),
    ORDER_ITEM_UPDATED(200, "Order item updated successfully"),
    ORDER_ITEM_DELETED(200, "Order item deleted successfully"),

    // === Discount Success ===
    DISCOUNT_CREATED(201, "Discount created successfully"),
    DISCOUNT_UPDATED(200, "Discount updated successfully"),
    DISCOUNT_DELETED(200, "Discount deleted successfully"),

    // === Product Sales Success ===
    PRODUCT_SALES_CREATED(201, "Product sales created successfully"),
    PRODUCT_SALES_UPDATED(200, "Product sales updated successfully"),
    PRODUCT_SALES_DELETED(200, "Product sales deleted successfully"),

    //=== stock ===
    STOCK_CREATED(201, "Stock created successfully"),
    STOCK_UPDATED(200, "Stock updated successfully"),
    STOCK_DELETED(200, "Stock deleted successfully"),

    //    === Stock item ===
    STOCK_ITEM_CREATED(201, "Stock Item created successfully"),
    STOCK_ITEM_UPDATED(200, "Stock Item updated successfully"),
    STOCK_ITEM_DELETED(200, "Stock Item deleted successfully"),

    // cart
    CART_ITEM_CREATED(201, "Cart item created successfully"),
    CART_ITEM_UPDATED(200, "Cart item updated successfully"),
    CART_ITEM_DELETED(200, "Cart item deleted successfully"),
    CART_CREATED(201, "Cart created successfully"),
    CART_UPDATED(200, "Cart updated successfully"),
    CART_DELETED(200, "Cart deleted successfully"),

    // ===== PAYMENT =====
    PAYMENT_SUCCESSFUL(200, "Payment successful"),
    PAYMENT_INITIATED(200, "Payment initiated"),
    PAYMENT_PENDING(200, "Payment is pending"),

//    ===file===
    FILE_UPLAOD_SUCCESSFULLY(200, "Upload file successfully"),

    SUCCESS(200, "Request successful"),
    FETCH_SUCCESS(200, "Data fetched successfully");

    private final int code;
    private final String message;
}
