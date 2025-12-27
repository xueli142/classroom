// src/api/ClassroomApi.js
import http from '@/api/http.js'

export const ClassroomApi = {

    /* ===== 教室管理 ===== */
    insertClassroom: (dto, file) => {
        const form = new FormData()
        form.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' }))
        form.append('file', file)
        return http.post('/api/classroom/insert', form, {
            headers: { 'Content-Type': 'multipart/form-data' }
        })
    },

    updateClassroom: (dto, file, oldName) => {
        const form = new FormData()
        form.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' }))
        form.append('file', file)
        return http.post('/api/classroom/update', form, {
            headers: { 'Content-Type': 'multipart/form-data' },
            params: { oldName }
        })
    },

    deleteByClassroomId: classroomId => http.delete(`/api/classroom/${classroomId}`),

    deleteByClassroomIds: classroomIds => http.delete('/api/classroom/deleteByIds', { data: classroomIds }),

    classroomIPage: dto => http.post('/api/classroom/page', dto)
}