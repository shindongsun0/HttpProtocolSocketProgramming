package server.Response;

import lombok.Getter;

@Getter
public enum StatusCodes {
    OK(200, "200 OK"),
    NOT_FOUND(404, "404 Not Found"),
    SERVER_ERROR(500, "500 Internal Server Error"),
    FOUND(302, "302 Found"),
    CREATED(201, "201 Created"),
    FORBIDDEN(403, "403 Forbidden"),
    BAD_REQUEST(400, "400 Bad Request");

    private int code;
    private String status;

    StatusCodes(int code, String status) {
        this.code = code;
        this.status = status;
    }
}
