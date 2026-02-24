package org.example.classroom.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认证授权错误码枚举
 * <p>
 * 定义用户认证和授权相关的错误码
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
@Getter
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    // ========== 认证错误 10xx ==========
    /**
     * 用户名或密码错误
     */
    INVALID_CREDENTIALS(1001, "auth.invalid_credentials", "用户名或密码错误"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1002, "auth.user_not_found", "用户不存在: %s"),

    /**
     * Token 无效
     */
    INVALID_TOKEN(1003, "auth.invalid_token", "Token 无效或已损坏"),

    /**
     * Token 已过期
     */
    TOKEN_EXPIRED(1004, "auth.token_expired", "Token 已过期，请重新登录"),

    /**
     * Token 在黑名单中
     */
    TOKEN_BLACKLISTED(1005, "auth.token_blacklisted", "Token 已失效，请重新登录"),

    /**
     * 密码错误
     */
    INVALID_PASSWORD(1006, "auth.invalid_password", "密码错误"),

    /**
     * 旧密码错误
     */
    INVALID_OLD_PASSWORD(1007, "auth.invalid_old_password", "旧密码错误"),

    /**
     * 新密码不能与旧密码相同
     */
    SAME_AS_OLD_PASSWORD(1008, "auth.same_as_old_password", "新密码不能与旧密码相同"),

    // ========== 授权错误 11xx ==========
    /**
     * 权限不足
     */
    ACCESS_DENIED(1101, "auth.access_denied", "权限不足，无法访问: %s"),

    /**
     * 角色权限不足
     */
    INSUFFICIENT_ROLE(1102, "auth.insufficient_role", "当前角色 %s 权限不足，需要 %s 角色"),

    /**
     * 用户已被禁用
     */
    USER_DISABLED(1103, "auth.user_disabled", "用户已被禁用: %s"),

    /**
     * 用户未激活
     */
    USER_INACTIVE(1104, "auth.user_inactive", "用户未激活"),

    /**
     * 角色不存在
     */
    ROLE_NOT_FOUND(1105, "auth.role_not_found", "角色不存在"),

    /**
     * 无效的角色
     */
    INVALID_ROLE(1106, "auth.invalid_role", "无效的角色: %s");

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
