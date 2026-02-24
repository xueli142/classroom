package org.example.classroom.exception.validation;

import org.example.classroom.enums.ErrorCode;

/**
 * 参数验证异常
 * <p>
 * 用于封装参数校验失败的业务异常
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public class ValidationException extends org.example.classroom.exception.BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     *
     * @param errorCode 错误码枚举
     */
    public ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * 构造函数 - 带消息参数
     *
     * @param errorCode 错误码枚举
     * @param args      消息参数
     */
    public ValidationException(ErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    /**
     * 构造函数 - 指定字段和错误消息
     *
     * @param field   字段名
     * @param message 错误消息
     */
    public ValidationException(String field, String message) {
        super(org.example.classroom.enums.ValidationErrorCode.INVALID_PARAMETER, field, message);
    }

    /**
     * 构造函数 - 自定义错误消息
     *
     * @param message 错误消息
     */
    public ValidationException(String message) {
        super(org.example.classroom.enums.ValidationErrorCode.INVALID_PARAMETER, message);
    }
}
