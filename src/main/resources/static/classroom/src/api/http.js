
// File: src/main/resources/static/demo5/src/api/http.js
import axios from 'axios';

const baseURL = import.meta?.env?.VITE_API_BASE_URL || '/';

const http = axios.create({
    baseURL,
    timeout: 15000,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json;charset=UTF-8'
    },
});

// 从本地恢复 token，避免刷新后丢失鉴权头
const savedToken = localStorage.getItem('auth.token');
if (savedToken) {
    http.defaults.headers.common['Authorization'] = `Bearer ${savedToken}`;
}

// 请求拦截器：可在此动态注入最新 token
http.interceptors.request.use((config) => {
    const tk = localStorage.getItem('auth.token');
    if (tk) {
        config.headers['Authorization'] = `Bearer ${tk}`;
    }
    return config;
});

// 响应拦截器：可根据后端约定统一处理 401、错误信息等
http.interceptors.response.use(
    (resp) => resp,
    (error) => {
        // 可在此处理 401 等逻辑
        return Promise.reject(error);
    }
);

export default http;

// 提示：在应用启动处调用一次 useAuthStore().initFromStorage() 恢复登录态