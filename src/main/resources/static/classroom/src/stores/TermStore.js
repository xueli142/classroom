// stores/termStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { TermApi } from '@/api/TermApi.js';

export const useTermStore = defineStore('term', () => {
    // 状态
    const list    = ref([]);
    const total   = ref(0);
    const loading = ref(false);
    const error   = ref(null);

    /* ===== 学期管理 ===== */

    // 分页查询
    // stores/termStore.js
    async function termPage(payload = { page: 1, size: 10 }) {
        loading.value = true;
        error.value   = null;
        try {
            const res = await TermApi.termPage(payload);   // 原始完整报文
            const { records, total: count } = res.data.data;
            list.value  = records;
            total.value = count;
            // ① 返回完整报文（兼容旧调用）
            // ② 再单独把第一条学期抛出去，方便直接拿 weekNow
            return { full: res.data, firstTerm: records[0] };
        } catch (e) {
            error.value = e.message || '获取学期列表失败';
            list.value  = [];
            total.value = 0;
            throw e;
        } finally {
            loading.value = false;
        }
    }
    // 新增学期
    async function insertTerm(dto) {
        loading.value = true;
        error.value   = null;
        try {
            await TermApi.insertTerm(dto);
        } catch (err) {
            error.value = err.message || '新增学期失败';
            throw err;
        } finally {
            loading.value = false;
        }
    }

    // 更新学期
    async function updateTerm(dto) {
        loading.value = true;
        error.value   = null;
        try {
            await TermApi.updateTerm(dto);
        } catch (err) {
            error.value = err.message || '修改学期失败';
            throw err;
        } finally {
            loading.value = false;
        }
    }

    // 删除学期
    async function deleteByTermId(termId) {
        loading.value = true;
        error.value   = null;
        try {
            await TermApi.deleteByTermId(termId);
        } catch (err) {
            error.value = err.message || '删除学期失败';
            throw err;
        } finally {
            loading.value = false;
        }
    }

    return {
        list,
        total,
        loading,
        error,
        termPage,
        insertTerm,
        updateTerm,
        deleteByTermId
    };
});