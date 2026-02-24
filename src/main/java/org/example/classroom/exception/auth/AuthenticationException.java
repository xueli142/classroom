package org.example.classroom.exception.auth;

import org.example.classroom.enums.ErrorCode;
import org.example.classroom.enums.AuthErrorCode;

/**
 * 认证异常
 * <p>
 * 用于用户认证失败的场景，如登录失败、Token 无效等
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public class AuthenticationException extends org.example.classroom.exception.BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     *
     * @param errorCode 错误码枚举
     */
    public AuthenticationException(AuthErrorCode errorCode) {
        super(errorCode);
    }

    /**
     * 构造函数 - 带消息参数
     *
     * @param errorCode 错误码枚举
     * @param args      消息参数
     */
    public AuthenticationException(AuthErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    /**
     * 用户名或密码错误
     */
    public static AuthenticationException invalidCredentials() {
        return new AuthenticationException(AuthErrorCode.INVALID_CREDENTIALS);
    }

    /**
     * 用户不存在
     *
     * @param uid 用户标识
     */
    public static AuthenticationException userNotFound(String uid) {
        return new AuthenticationException(AuthErrorCode.USER_NOT_FOUND, uid);
    }

    /**
     * Token 无效
     */
    public static AuthenticationException invalidToken() {
        return new AuthenticationException(AuthErrorCode.INVALID_TOKEN);
    }

    /**
     * Token 已过期
     */
    public static AuthenticationException tokenExpired() {
        return new AuthenticationException(AuthErrorCode.TOKEN_EXPIRED);
    }

    /**
     * 密码错误
     */
    public static AuthenticationException invalidPassword() {
        return new AuthenticationException(AuthErrorCode.INVALID_PASSWORD);
    }

    /**
     * 用户已被禁用
     *
     * @param uid 用户标识
     */
    public static AuthenticationException userDisabled(String uid) {
        return new AuthenticationException(AuthErrorCode.USER_DISABLED, uid);
    }
}
