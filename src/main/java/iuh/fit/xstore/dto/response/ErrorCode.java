package iuh.fit.xstore.dto.response;

public enum ErrorCode {
    PASSWORD_EMPTY(400, "Password cannot be empty"),
    USER_NOT_FOUND(404, "User not found"),
    ACCOUNT_NOT_FOUND(404, "Account not found"),
    INVALID_PASSWORD(401, "Invalid password"),
    TOKEN_EXPIRED(403, "Token has expired"),
    USER_EXISTED(409, "User already exists"),
    USERNAME_EXISTED(409, "Username already exists"),
    ACCOUNT_EXISTED(409, "Account already exists"),
    UNKNOWN_ERROR(500, "Something went wrong");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
