package org.example.classroom.exception.auth;

import org.example.classroom.enums.AuthErrorCode;

/**
 * 授权异常
 * <p>
 * 用于用户权限不足的场景，如访问禁止的资源、角色权限不足等
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public class AuthorizationException extends org.example.classroom.exception.BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     *
     * @param errorCode 错误码枚举
     */
    public AuthorizationException(AuthErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * 构造函数 - 带消息参数
     *
     * @param errorCode 错误码枚举
     * @param args      消息参数
     */
    public AuthorizationException(AuthErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    /**
     * 权限不足
     *
     * @param resource 资源名称
     */
    public static AuthorizationException accessDenied(String resource) {
        return new AuthorizationException(AuthErrorCode.ACCESS_DENIED, resource);
    }

    /**
     * 角色权限不足
     *
     * @param role     当前角色
     * @param required 需要的角色
     */
    public static AuthorizationException insufficientRole(String role, String required) {
        return new AuthorizationException(AuthErrorCode.INSUFFICIENT_ROLE, role, required);
    }

    /**
     * 用户已被禁用
     *
     * @param userId 用户ID
     */
    public static AuthorizationException userDisabled(String userId) {
        return new AuthorizationException(AuthErrorCode.USER_DISABLED, userId);
    }
}
