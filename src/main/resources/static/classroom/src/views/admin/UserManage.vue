<template>
  <el-card>
    <!-- 1. 抽屉开关 -->
    <el-button type="primary" @click="drawer = !drawer">
      {{ drawer ? '收起面板' : '展开面板' }}
    </el-button>

    <!-- 2. 抽屉按钮区 -->
    <el-collapse-transition>
      <div v-show="drawer" class="drawer-body">
        <el-space wrap>
          <el-button
              v-for="(item, idx) in pages"
              :key="idx"
              :type="currentView === item.comp ? 'primary' : ''"
              @click="currentView = item.comp"
          >
            {{ item.title }}
          </el-button>
        </el-space>
      </div>
    </el-collapse-transition>

    <!-- 3. 子组件挂载点 -->
    <div class="sub-view" v-if="currentView">
      <component :is="currentView" />
    </div>

    <!-- 4. 空状态 -->
    <el-empty
        v-else
        description="请先选择要进入的管理模块"
        :image-size="100"
    />
  </el-card>
</template>

<script setup>
import { shallowRef, ref } from 'vue'
import StudentMange from '@/views/admin/Manage/StudentMange.vue'
import TeacherMange from '@/views/admin/Manage/TeacherMange.vue'
import AdminMange from '@/views/admin/Manage/AdminMange.vue'

const drawer = ref(false)
const currentView = shallowRef(null)

const pages = [
  { title: '学生管理', comp: StudentMange },
  { title: '教师管理', comp: TeacherMange },
  { title: '课程管理', comp: AdminMange }
]
</script>

<style scoped>
.drawer-body {
  margin-top: 12px;
  padding: 12px;
  background: #fafafa;
  border-radius: 4px;
}
.sub-view {
  margin-top: 20px;
}
</style>