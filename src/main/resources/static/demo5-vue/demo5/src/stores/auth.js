// src/stores/auth.js
import { defineStore } from 'pinia'
import http from '@/api/http'

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
            const { data } = await http.post(`/auth/login/${role}`, form)
            this.token = data.token
            this.user  = data.user
            this.role  = role

            localStorage.setItem('token', data.token)
            localStorage.setItem('user',  JSON.stringify(data.user))
            localStorage.setItem('role',  role)
        },

        logout() {
            this.token = ''
            this.user  = null
            this.role  = ''
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            localStorage.removeItem('role')
            location.replace(`/${this.role || 'student'}/login`)
        },
        async register(role, form) {
            // 1. 提交注册
            const { data } = await http.post(`/auth/register/${role}`, form)



            this.user  = data.user
            this.role  = role

            localStorage.setItem('token', data.token)
            localStorage.setItem('user',  JSON.stringify(data.user))
            localStorage.setItem('role',  role)

            // 3. 跳到该角色首页（或登录页，按你业务来）
            location.replace(`/${role}/dashboard`)
        }
    }
})