package iuh.fit.xstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessCode {
    USER_CREATED(201, "User created successfully"),
    USER_UPDATED(200, "User updated successfully"),
    USER_DELETED(200, "User deleted successfully"),

    ACCOUNT_CREATED(201, "Account created successfully"),
    ACCOUNT_UPDATED(200, "Account updated successfully"),
    ACCOUNT_DELETED(200, "Account deleted successfully"),

    FETCH_SUCCESS(200, "Data fetched successfully");

    private final int code;
    private final String message;
}
