/**
 * API 请求处理工具
 * 统一处理后端 ResponseDto 格式的响应
 *
 * 后端响应格式:
 * {
 *   code: 200,
 *   success: true,
 *   message: "操作成功",
 *   data: {...}
 * }
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

import { Message } from '@/utils/message';

/**
 * 统一处理 API 响应
 *
 * @param {Promise} request - axios 请求 promise
 * @param {Object} options - 配置选项
 * @param {boolean} options.showSuccess - 成功时是否显示消息 (默认: false)
 * @param {boolean} options.showError - 失败时是否显示消息 (默认: true)
 * @param {string} options.successMsg - 成功时的自定义消息
 * @param {boolean} options.silent - 完全静默模式 (默认: false)
 * @returns {Promise} 返回 data.data 或抛出错误
 */
export function withApiHandler(request, options = {}) {
	const {
		showSuccess = false,    // 成功时显示消息
		showError = true,       // 失败时显示消息
		successMsg = '操作成功',
		silent = false,         // 完全静默模式
	} = options;

	return request
		.then((resp) => {
			const { data } = resp;

			// 处理 ResponseDto 格式
			// 后端返回 { code, success, message, data }
			const isSuccess = data.code === 200 || data.success === true;

			if (isSuccess) {
				if (showSuccess && !silent) {
					Message.success(data.message || successMsg);
				}
				// 返回实际数据
				return data.data !== undefined ? data.data : data;
			}

			// 业务错误 (code !== 200 或 success !== true)
			const errorMsg = data.message || '操作失败';
			if (showError && !silent) {
				Message.error(errorMsg);
			}
			const error = new Error(errorMsg);
			error.code = data.code;
			error.response = resp;
			return Promise.reject(error);
		})
		.catch((error) => {
			// 网络错误或 HTTP 状态码错误
			if (showError && !silent) {
				let msg = '请求失败';

				if (error.response) {
					// 服务器返回了错误响应
					const status = error.response.status;
					const data = error.response.data;

					// 从响应中提取错误消息
					if (data?.message) {
						msg = data.message;
					} else {
						switch (status) {
							case 400:
								msg = '请求参数错误';
								break;
							case 401:
								msg = '未授权，请重新登录';
								break;
							case 403:
								msg = '无权限访问';
								break;
							case 404:
								msg = '请求的资源不存在';
								break;
							case 500:
								msg = '服务器内部错误';
								break;
							case 502:
							case 503:
							case 504:
								msg = '服务暂时不可用';
								break;
							default:
								msg = `请求失败 (${status})`;
						}
					}
				} else if (error.request) {
					// 请求已发送但没有收到响应
					msg = '网络连接失败，请检查网络';
				} else {
					// 其他错误 (如取消请求)
					msg = error.message || msg;
				}

				Message.error(msg);
			}

			return Promise.reject(error);
		});
}

/**
 * 用于分页查询的辅助函数
 *
 * @param {Promise} request - axios 请求 promise
 * @param {Object} options - 配置选项
 * @returns {Promise} 返回 { records, total, page, size }
 */
export function withPageApiHandler(request, options = {}) {
	return withApiHandler(request, options);
}

/**
 * 用于文件上传的辅助函数
 *
 * @param {Promise} request - axios 请求 promise
 * @param {Object} options - 配置选项
 * @returns {Promise}
 */
export function withUploadHandler(request, options = {}) {
	const uploadOptions = {
		showSuccess: true,
		successMsg: '上传成功',
		...options
	};
	return withApiHandler(request, uploadOptions);
}

/**
 * 用于删除操作的辅助函数
 *
 * @param {Promise} request - axios 请求 promise
 * @param {Object} options - 配置选项
 * @returns {Promise}
 */
export function withDeleteHandler(request, options = {}) {
	const deleteOptions = {
		showSuccess: true,
		successMsg: '删除成功',
		...options
	};
	return withApiHandler(request, deleteOptions);
}

/**
 * 静默请求 - 不显示任何消息
 *
 * @param {Promise} request - axios 请求 promise
 * @returns {Promise}
 */
export function silentRequest(request) {
	return withApiHandler(request, { silent: true });
}

export default {
	withApiHandler,
	withPageApiHandler,
	withUploadHandler,
	withDeleteHandler,
	silentRequest
};
