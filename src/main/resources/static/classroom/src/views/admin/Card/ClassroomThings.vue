<template>
  <el-container class="thing-crud">
    <!-- 顶部：查询栏 + 按钮 -->
    <el-header class="crud-header">
      <el-row :gutter="12" align="middle">
        <el-col :span="4">
          <el-select v-model="field" placeholder="查询字段">
            <el-option label="物品编号" value="code"/>
            <el-option label="物品名称" value="thingName"/>
          </el-select>
        </el-col>

        <el-col :span="6">
          <el-input
              v-model="searchForm[field]"
              :placeholder="`请输入${labelMap[field]}`"
              clearable
              @keyup.enter="handleSearch"
          />
        </el-col>

        <el-col :span="6">
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-col>

        <el-col :span="8" style="text-align:right">
          <el-button type="primary" @click="openAdd">新增</el-button>

          <!-- 批量导入 -->
          <el-button type="success" @click="selectFile">批量导入</el-button>
          <input
              ref="fileRef"
              type="file"
              accept=".xlsx,.xls"
              style="display:none"
              @change="handleUpload"
          />

          <el-button
              type="danger"
              :disabled="selectedIds.length===0"
              @click="handleBatchDel"
          >批量删除</el-button>
        </el-col>
      </el-row>
    </el-header>

    <!-- 表格 -->
    <el-main class="crud-main">
      <el-table
          ref="multipleTableRef"
          v-loading="store.loading"
          :data="tableData"
          row-key="id"
          height="100%"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"/>

        <el-table-column prop="thingName" label="物品名称"/>
        <el-table-column prop="type" label="类型"/>
        <!-- 1. 表格展示布尔值转中文 -->
        <el-table-column label="能否使用">
          <template #default="{row}">
          <el-tag :type="row.needUse ? 'success':'info'" >
            {{ row.needUse ? '能' : '不能' }}
          </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="能否预约">
          <template #default="{ row }">
            <el-tag :type="row.needBooking ? 'success' : 'info'">
              {{ row.needBooking ? '能' : '不能' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述"/>
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button text type="primary" @click="openEdit(row)">编辑</el-button>
            <el-popconfirm title="确认删除？" width="200" @confirm="handleDel(row.id)">
              <template #reference>
                <el-button text type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-main>

    <!-- 分页 -->
    <el-footer class="crud-footer">
      <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10,20,50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadPage"
          @current-change="loadPage"
      />
    </el-footer>
  </el-container>

  <!-- 新增/编辑抽屉 -->
  <el-drawer
      v-model="drawerVisible"
      :title="drawerTitle"
      direction="rtl"
      size="480px"
      :before-close="handleDrawerClose"
  >
    <el-form ref="formRef" :model="formModel" label-width="100px" :rules="formRules">
      <el-form-item label="物品编号" prop="thingId">
        <el-input v-model="formModel.thingId" placeholder="唯一编号" disabled/>
      </el-form-item>
      <el-form-item label="物品名称" prop="thingName">
        <el-input v-model="formModel.thingName" placeholder="物品名称"/>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-input v-model="formModel.type" placeholder="类型"/>
      </el-form-item>
      <!-- 2. 表单 radio 使用布尔 label -->
      <el-form-item label="是否需要使用" prop="needUse">
        <el-radio-group v-model="formModel.needUse">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否需要预约" prop="needBooking">
        <el-radio-group v-model="formModel.needBooking">
          <el-radio :label="true">是</el-radio>
          <el-radio :label="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="formModel.description" type="textarea" :rows="3"/>
      </el-form-item>
      <!-- 所属教室：只读，自动填充 -->
      <el-form-item label="所属教室">
        <el-input :model-value="props.classroomId" disabled/>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleDrawerClose">取消</el-button>
      <el-button type="primary" :loading="store.loading" @click="handleSubmit">保存</el-button>
    </template>
  </el-drawer>
</template>

<script setup>
/* ---------------- props ---------------- */
const props = defineProps({
  classroomId: { type: [String, Number], required: true }
})

/* ---------------- 仓库 ---------------- */
import { useThingStore} from "@/stores/ThingStore.js";

const store = useThingStore()

/* ---------------- 表格 & 分页 ---------------- */
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import readXlsxFile from 'read-excel-file'

const tableData = ref([])
const selectedIds = ref([])
const pagination = reactive({ current: 1, size: 10, total: 0 })

function handleSelectionChange(rows) {
  selectedIds.value = rows.map(r => r.id)
}

async function loadPage() {
  try {
    await store.page({
      page: pagination.current,
      size: pagination.size,
      classroomId: props.classroomId,
      ...searchForm
    })
    tableData.value = store.list
    pagination.total = store.total
    selectedIds.value = []
  } catch {
    ElMessage.error(store.error || '加载失败')
  }
}

watch(() => props.classroomId, () => {
  pagination.current = 1
  loadPage()
})

/* ---------------- 查询/重置 ---------------- */
const searchForm = reactive({ code: '', thingName: '' })
const field = ref('thingName')
const labelMap = { code: '物品编号', thingName: '物品名称' }

function handleSearch() {
  pagination.current = 1
  loadPage()
}
function handleReset() {
  Object.keys(searchForm).forEach(k => (searchForm[k] = ''))
  field.value = 'thingName'
  handleSearch()
}

/* ---------------- 删除 ---------------- */
async function handleDel(id) {
  try {
    await store.deleteOne(id)
    ElMessage.success('删除成功')
    await loadPage()
  } catch {
    ElMessage.error(store.error || '删除失败')
  }
}
async function handleBatchDel() {
  if (!selectedIds.value.length) return
  try {
    await store.deleteBatch(selectedIds.value)
    ElMessage.success('批量删除成功')
    await loadPage()
  } catch {
    ElMessage.error(store.error || '批量删除失败')
  }
}

/* ---------------- 抽屉表单 ---------------- */
const drawerVisible = ref(false)
const isEdit = ref(false)
const drawerTitle = computed(() => (isEdit.value ? '编辑物品' : '新增物品'))

const formRef = ref()
// 3. 初始化为布尔 false
const formModel = reactive({
  id: undefined,
  thingId: '',
  thingName: '',
  type: '',
  besides: '',
  needUse: false,
  needBooking: false,
  description: '',
  classroomId: ''
})

const formRules = {
  thingName: [{ required: true, message: '请输入物品名称', trigger: 'blur' }]
}

function openAdd() {
  isEdit.value = false
  resetForm()
  drawerVisible.value = true
}
function openEdit(row) {
  isEdit.value = true
  resetForm()
  nextTick(() => Object.assign(formModel, row))
  drawerVisible.value = true
}
function resetForm() {
  formRef.value?.resetFields()
  Object.keys(formModel).forEach(k => (formModel[k] = k === 'id' ? undefined : (k === 'needUse' || k === 'needBooking' ? false : '')))
}
function handleDrawerClose() {
  drawerVisible.value = false
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  formModel.classroomId = props.classroomId
  try {
    isEdit.value ? await store.updateOne(formModel) : await store.insertOne(formModel)
    ElMessage.success('保存成功')
    drawerVisible.value = false
    await loadPage()
  } catch {
    ElMessage.error(store.error || '保存失败')
  }
}

/* ---------------- 批量导入 ---------------- */
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
    '物品编号': 'code',
    '物品名称': 'thingName',
    '类型': 'type',
    '是否需要使用': 'needUse',
    '是否需要预约': 'needBooking',
    '描述': 'description',
    '备注': 'besides'
  }
  const dtoList = rows.slice(1).map(row => {
    const item = { classroomId: props.classroomId }
    header.forEach((h, idx) => {
      const key = keyMap[h?.trim()]
      if (key) {
        let val = row[idx]
        // 4. Excel 里“是”→true，其余→false
        if (key === 'needUse' || key === 'needBooking') {
          val = (val === '是')
        }
        item[key] = val
      }
    })
    return item
  })
  try {
    await store.insertBatch(dtoList)
    ElMessage.success(`成功导入 ${dtoList.length} 条数据`)
    loadPage()
  } catch {
    ElMessage.error(store.error || '批量导入失败')
  }
}

onMounted(() => loadPage())
</script>

<style scoped>
.thing-crud {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #fff;
}
.crud-header {
  padding: 16px;
  flex-shrink: 0;
}
.crud-main {
  flex: 1;
  padding: 0 16px;
  overflow: hidden;
}
.crud-footer {
  padding: 0 16px 16px;
  flex-shrink: 0;
  text-align: right;
}
/* ========= 覆盖 Element 组件默认尺寸 ========= */
/* 让 el-button 默认就是 small */
.el-button {
  height: 32px;
  padding: 0 12px;
  font-size: 14px;
}

/* 表格里操作按钮再小一号，避免撑高行高 */
.crud-main .el-button--text {
  height: 24px;
  padding: 0 8px;
  font-size: 13px;
}

/* 查询区输入框、选择框与按钮保持同一高度 */
.crud-header .el-input__inner,
.crud-header .el-select__wrapper {
  height: 32px;
  line-height: 32px;
}

/* ========= 微调间距与对齐 ========= */
.crud-header {
  padding: 12px 16px;
  display: flex;
  align-items: center;
}
.crud-header .el-row {
  width: 100%;
  align-items: center;
}

/* 表格与分页之间的缝隙 */
.crud-main {
  padding: 0 16px 8px;
}
.crud-footer {
  padding: 0 16px 12px;
}

/* 分页器高度 32 px */
.el-pagination {
  height: 32px;
  line-height: 32px;
}

/* ========= 轻微交互增强 ========= */
.el-button:not(.is-disabled):hover {
  background-color: var(--el-color-primary-light-9);
}
.el-button--danger:not(.is-disabled):hover {
  background-color: var(--el-color-danger-light-9);
}
.el-button--success:not(.is-disabled):hover {
  background-color: var(--el-color-success-light-9);
}

/* 抽屉底部按钮也同步高度 */
.el-drawer__footer .el-button {
  height: 32px;
}
</style>