// src/main.js
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import router from './router'
import { setupGuard } from '@/router/guard.js'
import App from './App.vue'

const app = createApp(App)

/* 1️⃣ 必须先装 Pinia，再装路由，再注册守卫；
      否则守卫里一旦调用 useAuthStore() 会报错 */
app.use(createPinia())

/* 2️⃣ 再装路由，此时 Pinia 已就位 */
app.use(router)

/* 3️⃣ 注册全局路由守卫（内部可用 useAuthStore） */
setupGuard(router)

/* 4️⃣ 注册 ElementPlus（也要在 mount 之前） */
app.use(ElementPlus)

/* 5️⃣ 最后挂载 */
app.mount('#app')