import { createRouter, createWebHistory } from 'vue-router'
import TeacherCourse from "@/views/teacher/TeacherCourse.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      alias: '/login',
      name: 'Login',
      component: () => import('@/views/Login.vue')
    },

    /* 管理端 ---------------------------------------------------------- */
    {
      path: '/adminDashboard',
      name: 'AdminDashboard',
      component: () => import('@/views/admin/AdminDashboard.vue'),
      redirect: '/adminDashboard/home',   // 默认子页
      children: [
        { path: 'home',      name: 'AdminHome',     component: () => import('@/views/admin/AdminHome.vue') },
        { path: 'classroom', name: 'AdminClassroom',component: () => import('@/views/admin/Classroom.vue') },
        { path: 'notice',    name: 'AdminNotice',   component: () => import('@/views/admin/AdminNotice.vue') },






      ]
    },

    /* 教师端 ---------------------------------------------------------- */
    {
      path: '/teacherDashboard',
      name: 'TeacherDashboard',
      component: () => import('@/views/teacher/TeacherDashboard.vue'),
      redirect: '/teacherDashboard/home',
      children: [
        { path: 'home',      name: 'TeacherHome',   component: () => import('@/views/teacher/TeacherHome.vue') },
        { path: 'notice',    name: 'TeacherNotice', component: () => import('@/views/teacher/TeacherNotice.vue') },
        { path: 'schedule',  name: 'TeacherSchedule',      component: () => import('@/views/teacher/TeacherSchedule.vue') },
        {path:'course',name:'TeacherCourse',component:()=>import ('@/views/teacher/TeacherCourse.vue')},
        { path: 'setting',   name: 'TeacherSetting',component: () => import('@/views/teacher/TeacherSysSetting.vue') },
        {path:'activity',name:'TeacherActivity',component:()=>import('@/views/teacher/TeacherEvent.vue')},
        {path:'booking',name:'TeacherThingBooking',component:()=>import('@/views/teacher/TeacherThingBooking.vue')}



      ]
    },

    /* 学生端 ---------------------------------------------------------- */
    {
      path: '/studentDashboard',
      name: 'StudentDashboard',
      component: () => import('@/views/student/StudentDashboard.vue'),
      redirect: '/studentDashboard/home',
      children: [

        { path: 'home',       name: 'StudentHome',     component: () => import('@/views/student/StudentHome.vue') },
        {path:'schedule',     name:'StudentSchedule',component:()=>import('@/views/student/StudentSchedule.vue')},
        { path: 'notice',     name: 'StudentNotice',   component: () => import('@/views/student/StudentNotice.vue') },
        {path:'setting',      name: 'StudentSetting',component:()=>import('@/views/student/StudentSysSetting.vue')},
        {path:'courseBooking', name:'StudentCourseBooking',component:()=>import('@/views/student/StudentCourseBooking.vue') },
        {path:'activityBooking',name:'StudentActivityBooking',component:()=>import('@/views/student/StudentActivityBooking.vue')},
        {path:'booking',name:'StudentThingBooking',component:()=>import('@/views/student/StudentThingBooking.vue')}


      ]
    }
  ]
})

export default router