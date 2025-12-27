<template>
  <el-form-item label="标题" prop="title">
    <el-input v-model="inner.title" />
  </el-form-item>
  <el-form-item label="正文" prop="advice">
    <el-input
        v-model="inner.advice"
        type="textarea"
        :rows="3"
        placeholder="请输入正文"
    />
  </el-form-item>
</template>

<script setup>
/* 通过 v-model 绑定父级对象 */
const props = defineProps({
  modelValue: { type: Object, required: true }
})
const emit = defineEmits(['update:modelValue'])

/* 计算属性实现 v-model 语法糖 */
import { computed } from 'vue'
const inner = computed({
  get: () => props.modelValue,
  set: val => emit('update:modelValue', val)
})
</script>