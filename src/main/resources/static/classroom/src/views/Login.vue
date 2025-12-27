<!-- Login.vue -->
<template>
  <div class="login-page">
    <el-card class="login-card" shadow="never">
      <template #header>
        <div class="card-header">
          <el-icon :size="22" color="var(--primary)"><Promotion /></el-icon>
          <span class="card-header__title">系统登录</span>
        </div>
      </template>

      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          @keyup.enter="onSubmit"
      >
        <!-- 账号 -->
        <el-form-item prop="uid">
          <el-input
              v-model="form.uid"
              placeholder="请输入用户名"
              autocomplete="username"
              :prefix-icon="User"
              clearable
          />
        </el-form-item>

        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              autocomplete="current-password"
              :prefix-icon="Lock"
              show-password
              clearable
          />
        </el-form-item>

        <!-- 登录按钮 -->
        <el-form-item>
          <el-button
              type="primary"
              :loading="loading"
              class="submit-btn"
              @click="onSubmit"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 错误提示 -->
      <el-alert
          v-if="error"
          :title="error"
          type="error"
          :closable="false"
          center
          class="error-alert"
      />
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/AuthStore'
import { ElNotification } from 'element-plus'
import { User, Lock, Promotion } from '@element-plus/icons-vue'

/* ---------------- 路由 & 仓库 ---------------- */
const router = useRouter()
const store = useAuthStore()

const ROLE_HOME = {
  ADMIN: '/adminDashboard',
  TEACHER: '/teacherDashboard',
  STUDENT: '/studentDashboard'
}

/* ---------------- 表单数据 ---------------- */
const formRef = ref()
const form = reactive({ uid: '', password: '' })
const rules = {
  uid: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}
const loading = ref(false)
const error = ref('')

/* ---------------- 提交 ---------------- */
const onSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  error.value = ''

  try {
    const { user } = await store.login({
      uid: form.uid.trim(),
      password: form.password
    })

    ElNotification({
      title: '成功',
      message: `欢迎回来，${user.name || user.uid}`,
      type: 'success',
      duration: 1500
    })

    await router.replace(ROLE_HOME[user.role])
  } catch (e) {
    error.value = e?.response?.data?.message || e.message || '登录失败，请检查账号密码'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* ---------- CSS 变量（一键换肤） ---------- */
:root {
  --primary: #409eff;
  --error: #f56c6c;
  --bg-start: #f5f7fa;
  --bg-end: #c3cfe2;
  --radius: 12px;
  --transition: all 0.3s ease;
}

/* ---------- 布局 ---------- */
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--bg-start) 0%, var(--bg-end) 100%);
  padding: 24px;
}

/* ---------- 卡片 ---------- */
.login-card {
  width: 100%;
  max-width: 400px;
  border-radius: var(--radius);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* ---------- 头部 ---------- */
.card-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}
.card-header__title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

/* ---------- 输入框 ---------- */
/* 深度选择器，把 El-Input 原生样式覆盖掉 */
:deep(.el-input__wrapper) {
  height: 48px;
  padding: 0 42px;
  font-size: 15px;
  border-radius: 6px;
  box-shadow: none;
  border: 1px solid #dcdfe6;
  transition: var(--transition);
}
:deep(.el-input__wrapper:hover) {
  border-color: var(--primary);
}
:deep(.el-input__wrapper.is-focus) {
  border-color: var(--primary);
}
:deep(.el-input__prefix-inner > i) {
  color: #909399;
}

/* ---------- 按钮 ---------- */
.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 6px;
  border: none;

}
.submit-btn:hover {
  background: #66b1ff;
}
.submit-btn:disabled {
  background: #a0cfff;
  cursor: not-allowed;
}

/* ---------- 错误提示 ---------- */
.error-alert {
  margin-top: 12px;
}
</style>