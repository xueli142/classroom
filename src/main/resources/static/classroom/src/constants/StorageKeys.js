/**
 * localStorage/sessionStorage 键名常量
 * 避免硬编码字符串，便于统一管理
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

export const StorageKeys = {
	// ==================== 认证相关 ====================
	AUTH_TOKEN: 'auth.token',
	AUTH_USER: 'auth.user',

	// ==================== 应用设置 ====================
	THEME: 'app.theme',
	LANGUAGE: 'app.language',
	SIDEBAR_COLLAPSED: 'app.sidebar.collapsed',

	// ==================== 用户偏好 ====================
	PAGE_SIZE: 'user.preference.pageSize',
	LAST_VISITED: 'user.preference.lastVisited',
};

export default StorageKeys;
