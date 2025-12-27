    // src/api/CourseApi.js
    import http from '@/api/http.js'

    export const CourseApi = {

        /* ===== 课程管理 ===== */
        insertCourse: (dto, file) => {
            const form = new FormData()
            form.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' }))
            form.append('file', file)
            return http.post('/api/course/insert', form, {
                headers: { 'Content-Type': 'multipart/form-data' }
            })
        },

        updateCourse: (dto, file, oldName) => {
            const form = new FormData()
            form.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' }))
            form.append('file', file)
            return http.post('/api/course/update', form, {
                headers: { 'Content-Type': 'multipart/form-data' },
                params: { oldName }
            })
        },

        deleteByCourseId: courseId => http.delete(`/api/course/${courseId}`),

        deleteByCourseIds: courseIds => http.delete('/api/course/deleteByIds', { data: courseIds }),

        courseIPage: dto => http.post('/api/course/page', dto),

    }