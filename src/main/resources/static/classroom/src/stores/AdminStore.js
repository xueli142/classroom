/**
 * 管理员管理 Store
 */
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { UserManageApi } from '@/api/UserManageApi.js'

export const useAdminStore = defineStore('admin', () => {
  // ========== 状态 ==========
  const list = ref([])
  const total = ref(0)
  const loading = ref(false)
  const error = ref(null)

  // ========== 操作方法 ==========

  /**
   * 分页查询管理员列表
   */
  async function pageAdmin(payload) {
    loading.value = true
    error.value = null
    try {
      const res = await UserManageApi.pageAdmin(payload)
      const { records, total: count } = res.data.data
      list.value = records
      total.value = count
    } catch (err) {
      error.value = err.message || '获取管理员列表失败'
      list.value = []
      total.value = 0
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 新增管理员
   */
  async function addAdmin(payload) {
    loading.value = true
    error.value = null
    try {
      await UserManageApi.addAdmin(payload)
    } catch (err) {
      error.value = err.message || '新增管理员失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 更新管理员
   */
  async function updateAdmin(payload) {
    loading.value = true
    error.value = null
    try {
      await UserManageApi.updateAdmin(payload)
    } catch (err) {
      error.value = err.message || '修改管理员失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 删除管理员
   */
  async function delAdmin(userId) {
    loading.value = true
    error.value = null
    try {
      await UserManageApi.delAdmin(userId)
    } catch (err) {
      error.value = err.message || '删除管理员失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 批量删除管理员
   */
  async function batchDelAdmin(userIds) {
    loading.value = true
    error.value = null
    try {
      await UserManageApi.batchDelAdmin(userIds)
    } catch (err) {
      error.value = err.message || '批量删除管理员失败'
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
    pageAdmin,
    addAdmin,
    updateAdmin,
    delAdmin,
    batchDelAdmin
  }
})
