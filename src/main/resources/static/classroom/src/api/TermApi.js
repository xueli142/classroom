// src/api/TermApi.js
import http from '@/api/http.js'

export const TermApi = {

    /* ===== 学期管理 ===== */
    insertTerm: dto => http.post('/api/term', dto),

    updateTerm: dto => http.put('/api/term', dto),

    deleteByTermId: termId => http.delete(`/api/term/${termId}`),

    termPage: dto => http.post('/api/term/page', dto),

    /* ===== 手动刷新当前周数 ===== */
    manualRefreshWeekNow: () => http.post('/api/term/term/refresh-week')
}