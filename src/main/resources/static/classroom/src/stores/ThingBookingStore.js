import { defineStore } from 'pinia'
import { ref } from 'vue'
import { ThingBookingApi} from "@/api/ThingBookingApi.js";

export const useThingBookingStore = defineStore('thingBooking', () => {
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
            const res = await ThingBookingApi.page(payload)
            const { records, total: count } = res.data.data
            list.value  = records
            total.value = count
        } catch (e) {
            error.value = e.message || '获取预约列表失败'
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
            await ThingBookingApi.insertOne(dto)
        } catch (e) {
            error.value = e.message || '新增预约失败'
            throw e
        } finally {
            loading.value = false
        }
    }

    async function insertBatch(dtoList) {
        loading.value = true
        error.value   = null
        try {
            await ThingBookingApi.insertBatch(dtoList)
        } catch (e) {
            error.value = e.message || '批量新增预约失败'
            throw e
        } finally {
            loading.value = false
        }
    }

    async function updateOne(entity) {
        loading.value = true
        error.value   = null
        try {
            await ThingBookingApi.updateOne(entity)
        } catch (e) {
            error.value = e.message || '更新预约失败'
            throw e
        } finally {
            loading.value = false
        }
    }

    async function deleteOne(id) {
        loading.value = true
        error.value   = null
        try {
            await ThingBookingApi.deleteOne(id)
        } catch (e) {
            error.value = e.message || '删除预约失败'
            throw e
        } finally {
            loading.value = false
        }
    }

    async function deleteBatch(idList) {
        loading.value = true
        error.value   = null
        try {
            await ThingBookingApi.deleteBatch(idList)
        } catch (e) {
            error.value = e.message || '批量删除预约失败'
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