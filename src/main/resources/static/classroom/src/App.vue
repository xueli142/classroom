  <script setup>
  import { onMounted } from 'vue';
  import { useAuthStore } from '@/stores/AuthStore.js';

  onMounted(() => {
    // 恢复登录态（可选）
    const store = useAuthStore();
    store.loadLocalAuth();  // 关键：启动时恢复登录态

  });
  </script>

  <template>
    <router-view />
  </template>

  <style scoped></style>
