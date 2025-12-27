    import { defineStore } from 'pinia'
    import { ref } from 'vue'
    import { AdviceReplyApi } from '@/api/AdviceReplyApi.js'

    export const useAdviceReplyStore = defineStore('adviceReply', () => {
        /* ---------------- 状态 ---------------- */
        const list    = ref([])
        const total   = ref(0)
        const loading = ref(false)
        const error   = ref(null)
        const current = ref(null)

        /* ---------------- 方法 ---------------- */
    //通过adviceId 的单条查询
        async function selectByAdviceId(adviceId) {
            loading.value = true
            error.value   = null
            try {
                const res = await AdviceReplyApi.selectByAdviceId(adviceId) // 见下方 API 补充
                current.value = res.data.data
                return current.value
            } catch (e) {
                error.value = e.message || '获取回复失败'
                current.value = null
                throw e
            } finally {
                loading.value = false
            }
        }
        // 分页查询
        async function replyPage(payload = { page: 1, size: 10 }) {
            loading.value = true
            error.value   = null
            try {
                const res = await AdviceReplyApi.replyPage(payload)
                const { records, total: count } = res.data.data
                list.value  = records
                total.value = count
            } catch (e) {
                error.value = e.message || '获取回复列表失败'
                list.value  = []
                total.value = 0
                throw e
            } finally {
                loading.value = false
            }
        }

        // 新增单条
        async function insertOne(reply) {
            loading.value = true
            error.value   = null
            try {
                await AdviceReplyApi.insertOne(reply)
            } catch (err) {
                error.value = err.message || '新增回复失败'
                throw err
            } finally {
                loading.value = false
            }
        }

        // 单条更新
        async function updateOne(reply) {
            loading.value = true
            error.value   = null
            try {
                await AdviceReplyApi.updateOne(reply)
            } catch (err) {
                error.value = err.message || '更新回复失败'
                throw err
            } finally {
                loading.value = false
            }
        }

        // 单条删除
        async function deleteOne(adviceReplyId) {
            loading.value = true
            error.value   = null
            try {
                await AdviceReplyApi.deleteOne(adviceReplyId)
            } catch (err) {
                error.value = err.message || '删除回复失败'
                throw err
            } finally {
                loading.value = false
            }
        }

        // 批量删除
        async function deleteBatch(ids) {
            loading.value = true
            error.value   = null
            try {
                await AdviceReplyApi.deleteBatch(ids)
            } catch (err) {
                error.value = err.message || '批量删除回复失败'
                throw err
            } finally {
                loading.value = false
            }
        }

        /* ---------------- 导出 ---------------- */
        return {
            list,
            total,
            loading,
            error,
            current,
            selectByAdviceId,
            replyPage,
            insertOne,
            updateOne,
            deleteOne,
            deleteBatch
        }
    })