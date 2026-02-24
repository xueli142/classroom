/**
 * 统一表格逻辑 Composable
 *
 * @returns {Object} 表格相关的响应式数据和方法
 */
import { ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'

export function useTable() {
  const selectedIds = ref([])
  const selectedRows = ref([])

  /**
   * 处理选择变化
   */
  function handleSelectionChange(selection) {
    selectedRows.value = selection
    selectedIds.value = selection.map(item => item.id)
  }

  /**
   * 清空选择
   */
  function clearSelection() {
    selectedIds.value = []
    selectedRows.value = []
  }

  /**
   * 确认删除
   */
  async function confirmDelete(message = '确认删除选中的数据吗？') {
    try {
      await ElMessageBox.confirm(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      return true
    } catch {
      return false
    }
  }

  /**
   * 批量删除确认
   */
  async function confirmBatchDelete(count) {
    if (count === 0) {
      ElMessage.warning('请至少选择一条数据')
      return false
    }
    return confirmDelete(`确认删除选中的 ${count} 条数据吗？`)
  }

  return {
    selectedIds,
    selectedRows,
    handleSelectionChange,
    clearSelection,
    confirmDelete,
    confirmBatchDelete
  }
}
