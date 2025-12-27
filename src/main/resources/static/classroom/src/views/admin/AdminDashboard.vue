<template>
  <div class="dashboard-container">
    <!-- 顶部栏 -->
    <el-header class="dashboard-header">
      <div class="logo">系统管理平台</div>
      <div class="user-info">
        <el-button type="danger" size="small" @click="auth.logout()">退出登录</el-button>
      </div>
    </el-header>

    <el-container class="body-wrap">
      <!-- 侧边栏 -->
      <el-aside width="200px" class="side-bar">
        <el-menu

            :default-active="activeKey"
            @select="handleSelect"
            background-color="#fff"
            text-color="#333"
            active-text-color="#165dff"
        >
          <el-menu-item index="/adminDashboard/home">
            <el-icon><User /></el-icon>
            <span>个人信息</span>
          </el-menu-item>

          <el-sub-menu index="2">
            <template #title>
              <el-icon><OfficeBuilding /></el-icon>
              <span>教学资源</span>
            </template>

            <el-menu-item index="classroom">教室管理</el-menu-item>
            <el-menu-item index="course">课程管理</el-menu-item>
            <el-menu-item index="">教室资源管理</el-menu-item>
            <el-menu-item index="term">学期管理</el-menu-item>

          </el-sub-menu>

          <el-sub-menu index="3">
            <template #title>
              <el-icon><Avatar /></el-icon>
              <span>人员管理</span>
            </template>
            <el-menu-item index="teacher">教师管理</el-menu-item>
            <el-menu-item index="student">学生管理</el-menu-item>
            <el-menu-item index="admin">管理员管理</el-menu-item>
          </el-sub-menu >
          <el-sub-menu index="4">
            <template #title>
              <el-icon></el-icon>
              <span>预约管理</span>

            </template>

            <el-menu-item index="studentBooking" >学生预约</el-menu-item>
            <el-menu-item index="teacherBooking" >教师预约</el-menu-item>
            <el-menu-item index="classroomBooking">教室预约</el-menu-item>
            <el-menu-item index="resourceBooking">教室资源预约</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="5">
            <template #title>
              <el-icon></el-icon>
              <span>课程表管理</span>
            </template>
            <el-menu-item index="stuSchedule">学生课程表</el-menu-item>
            <el-menu-item index="teaSchedule">教师课程表</el-menu-item>
            <el-menu-item index="classroomSchedule">教室使用情况</el-menu-item>
            <el-menu-item index="classroomResource">教室资源使用情况</el-menu-item>
          </el-sub-menu>

          <el-menu-item index="notice">
            <el-icon><Bell /></el-icon>
            <span>公告管理</span>
          </el-menu-item>

          <el-sub-menu index="7">
            <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
              </template>

            <el-menu-item index="sysSetting">系统问题审核</el-menu-item>

          </el-sub-menu>
        </el-menu>
      </el-aside>

      <!-- 内容区 -->
      <el-main class="main-content">
        <component :is="currentCom" />
      </el-main>
    </el-container>
  </div>

</template>
<script setup>
import { ref, computed } from 'vue'
import { useAuthStore } from '@/stores/AuthStore.js'
import {
  User, OfficeBuilding, Avatar, Bell, Setting
} from '@element-plus/icons-vue'
import TeacherManage from '@/views/admin/Manage/TeacherMange.vue'
import StudentManage from '@/views/admin/Manage/StudentMange.vue'
import AdminManage   from '@/views/admin/Manage/AdminMange.vue'
import ClassroomMange from "@/views/admin/Manage/ClassroomMange.vue";
import CourseMange from "@/views/admin/Manage/CourseMange.vue";
import TermManage from "@/views/admin/Manage/TermManage.vue";

import ClassroomSchedule from "@/views/admin/Schedule/ClassroomSchedule.vue";

import ClassroomResource from "@/views/admin/Schedule/ClassroomResource.vue";

import ClassroomBookingReview from "@/views/admin/Booking/ClassroomBookingReview.vue";
import TeacherBooking from "@/views/admin/Booking/TeacherBooking.vue";
import StudentBooking from "@/views/admin/Booking/StudentBooking.vue";
import ResourcesBooking from "@/views/admin/Booking/ResourcesBooking.vue";

import SysSetting from "@/views/admin/Setting/SysSettingMange.vue";

const activeKey = ref('teacher')

/* 映射表：key -> 组件 */
const comMap = {
  teacher: TeacherManage,
  student: StudentManage,
  admin:AdminManage,
  classroom:ClassroomMange,
  course:CourseMange,
  term:TermManage,

  classroomSchedule:ClassroomSchedule,
  classroomResource:ClassroomResource,
  studentBooking:StudentBooking,
  teacherBooking:TeacherBooking,
  classroomBooking:ClassroomBookingReview,
  resourceBooking:ResourcesBooking,
  sysSetting:SysSetting,

}

/* 计算属性：返回当前要渲染的组件 */
const currentCom = computed(() => comMap[activeKey.value])

/* 菜单选中事件：只改本地状态，不跳转 */
function handleSelect(key) {
  activeKey.value = key
}






const auth = useAuthStore()
</script>

<style scoped>
.dashboard-container{height:100vh;display:flex;flex-direction:column;background:#f5f7f9;}
.dashboard-header{flex-shrink:0;height:60px;padding:0 20px;display:flex;justify-content:space-between;align-items:center;background:#fff;box-shadow:0 2px 8px rgba(0,0,0,.08);}
.body-wrap{flex:1;display:flex;overflow:hidden;}
.side-bar{background:#fff;border-right:1px solid #e5e7eb;}
.main-content{flex:1;padding:20px;overflow-y:auto;background:#f5f7f9;}
</style>