  <template>
    <el-dialog
        :title="(model.courseSlotId ? '编辑' : '新增') + '课程'"
        v-model="visible"
        width="660px"
    @close="close">

    <!-- 整个内容用 el-row 分成左右两栏 -->
    <el-row :gutter="20">


      <!-- ====== 右侧：原来的表单 ====== -->
      <el-col :span="14">
        <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
          <el-form-item label="当前周数">
            <el-input :value="form.week" disabled />
          </el-form-item>

          <el-form-item label="当前教室">
            <el-input :value="form.classroomName" disabled />
          </el-form-item>

          <el-form-item label="是否启用">
            <el-radio-group v-model="form.status">
              <el-radio :label="true">启用</el-radio>
              <el-radio :label="false">禁用</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="地点">
            <el-input v-model="form.location" placeholder="请输入"/>
          </el-form-item>

          <el-form-item label="最大人数">
            <el-input v-model="form.number" placeholder="请输入"/>
          </el-form-item>
          <el-form-item label="以确定人数">
            <el-input v-model="form.bookingNumber" placeholder="请输入"/>
          </el-form-item>

          <el-form-item label="课程名称">
            <el-input v-model="form.name" placeholder="请输入"/>
            <el-button type="primary" @click="courseSelVisible=true" >选择课程</el-button>
            <CourseSelect
            v-model:visible=" courseSelVisible"
            @selected="onCourseSelected"

            />


          </el-form-item>
          <el-form-item label="授课教师" prop="teacher_name">
            <el-input v-model="form.teacher_name" placeholder="例如：张老师" clearable />

          </el-form-item>


          <el-form-item label="星期">
            <el-select v-model="form.dayOfWeek">
              <el-option v-for="(d,i) in weekDays" :key="i" :label="d" :value="i"/>
            </el-select>
          </el-form-item>

          <el-form-item label="节次">
            <el-input-number v-model="form.slot" :min="1" :max="13"/>
          </el-form-item>

          <el-form-item label="备注">
            <el-input v-model="form.besides" type="textarea" :rows="2"/>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <!-- 底部按钮保持不动 -->
    <template #footer>
      <el-button @click="close">取消</el-button>
      <el-button type="danger" v-if="model.courseSlotId" @click="del">删除</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </template>
    </el-dialog>
  </template>
  <script setup>
  import { ref, watch, computed } from 'vue'
  import { ElMessageBox } from 'element-plus'
  import CourseSelect from "@/views/Select/CourseSelect.vue";


  const courseSelVisible = ref(false)


  const props = defineProps({
    model: Object,
    currentWeek:   Number,
    currentClassroom: Object
  })
  const emit = defineEmits(['update:modelValue', 'save', 'del'])

  const visible = computed({
    get: () => true,
    set: v => emit('update:modelValue', v)
  })
  const weekDays = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']

  const form = ref({ ...props.model })


  // CourseSlot.vue
  function onCourseSelected(list) {
    const raw = Array.isArray(list) ? list[0] : list
    const full = {
      ...raw,
      // 用表单里刚填的数值
      number: form.value.number,
      status: form.value.status,
      slot: form.value.slot,
      dayOfWeek: form.value.dayOfWeek,
      weekMonday: form.value.weekMonday,
      classroomId: props.currentClassroom.classroomId,
      teacherId: form.value.userId,
      // 不把 bookingNumber 写死，后端或父组件自己算
    }
    emit('save', full)
  }

  watch(
      [() => props.model, () => props.currentWeek, () => props.currentClassroom],
      ([m, week, cr]) => {
        form.value = {
          ...m,
          week,
          classroomId:   cr?.classroomId   ?? '',
          classroomName: cr?.classroomName ?? ''
        }
      },
      { immediate: true }
  )
  function save() {
    emit('save', form.value)
    close()//这里才是关闭弹窗的地方
  }
  function del() {
    ElMessageBox.confirm('确定删除该课程？', '提示', { type: 'warning' })
        .then(() => emit('del', form.value))
  }
  function close() {
    visible.value = false
  }
  </script>