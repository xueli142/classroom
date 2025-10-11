<template>
  <div style="width:300px;margin:80px auto;">
    <h3>学生登录</h3>
    <form @submit.prevent="handleLogin">
      <input v-model="form.uid" placeholder="用户名" /><br/>
      <input v-model="form.password" type="password" placeholder="密码" /><br/>
      <button type="submit">登录</button>
    </form>

    <!-- 👇 新增：跳转到注册页 -->
    <div style="margin-top: 12px;">
      <router-link to="/student/register">
        <button>学生注册</button>
      </router-link>
    </div>

    <div style="margin-top: 12px;">
      <router-link to="/teacher/login">
        <button style="margin-right: 8px;">教师入口</button>
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

const form = reactive({uid: '', password: '' })
const store = useAuthStore()
const router = useRouter()

async function handleLogin() {
  try {
    await store.login('student', form)
    router.push('/student/home')
  } catch (e) {
    alert('登录失败：' + (e.response?.data || e.message))
  }
}
</script>