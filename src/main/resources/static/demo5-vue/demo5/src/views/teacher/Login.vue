<template>
  <div style="width:300px;margin:80px auto;">
    <h3>教师登录</h3>
    <form @submit.prevent="handle">
      <input v-model="form.uid" placeholder="用户名" /><br/>
      <input v-model="form.password" type="password" placeholder="密码" /><br/>
      <button type="submit">登录</button>
    </form>
    <div style="margin-top: 12px;">
      <router-link to="/student/login">
        <button style="margin-right: 8px;">学生入口</button>
      </router-link>
      <router-link to="/admin/login">
        <button>管理员入口</button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const form = reactive({ uid: '', password: '' })
const store = useAuthStore()
const router = useRouter()

async function handle() {
  try {
    await store.login('teacher', form)
    router.push('/teacher/home')
  } catch (e) {
    alert('登录失败：' + (e.response?.data || e.message))
  }
}
</script>