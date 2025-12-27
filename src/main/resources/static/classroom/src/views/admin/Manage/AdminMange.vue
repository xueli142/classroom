<template>
  <el-container class="admin-crud">
    <!-- 头部：查询栏 + 按钮 -->
    <el-header class="crud-header">
      <el-row :gutter="12" align="middle">
        <el-col :span="4">
          <el-select v-model="field" placeholder="查询字段">
            <el-option label="用户名" value="uid"/>
            <el-option label="姓名" value="name"/>
            <el-option label="手机号" value="phone"/>
            <el-option label="邮箱" value="email"/>
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

    <!-- 中间：表格 -->
    <el-main class="crud-main">
      <el-table
          ref="multipleTableRef"
          v-loading="adminStore.loading"
          :data="tableData"
          row-key="uid"
          height="100%"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="uid"      label="用户名" />
        <el-table-column prop="name"     label="姓名" />
        <el-table-column prop="realName" label="真实姓名" />
        <el-table-column prop="phone"    label="手机号" />
        <el-table-column prop="email"    label="邮箱" />
        <el-table-column prop="createBy" label="创建人" />
        <el-table-column prop="role"     label="角色" />
        <el-table-column prop="isActive" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.isActive ? 'success' : 'info'">
              {{ row.isActive ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button text type="primary" @click="openEdit(row)">编辑</el-button>
            <el-popconfirm
                title="确认删除？"
                width="200"
                @confirm="handleDel(row.userId)"
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
      <el-form-item label="用户名" prop="uid">
        <el-input v-model="formModel.uid" placeholder="用于登录，唯一" clearable />
      </el-form-item>

      <el-form-item label="姓名" prop="name">
        <el-input v-model="formModel.name" placeholder="展示用昵称" clearable />
      </el-form-item>

      <el-form-item label="真实姓名" prop="realName">
        <el-input v-model="formModel.realName" placeholder="真实姓名" clearable />
      </el-form-item>

      <el-form-item label="手机号" prop="phone">
        <el-input v-model="formModel.phone" placeholder="手机号" clearable />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="formModel.email" placeholder="邮箱" clearable />
      </el-form-item>

      <!-- 创建人：仅显示，不可改 -->
      <el-form-item label="创建人">
        <el-input :model-value="currentUser.uid" disabled />
      </el-form-item>

      <el-form-item label="状态" prop="isActive">
        <el-switch v-model="formModel.isActive" active-text="启用" inactive-text="禁用" />
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input
            v-model="formModel.password"
            type="password"
            placeholder="6-20位密码"
            show-password
        />
      </el-form-item>

      <el-form-item label="备注" prop="besides">
        <el-input
            v-model="formModel.besides"
            type="textarea"
            :rows="3"
            placeholder="选填，最多2000字"
            maxlength="2000"
            show-word-limit
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleDrawerClose">取消</el-button>
      <el-button
          type="primary"
          :loading="adminStore.loading"
          @click="handleSubmit"
      >保存</el-button>
    </template>
  </el-drawer>
</template>

<script setup>
import { reactive, ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { useAdminStore } from '@/stores/AdminStore.js'
import { useAuthStore } from '@/stores/AuthStore.js'   // 你的登录用户 courseStore
import readXlsxFile from 'read-excel-file'

/* ---------- 当前登录用户 ---------- */
const authStore = useAuthStore()
const currentUser = authStore.user   // { uid, name, ... }

/* ---------- 主 courseStore ---------- */
const adminStore = useAdminStore()

/* 表格 & 分页 */
const tableData = ref([])
const selectedIds = ref([])
const pagination = reactive({ current: 1, size: 10, total: 0 })

function handleSelectionChange(rows) {
  selectedIds.value = rows.map(r => r.uid)
}

async function loadPage() {
  try {
    await adminStore.pageAdmin({
      page: pagination.current,
      size: pagination.size,
      ...searchForm
    })
    tableData.value = adminStore.list
    pagination.total = adminStore.total
    selectedIds.value = []
  } catch {
    ElMessage.error(adminStore.error || '加载失败')
  }
}

/* 查询/重置 */
const searchForm = reactive({ uid: '', name: '', phone: '', email: '' })
const field = ref('name')
const labelMap = { uid: '用户名', name: '姓名', phone: '手机号', email: '邮箱' }

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
async function handleDel(userId) {
  try {
    await adminStore.delAdmin(userId)
    ElMessage.success('删除成功')
    await loadPage()
  } catch {
    ElMessage.error(adminStore.error || '删除失败')
  }
}
async function handleBatchDel() {
  if (!selectedIds.value.length) return
  try {
    await adminStore.batchDelAdmin(selectedIds.value)
    ElMessage.success('批量删除成功')
    await loadPage()
  } catch {
    ElMessage.error(adminStore.error || '批量删除失败')
  }
}

/* 抽屉表单 */
const drawerVisible = ref(false)
const isEdit = ref(false)
const drawerTitle = computed(() => (isEdit.value ? '编辑管理员' : '新增管理员'))

const formRef = ref()
const formModel = reactive({
  uid: '',
  name: '',
  realName: '',
  phone: '',
  email: '',
  createBy: '',   // 后台实际接收字段
  isActive: true,
  password: '',
  besides: ''
})

/* 完整校验规则 */
const formRules = {
  uid: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
  isActive: [{ required: true, message: '请选择状态', trigger: 'change' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度 6-20 位', trigger: 'blur' }
  ],
  besides: [{ max: 2000, message: '备注不能超过2000字', trigger: 'blur' }]
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
  Object.keys(formModel).forEach(k => (formModel[k] = k === 'isActive' ? true : ''))
}
function handleDrawerClose() {
  drawerVisible.value = false
}

/* 保存：自动补 createBy = 当前用户 UID */
async function handleSubmit() {
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  /* 自动赋值创建人 */
  formModel.createBy = currentUser.uid

  try {
    isEdit.value
        ? await adminStore.updateAdmin(formModel)
        : await adminStore.addAdmin(formModel)
    ElMessage.success('保存成功')
    drawerVisible.value = false
    await loadPage()
  } catch {
    ElMessage.error(adminStore.error || '保存失败')
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
    '用户名': 'uid',
    '姓名': 'name',
    '真实姓名': 'realName',
    '手机号': 'phone',
    '邮箱': 'email',
    '密码': 'password',
    '备注': 'besides'
  }
  const dtoList = rows.slice(1).map(row => {
    const item = { isActive: true, createBy: currentUser.uid }
    header.forEach((h, idx) => {
      const key = keyMap[h?.trim()]
      if (key) item[key] = row[idx]
    })
    return item
  })
  try {
    await adminStore.batchAddAdmin(dtoList)
    ElMessage.success(`成功导入 ${dtoList.length} 条数据`)
    loadPage()
  } catch {
    ElMessage.error(adminStore.error || '批量导入失败')
  }
}

onMounted(() => loadPage())
</script>

<style scoped>
.admin-crud {
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