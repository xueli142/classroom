package org.example.classroom.constant;

/**
 * 安全相关常量
 * <p>
 * 定义安全认证、授权相关的常量
 *
 * @author Classroom Team
 * @since 2025-02-23
 */
public final class SecurityConstants {

    private SecurityConstants() {
        throw new UnsupportedOperationException("Constant class cannot be instantiated");
    }

    // ========== 角色相关 ==========
    /**
     * 角色前缀
     */
    public static final String ROLE_PREFIX = "ROLE_";

    /**
     * 管理员角色
     */
    public static final String ROLE_ADMIN = "admin";

    /**
     * 教师角色
     */
    public static final String ROLE_TEACHER = "teacher";

    /**
     * 学生角色
     */
    public static final String ROLE_STUDENT = "student";

    // ========== Token 相关 ==========
    /**
     * Token 请求头名称
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * Token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * Token 在请求头中的完整前缀
     */
    public static final String TOKEN_HEADER_PREFIX = TOKEN_PREFIX;

    // ========== 路径匹配 ==========
    /**
     * 所有路径
     */
    public static final String ALL_PATH = "/**";

    /**
     * 认证路径（不需要认证）
     */
    public static final String[] AUTH_WHITELIST = {
            "/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/image/**",
            "/error"
    };

    // ========== 用户相关 ==========
    /**
     * 默认用户状态（激活）
     */
    public static final boolean DEFAULT_USER_ACTIVE = true;

    /**
     * 用户默认头像
     */
    public static final String DEFAULT_AVATAR = "/image/default-avatar.png";
}
