/**
 * 用户角色枚举
 * 与后端 UserRole.java 保持一致
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

export const UserRole = {
	ADMIN: 'ADMIN',
	TEACHER: 'TEACHER',
	STUDENT: 'STUDENT',
};

/**
 * 角色显示名称映射
 */
export const UserRoleLabels = {
	[UserRole.ADMIN]: '管理员',
	[UserRole.TEACHER]: '教师',
	[UserRole.STUDENT]: '学生',
};

/**
 * 角色首页路径映射
 */
export const UserRoleHomePaths = {
	[UserRole.ADMIN]: '/adminDashboard',
	[UserRole.TEACHER]: '/teacherDashboard',
	[UserRole.STUDENT]: '/studentDashboard',
};

/**
 * 根据角色获取显示名称
 * @param {string} role - 角色值
 * @returns {string} 显示名称
 */
export function getUserRoleLabel(role) {
	return UserRoleLabels[role] || '未知角色';
}

/**
 * 根据角色获取首页路径
 * @param {string} role - 角色值
 * @returns {string} 首页路径
 */
export function getUserRoleHomePath(role) {
	return UserRoleHomePaths[role] || '/login';
}

export default {
	UserRole,
	UserRoleLabels,
	UserRoleHomePaths,
	getUserRoleLabel,
	getUserRoleHomePath,
};
