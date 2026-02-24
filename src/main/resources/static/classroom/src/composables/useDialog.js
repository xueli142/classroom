/**
 * 统一对话框逻辑 Composable
 *
 * @returns {Object} 对话框相关的响应式数据和方法
 */
import { ref, nextTick } from 'vue'

export function useDialog() {
  const dialogVisible = ref(false)
  const isEdit = ref(false)
  const loading = ref(false)

  /**
   * 打开新增对话框
   */
  function openAdd() {
    isEdit.value = false
    dialogVisible.value = true
  }

  /**
   * 打开编辑对话框
   */
  function openEdit() {
    isEdit.value = true
    dialogVisible.value = true
  }

  /**
   * 关闭对话框
   */
  function close() {
    dialogVisible.value = false
    isEdit.value = false
    loading.value = false
  }

  /**
   * 打开对话框（通用方法）
   */
  function open(edit = false) {
    isEdit.value = edit
    dialogVisible.value = true
  }

  return {
    dialogVisible,
    isEdit,
    loading,
    openAdd,
    openEdit,
    close,
    open
  }
}
