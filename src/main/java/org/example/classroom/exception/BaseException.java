package org.example.classroom.exception;

import org.example.classroom.enums.ErrorCode;

/**
 * 基础异常类
 * <p>
 * 所有自定义异常的父类，提供统一的错误码和错误消息处理
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final ErrorCode errorCode;

    /**
     * 错误消息参数（用于格式化消息）
     */
    private final Object[] args;

    /**
     * 构造函数 - 使用默认错误消息
     *
     * @param errorCode 错误码枚举
     */
    protected BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = null;
    }

    /**
     * 构造函数 - 使用自定义消息参数
     *
     * @param errorCode 错误码枚举
     * @param args      消息参数
     */
    protected BaseException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = args;
    }

    /**
     * 构造函数 - 包含原始异常
     *
     * @param errorCode 错误码枚举
     * @param cause     原始异常
     */
    protected BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.args = null;
    }

    /**
     * 构造函数 - 包含原始异常和消息参数
     *
     * @param errorCode 错误码枚举
     * @param cause     原始异常
     * @param args      消息参数
     */
    protected BaseException(ErrorCode errorCode, Throwable cause, Object... args) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.args = args;
    }

    /**
     * 获取错误码
     *
     * @return 错误码数值
     */
    public int getCode() {
        return errorCode.getCode();
    }

    /**
     * 获取错误码枚举
     *
     * @return 错误码枚举
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * 获取错误消息键（用于国际化）
     *
     * @return 消息键
     */
    public String getMessageKey() {
        return errorCode.getKey();
    }

    /**
     * 获取消息参数
     *
     * @return 消息参数数组
     */
    public Object[] getArgs() {
        return args != null ? args.clone() : null;
    }

    /**
     * 获取格式化后的错误消息
     *
     * @return 格式化后的消息
     */
    public String getFormattedMessage() {
        if (args == null || args.length == 0) {
            return errorCode.getMessage();
        }
        return String.format(errorCode.getMessage(), args);
    }

    @Override
    public String getMessage() {
        return getFormattedMessage();
    }
}
