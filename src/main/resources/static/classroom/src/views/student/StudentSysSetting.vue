<template>
  <el-container class="my-advice" direction="vertical">
    <!-- 顶部 -->
    <el-header height="60px" style="padding:16px;">
      <el-button type="primary" @click="openAdd">新增意见</el-button>
    </el-header>

    <!-- 表格 -->
    <el-main style="padding:0 16px;">
      <el-table
          v-loading="loading"
          :data="tableData"
          row-key="adviceId"
          height="100%"
          stripe
      >
        <el-table-column prop="title"   label="标题" width="140" show-overflow-tooltip />
        <el-table-column prop="type"    label="类型" width="90">
          <template #default="{row}">
            {{ {SUGGEST:'功能建议',BUG:'问题反馈',OTHER:'其它'}[row.type] }}
          </template>
        </el-table-column>
        <el-table-column label="反馈内容">
          <template #default="{ row }">
            <el-button
                type="text"
                size="small"
                @click="openAdvice(row)">
              查看
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="回复内容">
          <template #default="{ row }">
            <el-button
                type="text"
                size="small"
                @click="openReply(row)">
              查看
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status ? 'success' : 'warning'">
              {{ row.status ? '已处理' : '待处理' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="160"/>
      </el-table>
    </el-main>

    <!-- 分页 -->
    <el-footer height="50px" style="text-align:right;padding:0 16px 16px;">
      <el-pagination
          v-model:current-page="pager.page"
          v-model:page-size="pager.size"
          :total="pager.total"
          :page-sizes="[10, 20]"
          layout="total, prev, pager, next"
          @current-change="loadMyList"
      />
    </el-footer>
  </el-container>

  <!-- 新增抽屉 -->
  <el-drawer
      v-model="visible"
      title="新增意见"
      direction="rtl"
      size="480px"
      :before-close="close"
  >
    <el-form ref="formRef" :model="form" label-width="80px" :rules="rules">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" maxlength="50" show-word-limit />
      </el-form-item>

      <el-form-item label="意见类型" prop="type">
        <el-select v-model="form.type" placeholder="请选择" style="width:100%">
          <el-option label="功能建议" value="SUGGEST" />
          <el-option label="问题反馈" value="BUG" />
          <el-option label="其它" value="OTHER" />
        </el-select>
      </el-form-item>

      <el-form-item label="反馈内容" prop="advice">
        <el-input
            v-model="form.advice"
            type="textarea"
            :rows="4"
            maxlength="2000"
            show-word-limit
            placeholder="请详细描述您的意见或遇到的问题"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="close">取消</el-button>
      <el-button type="primary" :loading="loading" @click="submit">提交</el-button>
    </template>
  </el-drawer>
  <el-dialog
      title="反馈内容"
      v-model="adviceVisible"
      width="500px"
      top="5vh">
    <div>
      <div><strong>标题：</strong>{{ adviceTitle }}</div>
      <div style="margin:8px 0;"><strong>类型：</strong>{{ adviceType }}</div>
      <div><strong>详细内容：</strong></div>
      <div class="advice-box" style="white-space: pre-wrap; margin-top:4px;">
        {{ adviceContent }}
      </div>
    </div>

    <template #footer>
      <el-button @click="adviceVisible = false">关 闭</el-button>
    </template>
  </el-dialog>
  <!-- 管理回复弹窗 -->
  <el-dialog
      title="回复内容"
      v-model="replyVisible"
      width="500px"
      top="5vh"
  >
    <div>
      <div><strong>标题：</strong>{{ replyTitle }}</div>
      <div style="margin-top:12px;"><strong>回复内容：</strong></div>
      <div class="reply-box" style="white-space: pre-wrap; margin-top:4px;">
        {{ replyContent }}
      </div>
    </div>

    <template #footer>
      <el-button @click="replyVisible = false">关 闭</el-button>
    </template>
  </el-dialog>

</template>

<script setup>
import { reactive, ref} from 'vue'
import {ElMessage} from 'element-plus'
import {useAdviceStore} from '@/stores/AdviceStore.js'
import {useAuthStore} from '@/stores/AuthStore.js'
import {useAdviceReplyStore} from "@/stores/AdviceReplyStore.js";
import { watch } from 'vue'
import { useRoute} from 'vue-router'
/* ---------- 基础数据 ---------- */
const adviceStore = useAdviceStore()
const authStore   = useAuthStore()
const replyStore = useAdviceReplyStore()
const currentUserId = authStore.user.userId
/* ---------- 弹窗 ---------- */
/* ---------- 回复弹窗专用 ---------- */
const replyVisible   = ref(false)
const replyTitle     = ref('')
const replyContent   = ref('')
const adviceTitle = ref('');
const adviceVisible = ref(false)   // 弹窗开关
const adviceContent = ref('')      // 要显示的内容
const loading   = ref(false)
const tableData = ref([])
const pager     = reactive({ page: 1, size: 10, total: 0 })
const adviceType  = ref('')   // 新增
function openAdvice(row) {
  adviceTitle.value = row.title
  adviceType.value  = { SUGGEST:'功能建议',BUG:'问题反馈',OTHER:'其它' }[row.type] || ''
  adviceContent.value = row.advice
  adviceVisible.value = true
}
const route = useRoute()
async function openReply(row) {
  console.group('🔍 openReply 调试')


  try {
    loading.value = true


    const data = await replyStore.selectByAdviceId(row.adviceId)



    if (!data) {
      console.warn('8️⃣ 暂无数据，return')
      ElMessage.info('暂无回复')
      return
    }

    // 赋值
    replyTitle.value = data.title || '管理回复'
    replyContent.value = data.reply || '【暂无回复内容】'
    replyVisible.value = true


  } catch (e) {
    console.error('💥 catch 异常:', e)
    ElMessage.error(e.message || '获取回复失败')
  } finally {
    loading.value = false
    console.groupEnd()
  }
}
async function loadMyList() {
  loading.value = true
  try {
    await adviceStore.advicePage({ page: pager.page, size: pager.size, userId: currentUserId })
    tableData.value = adviceStore.list
    pager.total     = adviceStore.total
  } catch {
    ElMessage.error(adviceStore.error || '加载失败')
  } finally {
    loading.value = false
  }
}

/* ---------- 新增 ---------- */
const visible = ref(false)
const formRef = ref()
const form = ref({
  title: '',
  type: '',
  advice: '',
  name: authStore.user.name,
  userId: currentUserId
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择意见类型', trigger: 'change' }],
  advice: [{ required: true, message: '请输入反馈内容', trigger: 'blur' }]
}

function openAdd() {
  form.value = {
    title: '',
    type: '',
    advice: '',
    name: authStore.user.name,
    userId: currentUserId
  }
  visible.value = true
}

function close() {
  visible.value = false
}

async function submit() {
  try { await formRef.value.validate() } catch { return }
  try {
    await adviceStore.insertOne(form.value)
    ElMessage.success('提交成功')
    close()
    await loadMyList()
  } catch {
    ElMessage.error(adviceStore.error || '提交失败')
  }
}

watch(
    () => route.query,          // 或者 route.params 看你配的是哪一类
    async () => {
      console.log('[MyAdvice] 路由参数变化，重新加载列表')
      await loadMyList()
    },
    { immediate: true }          // 首次进入也执行一次
)
</script>

<style scoped>
.my-advice{
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #fff;
}
</style>