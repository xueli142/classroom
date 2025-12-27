import http from '@/api/http.js'

export const AdviceReplyApi = {
    /* ===== 意见回复管理 ===== */

    // 新增单条
    insertOne: reply => http.post('/api/advice-reply', reply),

    // 单条更新
    updateOne: reply => http.put('/api/advice-reply/updateById', reply),

    // 单条删除
    deleteOne: adviceReplyId => http.delete(`/api/advice-reply/${adviceReplyId}`),

    // 批量删除
    deleteBatch: ids => http.delete('/api/advice-reply/deleteByIds', { data: ids }),

    // 分页查询
    replyPage: dto => http.post('/api/advice-reply/page', dto),

    //通过adviceId查询
    // 通过 adviceId 查询
    selectByAdviceId: adviceId => http.post(`/api/advice-reply/${adviceId}`)
}