/**
 * HTTP 请求配置
 * 基于 axios 封装，统一处理请求/响应
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

import axios from 'axios';
import router from '@/router/index.js';

const baseURL = import.meta?.env?.VITE_API_BASE_URL || '/';

/**
 * axios 实例
 */
const http = axios.create({
	baseURL,
	timeout: 15000,
	headers: {
		Accept: 'application/json',
		'Content-Type': 'application/json;charset=UTF-8'
	},
});

/**
 * 清除本地认证信息
 */
function clearAuth() {
	localStorage.removeItem('auth.user');
	localStorage.removeItem('auth.token');
	delete http.defaults.headers.common['Authorization'];
}

/**
 * 从本地恢复 token，避免刷新后丢失鉴权头
 */
const savedToken = localStorage.getItem('auth.token');
if (savedToken) {
	http.defaults.headers.common['Authorization'] = `Bearer ${savedToken}`;
}

/**
 * 请求拦截器
 * 动态注入最新 token
 */
http.interceptors.request.use(
	(config) => {
		const tk = localStorage.getItem('auth.token');
		if (tk) {
			config.headers['Authorization'] = `Bearer ${tk}`;
		}
		return config;
	},
	(error) => {
		return Promise.reject(error);
	}
);

/**
 * 响应拦截器
 * 统一处理 HTTP 状态码和业务错误码
 */
http.interceptors.response.use(
	(resp) => {
		// 成功响应直接返回，交给 apiHelper 处理业务逻辑
		return resp;
	},
	(error) => {
		// HTTP 错误处理
		if (error.response) {
			const { status, data } = error.response;

			switch (status) {
				case 401:
					// 未授权 - 清除认证信息并跳转到登录页
					clearAuth();
					// 避免在登录页重复跳转
					if (router.currentRoute.value.path !== '/login') {
						router.push('/login');
					}
					break;
				case 403:
					error.message = data?.message || '无权限访问';
					break;
				case 404:
					error.message = data?.message || '请求的资源不存在';
					break;
				case 500:
					error.message = data?.message || '服务器内部错误';
					break;
				case 502:
				case 503:
				case 504:
					error.message = '服务暂时不可用，请稍后重试';
					break;
				default:
					error.message = data?.message || `请求失败 (${status})`;
			}
		} else if (error.request) {
			// 请求已发送但没有收到响应
			error.message = '网络连接失败，请检查网络';
		} else {
			// 其他错误 (如取消请求)
			error.message = error.message || '请求失败';
		}

		return Promise.reject(error);
	}
);

export default http;
