import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

/* 只要在这里维护角色即可 */
const roles = ['student', 'teacher', 'admin']

/* 自动生成路由表 */
const routes = []
roles.forEach(role => {
  routes.push(
      {
        path: `/${role}/login`,
        component: () => import(`@/views/${role}/Login.vue`),
        meta: { role, guest: true }
      },
      {
        path: `/${role}/home`,
        component: () => import(`@/views/${role}/Home.vue`),
        meta: { role, requiresAuth: true }
      },
      {
        path:`/${role}/register`,
        component: () => import(`@/views/${role}/Register.vue`),
        meta: { role, guest: true }
      },
      {
          path:`/${role}/classroom`,
          component:()=> import(`@/views/${role}/Classroom.vue`),
          meta:{role,requiresAuth:true}
      },
      {
        path:`/${role}/dashboard`,
        component:()=> import(`@/views/${role}/Dashboard.vue`),
        meta:{role,requiresAuth:true}

      },

  )
})

/* 默认重定向 */
routes.push({ path: '/', redirect: '/student/login' })

const router = createRouter({
  history: createWebHistory(),
  routes
})

/* 全局守卫保持原逻辑 */
router.beforeEach((to, from, next) => {
  const store = useAuthStore()
  const needAuth = to.meta.requiresAuth
  const guestOnly = to.meta.guest
  const role = to.meta.role

  if (needAuth && !store.isLogin) return next(`/${role}/login`)
  if (guestOnly && store.isLogin && store.role === role) return next(`/${role}/home`)
  next()
})

export default router