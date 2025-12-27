<template>
  <section class="home-fullscreen">
    <!-- 左侧大头像 -->
    <div class="avatar-box">
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
      <h1 class="name">你好，<strong>{{ auth.user?.name }}</strong> 同学</h1>

      <div class="info-row">
        <span class="label">学号</span>
        <span class="value">{{ auth.user?.uid }}</span>
      </div>

      <div class="info-row">
        <span class="label">联系电话</span>
        <span class="value">{{ auth.user?.phone }}</span>

      </div>

      <!-- 按钮组 -->
      <!-- 按钮组 -->
      <div class="btn-group">
        <el-button size="large" round @click="nameDlg.open(() => ({ name: auth.user.name }))">
          修改用户名
        </el-button>
        <el-button size="large" round @click="phoneDlg.open(() => ({ phone: auth.user.phone }))">
          修改手机号
        </el-button>
        <el-button size="large" round @click="pwdDlg.open()">
          修改密码
        </el-button>
        <el-button type="danger" size="large" round @click="logout">
          退出登录
        </el-button>
      </div>
    </div>



    <!-- 通用弹窗：用户名 -->
    <el-dialog
        v-model="nameDlg.visible.value"
        title="修改用户名"
        width="320"
        @close="nameDlg.reset"
    >
      <el-form :model="nameDlg.form" label-position="top">
        <el-form-item label="旧用户名">
          <span class="value">{{ auth.user?.name }}</span>
        </el-form-item>
        <el-form-item label="新用户名">
          <el-input
              v-model="nameDlg.form.name"
              maxlength="20"
              show-word-limit
              placeholder="请输入新用户名"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="nameDlg.visible.value = false">取消</el-button>
        <el-button
            type="primary"
            :loading="nameDlg.loading.value"
            @click="nameDlg.submit"
        >
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 通用弹窗：手机号 -->
    <el-dialog
        v-model="phoneDlg.visible.value"
        title="修改手机号"
        width="320"
        @close="phoneDlg.reset"
    >
      <el-form :model="phoneDlg.form" label-position="top">
        <el-form-item label="旧手机号">
          <span class="value">{{ auth.user?.phone }}</span>
        </el-form-item>
        <el-form-item label="新手机号">
          <el-input v-model="phoneDlg.form.phone" maxlength="11" placeholder="请输入新手机号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="phoneDlg.visible.value = false">取消</el-button>
        <el-button type="primary" :loading="phoneDlg.loading.value" @click="phoneDlg.submit">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 通用弹窗：密码 -->
    <el-dialog
        v-model="pwdDlg.visible.value"
        title="修改密码"
        width="360"
        @close="pwdDlg.reset"
    >
      <el-form :model="pwdDlg.form" label-position="top">
        <el-form-item label="原密码">
          <el-input v-model="pwdDlg.form.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdDlg.form.newPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDlg.visible.value = false">取消</el-button>
        <el-button type="primary" :loading="pwdDlg.loading.value" @click="pwdDlg.submit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
/* ------------------ 依赖 ------------------ */
import { ref } from 'vue'
import { useAuthStore } from '@/stores/AuthStore.js'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useEditDialog} from "@/views/composables/useEditDialog.js";

/* ------------------ 常量 ------------------ */
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

/* ------------------ 仓库 & 路由 ------------------ */
const auth = useAuthStore()
const router = useRouter()

/* ========== 头像上传（保持你原来能力） ========== */
const avatarInput = ref(null)
function openAvatarSelect() {
  avatarInput.value.click()
}
async function handleUploadAvatar(e) {
  const file = e.target.files[0]
  if (!file) return
  try {
    const hadImg = auth.user?.image_url && auth.user.image_url !== defaultAvatar
    const api = hadImg
        ? () => auth.changeImage(file, auth.user.image_url) // 修改
        : () => auth.insertImage(file)                     // 新增
    auth.user.image_url = await api()
    ElMessage.success('头像已更新')
  } catch (err) {
    ElMessage.error(err?.response?.data?.message || '上传失败')
  } finally {
    e.target.value = '' // 允许重复选同一张图
  }
}

/* ========== 修改姓名 ========== */
// 组合式函数已声明，无需改动，仅需把打开时的回显值补全：
const nameDlg = useEditDialog(
    ({ name }) => auth.changeUsername({ userId: auth.user.userId, name }),
    { name: '' },
    (_, { name }) => (auth.user.name = name)
)
// 打开时直接塞当前值
/* ========== 修改手机号 ========== */
const phoneDlg = useEditDialog(
    ({ phone }) => auth.changePhone({ userId: auth.user.userId, phone }),
    { phone: '' },
    (_, { phone }) => (auth.user.phone = phone)
)

/* ========== 修改密码 ========== */
const pwdDlg = useEditDialog(
    ({ oldPassword, newPassword }) =>
        auth.changePassword({ uid: auth.user.uid, oldPassword, newPassword }),
    { oldPassword: '', newPassword: '' },
    async () => {
      ElMessage.success('密码已修改，请重新登录')
      await auth.logout()
      await router.replace('/login')
    }
)

/* ========== 退出登录 ========== */
async function logout() {
  try {
    await auth.logout()
    ElMessage.success('已退出登录')
  } catch (e) {
    ElMessage.error(e.message || '退出失败')
  }
}
</script>

<style scoped>
/* 你原来的样式完全保留，只给按钮之间加一点点间距 */
.btn-group {
  margin-top: 16px;
}
.home-fullscreen {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 48px;
  height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}
.avatar-box {
  text-align: center;
}
.info-box {
  width: 400px;
}
.name {
  margin-bottom: 24px;
  font-size: 28px;
  color: #303133;
}
.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-size: 15px;
}
.label {
  width: 80px;
  color: #909399;
}
.value {
  flex: 1;
  color: #606266;
}
</style>