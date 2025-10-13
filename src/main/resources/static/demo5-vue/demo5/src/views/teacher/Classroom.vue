<!-- ClassroomManager.vue -->
<script setup>
import { reactive, ref, onMounted } from 'vue'
import {
  getPage,
  createClassroom,
  deleteClassroom,
  updateClassroom,
  getClassroom,
  createClassroomImage,
  updateClassroomImage
} from '@/api/classroom'

const state = reactive({
  page: 0,
  pageSize: 10,
  total: 0,
  classrooms: [],
  urls: [],
  loading: false
})

const keyword = ref('')

const drawer = reactive({
  show: false,
  isEdit: false,
  form: { id: null, name: '', capacity: '' },
  file: null
})

async function load(p = 0) {
  state.loading = true
  try {
    const res = await getPage(p, state.pageSize)
    Object.assign(state, res, { page: p })
  } finally {
    state.loading = false
  }
}

async function handleCreate() {
  Object.assign(drawer, {
    isEdit: false,
    form: { id: null, name: '', capacity: '' },
    file: null,
    show: true
  })
}

async function handleEdit(row) {
  const full = await getClassroom(row.id)
  Object.assign(drawer, {
    isEdit: true,
    form: { id: full.id, name: full.name, capacity: full.capacity },
    file: null,
    show: true
  })
}

async function handleSave() {
  const { id, name, capacity } = drawer.form
  if (!name || !capacity) return alert('请填写完整')

  drawer.show = false
  let classroomId = id
  if (drawer.isEdit) {
    await updateClassroom(id, { name, capacity })
  } else {
    const { id: newId } = await createClassroom({ name, capacity })
    classroomId = newId
  }

  if (drawer.file) {
    drawer.isEdit
        ? await updateClassroomImage(classroomId, drawer.file)
        : await createClassroomImage(classroomId, drawer.file)
  }
  load(state.page)
}

async function handleDelete(id) {
  if (!confirm('确定删除？')) return
  await deleteClassroom(id)
  load(state.page)
}

async function handleSearch() {
  state.loading = true
  try {
    const res = await getPage(0, state.pageSize, keyword.value)
    Object.assign(state, res, { page: 0 })
  } finally {
    state.loading = false
  }
}

function handleSizeChange(val) {
  state.pageSize = val
  load(0)
}

onMounted(() => load(0))
</script>

<template>
  <div class="card">
    <div class="header">
      <input v-model="keyword" placeholder="教室名称" @keyup.enter="handleSearch" />
      <button @click="handleSearch">搜索</button>
      <button @click="handleCreate">新增教室</button>
    </div>

    <table v-if="!state.loading" class="table">
      <thead>
      <tr>
        <th>ID</th>
        <th>教室名称</th>
        <th>容纳人数</th>
        <th>图片</th>
        <th>操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="row in state.classrooms" :key="row.id">
        <td>{{ row.id }}</td>
        <td>{{ row.name }}</td>
        <td>{{ row.capacity }}</td>
        <td>
          <img v-if="state.urls[row.id]" :src="state.urls[row.id]" class="thumb" />
          <span v-else>暂无</span>
        </td>
        <td>
          <button @click="handleEdit(row)">编辑</button>
          <button @click="handleDelete(row.id)">删除</button>
        </td>
      </tr>
      </tbody>
    </table>

    <div class="pagination">
      <button @click="load(state.page - 1)" :disabled="state.page === 0">上一页</button>
      <span>{{ state.page + 1 }} / {{ Math.ceil(state.total / state.pageSize) || 1 }}</span>
      <button @click="load(state.page + 1)" :disabled="(state.page + 1) * state.pageSize >= state.total">下一页</button>
      <select v-model="state.pageSize" @change="handleSizeChange">
        <option :value="10">10</option>
        <option :value="20">20</option>
        <option :value="50">50</option>
      </select>
    </div>
  </div>

  <div v-if="drawer.show" class="drawer">
    <h3>{{ drawer.isEdit ? '编辑教室' : '新增教室' }}</h3>
    <label>名称</label>
    <input v-model="drawer.form.name" />
    <label>容量</label>
    <input type="number" v-model.number="drawer.form.capacity" min="1" />
    <label>图片</label>
    <input type="file" accept="image/*" @change="e => drawer.file = e.target.files[0]" />
    <div>
      <button @click="handleSave">保存</button>
      <button @click="drawer.show = false">取消</button>
    </div>
  </div>
</template>

<style scoped>
.card { border: 1px solid #ddd; padding: 16px; border-radius: 4px; }
.header { display: flex; gap: 8px; margin-bottom: 12px; }
.table { width: 100%; border-collapse: collapse; }
.table th, .table td { border: 1px solid #eee; padding: 6px 8px; text-align: left; }
.thumb { width: 60px; height: 40px; object-fit: cover; }
.pagination { margin-top: 12px; display: flex; align-items: center; gap: 8px; justify-content: flex-end; }
.drawer { position: fixed; top: 0; right: 0; width: 400px; height: 100%; background: #fff; border-left: 1px solid #ccc; padding: 16px; z-index: 999; }
.drawer label { display: block; margin-top: 8px; }
.drawer input { width: 100%; margin-bottom: 8px; }
</style>