package org.example.classroom.exception.system;

import org.example.classroom.enums.SystemErrorCode;

/**
 * 外部服务异常
 * <p>
 * 当调用外部服务（如 Redis、外部 API）失败时抛出此异常
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public class ExternalServiceException extends org.example.classroom.exception.BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 服务名称
     */
    private final String serviceName;

    /**
     * 构造函数
     *
     * @param errorCode   错误码枚举
     * @param serviceName 服务名称
     */
    public ExternalServiceException(SystemErrorCode errorCode, String serviceName) {
        super(errorCode, serviceName);
        this.serviceName = serviceName;
    }

    /**
     * 构造函数 - 包含原始异常
     *
     * @param errorCode   错误码枚举
     * @param serviceName 服务名称
     * @param cause       原始异常
     */
    public ExternalServiceException(SystemErrorCode errorCode, String serviceName, Throwable cause) {
        super(errorCode, cause, serviceName);
        this.serviceName = serviceName;
    }

    /**
     * Redis 连接失败
     */
    public static ExternalServiceException redisFailed() {
        return new ExternalServiceException(SystemErrorCode.REDIS_CONNECTION_FAILED, "Redis");
    }

    /**
     * Redis 连接失败 - 包含原始异常
     *
     * @param cause 原始异常
     */
    public static ExternalServiceException redisFailed(Throwable cause) {
        return new ExternalServiceException(SystemErrorCode.REDIS_CONNECTION_FAILED, "Redis", cause);
    }

    /**
     * 数据库连接失败
     */
    public static ExternalServiceException databaseFailed() {
        return new ExternalServiceException(SystemErrorCode.DATABASE_CONNECTION_FAILED, "Database");
    }

    /**
     * 数据库连接失败 - 包含原始异常
     *
     * @param cause 原始异常
     */
    public static ExternalServiceException databaseFailed(Throwable cause) {
        return new ExternalServiceException(SystemErrorCode.DATABASE_CONNECTION_FAILED, "Database", cause);
    }

    /**
     * 外部 API 调用失败
     *
     * @param apiName API 名称
     */
    public static ExternalServiceException apiFailed(String apiName) {
        return new ExternalServiceException(SystemErrorCode.EXTERNAL_API_FAILED, apiName);
    }

    /**
     * 外部 API 调用失败 - 包含原始异常
     *
     * @param apiName API 名称
     * @param cause   原始异常
     */
    public static ExternalServiceException apiFailed(String apiName, Throwable cause) {
        return new ExternalServiceException(SystemErrorCode.EXTERNAL_API_FAILED, apiName, cause);
    }

    public String getServiceName() {
        return serviceName;
    }
}
