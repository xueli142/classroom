import { defineStore } from 'pinia'
import { ref } from 'vue'
import { AdviceApi } from '@/api/AdviceApi.js'

export const useAdviceStore = defineStore('advice', () => {
    /* ---------------- 状态 ---------------- */
    const list    = ref([])   // 意见列表
    const total   = ref(0)    // 总条数
    const loading = ref(false)
    const error   = ref(null)

    /* ---------------- 方法 ---------------- */

    // 分页查询
    async function advicePage(payload = { page: 1, size: 10 }) {
        loading.value = true
        error.value   = null
        try {
            const res = await AdviceApi.advicePage(payload)
            const { records, total: count } = res.data.data
            list.value  = records
            total.value = count
        } catch (e) {
            error.value = e.message || '获取意见列表失败'
            list.value  = []
            total.value = 0
            throw e
        } finally {
            loading.value = false
        }
    }

    // 新增单条
    async function insertOne(advice) {
        loading.value = true
        error.value   = null
        try {
            await AdviceApi.insertOne(advice)
        } catch (err) {
            error.value = err.message || '新增意见失败'
            throw err
        } finally {
            loading.value = false
        }
    }

    // 单条更新
    async function updateOne(advice) {
        loading.value = true
        error.value   = null
        try {
            await AdviceApi.updateOne(advice)
        } catch (err) {
            error.value = err.message || '更新意见失败'
            throw err
        } finally {
            loading.value = false
        }
    }

    // 单条删除
    async function deleteOne(adviceId) {
        loading.value = true
        error.value   = null
        try {
            await AdviceApi.deleteOne(adviceId)
        } catch (err) {
            error.value = err.message || '删除意见失败'
            throw err
        } finally {
            loading.value = false
        }
    }

    // 批量删除
    async function deleteBatch(ids) {
        loading.value = true
        error.value   = null
        try {
            await AdviceApi.deleteBatch(ids)
        } catch (err) {
            error.value = err.message || '批量删除意见失败'
            throw err
        } finally {
            loading.value = false
        }
    }

    /* ---------------- 导出 ---------------- */
    return {
        list,
        total,
        loading,
        error,
        advicePage,
        insertOne,
        updateOne,
        deleteOne,
        deleteBatch
    }
})