import { defineStore } from 'pinia'
import { ref } from 'vue'
import { ThingApi} from "@/api/ThingApi.js";

export const useThingStore = defineStore('thing', () => {
    /* ---------------- 状态 ---------------- */
    const list    = ref([])
    const total   = ref(0)
    const loading = ref(false)
    const error   = ref(null)

    /* ---------------- 方法 ---------------- */
    async function page(payload = { page: 1, size: 10 }) {
        loading.value = true
        error.value   = null
        try {
            const res = await ThingApi.page(payload)
            const { records, total: count } = res.data
            list.value  = records
            total.value = count
        } catch (e) {
            error.value = e.message || '获取物品列表失败'
            list.value  = []
            total.value = 0
            throw e
        } finally {
            loading.value = false
        }
    }

    async function insertOne(dto) {
        loading.value = true
        error.value   = null
        try {
            await ThingApi.insertOne(dto)
        } catch (e) {
            error.value = e.message || '新增物品失败'
            throw e
        } finally {
            loading.value = false
        }
    }

    async function insertBatch(dtoList) {
        loading.value = true
        error.value   = null
        try {
            await ThingApi.insertBatch(dtoList)
        } catch (e) {
            error.value = e.message || '批量新增物品失败'
            throw e
        } finally {
            loading.value = false
        }
    }

    async function updateOne(entity) {
        loading.value = true
        error.value   = null
        try {
            await ThingApi.updateOne(entity)
        } catch (e) {
            error.value = e.message || '更新物品失败'
            throw e
        } finally {
            loading.value = false
        }
    }

    async function deleteOne(id) {
        loading.value = true
        error.value   = null
        try {
            await ThingApi.deleteOne(id)
        } catch (e) {
            error.value = e.message || '删除物品失败'
            throw e
        } finally {
            loading.value = false
        }
    }

    async function deleteBatch(idList) {
        loading.value = true
        error.value   = null
        try {
            await ThingApi.deleteBatch(idList)
        } catch (e) {
            error.value = e.message || '批量删除物品失败'
            throw e
        } finally {
            loading.value = false
        }
    }

    /* ---------------- 导出 ---------------- */
    return {
        list, total, loading, error,
        page, insertOne, insertBatch, updateOne, deleteOne, deleteBatch
    }
})