// src/api/StudentCourseApi.js
import http from '@/api/http.js'

export const StudentCourseApi = {

    /* ===== 学生选课管理 ===== */
    insertStudentCourse: (studentCourse) => {
        return http.post('/api/student-course/insert', studentCourse)
    },

    insertBatch: (list) => {
        return http.post('/api/student-course/insertBatch', list)
    },

    updateStudentCourse: (studentCourse) => {
        return http.put('/api/student-course/update', studentCourse)
    },

    deleteByUserId: (userId) => {
        return http.delete(`/api/student-course/by-user/${userId}`)
    },

    deleteByCourseId: (courseId) => {
        return http.delete(`/api/student-course/by-course/${courseId}`)
    },

    deleteById: (id) => {
        return http.delete(`/api/student-course/${id}`)
    },

    deleteByUserIds: (userIds) => {
        return http.delete('/api/student-course/deleteByUserIds', { data: userIds })
    },

    deleteByCourseIds: (courseIds) => {
        return http.delete('/api/student-course/deleteByCourseIds', { data: courseIds })
    },

    studentCourseIPage: (dto) => {
        return http.post('/api/student-course/page', dto)
    },

    insertOne: (userId, courseId) => http.post('/api/student-course/insertOne', null, { params: { userId, courseId }}),

}