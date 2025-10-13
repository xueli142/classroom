// src/stores/auth.js
import { defineStore } from 'pinia'
import { authHttp } from '@/api/auth'

// 工具：安全解析
function safeJson(key, def = null) {
    try {
        const raw = localStorage.getItem(key)
        return raw ? JSON.parse(raw) : def
    } catch {
        localStorage.removeItem(key)   // 脏数据直接清掉
        return def
    }
}

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem('token') || '',
        user:  safeJson('user'),        // ← 不会抛异常
        role:  localStorage.getItem('role') || ''
    }),

    getters: {
        isLogin: (s) => !!s.token
    },

    actions: {
        async login(role, form) {
            // 现在走的 url = http://localhost:9090/auth/login/teacher
            const {data} = await authHttp.post(`/auth/login/${role}`, form)
            this.token = data.token
            this.user = data.user
            this.role = role
            localStorage.setItem('token', data.token)
            localStorage.setItem('user', JSON.stringify(data.user))
            localStorage.setItem('role', role)
        },




        logout() {
            this.token = ''
            this.user = null
            this.role = ''
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            localStorage.removeItem('role')
            location.replace(`/${this.role || 'student'}/login`)
        },
        async register(role, form) {
            const {data} = await authHttp.post(`/auth/register/${role}`, form)
            this.token = data.token
            this.user = data.user
            this.role = role
            localStorage.setItem('token', data.token)
            localStorage.setItem('user', JSON.stringify(data.user))
            localStorage.setItem('role', role)
            location.replace(`/${role}/dashboard`)
        },
    }
})