/**
 * 学生管理 Store
 */
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { UserManageApi } from '@/api/UserManageApi.js'

export const useStudentStore = defineStore('student', () => {
  // ========== 状态 ==========
  const list = ref([])
  const total = ref(0)
  const loading = ref(false)
  const error = ref(null)
  const user = ref(null)
  const token = ref('')

  // ========== 操作方法 ==========

  /**
   * 分页查询学生列表
   */
  async function pageStudent(payload) {
    loading.value = true
    error.value = null
    try {
      const res = await UserManageApi.pageStudent(payload)
      const { records, total: count } = res.data.data
      list.value = records
      total.value = count
    } catch (err) {
      error.value = err.message || '获取学生列表失败'
      list.value = []
      total.value = 0
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 新增学生
   */
  async function addStudent(payload) {
    loading.value = true
    error.value = null
    try {
      await UserManageApi.addStudent(payload)
    } catch (err) {
      error.value = err.message || '新增学生失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 更新学生
   */
  async function updateStudent(payload) {
    loading.value = true
    error.value = null
    try {
      await UserManageApi.updateStudent(payload)
    } catch (err) {
      error.value = err.message || '修改学生失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 删除学生
   */
  async function delStudent(userId) {
    loading.value = true
    error.value = null
    try {
      await UserManageApi.delStudent(userId)
    } catch (err) {
      error.value = err.message || '删除学生失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 批量删除学生
   */
  async function batchDelStudent(userIds) {
    loading.value = true
    error.value = null
    try {
      await UserManageApi.batchDelStudent(userIds)
    } catch (err) {
      error.value = err.message || '批量删除学生失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 批量新增学生
   */
  async function batchAddStudent(dtoList) {
    loading.value = true
    error.value = null
    try {
      await UserManageApi.batchAddStudent(dtoList)
    } catch (err) {
      error.value = err.message || '批量新增学生失败'
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
    user,
    token,
    pageStudent,
    addStudent,
    updateStudent,
    delStudent,
    batchDelStudent,
    batchAddStudent
  }
})
