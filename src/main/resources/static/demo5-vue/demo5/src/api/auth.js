// src/api/authHttp.js
import axios from 'axios'

export const authHttp = axios.create({
    baseURL: 'http://localhost:9090',   // 直连后端
    timeout: 10000,
    headers: { 'Content-Type': 'application/json' }
})