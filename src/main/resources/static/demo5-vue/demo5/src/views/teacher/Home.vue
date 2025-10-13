<template>
  <div style="padding: 20px;">
    <h3>个人首页</h3>
    <p>欢迎你，{{ store.user?.name || '老师' }}</p>
    <router-link to="/teacher/dashboard">
      <button style="margin-right: 8px;">挡泥板</button>
    </router-link>
    <router-link to="/teacher/classroom">
      <button style="margin-right: 8px;">教室</button>
    </router-link>

    <hr style="margin: 16px 0;">
    <button @click="store.logout()">退出登录</button>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import {useRouter} from "vue-router";
const store = useAuthStore()
const router = useRouter()
</script>