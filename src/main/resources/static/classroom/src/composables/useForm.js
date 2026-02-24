/**
 * 统一表单逻辑 Composable
 *
 * @param {Object} defaultForm - 表单默认值
 * @param {Object} rules - 表单验证规则
 * @returns {Object} 表单相关的响应式数据和方法
 */
import { reactive, ref, nextTick } from 'vue'
import { ElMessage } from 'element-plus'

export function useForm(defaultForm = {}, rules = {}) {
  const formData = reactive({ ...defaultForm })
  const formRules = rules
  const formRef = ref(null)

  /**
   * 重置表单
   */
  function resetForm() {
    formRef.value?.resetFields()
    Object.keys(formData).forEach(key => {
      if (typeof defaultForm[key] === 'boolean') {
        formData[key] = defaultForm[key]
      } else if (Array.isArray(defaultForm[key])) {
        formData[key] = []
      } else if (typeof defaultForm[key] === 'object' && defaultForm[key] !== null) {
        formData[key] = {}
      } else {
        formData[key] = ''
      }
    })
  }

  /**
   * 设置表单数据
   */
  function setForm(data) {
    nextTick(() => {
      Object.keys(data).forEach(key => {
        if (data[key] !== undefined && data[key] !== null) {
          formData[key] = data[key]
        }
      })
    })
  }

  /**
   * 验证表单
   */
  async function validate() {
    if (!formRef.value) {
      return Promise.resolve(true)
    }
    try {
      await formRef.value.validate()
      return true
    } catch (error) {
      return false
    }
  }

  /**
   * 清除表单验证
   */
  function clearValidate() {
    formRef.value?.clearValidate()
  }

  /**
   * 获取表单数据
   */
  function getFormData() {
    return { ...formData }
  }

  return {
    formData,
    formRules,
    formRef,
    resetForm,
    setForm,
    validate,
    clearValidate,
    getFormData
  }
}
