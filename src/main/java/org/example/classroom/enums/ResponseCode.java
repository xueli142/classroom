package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用响应码枚举
 * <p>
 * 定义系统中通用的响应码，如成功、客户端错误、服务器错误等
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum ResponseCode implements ErrorCode {

    // ========== 成功响应 ==========
    /**
     * 操作成功
     */
    SUCCESS(200, "success", "操作成功"),

    // ========== 客户端错误 4xx ==========
    /**
     * 请求参数错误
     */
    BAD_REQUEST(400, "bad_request", "请求参数错误"),

    /**
     * 未授权，需要登录
     */
    UNAUTHORIZED(401, "unauthorized", "未授权，请先登录"),

    /**
     * 禁止访问，权限不足
     */
    FORBIDDEN(403, "forbidden", "权限不足，禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "not_found", "请求的资源不存在"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "method_not_allowed", "请求方法不支持"),

    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(408, "request_timeout", "请求超时"),

    /**
     * 请求冲突
     */
    CONFLICT(409, "conflict", "请求冲突，资源已存在"),

    /**
     * 请求实体过大
     */
    PAYLOAD_TOO_LARGE(413, "payload_too_large", "上传文件过大"),

    /**
     * 请求过于频繁
     */
    TOO_MANY_REQUESTS(429, "too_many_requests", "请求过于频繁，请稍后再试"),

    // ========== 服务器错误 5xx ==========
    /**
     * 服务器内部错误
     */
    INTERNAL_ERROR(500, "internal_error", "系统内部错误，请稍后再试"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "service_unavailable", "服务暂时不可用，请稍后再试");

    /**
     * 错误码数值
     */
    private final int code;

    /**
     * 错误消息键（用于国际化）
     */
    private final String key;

    /**
     * 错误消息
     */
    private final String message;
}
