package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户模块错误码枚举
 * <p>
 * 定义用户相关的错误码，如用户不存在、用户名重复等
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // ========== 用户错误 12xx ==========
    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1201, "user.not_found", "用户不存在: %s"),

    /**
     * 用户名已存在
     */
    DUPLICATE_USERNAME(1202, "user.duplicate_username", "用户名已存在: %s"),

    /**
     * 手机号已存在
     */
    DUPLICATE_PHONE(1203, "user.duplicate_phone", "手机号已被使用: %s"),

    /**
     * 手机号格式错误
     */
    INVALID_PHONE(1204, "user.invalid_phone", "手机号格式错误"),

    /**
     * 用户已被禁用
     */
    USER_DISABLED(1205, "user.disabled", "用户已被禁用"),

    /**
     * 邮箱格式错误
     */
    INVALID_EMAIL(1206, "user.invalid_email", "邮箱格式错误"),

    /**
     * 身份证号格式错误
     */
    INVALID_ID_CARD(1207, "user.invalid_id_card", "身份证号格式错误"),

    /**
     * 学号格式错误
     */
    INVALID_STUDENT_ID(1208, "user.invalid_student_id", "学号格式错误"),

    /**
     * 工号格式错误
     */
    INVALID_TEACHER_ID(1209, "user.invalid_teacher_id", "工号格式错误"),

    /**
     * 用户信息不完整
     */
    INCOMPLETE_USER_INFO(1210, "user.incomplete_info", "用户信息不完整"),

    /**
     * 修改头像失败
     */
    AVATAR_UPDATE_FAILED(1211, "user.avatar_update_failed", "修改头像失败"),

    /**
     * 删除用户失败
     */
    USER_DELETE_FAILED(1212, "user.delete_failed", "删除用户失败");

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
