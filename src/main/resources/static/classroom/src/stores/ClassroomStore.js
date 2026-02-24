/**
 * 教室管理 Store
 */
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { ClassroomApi } from '@/api/ClassroomApi.js'

export const useClassroomStore = defineStore('classroom', () => {
  // ========== 状态 ==========
  const list = ref([])
  const total = ref(0)
  const loading = ref(false)
  const error = ref(null)

  // ========== 操作方法 ==========

  /**
   * 分页查询教室列表
   */
  async function classroomIPage(payload) {
    loading.value = true
    error.value = null
    try {
      const res = await ClassroomApi.classroomIPage(payload)
      const { records, total: count } = res.data.data
      list.value = records
      total.value = count
    } catch (err) {
      error.value = err.message || '获取教室列表失败'
      list.value = []
      total.value = 0
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 新增教室
   */
  async function insertClassroom(dto, file) {
    loading.value = true
    error.value = null
    try {
      await ClassroomApi.insertClassroom(dto, file)
    } catch (err) {
      error.value = err.message || '新增教室失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 更新教室
   */
  async function updateClassroom(dto, file, oldName) {
    loading.value = true
    error.value = null
    try {
      await ClassroomApi.updateClassroom(dto, file, oldName)
    } catch (err) {
      error.value = err.message || '修改教室失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 删除教室
   */
  async function deleteByClassroomId(classroomId) {
    loading.value = true
    error.value = null
    try {
      await ClassroomApi.deleteByClassroomId(classroomId)
    } catch (err) {
      error.value = err.message || '删除教室失败'
      throw err
    } finally {
      loading.value = false
    }
  }

  /**
   * 批量删除教室
   */
  async function deleteByClassroomIds(classroomIds) {
    loading.value = true
    error.value = null
    try {
      await ClassroomApi.deleteByClassroomIds(classroomIds)
    } catch (err) {
      error.value = err.message || '批量删除教室失败'
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
    classroomIPage,
    insertClassroom,
    updateClassroom,
    deleteByClassroomId,
    deleteByClassroomIds
  }
})
