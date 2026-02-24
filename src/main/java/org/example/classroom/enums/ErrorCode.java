package org.example.classroom.enums;

/**
 * 错误码接口
 * <p>
 * 所有错误码枚举都需要实现此接口，确保有统一的错误码结构
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public interface ErrorCode {

    /**
     * 获取错误码数值
     *
     * @return 错误码
     */
    int getCode();

    /**
     * 获取错误消息键（用于国际化）
     *
     * @return 消息键
     */
    String getKey();

    /**
     * 获取错误消息
     *
     * @return 错误消息
     */
    String getMessage();
}
