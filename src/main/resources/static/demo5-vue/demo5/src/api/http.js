import axios from 'axios'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'

const instance = axios.create({
    baseURL: '/api'   // 后端地址
})

// 请求拦截：统一加 token
instance.interceptors.request.use(cfg => {
    const store = useAuthStore()
    if (store.token) {
        cfg.headers.Authorization = `Bearer ${store.token}`
    }
    return cfg
})

// 响应拦截：401 清空 token 并跳登录
instance.interceptors.response.use(
    res => res,
    err => {
        if (err.response?.status === 401) {
            const store = useAuthStore()
            store.logout()
        }
        return Promise.reject(err)
    }
)

export default instance