<template>
  <el-card shadow="never">
    <template #header>
      <span>建议回复（adviceId = {{ adviceId }}）</span>
      <el-button
          v-if="!exist"
          type="primary"
          size="small"
          @click="openAdd"
      >写回复</el-button>
    </template>

    <div v-if="exist">
      <!-- 用 el-descriptions 更直观，也可用 div 自行排版 -->
      <el-descriptions :column="1" size="small" border>
        <el-descriptions-item label="标题">{{ form.title }}</el-descriptions-item>
        <el-descriptions-item label="回复内容">{{ form.reply }}</el-descriptions-item>
      </el-descriptions>

      <div class="mt12">
        <el-button size="small" @click="openEdit">修改</el-button>
        <el-popconfirm title="确定删除?" @confirm="del">
          <template #reference>
            <el-button size="small" type="danger">删除</el-button>
          </template>
        </el-popconfirm>
      </div>
    </div>

    <!-- 暂无回复 -->
    <el-empty v-else description="暂无回复" />
  </el-card>

  <!-- 新增 / 修改弹窗 -->
  <el-dialog
      v-model="show"
      :title="isAdd ? '新增回复' : '修改回复'"
      width="420"
      @close="reset"
  >
    <el-form ref="formRef" :model="form" label-width="60">
      <!-- 标题 -->
      <el-form-item
          label="标题"
          prop="title"
          :rules="[{ required: true, message: '请输入标题' }]"
      >
        <el-input v-model="form.title" />
      </el-form-item>

      <!-- 回复内容 -->
      <el-form-item
          label="内容"
          prop="reply"
          :rules="[{ required: true, message: '请输入回复内容' }]"
      >
        <el-input
            v-model="form.reply"
            type="textarea"
            :rows="3"
            placeholder="请输入回复内容"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="show = false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useAdviceReplyStore } from '@/stores/AdviceReplyStore.js'

const props = defineProps({
  adviceId: { type: [String, Number], required: true }
})

const store = useAdviceReplyStore()

/* 状态 */
const exist = ref(false)        // 是否存在回复
const show  = ref(false)
const isAdd = ref(true)
const formRef = ref()
const form = ref({
  adviceReplyId:undefined,
  adviceId: props.adviceId,
  reply: '',
  title:''
})

/* 查询单条 */
const loadOne = async () => {
  try {
    const res = await store.selectByAdviceId(props.adviceId)
    if (res) {
      exist.value = true
      form.value = { ...res }
    } else {
      exist.value = false
      reset()
    }
  } catch {
    exist.value = false
  }
}

/* 监听 id 变化 */
watch(() => props.adviceId, loadOne, { immediate: true })

/* 弹窗控制 */
const openAdd = () => {
  isAdd.value = true
  show.value = true
}
const openEdit = () => {
  isAdd.value = false
  show.value = true
}
const reset = () => {
  form.value = {
    adviceReplyId: undefined,
    adviceId: props.adviceId,
    reply: '',
    title:'',
  }
  formRef.value?.resetFields()
}

/* 提交 */
const submit = async () => {
  await formRef.value.validate()
  try {
    isAdd.value
        ? await store.insertOne(form.value)
        : await store.updateOne(form.value)
    ElMessage.success(isAdd.value ? '新增成功' : '修改成功')
    show.value = false
    loadOne()          // 重新查一次
  } catch {}
}

/* 删除 */
const del = async () => {
  await store.deleteOne(form.value.adviceReplyId)
  ElMessage.success('删除成功')
  loadOne()
}
</script>

<style scoped>
.mt12 { margin-top: 12px; }
</style>