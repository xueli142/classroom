// src/api/UserManageApi.js
import http from '@/api/http.js'

export const UserManageApi = {
    /* ===== 学生管理 ===== */
    pageStudent: data => http.post('/api/admin/student/page', data),
    addStudent: data => http.post('/api/admin/student', data),
    updateStudent: data => http.put('/api/admin/student', data),
    delStudent: userId => http.delete(`/api/admin/student/${userId}`),
    batchDelStudent: userIds => http.delete('/api/admin/student/batch', { data: userIds }),
    batchAddStudent: dtoList => http.post('/api/admin/student/batch', dtoList),

    /* ===== 教师管理 ===== */
    pageTeacher: data => http.post('/api/admin/teacher/page', data),
    addTeacher: data => http.post('/api/admin/teacher', data),
    updateTeacher: data => http.put('/api/admin/teacher', data),
    delTeacher: userId => http.delete(`/api/admin/teacher/${userId}`),
    batchDelTeacher: userIds => http.delete('/api/admin/teacher/batch', { data: userIds }),

    /* ===== 管理员管理 ===== */
    pageAdmin: data => http.post('/api/admin/admin/page', data),
    addAdmin: data => http.post('/api/admin/admin', data),
    updateAdmin: data => http.put('/api/admin/admin', data),
    delAdmin: userId => http.delete(`/api/admin/admin/${userId}`),
    batchDelAdmin: userIds => http.delete('/api/admin/admin/batch', { data: userIds })
}