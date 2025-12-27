<script setup>
import {useStudentCourseStore} from "@/stores/StudentCourseStore.js";
import {useAuthStore} from "@/stores/AuthStore.js";
import {useCourseStore} from "@/stores/CourseStore.js";
import {onMounted, reactive, ref} from "vue";
import {ElMessage} from "element-plus";

const courseStore = useCourseStore()
const authStore = useAuthStore()
const studentCourseStore = useStudentCourseStore()
const list    = ref([])
const loading = ref(false)
const searchForm = reactive({ name: '' /* 其余字段按需保留 */ })
const page = reactive({ current: 1, size: 20, total: 0 })
const courseId = ref('')
const selectedRow = ref(null)   // 当前选中的行对象
const classroom =ref()
/* ---------- 预约占位 ---------- */
async function addOne() {
  if (!selectedRow.value) {
    ElMessage.warning('请先选择一门课程')
    return
  }
  try {
    // 注意：传 .value
    await studentCourseStore.insertOne(authStore.user.userId, courseId.value)
    ElMessage.success('预约成功')
    selectedRow.value = null
    courseId.value    = ''
    await loadData()          // 刷新
  } catch {
    ElMessage.error(studentCourseStore.studentCourseError || '预约失败')
  }
}


async function loadData() {
  loading.value = true
  try {
    // 2. 调用课程分页方法，参数里把课程名称传过去
    await courseStore.courseIPage({
      page: page.current,
      size: page.size,
      needAudit:true,

    })
    list.value  = courseStore.courseList   // 3. 对应字段改名
    page.total  = courseStore.courseTotal
  } catch {
    ElMessage.error(courseStore.courseError || '加载失败')
  } finally {
    loading.value = false
  }
}

/* ---------- 查询 ---------- */
function handleSearch() {
  page.current = 1
  loadData()
}
/* ---------- 分页 ---------- */
function handleCurrentChange(val) {
  page.current = val
  loadData()
}


onMounted(() => loadData())
</script>


<template>
  <div class="wrap">
    <!-- 查询条 -->
    <el-form :inline="true" :model="searchForm" class="search-bar">
      <el-form-item label="课程名称">
        <el-input v-model="searchForm.courseName" placeholder="请输入" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
      </el-form-item>
    </el-form>

    <!-- 列表 -->
    <el-table
        v-loading="loading"
        :data="list"
        highlight-current-row
        @current-change="(row) => { selectedRow = row; courseId = row?.courseId || '' }"
        border
    >
      <el-table-column width="55">
        <!-- 自定义单选列，element-plus 没有内置单选列，用 current-row 即可 -->
        <template #default="{ row }">
          <el-radio :model-value="selectedRow?.courseId" :label="row.courseId">
            &nbsp;
          </el-radio>
        </template>
      </el-table-column>


      <el-table-column prop="courseName" label="课程名称" />
      <el-table-column prop="teacherName" label="授课教师" />
      <el-table-column prop="description" label="课程介绍" />
      <el-table-column prop="number" label="已经预约人数" />
      <!-- 其它字段按需添加 -->
    </el-table>

    <!-- 分页 -->
    <el-pagination
        background
        layout="total, prev, pager, next"
        :total="page.total"
        :page-size="page.size"
        :current-page="page.current"
        @current-change="handleCurrentChange"
    />

    <!-- 操作按钮 -->
    <div class="footer-btn">
      <el-button type="primary" :disabled="!selectedRow" @click="addOne">
        预约占位
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.wrap { padding: 16px; }
.search-bar { margin-bottom: 12px; }
.footer-btn { margin-top: 16px; text-align: right; }
</style>