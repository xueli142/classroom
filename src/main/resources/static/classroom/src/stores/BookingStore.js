/**
 * 预订管理 Store
 */
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { BookingApi } from '@/api/BookingApi.js'

export const useBookingStore = defineStore('booking', () => {
  // ========== 状态 ==========
  const list = ref([])
  const total = ref(0)
  const loading = ref(false)
  const error = ref(null)

  // ========== 操作方法 ==========

  /**
   * 分页查询预订列表
   */
  async function bookingIPage(payload) {
    loading.value = true
    error.value = null
    try {
      const res = await BookingApi.bookingIPage(payload)
      const { records, total: count } = res.data.data
      list.value = records
      total.value = count
    } catch (err) {
      error.value = err.message || '获取预订列表失败'
      list.value = []
      total.value = 0
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 批量新增预订
   */
  async function insertBatch(dtoList) {
    loading.value = true
    error.value = null
    try {
      await BookingApi.insertBatch(dtoList)
    } catch (err) {
      error.value = err.message || '批量预订失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 更新预订
   */
  async function updateById(booking) {
    loading.value = true
    error.value = null
    try {
      await BookingApi.updateById(booking)
    } catch (err) {
      error.value = err.message || '更新预订失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 删除预订
   */
  async function deleteById(bookingId) {
    loading.value = true
    error.value = null
    try {
      await BookingApi.deleteById(bookingId)
    } catch (err) {
      error.value = err.message || '删除预订失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 批量删除预订
   */
  async function deleteByIds(ids) {
    loading.value = true
    error.value = null
    try {
      await BookingApi.deleteByIds(ids)
    } catch (err) {
      error.value = err.message || '批量删除预订失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    list,
    total,
    loading,
    error,
    bookingIPage,
    insertBatch,
    updateById,
    deleteById,
    deleteByIds
  }
})
