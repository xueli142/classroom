// src/api/BookingApi.js
import http from '@/api/http.js'

export const BookingApi = {

    /* ===== 预订管理 ===== */
    insertBatch: dtoList => http.post('/api/booking/insertBatch', dtoList),

    updateById: booking => http.put('/api/booking/updateById', booking),

    deleteById: bookingId => http.delete(`/api/booking/${bookingId}`),

    deleteByIds: ids => http.delete('/api/booking/deleteByIds', { data: ids }),


}