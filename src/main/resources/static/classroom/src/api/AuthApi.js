/**
 * 认证相关 API
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

import http from '@/api/http.js';
import { withApiHandler } from '@/api/apiHelper.js';
import { ApiEndpoints } from '@/constants/ApiConstants.js';

export const AuthApi = {
	/**
	 * 用户登录
	 * @param {Object} payload - { uid, password }
	 * @returns {Promise<{ token, user }>}
	 */
	loginUser: (payload) => {
		return withApiHandler(
			http.post(ApiEndpoints.AUTH.LOGIN, payload),
			{ showSuccess: true, successMsg: '登录成功' }
		);
	},

	/**
	 * 修改密码
	 * @param {Object} payload - { uid, oldPassword, newPassword }
	 * @returns {Promise<void>}
	 */
	changePassword: (payload) => {
		return withApiHandler(
			http.post(ApiEndpoints.AUTH.CHANGE_PASSWORD, payload),
			{ showSuccess: true, successMsg: '密码修改成功' }
		);
	},

	/**
	 * 修改用户名
	 * @param {Object} payload - { userId, name }
	 * @returns {Promise<void>}
	 */
	changeUsername: (payload) => {
		return withApiHandler(
			http.post(ApiEndpoints.AUTH.CHANGE_USERNAME, payload),
			{ showSuccess: true, successMsg: '用户名修改成功' }
		);
	},

	/**
	 * 修改手机号
	 * @param {Object} payload - { userId, phone }
	 * @returns {Promise<void>}
	 */
	changePhone: (payload) => {
		return withApiHandler(
			http.post(ApiEndpoints.AUTH.CHANGE_PHONE, payload),
			{ showSuccess: true, successMsg: '手机号修改成功' }
		);
	},

	/**
	 * 上传头像
	 * @param {File} file - 图片文件
	 * @param {string} userId - 用户 ID
	 * @returns {Promise<void>}
	 */
	insertImage: (file, userId) => {
		const formData = new FormData();
		formData.append('file', file);
		formData.append('userId', userId);
		return withApiHandler(
			http.post(ApiEndpoints.AUTH.UPLOAD_IMAGE, formData, {
				headers: { 'Content-Type': 'multipart/form-data' }
			}),
			{ showSuccess: true, successMsg: '头像上传成功' }
		);
	},

	/**
	 * 更换头像
	 * @param {File} file - 新头像文件
	 * @param {string} oldName - 旧文件名
	 * @param {string} userId - 用户 ID
	 * @returns {Promise<void>}
	 */
	changeImage: (file, oldName, userId) => {
		const formData = new FormData();
		formData.append('file', file);
		formData.append('oldName', oldName);
		formData.append('userId', userId);
		return withApiHandler(
			http.post(ApiEndpoints.AUTH.CHANGE_IMAGE, formData, {
				headers: { 'Content-Type': 'multipart/form-data' }
			}),
			{ showSuccess: true, successMsg: '头像更新成功' }
		);
	},

	/**
	 * 登出
	 * @param {string} token - JWT Token
	 * @returns {Promise<void>}
	 */
	logout: (token) => {
		return withApiHandler(
			http.post(`${ApiEndpoints.AUTH.LOGOUT}?token=${encodeURIComponent(token)}`),
			{ showError: false }  // 登出失败不显示错误，静默处理
		);
	},
};

export default AuthApi;
