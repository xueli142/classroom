<template>
  <section class="page">
    <!-- 列表模式 -->
    <div v-if="!current" class="list-mode">
      <div class="card-wrap">
        <ClassroomCard
            v-for="item in store.list"
            :key="item.classroomId"
            :data="item"
            @click-card="handleClickCard"
        />
      </div>
      <el-pagination
          background
          layout="prev, pager, next"
          :total="store.total"
          :page-size="10"
          @current-change="handlePage"
      />
    </div>

    <!-- 详情模式 -->
    <ClassroomDetail v-else :record="current" @back="current = null" />
  </section>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useClassroomStore } from '@/stores/ClassroomStore'
import ClassroomCard from '@/views/admin/Card/ClassroomCard.vue'
import ClassroomDetail from '@/views/admin/Card/ClassroomDetail.vue'

/* ----- 状态 ----- */
const store      = useClassroomStore()
const current    = ref(null)   // null 表示列表，非 null 表示当前要展示的记录

/* ----- 事件 ----- */
// 点击卡片：把整条记录塞进 current，模板会自动切到详情
function handleClickCard(record) {
  current.value = record
}



// 分页：保持原有逻辑
function handlePage(page) {
  store.classroomIPage({ page, size: 10 })
}

/* ----- 生命周期 ----- */
onMounted(() => store.classroomIPage({ page: 1, size: 10 }))
</script>

<style scoped>
.page { padding: 20px; }
.card-wrap { display: flex; flex-wrap: wrap; gap: 16px; margin-bottom: 20px; }
</style>