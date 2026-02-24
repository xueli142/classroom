/**
 * 统一消息提示工具
 * 封装 Element Plus 的 ElMessage 和 ElNotification
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

import { ElMessage, ElNotification } from 'element-plus';

/**
 * 消息提示工具类
 */
export const Message = {
	/**
	 * 成功消息
	 * @param {string} msg - 消息内容
	 * @param {Object} options - Element Plus Message 配置项
	 */
	success(msg, options = {}) {
		ElMessage.success({
			message: msg,
			duration: 3000,
			showClose: true,
			...options
		});
	},

	/**
	 * 错误消息
	 * @param {string} msg - 消息内容
	 * @param {Object} options - Element Plus Message 配置项
	 */
	error(msg, options = {}) {
		ElMessage.error({
			message: msg,
			duration: 5000,
			showClose: true,
			...options
		});
	},

	/**
	 * 警告消息
	 * @param {string} msg - 消息内容
	 * @param {Object} options - Element Plus Message 配置项
	 */
	warning(msg, options = {}) {
		ElMessage.warning({
			message: msg,
			duration: 4000,
			showClose: true,
			...options
		});
	},

	/**
	 * 信息消息
	 * @param {string} msg - 消息内容
	 * @param {Object} options - Element Plus Message 配置项
	 */
	info(msg, options = {}) {
		ElMessage.info({
			message: msg,
			duration: 3000,
			showClose: true,
			...options
		});
	},

	/**
	 * 通知提示
	 * @param {string} title - 标题
	 * @param {string} msg - 消息内容
	 * @param {string} type - 类型: success | warning | info | error
	 * @param {Object} options - Element Plus Notification 配置项
	 */
	notify(title, msg, type = 'info', options = {}) {
		ElNotification({
			title,
			message: msg,
			type,
			duration: 3000,
			...options
		});
	}
};

/**
 * 默认导出
 */
export default Message;
