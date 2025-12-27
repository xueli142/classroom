<template>
  <el-container class="thing-crud">
    <!-- 顶部：仅查询 -->
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
        </el-col>
      </el-row>
    </el-header>

    <!-- 表格 -->
    <el-main class="crud-main">
      <el-table
          v-loading="store.loading"
          :data="tableData"
          row-key="id"
          height="100%"
      >
        <el-table-column prop="thingName" label="物品名称"/>
        <el-table-column prop="type" label="类型"/>
        <el-table-column label="能否使用">
          <template #default="{row}">
            <el-tag :type="row.needUse ? 'success':'info'">
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
</template>

<script setup>
/* ---------------- 仓库 ---------------- */
import { useThingStore } from "@/stores/ThingStore.js";
const store = useThingStore();

/* ---------------- 表格 & 分页 ---------------- */
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

const tableData = ref([]);
const pagination = reactive({ current: 1, size: 10, total: 0 });

async function loadPage() {
  try {
    await store.page({
      page: pagination.current,
      size: pagination.size,
      ...searchForm
    });
    tableData.value = store.list;
    pagination.total = store.total;
  } catch {
    ElMessage.error(store.error || '加载失败');
  }
}
  /* ---------------- 查询 ---------------- */
  const searchForm = reactive({ code: '', thingName: '' });
  const field = ref('thingName');
  const labelMap = { code: '物品编号', thingName: '物品名称' };

  function handleSearch() {
    pagination.current = 1;
    loadPage();
  }

  onMounted(() => loadPage());
</script>

<style scoped>
/* 仅保留最简布局，其余样式全部删除 */
.thing-crud {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #fff;
}
.crud-header {
  padding: 12px 16px;
  flex-shrink: 0;
}
.crud-main {
  flex: 1;
  padding: 0 16px 8px;
  overflow: hidden;
}
.crud-footer {
  padding: 0 16px 12px;
  flex-shrink: 0;
  text-align: right;
}
</style>