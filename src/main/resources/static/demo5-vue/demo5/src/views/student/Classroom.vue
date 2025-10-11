<template>
  <div class="bar">
    <input
        v-model="keyword"
        placeholder="请输入教室名称或位置"
        @keyup.enter="handleSearch"
    />
    <button @click="handleSearch">查询</button>
    <button @click="handleReset">重置</button>
  </div>

  <!-- 教室卡片 -->
  <div class="list">
    <div class="card" v-for="(c,i) in state.classrooms" :key="c.classroomId">
      <img :src="state.urls[i]" :alt="c.name + '照片'" />
      <h3>{{ c.name }}</h3>
      <p>位置：{{ c.location }}</p>
      <p>描述：{{ c.description }}</p>
      <p class="status" :class="c.isActive?'ok':'no'">
        状态：{{ c.isActive ? '不可用' : '可用' }}
      </p>
    </div>
  </div>

  <!-- 分页 -->
  <button @click="load(state.page-1)" :disabled="state.page<=0">上一页</button>
  <button @click="load(state.page+1)">下一页</button>
</template>

<script setup>
import { ref } from 'vue'
import { useClassroom } from '@/stores/classroom.js'

const { state, load, search } = useClassroom()

/* 查询关键字 */
const keyword = ref('')

/* 点查询 */
function handleSearch() {
  search(keyword.value.trim())
}

/* 重置 */
function handleReset() {
  keyword.value = ''
  load(0)          // 回到第一页
}
</script>

<style scoped>
.bar{margin-bottom:12px;display:flex;gap:8px}
.bar input{flex:1;padding:4px 8px}
.list{display:flex;flex-wrap:wrap;gap:12px}
.card{border:1px solid #ccc;padding:12px;width:220px;border-radius:6px}
.status.ok{color:green}
.status.no{color:red}
</style>