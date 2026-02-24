package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 参数验证错误码枚举
 * <p>
 * 定义参数验证相关的错误码
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum ValidationErrorCode implements ErrorCode {

    // ========== 参数验证错误 16xx ==========
    /**
     * 参数无效
     */
    INVALID_PARAMETER(1601, "validation.invalid_parameter", "参数无效: %s"),

    /**
     * 必填参数缺失
     */
    REQUIRED_PARAMETER_MISSING(1602, "validation.required_missing", "必填参数缺失: %s"),

    /**
     * 参数格式错误
     */
    INVALID_FORMAT(1603, "validation.invalid_format", "参数格式错误: %s"),

    /**
     * 参数长度超出限制
     */
    LENGTH_EXCEEDED(1604, "validation.length_exceeded", "参数长度超出限制: %s"),

    /**
     * 参数值超出范围
     */
    VALUE_OUT_OF_RANGE(1605, "validation.value_out_of_range", "参数值超出范围: %s"),

    /**
     * 参数类型错误
     */
    TYPE_MISMATCH(1606, "validation.type_mismatch", "参数类型错误: %s");

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
