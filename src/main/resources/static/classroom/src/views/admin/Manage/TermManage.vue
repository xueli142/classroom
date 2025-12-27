<template>
  <el-container class="term-crud">
    <!-- 头部：查询栏 + 按钮 -->
    <el-header class="crud-header">
      <el-row :gutter="12" align="middle">
        <el-col :span="4">
          <el-select v-model="field" placeholder="查询字段">
            <el-option label="学期编号" value="termId"/>
            <el-option label="学期名称" value="termName"/>
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
          <el-button
              type="danger"
              :disabled="selectedIds.length===0"
              @click="handleBatchDel"
          >批量删除</el-button>
        </el-col>
      </el-row>
    </el-header>

    <!-- 中间：表格 -->
    <el-main class="crud-main">
      <el-table
          ref="multipleTableRef"
          v-loading="termStore.loading"
          :data="tableData"
          row-key="termId"
          height="100%"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />

        <el-table-column prop="termName" label="学期名称" />
        <el-table-column prop="isActive" label="是否启用"/>
        <el-table-column prop="startTime" label="开始时间" width="160"/>
        <el-table-column prop="endTime"  label="结束时间" width="160"/>

        <el-table-column prop="weekNow"  label="当前周次" width="100" />

        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button text type="primary" @click="openEdit(row)">编辑</el-button>
            <el-popconfirm
                title="确认删除？"
                width="200"
                @confirm="handleDel(row.termId)"
            >
              <template #reference>
                <el-button text type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
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
      size="480px"
      :before-close="handleDrawerClose"
  >
    <el-form
        ref="formRef"
        :model="formModel"
        label-width="90px"
        :rules="formRules"
    >
      <el-form-item label="学期名称" prop="termName">
        <el-input v-model="formModel.termName" placeholder="如 2025春季学期" clearable />
      </el-form-item>
      <el-form-item label="状态" prop="isActive">
        <el-switch v-model="formModel.isActive" active-text="启用" inactive-text="禁用"  />
      </el-form-item>

      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker
            v-model="formModel.startTime"
            type="date"
            style="width:100%"
            :disabled-date="onlyMonday"
            @change="onStartChange" />
      </el-form-item>

      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker
            v-model="formModel.endTime"
            type="date"
            style="width:100%"
            :disabled-date="onlySunday"
            :disabled="!formModel.startTime" />
      </el-form-item>

      <el-form-item label="当前周次" prop="weekNow">
        <el-input-number v-model="formModel.weekNow" :min="1" :max="30" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleDrawerClose">取消</el-button>
      <el-button
          type="primary"
          :loading="termStore.loading"
          @click="handleSubmit"
      >保存</el-button>
    </template>
  </el-drawer>
</template>

<script setup>
import { reactive, ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { useTermStore } from '@/stores/TermStore.js'

/* ---------- courseStore ---------- */
const termStore = useTermStore()

/* ---------- 表格 & 分页 ---------- */
const tableData   = ref([])
const selectedIds = ref([])
const pagination  = reactive({ current: 1, size: 10, total: 0 })

function handleSelectionChange(rows) {
  selectedIds.value = rows.map(r => r.termId)
}
async function loadPage() {
  try {
    await termStore.termPage({ page: pagination.current, size: pagination.size, ...searchForm })
    tableData.value  = termStore.list
    pagination.total = termStore.total
    selectedIds.value = []
  } catch {
    ElMessage.error(termStore.error || '加载失败')
  }
}

/* ---------- 查询/重置 ---------- */
const searchForm = reactive({ termId: '', termName: '' })
const field = ref('termId')
const labelMap = { termId: '学期编号', termName: '学期名称' }

function handleSearch() {
  pagination.current = 1
  loadPage()
}
function handleReset() {
  Object.keys(searchForm).forEach(k => searchForm[k] = '')
  field.value = 'termId'
  handleSearch()
}

/* ---------- 删除 ---------- */
async function handleDel(termId) {
  try {
    await termStore.deleteByTermId(termId)
    ElMessage.success('删除成功')
    await loadPage()
  } catch {
    ElMessage.error(termStore.error || '删除失败')
  }
}
async function handleBatchDel() {
  if (!selectedIds.value.length) return
  try {
    for (const id of selectedIds.value) await termStore.deleteByTermId(id)
    ElMessage.success('批量删除成功')
    await loadPage()
  } catch {
    ElMessage.error('批量删除失败')
  }
}

/* ---------- 抽屉表单 ---------- */
const drawerVisible = ref(false)
const isEdit = ref(false)
const drawerTitle = computed(() => (isEdit.value ? '编辑学期' : '新增学期'))

const formRef = ref()
const formModel = reactive({
  termId: '',
  termName: '',
  startTime: '',
  endTime: '',
  weekNow: 1,
  isActive: true
})
const formRules = reactive({
  termId:   [{ required: true, message: '请输入学期编号', trigger: 'blur' }],
  termName: [{ required: true, message: '请输入学期名称', trigger: 'blur' }],
  startTime:[{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime:  [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  weekNow:  [{ required: true, type: 'number', message: '请输入当前周次', trigger: 'blur' }]
})

function openAdd() {
  isEdit.value = false
  resetForm()
  drawerVisible.value = true
}
function openEdit(row) {
  isEdit.value = true
  resetForm()
  Object.assign(formModel, row)
  drawerVisible.value = true
}
function resetForm() {
  formRef.value?.resetFields()
  Object.assign(formModel, {
    termId: '',
    termName: '',
    startTime: '',
    endTime: '',
    weekNow: 1,
    isActive: true
  })
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
  try {
    isEdit.value ? await termStore.updateTerm(formModel) : await termStore.insertTerm(formModel)
    ElMessage.success('保存成功')
    drawerVisible.value = false
    await loadPage()
  } catch {
    ElMessage.error(termStore.error || '保存失败')
  }
}

/* ---------- 日期限制 ---------- */
const onlyMonday  = time => time.getDay() !== 1
const onlySunday  = time => time.getDay() !== 0
function onStartChange(val) {
  formModel.endTime = ''
  if (!val) return
  const monday = new Date(val)
  const sunday = new Date(monday)
  sunday.setDate(monday.getDate() + 6)
  nextTick(() => {
    formRules.endTime = [
      { required: true, message: '请选择结束时间', trigger: 'change' },

    ]
  })
}


onMounted(() => loadPage())
</script>

<style scoped>
.term-crud {
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
</style>