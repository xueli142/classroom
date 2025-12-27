
<template>
  <el-drawer
      :model-value="visible"
      title="选择课程"
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
            placeholder="课程名称"
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

      <el-table-column prop="courseName" label="课程名称"/>
      <el-table-column
          prop="description"
          label="课程介绍"

          show-overflow-tooltip>
        <template #default="{ row }">
          <!-- 有内容正常显示，空字段用零宽空格占位 -->
          <span>{{ row.description || '\u200B' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="teacherName" label="老师"/>



      <el-table-column
          label="图片"
          width="80">
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
import { useCourseStore } from '@/stores/CourseStore.js'   // 1. 换成课程 courseStore
import { ElMessage } from 'element-plus'

/* ---------- props & emit ---------- */
const props = defineProps({ visible: { type: Boolean, default: false }})
const emit  = defineEmits(['update:visible', 'selected'])

/* ---------- 选中缓存 ---------- */
const selectedRows = ref([])

/* ---------- 查询表单 / 分页 ---------- */
const searchForm = reactive({ name: '' /* 其余字段按需保留 */ })
const page = reactive({ current: 1, size: 20, total: 0 })

/* ---------- 表格数据 ---------- */
const list    = ref([])
const loading = ref(false)

/* ---------- courseStore 实例 ---------- */
const store = useCourseStore()

/* ---------- 拉取数据 ---------- */
async function loadData() {
  loading.value = true
  try {
    // 2. 调用课程分页方法，参数里把课程名称传过去
    await store.courseIPage({
      page: page.current,
      size: page.size,
      name: searchForm.name          // 课程名称作为查询条件
    })
    list.value  = store.courseList   // 3. 对应字段改名
    page.total  = store.courseTotal
  } catch {
    ElMessage.error(store.courseError || '加载失败')
  } finally {
    loading.value = false
  }
}

/* ---------- 生命周期 ---------- */
function onOpen() { nextTick(() => loadData()) }

/* ---------- 选中逻辑 ---------- */
function toggleSelect(row) {
  if (row.__selected === undefined) row.__selected = false
  row.__selected = !row.__selected
  if (row.__selected) selectedRows.value.push(row)
  else selectedRows.value = selectedRows.value.filter(r => r !== row)
}

function onConfirm() {
  if (!selectedRows.value.length) {
    ElMessage.warning('请至少选择一门课程')
    return
  }
  emit('selected', selectedRows.value)
  close()
}

function close() {
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
