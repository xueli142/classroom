    import http from '@/api/http.js'

    export const AdviceApi = {
        /* ===== 意见反馈管理 ===== */

        // 新增单条
        insertOne: advice => http.post('/api/advice/insert', advice),

        // 单条更新（by adviceId）
        updateOne: advice => http.put('/api/advice/updateById', advice),

        // 单条删除（by adviceId）
        deleteOne: adviceId => http.delete(`/api/advice/${adviceId}`),

        // 批量删除（by adviceIds）
        deleteBatch: ids => http.delete('/api/advice/deleteByIds', { data: ids }),

        // 通用分页查询
        advicePage: dto => http.post('/api/advice/page', dto)
    }