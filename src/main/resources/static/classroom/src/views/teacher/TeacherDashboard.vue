<template>
  <div class="dashboard-container">
    <!-- 顶部栏 -->
    <el-header class="dashboard-header">
      <div class="logo">教师管理后台</div>
      <div class="user-info">
        <el-button type="danger" size="small" @click="handleLogout">
          退出登录
        </el-button>
      </div>
    </el-header>

    <el-container class="body-wrap">
      <!-- 侧边栏 -->
      <el-aside width="200px" class="side-bar">
        <!-- 改动①：计算属性保持高亮 -->
        <el-menu
            router
            :default-active="activeMenu"
            background-color="#fff"
            text-color="#333"
            active-text-color="#165dff"
        >
          <el-menu-item index="/teacherDashboard/home">
            <el-icon><User /></el-icon>
            <span>教师主页</span>
          </el-menu-item>

          <el-menu-item index="/teacherDashboard/notice">
            <el-icon><Bell /></el-icon>
            <span>公告管理</span>
          </el-menu-item>

          <el-menu-item index="/teacherDashboard/schedule">
            <el-icon><Calendar /></el-icon>
            <span>课表管理</span>
          </el-menu-item>
          <el-menu-item index="/teacherDashboard/course">
            <el-icon><Setting /></el-icon>
            <span>课程设计</span>
          </el-menu-item>
          <el-menu-item index="/teacherDashboard/review">
            <el-icon><Setting /></el-icon>
            <span>课程预约审核</span>
          </el-menu-item>
          <el-menu-item index="/teacherDashboard/activity">
            <el-icon><Setting /></el-icon>
            <span>活动设计</span>
          </el-menu-item>
          <el-menu-item index="/teacherDashboard/booking">
            <el-icon>
              <Setting />          <!-- 这里是真正的图标组件 -->
            </el-icon>
            <span>物品预约</span>
          </el-menu-item>
          <el-menu-item index="/teacherDashboard/setting">
            <el-icon><Setting /></el-icon>
            <span>系统设置</span>
          </el-menu-item>


        </el-menu>
      </el-aside>

      <!-- 内容区 -->
      <el-main class="main-content">
        <!-- 改动②：加 key 强制重新挂载 -->
        <router-view :key="$route.fullPath" />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
/* -------------------- 引入 -------------------- */
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { User, Bell, Calendar, Setting } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/AuthStore'

/* -------------------- 改动①：高亮恢复 -------------------- */
const route = useRoute()
const activeMenu = computed(() => route.path)

/* -------------------- 改动③：退出登录 -------------------- */
const router = useRouter()
const auth = useAuthStore()

async function handleLogout() {
  try {
    await ElMessageBox.confirm('确认退出登录？', '提示', { type: 'warning' })
    await auth.logout()
    router.replace({ name: 'Login' })
  } catch (e) {
    // 用户点“取消”
  }
}
</script>

<style scoped>
/* 原样式一分不改 */
</style>