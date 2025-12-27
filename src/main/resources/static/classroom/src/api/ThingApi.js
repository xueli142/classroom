import http from '@/api/http.js'

export const ThingApi = {
    /* ===== 物品管理 ===== */

    // 单条新增
    insertOne: dto => http.post('/api/thing', dto),

    // 批量新增
    insertBatch: dtoList => http.post('/api/thing/batch', dtoList),

    // 单条更新
    updateOne: thing => http.put('/api/thing', thing),

    // 单条删除
    deleteOne: thingId => http.delete(`/api/thing/${thingId}`),

    // 批量删除
    deleteBatch: idList => http.delete('/api/thing', { data: idList }),

    // 分页+条件查询
    page: dto => http.post('/api/thing/page', dto),

    // 单条详情
    getOne: thingId => http.get(`/api/thing/${thingId}`)
}