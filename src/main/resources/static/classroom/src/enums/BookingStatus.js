/**
 * 预约状态枚举
 * 与后端 BookingStatus.java 保持一致
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

export const BookingStatus = {
	PENDING: 'PENDING',       // 待审批
	APPROVED: 'APPROVED',     // 已批准
	REJECTED: 'REJECTED',     // 已拒绝
	CANCELLED: 'CANCELLED',   // 已取消
	COMPLETED: 'COMPLETED',   // 已完成
};

/**
 * 状态显示名称映射
 */
export const BookingStatusLabels = {
	[BookingStatus.PENDING]: '待审批',
	[BookingStatus.APPROVED]: '已批准',
	[BookingStatus.REJECTED]: '已拒绝',
	[BookingStatus.CANCELLED]: '已取消',
	[BookingStatus.COMPLETED]: '已完成',
};

/**
 * 状态对应的 Element Plus Tag 类型
 */
export const BookingStatusTypes = {
	[BookingStatus.PENDING]: 'warning',
	[BookingStatus.APPROVED]: 'success',
	[BookingStatus.REJECTED]: 'danger',
	[BookingStatus.CANCELLED]: 'info',
	[BookingStatus.COMPLETED]: 'info',
};

/**
 * 根据状态获取显示名称
 * @param {string} status - 状态值
 * @returns {string} 显示名称
 */
export function getBookingStatusLabel(status) {
	return BookingStatusLabels[status] || '未知状态';
}

/**
 * 根据状态获取 Tag 类型
 * @param {string} status - 状态值
 * @returns {string} Tag 类型
 */
export function getBookingStatusType(status) {
	return BookingStatusTypes[status] || 'info';
}

export default {
	BookingStatus,
	BookingStatusLabels,
	BookingStatusTypes,
	getBookingStatusLabel,
	getBookingStatusType,
};
