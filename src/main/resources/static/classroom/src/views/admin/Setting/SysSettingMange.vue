<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>系统设置管理</span>
        <el-button type="primary" @click="openAdd">新增</el-button>
      </div>
    </template>

    <!-- 列表 -->
    <el-table
        v-loading="store.loading"
        :data="store.list"
        @selection-change="selChange"
    >
      <el-table-column type="selection" width="50" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="advice" label="正文" show-overflow-tooltip />
      <el-table-column prop="type" label="类型" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status ? 'success' : 'warning'">
            {{ row.status ? '已处理' : '待处理' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{row}">
          <el-button link type="primary" @click="openAudit(row)">审核</el-button>
          <el-button link type="primary" @click="openEdit(row)">修改</el-button>
          <el-popconfirm title="确认删除?" @confirm="del(row.userId)">
            <template #reference>
              <el-button link type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        class="mt16"
        background
        layout="prev, pager, next, total"
        :total="store.total"
        :page-size="page.size"
        :current-page="page.current"
        @current-change="jump"
    />

    <!-- 批量删除 -->
    <el-button
        v-if="selectIds.length"
        class="mt16"
        type="danger"
        @click="batchDel"
    >批量删除</el-button>
  </el-card>

  <!-- 新增/修改弹窗 -->
  <el-dialog
      v-model="show"
      :title="isAdd ? '新增' : '修改'"
      width="420"
      @close="reset"
  >
    <el-form ref="formRef" :model="form" label-width="60">
      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <!-- 子组件：title + advice -->
      <SysSettingTitleAdviceForm v-model="form" />
      <el-form-item label="类型" prop="type">
        <el-input v-model="form.type" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="show = false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </template>
  </el-dialog>
  <AdviceReply :advice-id="selectedAdviceId" />
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useAdviceStore} from "@/stores/AdviceStore.js";
import SysSettingTitleAdviceForm from './SysSettingTitleAdviceForm.vue'

import AdviceReply from "@/views/admin/Setting/AdviceReply.vue";

const selectedAdviceId = ref('')
const store =useAdviceStore()
/* 打开审核面板（即回复面板） */
const openAudit = (row) => {
  selectedAdviceId.value = row.adviceId   // 关键：把 id 传下去
  // 如果你希望弹窗再出来，可以在这里加一个 visible 控制
}
/* 分页参数 */
const page = reactive({ current: 1, size: 10 })
const loadTable = () => store.advicePage(page)

/* 初始加载 */
loadTable()

/* 分页跳转 */
const jump = (p) => {
  page.current = p
  loadTable()
}

/* 多选 */
const selectIds = ref([])
const selChange = (rows) => (selectIds.value = rows.map(r => r.userId))

/* 删除 / 批量删除 */
const del = async (id) => {
  await store.deleteOne(id)
  ElMessage.success('删除成功')
  loadTable()
}
const batchDel = async () => {
  await store.deleteBatch(selectIds.value)
  ElMessage.success('批量删除成功')
  loadTable()
}

/* 审核开关 */
const toggleStatus = async (row) => {
  const payload = { ...row, status: row.status === 1 ? 0 : 1 }
  await store.updateOne(payload)
  ElMessage.success('状态已变更')
  loadTable()
}

/* 新增/修改弹窗 */
const show = ref(false)
const isAdd = ref(true)
const formRef = ref()
const form = reactive({
  userId: undefined,
  name: '',
  title: '',
  advice: '',
  type: '',
  status: 0
})

const openAdd = () => {
  isAdd.value = true
  show.value = true
}
const openEdit = (row) => {
  Object.assign(form, row)
  isAdd.value = false
  show.value = true
}
const reset = () => {
  Object.assign(form, {
    userId: undefined,
    name: '',
    title: '',
    advice: '',
    type: '',
    status: 0
  })
  formRef.value?.resetFields()
}
const submit = async () => {
  try {
    isAdd.value ? await store.insertOne(form) : await store.updateOne(form)
    ElMessage.success(isAdd.value ? '新增成功' : '修改成功')
    show.value = false
    loadTable()
  } catch {}
}
</script>

<style scoped>
.mt16 { margin-top: 16px; }
.card-header { display: flex; justify-content: space-between; }
</style>