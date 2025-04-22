package com.masofino.birp.chatclient.api;

public class ApiResult<T> {
    private final T      data;
    private final int    statusCode;
    private final String rawBody;

    private ApiResult(T data, int statusCode, String rawBody) {
        this.data       = data;
        this.statusCode = statusCode;
        this.rawBody    = rawBody;
    }

    public static <T> ApiResult<T> success(T data, int statusCode, String rawBody) {
        return new ApiResult<>(data, statusCode, rawBody);
    }

    public static <T> ApiResult<T> error(int statusCode, String rawBody) {
        return new ApiResult<>(null, statusCode, rawBody);
    }

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }

    public T getData() {
        return data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getRawBody() {
        return rawBody;
    }
}