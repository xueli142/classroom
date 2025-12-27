<template>
  <el-container class="event-crud">
    <!-- 头部：查询栏 + 按钮 -->
    <el-header class="crud-header">
      <el-row :gutter="12" align="middle">
        <!-- 1. 选字段 -->
        <el-col :span="4">
          <el-select v-model="field" placeholder="查询字段">
            <el-option label="活动名称" value="eventName"/>
            <el-option label="发布人"   value="userName"/>
            <el-option label="活动编号" value="eventId"/>
            <el-option label="能否预约" value="needAudit"/>
          </el-select>
        </el-col>

        <!-- 3. 按钮 -->
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-col>

        <!-- 4. 右侧按钮 -->
        <el-col :span="8" style="text-align:right">
          <el-button type="primary" @click="openAdd">新增</el-button>
          <el-button type="success" @click="selectFile">批量导入</el-button>
          <el-button type="danger" :disabled="selectedIds.length===0" @click="handleBatchDel">批量删除</el-button>
        </el-col>
      </el-row>
    </el-header>

    <!-- 中间：表格 -->
    <el-main class="crud-main">
      <el-table
          ref="multipleTableRef"
          v-loading="eventStore.eventLoading"
          :data="tableData"
          row-key="eventId"
          height="100%"
          highlight-current-row
          @selection-change="handleSelectionChange"
          @current-change="handleRowChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="eventName" label="活动名称" />
        <el-table-column prop="userName"  label="发布人" />
        <el-table-column prop="needAudit" label="是否允许预约">
          <template #default="{ row }">
            <el-tag :type="row.needAudit ? 'warning' : 'success'">
              {{ row.needAudit ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="当前人数"/>
        <el-table-column label="封面图片">
          <template #default="{ row }">
            <el-image
                v-if="row.imageUrl"
                :src="row.imageUrl"
                style="width:60px;height:40px"
                fit="cover"
                :preview-src-list="[row.imageUrl]"
            />
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button text type="primary" @click="openEdit(row)">编辑</el-button>
            <el-popconfirm
                title="确认删除？"
                width="200"
                @confirm="handleDel(row.eventId)"
            >
              <template #reference>
                <el-button text type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
        <el-table-column label="活动学生选择" width="120" align="center">
          <template #default="{ row }">
            <el-button type="primary" text @click="openStudentSelect(row.eventId)">
              选择学生
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>

  <!-- 抽屉：新增 / 编辑 -->
  <el-drawer
      v-model="drawerVisible"
      :title="drawerTitle"
      direction="rtl"
      size="520px"
      :before-close="handleDrawerClose"
  >
    <el-form
        ref="formRef"
        :model="formModel"
        label-width="100px"
        :rules="formRules"
    >
      <el-form-item label="活动名称" prop="eventName">
        <el-input v-model="formModel.eventName" placeholder="例如：Vue3 实战" clearable />
      </el-form-item>

      <!-- 发布人：仅展示，不可改 -->
      <el-form-item label="发布人" prop="userName">
        <el-input v-model="formModel.userName" disabled />
      </el-form-item>

      <el-form-item label="允许预约" prop="needAudit">
        <el-switch v-model="formModel.needAudit" active-text="是" inactive-text="否" />
      </el-form-item>

      <el-form-item label="活动描述" prop="description">
        <el-input v-model="formModel.description" type="textarea" :rows="2" placeholder="选填" />
      </el-form-item>

      <el-form-item label="备注" prop="besides">
        <el-input v-model="formModel.besides" type="textarea" :rows="2" placeholder="选填" />
      </el-form-item>

      <!-- 封面图片上传 -->
      <el-form-item label="封面图片">
        <el-upload
            ref="uploadRef"
            action="#"
            :limit="1"
            :auto-upload="false"
            :file-list="fileList"
            :on-change="handlePicChange"
            :on-remove="handlePicRemove"
            accept="image/*"
            list-type="picture-card"
        >
          <el-icon><Plus /></el-icon>
        </el-upload>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleDrawerClose">取消</el-button>
      <el-button type="primary" :loading="eventStore.eventLoading" @click="handleSubmit">保存</el-button>
    </template>
  </el-drawer>

  <!-- 隐藏的文件选择 -->
  <input ref="fileRef" type="file" accept=".xlsx,.xls" style="display:none" @change="handleUpload">

  <!-- 学生选活动弹窗 -->
  <el-dialog
      v-model="studentDlgVisible"
      title="活动学生选择"
      width="60%"
      top="5vh"
      :close-on-click-modal="false"
      destroy-on-close
  >
    <StudentCourseSelect :course-id="activeEventId" />
  </el-dialog>
</template>

<script setup>
/* --------------------  引入  -------------------- */
import { reactive, ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useEventStore } from '@/stores/eventStore'
import { useAuthStore } from '@/stores/AuthStore.js'
import readXlsxFile from 'read-excel-file'
import StudentCourseSelect from "@/views/Select/StudentCourseSelect.vue";

/* --------------------  store  -------------------- */
const eventStore = useEventStore()
const authStore  = useAuthStore()

/* --------------------  表格 / 分页  -------------------- */
const tableData   = ref([])
const selectedIds = ref([])
const activeEventId = ref('')

const pagination  = reactive({ current: 1, size: 10, total: 0 })
const studentDlgVisible = ref(false)

function openStudentSelect(eventId) {
  activeEventId.value = eventId
  studentDlgVisible.value = true
}
function handleSelectionChange(rows) {
  selectedIds.value = rows.map(r => r.eventId)
}
function handleRowChange(row) {
  activeEventId.value = row ? row.eventId : ''
}
async function loadPage() {
  try {
    await eventStore.eventIPage({
      page: pagination.current,
      size: pagination.size,
      userId: authStore.user.userId,
    })
    tableData.value   = eventStore.eventList
    pagination.total  = eventStore.eventTotal
    selectedIds.value = []
  } catch {
    ElMessage.error(eventStore.eventError || '加载失败')
  }
}

/* --------------------  查询 / 重置  -------------------- */
const field = ref('eventName')
function handleSearch() {
  pagination.current = 1
  loadPage()
}
function handleReset() {
  field.value = 'eventName'
  pagination.current = 1
  loadPage()
}

/* --------------------  删除  -------------------- */
async function handleDel(id) {
  try {
    await eventStore.deleteByEventId(id)
    ElMessage.success('删除成功')
    await loadPage()
  } catch {
    ElMessage.error(eventStore.eventError || '删除失败')
  }
}
async function handleBatchDel() {
  if (!selectedIds.value.length) return
  try {
    await eventStore.deleteByEventIds(selectedIds.value)
    ElMessage.success('批量删除成功')
    await loadPage()
  } catch {
    ElMessage.error(eventStore.eventError || '批量删除失败')
  }
}

/* --------------------  抽屉表单  -------------------- */
const drawerVisible = ref(false)
const isEdit = ref(false)
const drawerTitle = computed(() => (isEdit.value ? '编辑活动' : '新增活动'))

const formRef = ref()
const uploadRef = ref()
const fileList  = ref([])
let   rawFile   = null

const formModel = reactive({
  eventId: '',
  eventName: '',
  userName: '',
  userId: '',
  description: '',
  needAudit: false,
  besides: ''
})
const formRules = {
  eventName:  [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  userName:   [{ required: true, message: '请输入发布人', trigger: 'blur' }]
}

function openAdd() {
  isEdit.value = false
  resetForm()
  formModel.userId = authStore.user.userId
  formModel.userName = authStore.user.name
  drawerVisible.value = true
}
function openEdit(row) {
  isEdit.value = true
  resetForm()
  nextTick(() => {
    Object.keys(formModel).forEach(k => { formModel[k] = row[k] })
    if (row.imageUrl) fileList.value = [{ url: row.imageUrl, name: '封面' }]
    formModel.userId = authStore.user.userId
    formModel.userName = authStore.user.name
  })
  drawerVisible.value = true
}
function resetForm() {
  formRef.value?.resetFields()
  Object.keys(formModel).forEach(k => (formModel[k] = k === 'needAudit' ? false : ''))
  fileList.value = []
  rawFile = null
}
function handleDrawerClose() {
  drawerVisible.value = false
}

/* 图片选择 */
function handlePicChange(uploadFile) {
  rawFile = uploadFile.raw
}
function handlePicRemove() {
  rawFile = null
}

/* 保存 */
async function handleSubmit() {
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  try {
    isEdit.value
        ? await eventStore.updateEvent(formModel, rawFile, fileList.value[0]?.url)
        : await eventStore.insertEvent(formModel, rawFile)
    ElMessage.success('保存成功')
    drawerVisible.value = false
    await loadPage()
  } catch {
    ElMessage.error(eventStore.eventError || '保存失败')
  }
}

/* --------------------  批量导入  -------------------- */
const fileRef = ref(null)
function selectFile() {
  fileRef.value.value = ''
  fileRef.value.click()
}
async function handleUpload(ev) {
  const file = ev.target.files?.[0]
  if (!file) return
  let rows
  try {
    rows = await readXlsxFile(file)
  } catch {
    ElMessage.error('Excel 解析失败')
    return
  }
  const header = rows[0]
  const keyMap = {
    '活动名称': 'eventName',
    '发布人':   'userName',
    '活动编号': 'eventId',
    '需要审核': 'needAudit',
    '描述':     'description',
    '备注':     'besides'
  }
  const dtoList = rows.slice(1).map(row => {
    const item = { needAudit: false }
    header.forEach((h, idx) => {
      const key = keyMap[h?.trim()]
      if (key === 'needAudit') item[key] = row[idx] === '是'
      else if (key) item[key] = row[idx] || ''
    })
    return item
  })
  try {
    await eventStore.batchInsertEvent(dtoList)
    ElMessage.success(`成功导入 ${dtoList.length} 条数据`)
    await loadPage()
  } catch {
    ElMessage.error(eventStore.eventError || '批量导入失败')
  }
}

/* --------------------  钩子  -------------------- */
onMounted(() => loadPage())
</script>

<style scoped>
.event-crud { height: 100vh; display: flex; flex-direction: column; background: #fff; }
.crud-header  { padding: 16px; flex-shrink: 0; }
.crud-main    { flex: 1; padding: 0 16px; overflow: hidden; }
</style>