// src/api/EventApi.js
import http from '@/api/http.js'

export const EventApi = {

    /* ===== 活动管理 ===== */
    insertEvent: (dto, file) => {
        const form = new FormData()
        form.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' }))
        form.append('file', file)
        return http.post('/api/event/insert', form, {
            headers: { 'Content-Type': 'multipart/form-data' }
        })
    },

    updateEvent: (dto, file, oldName) => {
        const form = new FormData()
        form.append('dto', new Blob([JSON.stringify(dto)], { type: 'application/json' }))
        form.append('file', file)
        return http.post('/api/event/update', form, {
            headers: { 'Content-Type': 'multipart/form-data' },
            params: { oldName }
        })
    },

    deleteByEventId: eventId => http.delete(`/api/event/${eventId}`),

    deleteByEventIds: eventIds => http.delete('/api/event/deleteByIds', { data: eventIds }),

    eventIPage: dto => http.post('/api/event/page', dto),

}