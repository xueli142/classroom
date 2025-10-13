import http from './http'   // 你前面已经配好 token 拦截器、baseURL = '/api'

// 获取分页数据
export const getPage = (page = 0) =>
    http.get('/classroom/page', { params: { page } }).then(res => res.data.data)

// 增加教室
export const createClassroom = form =>
    http.post('/classroom', form).then(res => res.data.data)

// 删除教室
export const deleteClassroom = id =>
    http.delete(`/classroom/${id}`).then(res => res.data.data)

// 修改教室
export const updateClassroom = (id, form) =>
    http.put(`/classroom/${id}`, form).then(res => res.data.data)

// 查询单个教室
export const getClassroom = id =>
    http.get(`/classroom/${id}`).then(res => res.data.data)

// 新增/修改教室图片（multipart）
export const createClassroomImage = (id, file) => {
    const fd = new FormData()
    fd.append('image', file)
    return http.post(`/classroom/${id}/image`, fd, {
        headers: { 'Content-Type': 'multipart/form-data' }
    })
}

export const updateClassroomImage = (id, file) => {
    const fd = new FormData()
    fd.append('image', file)
    return http.put(`/classroom/${id}/image`, fd, {
        headers: { 'Content-Type': 'multipart/form-data' }
    })
}