package org.example.classroom.util;

import org.example.classroom.enums.ErrorCode;
import org.example.classroom.enums.ResponseCode;
import org.example.classroom.transfer.dto.besides.ResponseDto;

/**
 * 统一响应工具类
 * <p>
 * 提供便捷的方法构建标准的 ResponseDto 响应
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public final class ResponseUtils {

    private ResponseUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ==================== 成功响应 ====================

    /**
     * 成功响应 - 无数据
     *
     * @return 响应对象
     */
    public static <T> ResponseDto<T> success() {
        return ResponseDto.success(null);
    }

    /**
     * 成功响应 - 带数据
     *
     * @param data 响应数据
     * @return 响应对象
     */
    public static <T> ResponseDto<T> success(T data) {
        return ResponseDto.success(data);
    }

    /**
     * 成功响应 - 带数据和自定义消息
     *
     * @param data    响应数据
     * @param message 成功消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> success(T data, String message) {
        return ResponseDto.success(data, message);
    }

    /**
     * 成功响应 - 仅带消息
     *
     * @param message 成功消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> successMessage(String message) {
        return ResponseDto.success(null, message);
    }

    // ==================== 失败响应 ====================

    /**
     * 失败响应 - 默认错误码
     *
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> error(String message) {
        return ResponseDto.error(message);
    }

    /**
     * 失败响应 - 自定义错误码
     *
     * @param code    错误码
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> error(int code, String message) {
        return ResponseDto.error(code, message);
    }

    /**
     * 失败响应 - 使用错误码枚举
     *
     * @param errorCode 错误码枚举
     * @return 响应对象
     */
    public static <T> ResponseDto<T> error(ErrorCode errorCode) {
        return ResponseDto.error(errorCode.getCode(), errorCode.getMessage());
    }

    /**
     * 失败响应 - 使用错误码枚举和消息参数
     *
     * @param errorCode 错误码枚举
     * @param args      消息参数
     * @return 响应对象
     */
    public static <T> ResponseDto<T> error(ErrorCode errorCode, Object... args) {
        String message = args == null || args.length == 0
                ? errorCode.getMessage()
                : String.format(errorCode.getMessage(), args);
        return ResponseDto.error(errorCode.getCode(), message);
    }

    // ==================== 常用快捷方法 ====================

    /**
     * 参数错误响应
     *
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> badRequest(String message) {
        return ResponseDto.error(ResponseCode.BAD_REQUEST.getCode(), message);
    }

    /**
     * 未授权响应
     *
     * @return 响应对象
     */
    public static <T> ResponseDto<T> unauthorized() {
        return error(ResponseCode.UNAUTHORIZED);
    }

    /**
     * 未授权响应 - 自定义消息
     *
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> unauthorized(String message) {
        return ResponseDto.error(ResponseCode.UNAUTHORIZED.getCode(), message);
    }

    /**
     * 禁止访问响应
     *
     * @return 响应对象
     */
    public static <T> ResponseDto<T> forbidden() {
        return error(ResponseCode.FORBIDDEN);
    }

    /**
     * 禁止访问响应 - 自定义消息
     *
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> forbidden(String message) {
        return ResponseDto.error(ResponseCode.FORBIDDEN.getCode(), message);
    }

    /**
     * 资源不存在响应
     *
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> notFound(String message) {
        return ResponseDto.error(ResponseCode.NOT_FOUND.getCode(), message);
    }

    /**
     * 服务器错误响应
     *
     * @return 响应对象
     */
    public static <T> ResponseDto<T> internalError() {
        return error(ResponseCode.INTERNAL_ERROR);
    }

    /**
     * 服务器错误响应 - 自定义消息
     *
     * @param message 错误消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> internalError(String message) {
        return ResponseDto.error(ResponseCode.INTERNAL_ERROR.getCode(), message);
    }

    // ==================== 条件响应 ====================

    /**
     * 根据布尔值返回成功或失败响应
     *
     * @param condition 条件
     * @param data      成功时的数据
     * @param errorMsg  失败时的消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> fromBoolean(boolean condition, T data, String errorMsg) {
        return condition ? success(data) : badRequest(errorMsg);
    }

    /**
     * 根据布尔值返回成功或失败响应
     *
     * @param condition 条件
     * @param successMsg 成功时的消息
     * @param errorMsg   失败时的消息
     * @return 响应对象
     */
    public static <T> ResponseDto<T> fromBoolean(boolean condition, String successMsg, String errorMsg) {
        return condition ? successMessage(successMsg) : badRequest(errorMsg);
    }
}
