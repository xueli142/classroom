import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  // 新增：接口代理配置
  server: {
    proxy: {
      // 匹配所有以 "/auth" 开头的请求（根据你的接口路径调整）
      '/auth': {
        target: 'http://localhost:8080', // 后端服务器地址（你的后端端口 8080）
        changeOrigin: true, // 允许跨域（模拟请求的 Origin 为后端地址）
        // rewrite: (path) => path.replace(/^\/auth/, ''), // 可选：若后端接口没有 /auth 前缀则开启
      },
      '/api': {
        target: 'http://localhost:8080', // 后端服务器地址（你的后端端口 8080）
        changeOrigin: true, // 允许跨域（模拟请求的 Origin 为后端地址）
      }
    }
  }
})
