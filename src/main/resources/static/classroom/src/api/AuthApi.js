import http from '../api/http.js';

export const AuthApi = {
    loginUser: (payload) =>
        http.post('/auth/user/login', payload).then(r => r.data),

    changePassword: (payload) =>
        http.post('/auth/user/changePassword', payload).then(r => r.data),
    changeUid: (payload) =>
        http.post('/auth/user/changeUsername', payload).then(r => r.data),
    changePhone: (payload) =>
        http.post('/auth/user/changePhone', payload).then(r => r.data),

     insertImage :(payload) => {
         const formData = new FormData()
         formData.append('file', payload)
        // 发送 POST 请求，后端接口为 @PostMapping("/img")
        return http.post(`/auth/user/insertImage`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data' // 显式指定表单类型
            }
        }).then(res => {
            // 假设后端返回的是图片 URL 字符串（如 "https://xxx.jpg"）
            return res.data // 直接返回 URL，供编辑器插入
        })
    },

    changeImage: (file,oldName) => {
        const formData = new FormData()
        formData.append('file', file)
        formData.append('oldName', oldName)
        return http.post(`/auth/user/changeImage`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data' // 显式指定表单类型
            }
        }).then(res => {
            return res.data // 直接返回 URL，供编辑器插入
        })
    },

    logout: (token) =>
        http.post(`/auth/user/logout?token=${encodeURIComponent(token)}`)
            .then(r => r.data),






}