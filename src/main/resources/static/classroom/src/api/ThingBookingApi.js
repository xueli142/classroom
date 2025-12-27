
import http from '@/api/http.js'

export const ThingBookingApi = {
    /* ===== 物品预约管理 ===== */

    // 单条新增
    insertOne: dto => http.post('/api/thing-booking', dto),

    // 批量新增
    insertBatch: dtoList => http.post('/api/thing-booking/batch', dtoList),

    // 单条更新
    updateOne: entity => http.put('/api/thing-booking', entity),

    // 单条删除
    deleteOne: id => http.delete(`/api/thing-booking/${id}`),

    // 批量删除
    deleteBatch: idList => http.delete('/api/thing-booking', { data: idList }),

    // 分页+条件查询
    page: dto => http.post('/api/thing-booking/page', dto),

    // 单条详情
    getOne: id => http.get(`/api/thing-booking/${id}`)
}