<!-- src/views/Dashboard.vue -->
<script setup>
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'

const auth = useAuthStore()
// 解构保持响应式
const { user, role, token, isLogin } = storeToRefs(auth)
</script>

<template>
  <div class="dashboard">
    <h1>个人中心</h1>

    <!-- 未登录兜底 -->
    <div v-if="!isLogin" class="tip">
      您还未登录，<router-link to="/login">请先登录</router-link>
    </div>

    <!-- 已登录 -->
    <div v-else class="card">
      <img
          class="avatar"
          :src="user?.avatar || 'https://via.placeholder.com/80'"
          alt="avatar"
      />
      <p class="name">{{ user?.name || '未登录状态' }}</p>

      <p class="meta">
        Token：<code class="token">{{ token.slice(0, 8) }}***</code>
      </p>

      <button class="logout" @click="auth.logout">退出登录</button>
    </div>

    <div style="margin-top: 12px;">
      <router-link to="/student/classroom">
        <button>教室</button>
      </router-link>
    </div>
    <div style="margin-top: 12px;">
      <router-link to="/student/home">
        <button>主页</button>
      </router-link>
    </div>
  </div>
</template>

<style scoped>
.dashboard {
  max-width: 480px;
  margin: 60px auto;
  text-align: center;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}
.tip {
  color: #999;
}
.card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  padding: 32px 24px;
}
.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-bottom: 12px;
}
.name {
  font-size: 20px;
  font-weight: 600;
  margin: 8px 0;
}
.meta {
  color: #666;
  margin: 4px 0;
}
.role {
  color: #409eff;
  font-weight: 500;
}
.token {
  background: #f5f5f5;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 13px;
}
.logout {
  margin-top: 24px;
  padding: 8px 24px;
  border: none;
  background: #409eff;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;
}
.logout:hover {
  background: #66b1ff;
}
</style>