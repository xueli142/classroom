package org.example.demo5.dto;


import lombok.Data;

@Data
public class ResponseDto<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp;

    // 传统构造方法（私有化，强制使用静态工厂）
    private ResponseDto(int code, String message, T data, long timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }
    // 成功响应（自定义 message）
    public static <T> ResponseDto<T> success(T data, String message) {
        return new ResponseDto<>(200, message, data, System.currentTimeMillis());
    }

    // 静态工厂方法：成功响应（默认参数）
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(200, "success", data, System.currentTimeMillis());
    }


    // 静态工厂方法：错误响应（默认参数）
    public static <T> ResponseDto<T> error(String message) {
        return new ResponseDto<>(400, message, null, System.currentTimeMillis());
    }
    // 错误响应（必须提供 code 和 message）

    public static <T> ResponseDto<T> error(int code, String message) {
        return new ResponseDto<>(code, message, null, System.currentTimeMillis());
    }
}