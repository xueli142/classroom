<template>
  <section class="home-fullscreen">
    <!-- 左侧大头像 -->
    <div class="avatar-box">
      <!-- 点击头像直接换图 -->
      <el-avatar
          :size="160"
          :src="auth.user?.image_url || defaultAvatar"
          shape="circle"
          style="cursor: pointer"
          @click="openAvatarSelect"
      />
      <input
          ref="avatarInput"
          type="file"
          accept="image/*"
          style="display: none"
          @change="handleUploadAvatar"
      />
    </div>

    <!-- 右侧信息区 -->
    <div class="info-box">
      <h1 class="name">你好，<strong>{{ auth.user?.name }}</strong> 管理员</h1>

      <div class="info-row">
        <span class="label">学号</span>
        <span class="value">{{ auth.user?.uid }}</span>
      </div>

      <div class="info-row">
        <span class="label">联系电话</span>
        <span class="value">{{ auth.user?.phone }}</span>
        <el-link type="primary" :underline="false" @click="openPhoneDialog">修改</el-link>
      </div>

      <!-- 按钮组 -->
      <div class="btn-group">
        <el-button type="primary" size="large" round>查看公告</el-button>
        <el-button size="large" round>教室查询</el-button>
      </div>

      <div class="btn-group" style="margin-top: 16px">
        <el-button size="large" round @click="openPasswordDialog">修改密码</el-button>
        <!-- 🔥 退出登录 -->
        <el-button type="danger" size="large" round @click="logout">
          退出登录
        </el-button>
      </div>
    </div>

    <!-- 修改手机号弹窗 -->
    <el-dialog v-model="phoneVisible" title="修改手机号" width="320" @close="phoneForm.phone=''">
      <el-form :model="phoneForm" label-position="top">
        <el-form-item label="新手机号">
          <el-input v-model="phoneForm.phone" placeholder="请输入新手机号" maxlength="11" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="phoneVisible=false">取消</el-button>
        <el-button type="primary" :loading="phoneLoading" @click="submitPhone">确定</el-button>
      </template>
    </el-dialog>

    <!-- 修改密码弹窗 -->
    <el-dialog v-model="pwdVisible" title="修改密码" width="360" @close="pwdForm={}">
      <el-form :model="pwdForm" label-position="top">
        <el-form-item label="原密码">
          <el-input v-model="pwdForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdVisible=false">取消</el-button>
        <el-button type="primary" :loading="pwdLoading" @click="submitPassword">确定</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
/* ---------- 依赖 ---------- */
import {reactive, ref} from 'vue'
import {useAuthStore} from '@/stores/AuthStore.js'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'

/* ---------- 常量 ---------- */
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

/* ---------- 仓库 & 路由 ---------- */
const auth   = useAuthStore()
const router = useRouter()

/* ---------- 头像上传 ---------- */
const avatarInput = ref(null)
function openAvatarSelect() {
  avatarInput.value.click()
}
async function handleUploadAvatar(e) {
  const file = e.target.files[0]
  if (!file) return
  try {
    const hasImages = auth.user?.image_url&& auth.user.image_url !== defaultAvatar
    const uploadApi = hasImages
        ? () => auth.changeImage( file, auth.user.image_url ) // 修改
        : () => auth.insertImage(file)

    auth.user.image_url = await uploadApi()
    ElMessage.success('头像已更新')
  } catch (err) {
    ElMessage.error(err?.response?.data?.message || '上传失败')
  } finally {
    e.target.value = '' // 允许重复选同一张图
  }
  // 清空 input，允许重复选同一张图
  e.target.value = ''
}

/* ---------- 修改手机号 ---------- */
const phoneVisible = ref(false)
const phoneLoading = ref(false)
const phoneForm = reactive({ phone: '' })
function openPhoneDialog() {
  phoneForm.phone = auth.user.phone || ''
  phoneVisible.value = true
}
async function submitPhone() {

  phoneLoading.value = true
  try {
    await auth.changePhone({ userId: auth.user.userId, phone: phoneForm.phone })
    auth.user.phone = phoneForm.phone
    ElMessage.success('手机号已修改')
    phoneVisible.value = false
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || '修改失败')
  } finally {
    phoneLoading.value = false
  }
}

/* ---------- 修改密码 ---------- */
const pwdVisible = ref(false)
const pwdLoading = ref(false)
const pwdForm = reactive({ oldPassword: '', newPassword: '' })
function openPasswordDialog() {
  pwdForm.oldPassword = ''
  pwdForm.newPassword = ''
  pwdVisible.value = true
}
async function submitPassword() {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) {
    return ElMessage.warning('请填写完整')
  }
  pwdLoading.value = true
  try {
    await auth.changePassword({
      oldPassword: pwdForm.oldPassword,
      newPassword: pwdForm.newPassword,
      uid: auth.user.uid

    })
    ElMessage.success('密码已修改，请重新登录')
    pwdVisible.value = false
    // 修改完直接退出
    await auth.logout()
    await router.replace('/login')
  } catch (e) {
    ElMessage.error(e?.response?.data?.message || '修改失败')
  } finally {
    pwdLoading.value = false
  }
}

/* ---------- 退出登录 ---------- */
async function logout() {
  try {
    await auth.logout()          // courseStore 里已清 token + 跳路由
    ElMessage.success('已退出登录')
  } catch (e) {
    ElMessage.error(e.message || '退出失败')
  }
}
</script>

<style scoped>
/* 你原来的样式完全保留，只给按钮之间加一点点间距 */
.btn-group {
  margin-top: 12px;
}
</style>