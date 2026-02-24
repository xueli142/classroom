package org.example.classroom.constant;

/**
 * JWT 相关常量
 * <p>
 * 定义 JWT Token 相关的常量
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public final class JwtConstants {

    private JwtConstants() {
        throw new UnsupportedOperationException("Constant class cannot be instantiated");
    }

    // ========== Token 有效期 ==========
    /**
     * Token 默认有效期（毫秒）- 1小时
     */
    public static final long DEFAULT_EXPIRATION = 3600000L;

    /**
     * Token 记住我有效期（毫秒）- 7天
     */
    public static final long REMEMBER_ME_EXPIRATION = 604800000L;

    // ========== Redis Key 前缀 ==========
    /**
     * Token 黑名单前缀
     */
    public static final String BLACKLIST_PREFIX = "blacklist:";

    /**
     * Token 刷新令牌前缀
     */
    public static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    /**
     * 用户在线状态前缀
     */
    public static final String ONLINE_USER_PREFIX = "online_user:";

    // ========== Claims Key ==========
    /**
     * 用户 ID 在 Claims 中的键名
     */
    public static final String CLAIM_KEY_USER_ID = "userId";

    /**
     * 角色在 Claims 中的键名
     */
    public static final String CLAIM_KEY_ROLE = "role";

    /**
     * 用户名在 Claims 中的键名（subject）
     */
    public static final String CLAIM_KEY_USERNAME = "sub";

    // ========== Token 生成相关 ==========
    /**
     * JWT ID 长度（UUID）
     */
    public static final int JTI_LENGTH = 36;

    // ========== 错误消息 ==========
    /**
     * Token 无效消息
     */
    public static final String MSG_TOKEN_INVALID = "Token 无效或已损坏";

    /**
     * Token 已过期消息
     */
    public static final String MSG_TOKEN_EXPIRED = "Token 已过期，请重新登录";

    /**
     * Token 在黑名单中消息
     */
    public static final String MSG_TOKEN_BLACKLISTED = "Token 已失效，请重新登录";
}
