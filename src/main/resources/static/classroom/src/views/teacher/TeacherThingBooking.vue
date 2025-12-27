<template>
  <el-container class="thing-crud">
    <!-- 顶部：仅查询 -->
    <el-header class="crud-header">
      <el-row :gutter="12" align="middle">
        <el-col :span="4">
          <el-select v-model="field" placeholder="查询字段">
            <el-option label="物品描述" value="description"/>
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

    <!-- 表格：增加“借阅”按钮 -->
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

        <!-- 新增操作列 -->
        <el-table-column label="操作" width="100">
          <template #default="{row}">
            <el-button type="primary" link @click="openBorrow(row)">借阅</el-button>
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

  <!-- 借阅弹窗 -->
  <el-dialog v-model="borrowVisible" title="物品借阅" width="380px" append-to-body>
    <el-form :model="borrowForm" label-width="80px">
      <el-form-item label="物品名称">
        <el-input :model-value="borrowForm.thingName" disabled/>
      </el-form-item>
      <el-form-item label="借阅天数">
        <el-input-number v-model="borrowForm.time" :min="1" style="width:100%"/>
      </el-form-item>
      <!-- 开始日期：仅展示，不可改 -->
      <el-form-item label="开始日期">
        <el-date-picker
            :model-value="today"
            type="date"
            style="width:100%"
            disabled
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="borrowVisible=false">取消</el-button>
      <el-button type="primary" :loading="bookingStore.loading" @click="confirmBorrow">确认</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
/* ---------------- 仓库 ---------------- */
import { useThingStore } from "@/stores/ThingStore.js";
import { useThingBookingStore } from "@/stores/ThingBookingStore.js";
import {useAuthStore} from "@/stores/AuthStore.js";

const store = useThingStore();
const bookingStore = useThingBookingStore();
const authStore=useAuthStore();
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

/* ---------------- 借阅 ---------------- */
const borrowVisible = ref(false);
const borrowForm = reactive({
  thingId: '',
  thingName: '',
  userId: authStore.user.userId, // 请换成真实 userId
  time: 1
  // startTime 不再绑定，直接拿 today
});

/* 今天（YYYY-MM-DD） */
const today = new Date().toISOString().slice(0, 10);

function openBorrow(row) {
  borrowForm.thingId = row.thingId;
  borrowForm.thingName = row.thingName;
  borrowForm.time = 1;
  borrowVisible.value = true;
}

async function confirmBorrow() {
  try {
    // 直接把 today 作为 startTime 发给后端
    await bookingStore.insertOne({
      ...borrowForm,
      startTime: today
    });
    ElMessage.success('借阅成功');
    borrowVisible.value = false;
    loadPage();
  } catch {
    ElMessage.error(bookingStore.error || '借阅失败');
  }
}

onMounted(() => loadPage());
</script>

<style scoped>
/* 原样式不动 */
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