package com.sap.authority.enity;

public class Result<T> {
    private int code;
    private String message;
    private T token;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }




    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getToken() {
        return token;
    }

    public void setToken(T token) {
        this.token = token;
    }

    public Result(int code, String message, T token) {
        this.code = code;
        this.message = message;
        this.token = token;
    }

    public Result(int code, T data) {
        this.code = code;
        this.token = data;
    }

    public Result() {
    }
}
