<template>
  <div class="dashboard-container">
    <!-- 顶部栏 -->
    <el-header class="dashboard-header">
      <div class="logo">课表查看平台</div>
      <div class="user-info">
        <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
      </div>
    </el-header>

    <el-container class="body-wrap">
      <!-- 侧边栏 -->
      <el-aside width="200px" class="side-bar">
        <el-menu
            router
            :default-active="$route.path"
            background-color="#fff"
            text-color="#333"
            active-text-color="#165dff"
        >
          <el-menu-item index="/studentDashboard/home">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>

          <el-menu-item index="/studentDashboard/notice">
            <el-icon><Bell /></el-icon>
            <span>公告板</span>
          </el-menu-item>

          <el-menu-item index="/studentDashboard/schedule">
            <el-icon><Calendar /></el-icon>
            <span>个人课表</span>
          </el-menu-item>
          <el-menu-item index="/studentDashboard/activityBooking">
            <el-icon>
              <Setting />          <!-- 这里是真正的图标组件 -->
            </el-icon>
            <span>活动预约</span>
          </el-menu-item>
          <el-menu-item index="/studentDashboard/courseBooking">
            <el-icon>
              <Setting />          <!-- 这里是真正的图标组件 -->
            </el-icon>
            <span>课程预约</span>
          </el-menu-item>
          <el-menu-item index="/studentDashboard/booking">
            <el-icon>
              <Setting />          <!-- 这里是真正的图标组件 -->
            </el-icon>
            <span>物品预约</span>
          </el-menu-item>

          <el-menu-item index="/studentDashboard/setting">
            <el-icon>
              <Setting />          <!-- 这里是真正的图标组件 -->
            </el-icon>
            <span>系统设置</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 内容区 -->
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
/* -------------------- 引入 -------------------- */
import { useRouter } from 'vue-router'
import {User, Bell, Calendar, Setting} from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/AuthStore'


/* -------------------- 退出登录 -------------------- */
const router = useRouter()
const auth   = useAuthStore()

function handleLogout() {
  ElMessageBox.confirm('确认退出登录？', '提示', { type: 'warning' })
      .then(() => auth.logout())
      .then(() => router.replace({ name: 'Login' }))
}
</script>

<style scoped>
.dashboard-container{
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f7f9;
}
.dashboard-header{
  flex-shrink: 0;
  height: 60px;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,.08);
}
.body-wrap{
  flex: 1;
  display: flex;
  overflow: hidden;
}
.side-bar{
  background: #fff;
  border-right: 1px solid #e5e7eb;
}
.main-content{
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f5f7f9;
}

/* 路由切换动画 */
.fade-enter-active,
.fade-leave-active{
  transition: opacity .2s;
}
.fade-enter-from,
.fade-leave-to{
  opacity: 0;
}
</style>