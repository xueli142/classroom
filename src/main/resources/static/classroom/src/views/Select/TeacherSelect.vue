<!-- TeacherSelector.vue -->
<script setup>
import { reactive, ref, nextTick } from 'vue'
import { useTeacherStore } from '@/stores/TeacherStore'
import { ElMessage } from 'element-plus'

/* ---------- props & emit ---------- */
const props = defineProps({
  visible: { type: Boolean, default: false }
})
const emit  = defineEmits(['update:visible', 'selected'])

/* ---------- 查询表单 / 分页 ---------- */
const searchForm = reactive({
  name: '', phone: '', uid: '', uuid: '', school: '', college: '',imageUrl:'',major:'',
})
const page = reactive({ current: 1, size: 20, total: 0 })

/* ---------- 表格数据 ---------- */
const list      = ref([])
const loading   = ref(false)
let   selectedRow = null          // 当前高亮行

/* ---------- 拉取数据 ---------- */
const store = useTeacherStore()
async function loadData() {
  loading.value = true
  try {
    await store.pageTeacher({ page: page.current, size: page.size, ...searchForm })
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

/* ---------- 确认选择 ---------- */
function onConfirm() {
  if (!selectedRow) {
    ElMessage.warning('请先选择教师')
    return
  }
  emit('selected', { id: selectedRow.userId, name: selectedRow.name })
  close()
}

/* ---------- 关闭 ---------- */
function close() {
  emit('update:visible', false)
}
</script>

<template>
  <el-drawer
      :model-value="visible"
      title="选择教师"
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
            placeholder="教师姓名"
            clearable
            @keyup.enter="loadData"
        />
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="loadData">查询</el-button>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table
        v-loading="loading"
        :data="list"
        height="400"
        highlight-current-row
        @current-change="val => selectedRow = val"
    >
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="school" label="学校" />
      <el-table-column prop="college" label="学院" />
      <el-table-column label="图片">
        <template #default="{ row }">
          <el-image
              v-if="row.imageUrl"
              :src="row.imageUrl"
              style="width:60px;height:40px"
              fit="cover"
              :preview-src-list="[row.imageUrl]"
          />
          <span v-else>没有图片</span>
        </template>
      </el-table-column>
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