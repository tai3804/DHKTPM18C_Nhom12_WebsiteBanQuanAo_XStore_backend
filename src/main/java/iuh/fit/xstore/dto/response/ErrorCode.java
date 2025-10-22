package iuh.fit.xstore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_NOT_FOUND(404, "User not found"),
    USER_EXISTED(409, "User already exists"),

    ACCOUNT_NOT_FOUND(404, "Account not found"),
    ACCOUNT_EXISTED(409, "Account already exists"),
    USERNAME_EXISTED(409, "Username already exists"),
    PASSWORD_EMPTY(400, "Password cannot be empty"),
    INVALID_PASSWORD(401, "Invalid password"),

    TOKEN_EXPIRED(403, "Token has expired"),
    UNKNOWN_ERROR(500, "Something went wrong");

    private final int code;
    private final String message;
}
