package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统错误码枚举
 * <p>
 * 定义系统层面的错误码，如文件操作、外部服务等
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum SystemErrorCode implements ErrorCode {

    // ========== 文件操作错误 17xx ==========
    /**
     * 文件上传失败
     */
    FILE_UPLOAD_FAILED(1701, "system.file_upload_failed", "文件上传失败: %s"),

    /**
     * 文件删除失败
     */
    FILE_DELETE_FAILED(1702, "system.file_delete_failed", "文件删除失败: %s"),

    /**
     * 文件不存在
     */
    FILE_NOT_FOUND(1703, "system.file_not_found", "文件不存在: %s"),

    /**
     * 文件格式不支持
     */
    FILE_FORMAT_NOT_SUPPORTED(1704, "system.file_format_not_supported", "文件格式不支持: %s"),

    /**
     * 文件大小超限
     */
    FILE_SIZE_EXCEEDED(1705, "system.file_size_exceeded", "文件大小超限: %s"),

    /**
     * 文件读取失败
     */
    FILE_READ_FAILED(1706, "system.file_read_failed", "文件读取失败"),

    /**
     * 文件写入失败
     */
    FILE_WRITE_FAILED(1707, "system.file_write_failed", "文件写入失败"),

    // ========== 外部服务错误 18xx ==========
    /**
     * Redis 连接失败
     */
    REDIS_CONNECTION_FAILED(1801, "system.redis_failed", "%s 连接失败"),

    /**
     * 数据库连接失败
     */
    DATABASE_CONNECTION_FAILED(1802, "system.database_failed", "%s 连接失败"),

    /**
     * 外部 API 调用失败
     */
    EXTERNAL_API_FAILED(1803, "system.external_api_failed", "外部服务调用失败: %s"),

    /**
     * 网络超时
     */
    NETWORK_TIMEOUT(1804, "system.network_timeout", "网络请求超时"),

    /**
     * 网络连接失败
     */
    NETWORK_ERROR(1805, "system.network_error", "网络连接失败"),

    // ========== 其他系统错误 19xx ==========
    /**
     * 系统繁忙
     */
    SYSTEM_BUSY(1901, "system.busy", "系统繁忙，请稍后再试"),

    /**
     * 系统维护中
     */
    SYSTEM_MAINTENANCE(1902, "system.maintenance", "系统维护中，请稍后再试"),

    /**
     * 缓存操作失败
     */
    CACHE_OPERATION_FAILED(1903, "system.cache_failed", "缓存操作失败"),

    /**
     * 数据序列化失败
     */
    SERIALIZATION_FAILED(1904, "system.serialization_failed", "数据序列化失败"),

    /**
     * 数据反序列化失败
     */
    DESERIALIZATION_FAILED(1905, "system.deserialization_failed", "数据反序列化失败");

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
