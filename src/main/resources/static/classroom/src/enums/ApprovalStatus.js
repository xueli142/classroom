/**
 * 审批状态枚举
 * 与后端 ApprovalStatus.java 保持一致
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

export const ApprovalStatus = {
	PENDING: 'PENDING',       // 待审批
	APPROVED: 'APPROVED',     // 已批准
	REJECTED: 'REJECTED',     // 已拒绝
};

/**
 * 状态显示名称映射
 */
export const ApprovalStatusLabels = {
	[ApprovalStatus.PENDING]: '待审批',
	[ApprovalStatus.APPROVED]: '已批准',
	[ApprovalStatus.REJECTED]: '已拒绝',
};

/**
 * 状态对应的 Element Plus Tag 类型
 */
export const ApprovalStatusTypes = {
	[ApprovalStatus.PENDING]: 'warning',
	[ApprovalStatus.APPROVED]: 'success',
	[ApprovalStatus.REJECTED]: 'danger',
};

/**
 * 根据状态获取显示名称
 * @param {string} status - 状态值
 * @returns {string} 显示名称
 */
export function getApprovalStatusLabel(status) {
	return ApprovalStatusLabels[status] || '未知状态';
}

/**
 * 根据状态获取 Tag 类型
 * @param {string} status - 状态值
 * @returns {string} Tag 类型
 */
export function getApprovalStatusType(status) {
	return ApprovalStatusTypes[status] || 'info';
}

export default {
	ApprovalStatus,
	ApprovalStatusLabels,
	ApprovalStatusTypes,
	getApprovalStatusLabel,
	getApprovalStatusType,
};
