<template>
  <div style="width:320px;margin:80px auto;">
    <h3>学生注册</h3>
    <form @submit.prevent="handleRegister">
      <input v-model="form.name"    placeholder="姓名" required /><br/>
      <input v-model="form.password" type="password" placeholder="密码" required /><br/>
      <input v-model="form.uid"     placeholder="学号" required /><br/>
      <input v-model="form.phone"   placeholder="手机号" required /><br/>
      <input v-model="form.email"   placeholder="邮箱"  required /><br/>
      <button type="submit">注册</button>
    </form>

    <div style="margin-top: 12px;">
      <router-link to="/student/login">
        <button>已有账号？去登录</button>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const form = reactive({
  name: '',
  password: '',
  uid: '',
  phone: '',
  email: ''
})

const store = useAuthStore()
const router = useRouter()

async function handleRegister() {
  try {
    await store.register('student', form)
    alert('注册成功！')
    router.push('/student/login')   // ← 跳到登录首页
  } catch (e) {
    alert('注册失败：' + (e.response?.data || e.message))
  }
}
</script>