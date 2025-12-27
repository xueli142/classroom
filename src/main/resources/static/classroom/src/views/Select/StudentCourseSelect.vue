<template>
  <div class="course-student-manager">
    <!-- 顶部工具栏 -->
    <div class="toolbar">
      <el-button type="primary" size="large" :loading="loading" @click="handleSubmit">
        打包上传
      </el-button>

      <el-button size="large" @click="studentCourseSelVisible = true">
        选择学生
      </el-button>

      <!-- 批量删除：只有勾选了才高亮 -->
      <el-button
          size="large"
          :disabled="selectedIds.length === 0"
          :loading="batchRemoveLoading"
          @click="handleBatchRemove"
      >
        批量移除
      </el-button>
    </div>

    <!-- 学生列表 -->
    <el-table
        v-loading="tableLoading"
        :data="scStore.studentCourseList"
        size="large"
        border
        stripe
        style="width: 100%; margin-top: 12px"
        @selection-change="handleSelectionChange"
    >
      <template #empty>
        <el-empty description="暂无学生，请先点击右上角「选择学生」添加" />
      </template>

      <!-- 多选列 -->
      <el-table-column type="selection" width="55" />

      <el-table-column label="姓名" prop="userId" width="120" />
      <el-table-column label="课程ID" prop="courseId" width="120" />
      <el-table-column label="状态" width="160">
        <template #default="{ row }">
          <el-tag :type="row.status ? 'success' : 'warning'">
            {{ row.status ? '已审核' : '待审核' }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">


          <el-button
              link
              type="danger"
              size="small"
              :loading="row._loading"
              @click="handleSingleRemove(row.userId)"
          >
            移除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 选择学生弹窗 -->
    <StudentSelect
        v-model:visible="studentCourseSelVisible"
        :course-id="props.courseId"
        multiple
        @selected="onStudentCourseSelected"
    />
  </div>
</template>

<script setup>
/* -----------------  imports  ----------------- */
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import StudentSelect from '@/views/Select/StudentSelect.vue'
import { useStudentCourseStore } from '@/stores/StudentCourseStore.js'

/* -----------------  props  ----------------- */
const props = defineProps({ courseId: { type: String, required: true } })

/* -----------------  store  ----------------- */
const scStore = useStudentCourseStore()

/* -----------------  state  ----------------- */
const loading           = ref(false)          // 提交按钮
const tableLoading      = ref(false)          // 表格加载
const batchRemoveLoading= ref(false)          // 批量删除
const studentCourseSelVisible = ref(false)    // 选择器弹窗
const selectedIds       = ref([])             // 多选 id 集合

/* -----------------  computed  ----------------- */
// 本地列表为空时禁用按钮
const hasData = computed(() => scStore.studentCourseList.length > 0)

/* -----------------  methods  ----------------- */

// 1. 查询课程已有学生
async function fetchList() {
  if (!props.courseId) return
  tableLoading.value = true
  try {
    await scStore.studentCourseIPage({ page: 1, size: 999, courseId: props.courseId})
  } catch (e) {
    ElMessage.error('获取学生列表失败：' + (e.message || e))
  } finally {
    tableLoading.value = false
  }
}

// 2. 打包上传（批量新增）
async function handleSubmit() {
  if (!scStore.studentCourseList.length) {
    ElMessage.warning('请先添加学生')
    return
  }
  loading.value = true
  const toSubmit = scStore.studentCourseList.map(item => ({ ...item, status: true }))
  try {
    await scStore.insertBatch(toSubmit)
    ElMessage.success('提交成功')
    await fetchList()
  } catch (e) {
    ElMessage.error('提交失败：' + (e.message || e))
  } finally {
    loading.value = false
  }
}

// 3. 选择器回调 → 去重追加
function onStudentCourseSelected(userList = []) {
  userList.forEach(u => {
    const exist = scStore.studentCourseList.some(v => v.userId === u.userId)
    if (!exist) scStore.studentCourseList.push({ userId: u.userId, courseId: props.courseId, status: 0 })
  })
}

// 4. 审核单行
async function approveRow(row) {
  try {
    await scStore.updateStatus(row.userId, true)
    row.status = 1
    ElMessage.success('审核成功')
  } catch (e) {
    ElMessage.error('审核失败：' + (e.message || e))
  }
}

// 5. 单行删除
async function handleSingleRemove(userId) {
  await removeByIds([userId])
}

// 6. 批量删除（勾选）
async function handleBatchRemove() {
  if (!selectedIds.value.length) return
  await removeByIds(selectedIds.value)
}

// 7. 统一走 deleteByUserIds
async function removeByIds(userIds) {
  if (!userIds.length) return
  // 按钮菊花
  userIds.forEach(uid => {
    const row = scStore.studentCourseList.find(v => v.userId === uid)
    if (row) row._loading = true
  })
  batchRemoveLoading.value = true
  try {
    await scStore.deleteByUserIds(userIds)   // 你的批量接口
    // 前端立即剔除
    userIds.forEach(uid => {
      const idx = scStore.studentCourseList.findIndex(v => v.userId === uid)
      if (idx > -1) scStore.studentCourseList.splice(idx, 1)
    })
    selectedIds.value = []
    ElMessage.success('移除成功')
  } catch (e) {
    ElMessage.error('移除失败：' + (e.message || e))
  } finally {
    userIds.forEach(uid => {
      const row = scStore.studentCourseList.find(v => v.userId === uid)
      if (row) row._loading = false
    })
    batchRemoveLoading.value = false
  }
}

// 8. 多选事件
function handleSelectionChange(rows) {
  selectedIds.value = rows.map(r => r.userId)
}

/* -----------------  watch  ----------------- */
watch(() => props.courseId, fetchList, { immediate: true })

/* -----------------  mount  ----------------- */
onMounted(() => {
  console.log('[CourseStudentManager] mounted with courseId:', props.courseId)
})
</script>

<style scoped>
.course-student-manager {
  padding: 16px;
  background: #fff;
}
.toolbar {
  display: flex;
  gap: 12px;
}
</style>