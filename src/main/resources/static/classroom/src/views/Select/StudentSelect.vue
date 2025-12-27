
<template>
  <el-drawer
      :model-value="visible"
      title="选择学生"
      direction="rtl"
      size="600px"
      @open="onOpen"
      @closed="close"
  >
    <!-- 查询栏 -->
    <el-row :gutter="12" align="middle" style="margin-bottom:12px">
      <el-col :span="8">
        <el-input
            v-model="searchForm.name"
            placeholder="学生姓名"
            clearable
            @keyup.enter="loadData"
        />
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="loadData">查询</el-button>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table v-loading="loading" :data="list" height="400">
      <!-- 左侧“选择状态”列 -->
      <el-table-column width="50" align="center">
        <template #default="{ row }">
          <div
              class="select-box"
              :class="{ selected: row.__selected }"
              @click="toggleSelect(row)">
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="name" label="姓名"/>
      <el-table-column prop="phone" label="手机号"/>
      <el-table-column prop="school" label="学校"/>
      <el-table-column prop="college" label="学院"/>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="page.current"
        v-model:page-size="page.size"
        :total="page.total"
        :page-sizes="[10, 20, 50]"
        layout="total, prev, pager, next"
        @current-change="loadData"
        @size-change="loadData"
        style="margin-top:12px;text-align:right"
    />

    <!-- 底部按钮 -->
    <template #footer>
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="onConfirm">确认</el-button>
    </template>
  </el-drawer>
</template>

<script setup>
import { reactive, ref, nextTick } from 'vue'
import { useStudentStore } from '@/stores/StudentStore.js'
import { ElMessage } from 'element-plus'

/* ---------- props & emit ---------- */
const props = defineProps({
  visible: { type: Boolean, default: false }
})
const emit  = defineEmits(['update:visible', 'selected'])


/* ✅ 1. 用来存放所有被选中的行 */
const selectedRows = ref([])
/* ---------- 查询表单 / 分页 ---------- */
const searchForm = reactive({
  name: '', phone: '', uid: '', uuid: '', school: '', college: '',imageUrl:'',grade:'',major:''
})
const page = reactive({ current: 1, size: 20, total: 0 })

/* ---------- 表格数据 ---------- */
const list      = ref([])
const loading   = ref(false)
let   selectedRow = null          // 当前高亮行

/* ---------- 拉取数据 ---------- */
const store = useStudentStore()
async function loadData() {
  loading.value = true
  try {
    await store.pageStudent({ page: page.current, size: page.size, ...searchForm })
    list.value  = store.list
    page.total  = store.total
  } catch {
    ElMessage.error(store.error || '加载失败')
  } finally {
    loading.value = false
  }
}

/* ---------- 打开抽屉时自动查一次 ---------- */
function onOpen() {
  nextTick(() => loadData())
}



/* ✅ 2. 切换选中状态 */
function toggleSelect(row) {
  // 如果之前没这个字段，先初始化
  if (row.__selected === undefined) row.__selected = false
  row.__selected = !row.__selected

  if (row.__selected) {
    selectedRows.value.push(row)
  } else {
    selectedRows.value = selectedRows.value.filter(r => r !== row)
  }
}
/* ✅ 3. 确认：把数组抛出去 */
function onConfirm() {
  if (!selectedRows.value.length) {
    ElMessage.warning('请至少选择一名学生')
    return
  }
  emit('selected', selectedRows.value)
  close()
}
/* ✅ 4. 关闭时清掉临时状态，防止下次再打开残留 */
function close() {
  selectedRows.value = []
  list.value.forEach(r => delete r.__selected)
  emit('update:visible', false)
}
/* ---------- 关闭 ---------- */
// 脚本部分
/* ---------- 关闭 ---------- */
function handleClose() {     // 2. 同步改名
  selectedRows.value = []
  list.value.forEach(r => delete r.__selected)
  emit('update:visible', false)
}
</script>

<style scoped>
/* 小框样式 */
.select-box {
  width: 16px;
  height: 16px;
  border: 1px solid #dcdfe6;
  border-radius: 2px;
  cursor: pointer;
  transition: background .2s;
}
.select-box.selected {
  background: #67c23a;
  border-color: #67c23a;
}
</style>
