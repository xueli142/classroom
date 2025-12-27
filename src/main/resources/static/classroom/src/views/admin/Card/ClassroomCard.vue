<template>
  <div
      class="card"
      :class="{ active: data.isActive }"
      @click="$emit('click-card', data)"
  >
  <img :src="data.imageUrl" alt="cover" class="card-image" />
  <div class="card-body">
    <h3 class="card-title">{{ data.name }}</h3>
    <span class="card-type">{{ data.type }}</span>
  </div>
  </div>
</template>

<script setup>
defineProps({ data: { type: Object, required: true } })
defineEmits(['click-card'])             // ← 顺便声明
</script>
<style scoped>
.card {
  width: 240px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: 0.2s;
  background: #fff;
}
.card.active {
  border: 2px solid #409eff;
}
.card-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
  display: block;
}
.card-body {
  padding: 12px;
}
.card-title {
  margin: 0 0 4px;
  font-size: 16px;
  color: #303133;
}
.card-type {
  font-size: 14px;
  color: #909399;
}
</style>