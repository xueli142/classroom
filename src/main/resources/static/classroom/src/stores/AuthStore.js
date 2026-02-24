/**
 * 认证状态管理 Store
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import router from '@/router/index.js'
import { AuthApi } from '@/api/AuthApi.js'
import { StorageKeys } from '@/constants/StorageKeys.js'
import { UserRoleHomePaths } from '@/enums/UserRole.js'

/* ========== 持久化工具 ========== */
const persist = {
  save(user, token) {
    if (user) localStorage.setItem(StorageKeys.AUTH_USER, JSON.stringify(user))
    if (token) localStorage.setItem(StorageKeys.AUTH_TOKEN, token)
  },
  load() {
    const userStr = localStorage.getItem(StorageKeys.AUTH_USER)
    const token = localStorage.getItem(StorageKeys.AUTH_TOKEN)
    return {
      user: userStr ? JSON.parse(userStr) : null,
      token: token || ''
    }
  },
  clear() {
    localStorage.removeItem(StorageKeys.AUTH_USER)
    localStorage.removeItem(StorageKeys.AUTH_TOKEN)
  }
}

export const useAuthStore = defineStore('auth', () => {
  // ========== 状态 ==========
  const user = ref(null)
  const token = ref('')
  const loading = ref(false)
  const error = ref(null)

  // ========== 计算属性 ==========
  const isLogin = computed(() => !!token.value)
  const userRole = computed(() => user.value?.role || '')
  const userName = computed(() => user.value?.name || user.value?.uid || '')
  const userHomePath = computed(() => {
    return UserRoleHomePaths[userRole.value] || '/login'
  })

  // ========== 操作方法 ==========

  /**
   * 恢复登录态
   */
  function loadLocalAuth() {
    const { user: localUser, token: localToken } = persist.load()
    if (localToken && localUser) {
      user.value = localUser
      token.value = localToken
    }
  }

  /**
   * 用户登录
   */
  async function login(payload) {
    loading.value = true
    error.value = ''

    try {
      const result = await AuthApi.loginUser(payload)
      user.value = result.user
      token.value = result.token
      persist.save(result.user, result.token)
      return result
    } catch (e) {
      error.value = e.message || '登录失败'
      throw e
    } finally {
      loading.value = false
    }
  }

  /**
   * 用户登出
   */
  async function logout() {
    if (!token.value) return

    loading.value = true
    error.value = ''

    try {
      await AuthApi.logout(token.value)
    } catch (e) {
      // 即使接口失败也强制清本地
    } finally {
      user.value = null
      token.value = ''
      persist.clear()
      loading.value = false
      await router.replace('/login')
    }
  }

  /**
   * 修改密码
   */
  async function changePassword(payload) {
    loading.value = true
    error.value = ''

    try {
      await AuthApi.changePassword(payload)
      return true
    } catch (e) {
      error.value = e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  /**
   * 修改用户名
   */
  async function changeUsername(payload) {
    loading.value = true
    error.value = ''

    try {
      await AuthApi.changeUsername(payload)
      if (payload.name && user.value) {
        user.value.name = payload.name
        persist.save(user.value, token.value)
      }
      return true
    } catch (e) {
      error.value = e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  /**
   * 修改手机号
   */
  async function changePhone(payload) {
    loading.value = true
    error.value = ''

    try {
      await AuthApi.changePhone(payload)
      if (payload.phone && user.value) {
        user.value.phone = payload.phone
        persist.save(user.value, token.value)
      }
      return true
    } catch (e) {
      error.value = e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  /**
   * 上传头像
   */
  async function uploadAvatar(file) {
    if (!user.value?.userId) {
      error.value = '用户信息不存在'
      throw new Error('用户信息不存在')
    }

    loading.value = true
    error.value = ''

    try {
      await AuthApi.insertImage(file, user.value.userId)
      return true
    } catch (e) {
      error.value = e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  /**
   * 更换头像
   */
  async function changeAvatar(file, oldName) {
    if (!user.value?.userId) {
      error.value = '用户信息不存在'
      throw new Error('用户信息不存在')
    }

    loading.value = true
    error.value = ''

    try {
      await AuthApi.changeImage(file, oldName, user.value.userId)
      return true
    } catch (e) {
      error.value = e.message
      throw e
    } finally {
      loading.value = false
    }
  }

  return {
    // 状态
    user,
    token,
    loading,
    error,
    // 计算属性
    isLogin,
    userRole,
    userName,
    userHomePath,
    // 操作方法
    loadLocalAuth,
    login,
    logout,
    changePassword,
    changeUsername,
    changePhone,
    uploadAvatar,
    changeAvatar
  }
})
