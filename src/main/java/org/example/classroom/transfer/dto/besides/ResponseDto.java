package org.example.classroom.transfer.dto.besides;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 统一响应 DTO
 * <p>
 * 封装所有 API 接口的返回结果，提供统一的响应格式
 * <p>
 * 响应结构示例：
 * <pre>{@code
 * {
 *     "code": 200,
 *     "message": "操作成功",
 *     "data": { ... },
 *     "timestamp": 1234567890
 * }
 * }</pre>
 *
 * @param <T> 数据类型
 * @author Classroom Team
 * @since 2025-02-23
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    /**
     * 响应码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    // ==================== 构造方法 ====================

    /**
     * 私有构造方法，强制使用静态工厂方法
     */
    private ResponseDto(int code, String message, T data, long timestamp) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    // ==================== 成功响应 ====================

    /**
     * 成功响应 - 带数据
     *
     * @param data 响应数据
     * @return 响应对象
     */
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(200, "操作成功", data, System.currentTimeMillis());
    }

    /**
     * 成功响应 - 带数据和自定义消息
     *
     * @param data    响应数据
     * @param message 成功消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> success(T data, String message) {
        return new ResponseDto<>(200, message, data, System.currentTimeMillis());
    }

    /**
     * 成功响应 - 无数据
     *
     * @return 响应对象
     */
    public static <T> ResponseDto<T> success() {
        return success(null);
    }

    /**
     * 成功响应 - 仅带消息
     *
     * @param message 成功消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> successMessage(String message) {
        return success(null, message);
    }

    // ==================== 错误响应 ====================

    /**
     * 错误响应 - 默认错误码
     *
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> error(String message) {
        return new ResponseDto<>(400, message, null, System.currentTimeMillis());
    }

    /**
     * 错误响应 - 自定义错误码
     *
     * @param code    错误码
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> error(int code, String message) {
        return new ResponseDto<>(code, message, null, System.currentTimeMillis());
    }

    /**
     * 错误响应 - 带数据（特殊情况）
     *
     * @param code    错误码
     * @param message 错误消息
     * @param data    响应数据
     * @return 响应对象
     */
    public static <T> ResponseDto<T> error(int code, String message, T data) {
        return new ResponseDto<>(code, message, data, System.currentTimeMillis());
    }

    // ==================== 判断方法 ====================

    /**
     * 判断是否成功
     *
     * @return true 如果响应码为 200
     */
    public boolean isSuccess() {
        return this.code == 200;
    }

    /**
     * 判断是否失败
     *
     * @return true 如果响应码不为 200
     */
    public boolean isFailed() {
        return this.code != 200;
    }

    // ==================== 链式调用 ====================

    /**
     * 设置响应数据（链式调用）
     *
     * @param data 响应数据
     * @return 当前对象
     */
    public ResponseDto<T> withData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 设置响应消息（链式调用）
     *
     * @param message 响应消息
     * @return 当前对象
     */
    public ResponseDto<T> withMessage(String message) {
        this.message = message;
        return this;
    }
}