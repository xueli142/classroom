<template>
  <el-container class="classroom-crud">
    <!-- 头部：查询栏 + 按钮 -->
    <el-header class="crud-header">
      <!-- 替换 classroom-crud 的 el-header 内 el-row -->
      <el-row :gutter="12" align="middle">
        <!-- 1. 选字段 -->
        <el-col :span="4">
          <el-select v-model="field" placeholder="查询字段">
            <el-option label="教室名称" value="name"/>
            <el-option label="位置"      value="location"/>
            <el-option label="类型"      value="type"/>
            <el-option label="容纳人数"  value="number"/>
          </el-select>
        </el-col>

        <!-- 2. 单个输入框（同学生） -->
        <el-col :span="6">
          <el-input
              v-model="searchForm[field]"
              :placeholder="`请输入${labelMap[field]}`"
              clearable
              @keyup.enter="handleSearch"/>
        </el-col>

        <!-- 3. 按钮 -->
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-col>

        <!-- 4. 右侧按钮保持不动 -->
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
          v-loading="store.loading"
          :data="tableData"
          row-key="classroomId"
          height="100%"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name"     label="教室名称" />
        <el-table-column prop="location" label="位置" />
        <el-table-column prop="type"     label="类型" />
        <el-table-column prop="number"   label="容纳人数" />
        <el-table-column prop="isActive" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.isActive ? 'success' : 'info'">
              {{ row.isActive ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="图片">
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
                @confirm="handleDel(row.classroomId)"
            >
              <template #reference>
                <el-button text type="danger">删除</el-button>
              </template>
            </el-popconfirm>
            <el-button text type="primary" @click="openClassroom(row.classroomId)">教室资源</el-button>
          </template>
        </el-table-column>

        <el-table-column label="使用情况" v-model="showSchedule" title="教室课程使用情况">

<ClassroomSelect v-model:classroom="selectedCr"/>
        </el-table-column>
      </el-table>
    </el-main>

    <!-- 底部：分页器 -->
    <el-footer class="crud-footer">
      <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadPage"
          @current-change="loadPage"
      />
    </el-footer>
  </el-container>

  <!-- 抽屉：新增/编辑 -->
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
      <el-form-item label="教室名称" prop="name">
        <el-input v-model="formModel.name" placeholder="例如：A101" clearable />
      </el-form-item>

      <el-form-item label="位置" prop="location">
        <el-input v-model="formModel.location" placeholder="例如：教学楼A栋1层" clearable />
      </el-form-item>

      <el-form-item label="类型" prop="type">
        <el-select v-model="formModel.type" placeholder="请选择">
          <el-option label="多媒体" value="多媒体"/>
          <el-option label="普通" value="普通"/>
          <el-option label="实验室" value="实验室"/>
          <el-option label="会议室" value="会议室"/>
        </el-select>
      </el-form-item>

      <el-form-item label="容纳人数" prop="number">
        <el-input-number
            v-model="formModel.number"
            :min="1"
            :max="999"
            placeholder="人数"
        />
      </el-form-item>

      <el-form-item label="状态" prop="isActive">
        <el-switch v-model="formModel.isActive" active-text="启用" inactive-text="禁用" />
      </el-form-item>

      <el-form-item label="描述" prop="description">
        <el-input
            v-model="formModel.description"
            type="textarea"
            :rows="2"
            placeholder="简要描述（选填）"
        />
      </el-form-item>

      <el-form-item label="备注" prop="besides">
        <el-input
            v-model="formModel.besides"
            type="textarea"
            :rows="2"
            placeholder="备注（选填）"
        />
      </el-form-item>

      <!-- 图片上传 -->
      <el-form-item label="教室图片">
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
      <el-button
          type="primary"
          :loading="store.loading"
          @click="handleSubmit"
      >保存</el-button>
    </template>
  </el-drawer>
  <el-dialog
      v-model="classroomUse"
      title="课程资源"
      width="60%"
      top="5vh"
      :close-on-click-modal="false"
      destroy-on-close
  >
    <ClassroomThings :classroom-id="currentClassroomId"/>
  </el-dialog>

</template>

<script setup>
import { reactive, ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useClassroomStore } from '@/stores/ClassroomStore'
import readXlsxFile from 'read-excel-file'
import ClassroomSelect from "@/views/Select/ClassroomSelect.vue";
import ClassroomThings from "@/views/admin/Card/ClassroomThings.vue";
const showSchedule = ref(false)
const selectedCr   = ref({ classroomId: '', classroomName: '' })
/* ---------- courseStore ---------- */
const store = useClassroomStore()

/* 表格 & 分页 */
const tableData   = ref([])
const selectedIds = ref([])
const pagination  = reactive({ page: 1, size: 10, total: 0 })
const classroomUse=ref(false)
function handleSelectionChange(rows) {
  selectedIds.value = rows.map(r => r.classroomId)
}
async function loadPage() {
  try {
    await store.classroomIPage({
      page: pagination.page,
      size:    pagination.size,
      ...searchForm
    })
    tableData.value   = store.list
    pagination.total  = store.total
    selectedIds.value = []
  } catch {
    ElMessage.error(store.error || '加载失败')
  }
}

/* 查询/重置 */
const searchForm = reactive({
  name: '',
  location: '',
  type: '',
  number: ''
})
const field = ref('name')
const labelMap = {
  name: '教室名称',
  location: '位置',
  type: '类型',
  number: '容纳人数'
}

function handleSearch() {
  pagination.current = 1
  loadPage()
}
function handleReset() {
  Object.keys(searchForm).forEach(k => searchForm[k] = '')
  field.value = 'name'
  handleSearch()
}

/* 删除 */
async function handleDel(id) {
  try {
    await store.deleteByClassroomId(id)
    ElMessage.success('删除成功')
    await loadPage()
  } catch {
    ElMessage.error(store.error || '删除失败')
  }
}
async function handleBatchDel() {
  if (!selectedIds.value.length) return
  try {
    await store.deleteByClassroomIds(selectedIds.value)
    ElMessage.success('批量删除成功')
    await loadPage()
  } catch {
    ElMessage.error(store.error || '批量删除失败')
  }
}

/* 抽屉表单 */
const drawerVisible = ref(false)
const isEdit = ref(false)
const drawerTitle = computed(() => (isEdit.value ? '编辑教室' : '新增教室'))
const currentClassroomId=ref()
const formRef = ref()
const uploadRef = ref()
const fileList  = ref([])          // el-upload 展示用
let   rawFile   = null             // 真正要上传的 File
function resetForm() {
  formRef.value?.resetFields()          // 清空校验
  /* 重置所有字段 */
  Object.keys(formModel).forEach(k => {
    formModel[k] = k === 'isActive' ? true : ''
  })
  formModel.classroomId = undefined
  fileList.value = []                   // 清空上传列表
  rawFile = null
}
const formModel = reactive({
  classroomId: undefined,
  name: '',
  location: '',
  type: '',
  number: 50,
  isActive: true,
  description: '',
  besides: ''
})

const formRules = {

    // 教室名称
    name: [
      { required: true, message: '请输入教室名称', trigger: 'blur' },
      { min: 2, max: 50, message: '长度 2 ~ 50 个字符', trigger: 'blur' }
    ],

    // 位置
    location: [
      { required: true, message: '请输入位置', trigger: 'blur' },
      { min: 2, max: 100, message: '长度 2 ~ 100 个字符', trigger: 'blur' }
    ],

    // 类型
    type: [
      { required: true, message: '请选择类型', trigger: 'change' }
    ],

    // 最大容纳人数（后端 Long）
    number: [
      { required: true, type: 'integer', min: 1, max: 9999, message: '人数为 1~9999 的整数', trigger: 'blur' }
    ],


    isActive: [],

    // 描述（选填）
    description: [
      { max: 500, message: '描述最多 500 字', trigger: 'blur' }
    ],

    // 备注（选填）
    besides: [
      { max: 500, message: '备注最多 500 字', trigger: 'blur' }
    ]

}

function openAdd() {
  isEdit.value = false
    resetForm()
  drawerVisible.value = true
}
function openEdit(row) {
  isEdit.value = true
  resetForm()
  nextTick(() => {
    Object.keys(formModel).forEach(k => { formModel[k] = row[k] })
    formModel.classroomId = row.classroomId
    /* 回显已有图片 */
    if (row.imageUrl) {
      fileList.value = [{ url: row.imageUrl, name: '教室图片' }]
    }
  })
  drawerVisible.value = true
  function resetForm() {
}
  formRef.value?.resetFields()
  Object.keys(formModel).forEach(k => (formModel[k] = k === 'isActive' ? true : ''))
  formModel.classroomId = undefined
  fileList.value = []
  rawFile = null
}
function handleDrawerClose() {
  drawerVisible.value = false
}

function openClassroom(classroomId){
  classroomUse.value=true
  currentClassroomId.value=classroomId

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
        ? await store.updateClassroom(formModel, rawFile, fileList.value[0]?.url)
        : await store.insertClassroom(formModel, rawFile)
    ElMessage.success('保存成功')
    drawerVisible.value = false
    await loadPage()
  } catch {
    ElMessage.error(store.error || '保存失败')
  }
}

/* 批量导入 Excel */
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
    '教室名称': 'name',
    '位置': 'location',
    '类型': 'type',
    '容纳人数': 'number',
    '描述': 'description',
    '备注': 'besides',
    '状态': 'isActive'
  }
  const dtoList = rows.slice(1).map(row => {
    const item = { isActive: true }
    header.forEach((h, idx) => {
      const key = keyMap[h?.trim()]
      if (key === 'number') item[key] = Number(row[idx]) || 50
      else if (key === 'isActive') item[key] = row[idx] === '启用'
      else if (key) item[key] = row[idx] || ''
    })
    return item
  })
  try {
    await store.batchInsertClassroom(dtoList)   // 如需批量接口，请在 courseStore 补充
    ElMessage.success(`成功导入 ${dtoList.length} 条数据`)
    await loadPage()
  } catch {
    ElMessage.error(store.error || '批量导入失败')
  }
}

onMounted(() => loadPage())
</script>

<style scoped>
.classroom-crud { height: 100vh; display: flex; flex-direction: column; background: #fff; }
.crud-header  { padding: 16px; flex-shrink: 0; }
.crud-main    { flex: 1; padding: 0 16px; overflow: hidden; }
.crud-footer  { padding: 0 16px 16px; flex-shrink: 0; text-align: right; }
</style>