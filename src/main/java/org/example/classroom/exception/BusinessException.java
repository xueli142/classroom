package org.example.classroom.exception;

import org.example.classroom.enums.ErrorCode;

/**
 * 业务异常类
 * <p>
 * 用于封装业务逻辑中的错误情况，如资源不存在、参数校验失败等
 * <p>
 * 使用示例：
 * <pre>{@code
 * throw new BusinessException(UserErrorCode.USER_NOT_FOUND, userId);
 * }</pre>
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数 - 使用默认错误消息
     *
     * @param errorCode 错误码枚举
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * 构造函数 - 使用自定义消息参数
     *
     * @param errorCode 错误码枚举
     * @param args      消息参数
     */
    public BusinessException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    /**
     * 构造函数 - 包含原始异常
     *
     * @param errorCode 错误码枚举
     * @param cause     原始异常
     */
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    /**
     * 构造函数 - 包含原始异常和消息参数
     *
     * @param errorCode 错误码枚举
     * @param cause     原始异常
     * @param args      消息参数
     */
    public BusinessException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode, cause, args);
    }
}
