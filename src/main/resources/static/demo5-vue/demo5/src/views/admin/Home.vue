<template>
  <div>
    <h3>管理员首页</h3>
    <p>欢迎你，{{ store.user?.name }}</p>
    <button @click="store.logout()">退出</button>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
const store = useAuthStore()
</script>