/**
 * 统一分页逻辑 Composable
 *
 * @param {Function} loadFn - 加载数据的函数
 * @returns {Object} 分页相关的响应式数据和方法
 */
import { reactive } from 'vue'

export function usePagination(loadFn) {
  const pagination = reactive({
    current: 1,
    size: 10,
    total: 0
  })

  /**
   * 加载数据
   */
  async function load() {
    await loadFn({
      page: pagination.current,
      size: pagination.size
    })
  }

  /**
   * 页码变化处理
   */
  function handlePageChange(page) {
    pagination.current = page
    load()
  }

  /**
   * 每页条数变化处理
   */
  function handleSizeChange(size) {
    pagination.size = size
    load()
  }

  /**
   * 重置到第一页
   */
  function reset() {
    pagination.current = 1
  }

  /**
   * 更新总数
   */
  function updateTotal(total) {
    pagination.total = total
  }

  return {
    pagination,
    load,
    handlePageChange,
    handleSizeChange,
    reset,
    updateTotal
  }
}
