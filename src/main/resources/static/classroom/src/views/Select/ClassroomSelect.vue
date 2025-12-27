<template>
  <el-drawer
      :model-value="visible"
      title="选择教室"
      direction="rtl"
      size="700px"
      @open="onOpen"
      @closed="onClose"
  >
    <!-- 查询栏 -->
    <el-row :gutter="12" align="middle" style="margin-bottom: 12px">
      <!-- 字段下拉 -->
      <el-col :span="5">
        <el-select v-model="field" placeholder="查询字段">
          <el-option
              v-for="(label, key) in labelMap"
              :key="key"
              :label="label"
              :value="key"
          />
        </el-select>
      </el-col>

      <!-- 对应输入框 / 下拉 -->
      <el-col :span="7">
        <el-input
            v-if="field !== 'isActive'"
            v-model="searchForm[field]"
            :placeholder="`请输入${labelMap[field]}`"
            clearable
            @keyup.enter="loadData"
        />
        <el-select
            v-else
            v-model="searchForm.isActive"
            placeholder="能否使用"
            clearable
            style="width: 100%"
        >
          <el-option
              v-for="item in activeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-col>

      <!-- 按钮 -->
      <el-col :span="6">
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="onClose">重置</el-button>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table
        v-loading="loading"
        :data="list"
        height="400"
        empty-text="暂无数据"
    >
      <!-- 单选 -->
      <el-table-column width="50" align="center">
        <template #default="{ row }">
          <div
              class="select-box"
              :class="{ selected: row.__selected }"
              @click="toggleSelect(row)"
          />
        </template>
      </el-table-column>

      <el-table-column prop="name"     label="教室名称" />
      <el-table-column prop="location" label="位置" />
      <el-table-column prop="number"   label="最大人数" />
      <el-table-column prop="isActive" label="能否使用" width="100">
        <template #default="{ row }">
          <span :style="{ color: row.isActive ? '#67C23A' : '#F56C6C' }">
            {{ row.isActive ? '可用' : '不可用' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="type"        label="类型" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />

      <!-- 教室图片 -->
      <el-table-column label="教室图片" width="120">
        <template #default="{ row }">
          <el-image
              v-if="row.imageUrl"
              :src="row.imageUrl"
              style="width: 60px; height: 40px"
              fit="cover"
              :preview-src-list="[row.imageUrl]"
              hide-on-click-modal
          >
            <template #error><div class="img-error">暂无</div></template>
          </el-image>
          <div v-else class="img-error">暂无</div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
        v-model:current-page="page.current"
        v-model:page-size="page.size"
        :total="page.total"
        :page-sizes="[10, 20, 50]"
        layout="total, prev, pager, next, sizes"
        @size-change="loadData"
        @current-change="loadData"
        style="margin-top: 12px; text-align: right"
    />

    <!-- 底部按钮 -->
    <template #footer>
      <el-button @click="onClose">取消</el-button>
      <el-button type="primary" @click="onConfirm">确认</el-button>
    </template>
  </el-drawer>
</template>

<script setup>
/* ============================== 依赖 ============================== */
import { reactive, ref, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { useClassroomStore } from '@/stores/ClassroomStore.js'

/* ============================== 接口 ============================== */
const props = defineProps({ visible: { type: Boolean, default: false } })
const emit  = defineEmits(['update:visible', 'selected'])

/* ============================== 查询 & 分页 ============================== */
const searchForm = reactive({ name: '', location: '', type: '', isActive: null })
const field      = ref('name')
const labelMap   = { name: '教室名称', location: '位置', type: '类型' }
const activeOptions = [
  { label: '全部', value: null },
  { label: '可用', value: true },
  { label: '不可用', value: false }
]
const page = reactive({ current: 1, size: 20, total: 0 })

/* ============================== 表格 ============================== */
const list        = ref([])
const loading     = ref(false)
const selectedMap = reactive(new Map())   // 单选缓存：key = classroomId

/* ============================== store ============================== */
const store = useClassroomStore()

async function loadData() {
  loading.value = true
  try {
    const params = {
      page: page.current,
      size: page.size,
      [field.value]: searchForm[field.value] || undefined,
      isActive: searchForm.isActive !== null ? searchForm.isActive : undefined
    }
    await store.classroomIPage(params)
    list.value = store.list.map(r => ({
      ...r,
      __selected: selectedMap.has(r.classroomId)
    }))
    page.total = store.total
  } catch {
    ElMessage.error(store.error || '网络异常，请稍后重试')
  } finally {
    loading.value = false
  }
}

/* ============================== 生命周期 ============================== */
function onOpen() { nextTick(() => loadData()) }

function onClose() {
  Object.keys(searchForm).forEach(k => (searchForm[k] = ''))
  searchForm.isActive = null
  field.value   = 'name'
  page.current  = 1
  selectedMap.clear()
  emit('update:visible', false)
}

/* ============================== 单选 ============================== */
function toggleSelect(row) {
  list.value.forEach(r => (r.__selected = false))
  selectedMap.clear()
  row.__selected = true
  selectedMap.set(row.classroomId, row)
}

function onConfirm() {
  if (!selectedMap.size) return ElMessage.warning('请先选择教室')
  const [row] = selectedMap.values()
  emit('selected', {
    classroomId: row.classroomId,
    name:        row.name,
    number:      row.number,
    location:    row.location
  })
  onClose()
}
</script>

<style scoped>
/* ========= 单选小方块 ========= */
.select-box {
  width: 16px;
  height: 16px;
  border: 1px solid #dcdfe6;
  border-radius: 2px;
  cursor: pointer;
  transition: background .2s;
}
.select-box.selected {
  background: #409EFF;
  border-color: #409EFF;
}

/* ========= 图片占位 ========= */
.img-error {
  width: 60px;
  height: 40px;
  line-height: 40px;
  background: #f5f7fa;
  color: #909399;
  text-align: center;
  font-size: 12px;
}
</style>