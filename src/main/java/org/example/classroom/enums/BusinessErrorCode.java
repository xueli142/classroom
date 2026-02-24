package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务错误码枚举
 * <p>
 * 定义业务逻辑相关的通用错误码
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum BusinessErrorCode implements ErrorCode {

    // ========== 资源错误 13xx ==========
    /**
     * 资源不存在
     */
    RESOURCE_NOT_FOUND(1301, "business.resource_not_found", "%s 不存在: %s"),

    /**
     * 资源重复
     */
    DUPLICATE_RESOURCE(1302, "business.duplicate_resource", "%s 已存在: %s"),

    // ========== 操作错误 14xx ==========
    /**
     * 操作不允许
     */
    OPERATION_NOT_ALLOWED(1401, "business.operation_not_allowed", "不允许 %s，原因：%s"),

    /**
     * 状态无效
     */
    INVALID_STATUS(1402, "business.invalid_status", "当前状态不允许 %s（当前状态：%s，需要：%s）"),

    /**
     * 操作失败
     */
    OPERATION_FAILED(1403, "business.operation_failed", "操作失败"),

    /**
     * 数据冲突
     */
    DATA_CONFLICT(1404, "business.data_conflict", "数据冲突，操作无法完成"),

    /**
     * 业务规则验证失败
     */
    BUSINESS_RULE_VIOLATION(1405, "business.rule_violation", "业务规则验证失败"),

    // ========== 时间相关错误 15xx ==========
    /**
     * 不在有效时间范围内
     */
    OUT_OF_TIME_RANGE(1501, "business.out_of_time_range", "不在有效时间范围内"),

    /**
     * 开始时间不能晚于结束时间
     */
    INVALID_TIME_RANGE(1502, "business.invalid_time_range", "开始时间不能晚于结束时间"),

    /**
     * 时间段冲突
     */
    TIME_CONFLICT(1503, "business.time_conflict", "时间段存在冲突"),

    /**
     * 已过期
     */
    EXPIRED(1504, "business.expired", "已过期"),

    /**
     * 尚未开始
     */
    NOT_STARTED(1505, "business.not_started", "尚未开始");

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
