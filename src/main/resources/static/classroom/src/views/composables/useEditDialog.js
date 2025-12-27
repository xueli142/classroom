import { reactive, ref, toRefs } from 'vue'
import { ElMessage } from 'element-plus'

/**
 * 通用“弹窗编辑”逻辑
 * @param {Function} api      保存接口，接收 form 对象，返回 Promise
 * @param {Object}   initForm 表单初始值
 * @param {Function} onOk     可选：接口成功后的额外回调，参数 (res, form)
 */export function useEditDialog(api, initForm = {}, onOk) {
    const visible = ref(false)
    const loading = ref(false)
    const form    = reactive({ ...initForm })

    function open(getter = () => ({})) {
        Object.assign(form, getter())
        visible.value = true
    }

    async function submit() {
        loading.value = true
        try {
            const res = await api(form)
            if (onOk) onOk(res, form)
            ElMessage.success('已保存')
            visible.value = false
        } catch (e) {
            ElMessage.error(e?.response?.data?.message || '操作失败')
        } finally {
            loading.value = false
        }
    }

    function reset() {
        Object.assign(form, initForm)
    }

    return {
        visible,
        loading,
        form,
        open,
        submit,
        reset
    }
}