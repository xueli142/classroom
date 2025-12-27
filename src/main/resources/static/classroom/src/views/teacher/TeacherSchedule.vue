    <template>
      <section class="detail-page">
        <!-- 顶部栏 -->
        <div class="header">
          <el-button type="primary" :icon="ArrowLeft" @click="router.back()">返回</el-button>

          <el-button-group style="margin-left:16px">
            <el-button :icon="ArrowLeft" @click="changeWeek(-1)">上一周</el-button>
            <el-button :icon="ArrowRight" @click="changeWeek(1)">下一周</el-button>
          </el-button-group>
          <span style="margin-left:8px;font-size:14px;color:#666">第 {{ displayWeek }} 周</span>
          <span style="margin-left:8px;font-size:14px;color:#666">{{authStore.user.name}}的课表</span>
          <el-button type="success" :loading="saveLoading" style="margin-left:auto" @click="saveWholeSchedule">
            保存课表
          </el-button>
        </div>


        <!-- 课表 -->
        <div class="table-box" v-loading="slotLoading">
          <el-table
              :data="tableData"
              border
              style="width: 100%; height: 100%"
              :header-cell-style="{ padding: '4px 0', background: '#fafafa' }"
              :cell-style="{ padding: '0' }"
          >
            <el-table-column label="节次" width="70" align="center" fixed>
              <template #default="{ row }">{{ row.slot }}</template>
            </el-table-column>

            <el-table-column
                v-for="(day, idx) in weekDays" :key="idx"
                :label="day" min-width="140" align="center"
            >
              <template #default="{ row }">
                <div
                    class="card-pool"
                    @drop="onDrop($event, row.slot - 1, idx)"
                    @dragover.prevent
                    :class="{ empty: !cardList[row.slot - 1][idx].length }"
                >
                  <!-- 卡片 -->
                  <div
                      v-for="c in cardList[row.slot - 1][idx]"
                      :key="c.tempId"
                      class="course-card"
                      :style="{ background: c.color }"
                      draggable="true"
                      @dragstart="onDrag($event, c, row.slot - 1, idx)"
                      @click="openForm(row.slot, idx, c)"
                  >
                    <div class="card-name">{{ c.courseName }}</div>
                    <div class="card-teacher">{{ c.teacherName }}</div>
                  </div>

                  <!-- 空池快速添加 -->
                  <el-button
                      v-if="!cardList[row.slot - 1][idx].length"
                      type="primary" link
                      @click="openForm(row.slot, idx)"
                  >
                    + 添加
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </section>

      <!-- 弹窗 -->
      <TeacherCourseSlot
          v-model="courseSlotDialogVisible"
          :model="formModel"
          :current-teacher="authStore.user"
          @save="handleSave"
          @del="handleDel"/>


    </template>

    <script setup>
    /* --------------------  引入  -------------------- */

    import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
    import { onMounted, ref, watch } from 'vue'
    import { useRouter } from 'vue-router'
    import { ElMessage } from 'element-plus'
    import { useCourseSlotStore } from '@/stores/CrouseSlotStore.js'
    import { useTermStore } from '@/stores/TermStore.js'
    import { useAuthStore } from '@/stores/AuthStore.js'
    import TeacherCourseSlot from "@/views/teacher/TeacherSon/TeacherCourseSlot.vue";


    /* --------------------  路由  -------------------- */
    const router = useRouter()

    /* --------------------  响应式数据  -------------------- */
    const weekDays = ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期日']
    const currentWeek = ref(null)
    const displayWeek = ref(null)
    const slotLoading = ref(false)
    const saveLoading = ref(false)
    const courseSlotDialogVisible = ref(false)
    const formModel = ref({})
    const ScheduleList = ref([])


    const cardList = ref(
        Array.from({ length: 13 }, () => Array.from({ length: 7 }, () => []))
    )
    const tableData = Array.from({ length: 13 }, (_, i) => ({ slot: i + 1 }))

    /* --------------------  Store  -------------------- */
    const slotStore = useCourseSlotStore()
    const termStore = useTermStore()
    const authStore = useAuthStore()

    watch(
        [() => authStore.user, currentWeek],
        async ([r, week]) => {
          if (!r?.userId || !week) return
          displayWeek.value = week
          await loadOccupy()          // ← 第一次调用
        },
        { immediate: true }
    )

    /* --------------------  生命周期  -------------------- */
    onMounted(async () => {
      await getTerm()

    })


    /* --------------------  业务函数  -------------------- */
    async function getTerm() {
      try {
        await termStore.termPage({ isActive: true })
        const firstTerm = termStore.list[0]
        if (firstTerm) {
          currentWeek.value = firstTerm.weekNow
          displayWeek.value = firstTerm.weekNow
        }
      } catch (e) {
        ElMessage.error(e.message || '获取学期信息失败')
      }
    }


    async function loadOccupy() {
      if (!authStore.user.userId || displayWeek.value == null) return
      slotLoading.value = true
      try {
        const raw = await slotStore.classroomOccupyPage({

          teacherId: authStore.user.userId,
          weekMonday: displayWeek.value,

        })
        fillCardList(raw)
      } catch {
        ElMessage.error('获取课表失败')
      } finally {
        slotLoading.value = false
      }
    }

    function fillCardList(list = []) {
      cardList.value.forEach(row => row.forEach(cell => (cell.length = 0)))
      ScheduleList.value = []
      list.forEach(item => {
        const { slot, dayOfWeek, ...rest } = item
        if (slot < 1 || slot > 13 || dayOfWeek < 0 || dayOfWeek > 6) return
        const card = { ...rest, slot, dayOfWeek, tempId: rest.courseSlotId || Date.now() + Math.random() }
        cardList.value[slot - 1][dayOfWeek].push(card)
        ScheduleList.value.push(card)
      })
    }

    function changeWeek(delta) {
      displayWeek.value += delta
      loadOccupy()
    }

    function openForm(slot, dayOfWeek, course = null) {
      formModel.value = course
          ? { ...course, slot, dayOfWeek }
          : { slot, dayOfWeek, weekMonday: displayWeek.value }
      courseSlotDialogVisible.value = true
    }

    async function saveWholeSchedule() {
      if (!ScheduleList.value.length) return ElMessage.warning('课表为空')
      const dtoList = ScheduleList.value.map(c => ({
        status:        c.status ?? true,               // 布尔
        slot:          c.slot ?? 1,
        userId:        c.userId ?? authStore.user.userId, // 兜底当前用户
        courseId:      c.courseId ?? '',               // 字符串
        dayOfWeek:     c.dayOfWeek ?? 0,
        weekMonday:    displayWeek.value,
        classroomId:   c.classroomId ?? '',            // 字符串
        teacherId:     c.teacherId ?? authStore.user.userId,
        courseName:    c.courseName ?? '未命名课程',
        location:      c.location ?? null5,
        teacherName:   c.teacherName ?? '未命名教师',
        number:        c.number ?? 0,                  // 数字
        bookingNumber: c.bookingNumber ?? 0,
        imageUrl:      c.imageUrl ?? null,
        besides:       c.besides ?? '',
        courseSlotId:  c.courseSlotId ?? null,         // 主键：有就更新，无就新增
      }))
      saveLoading.value = true
      try {
        await slotStore.insertBatch(dtoList)
        ElMessage.success('课表已保存')
      } catch {
        ElMessage.error('保存失败')
      } finally {
        saveLoading.value = false
      }
    }

    function handleSave(data) {
      const { slot, dayOfWeek } = data
      const arr = cardList.value[slot - 1][dayOfWeek]
      if (!data.tempId || data.tempId.length > 20) {
        data.tempId = data.courseSlotId || Date.now() + ''
      }
      const idx = arr.findIndex(c => c.tempId === data.tempId)
      if (idx > -1) {
        Object.assign(arr[idx], data)
      } else {
        arr.push(data)
        ScheduleList.value.push(data)
      }
      courseSlotDialogVisible.value = false
      ElMessage.success('保存成功')
    }

    async function handleDel(data) {
      const { slot, dayOfWeek } = data
      cardList.value[slot - 1][dayOfWeek] = cardList.value[slot - 1][dayOfWeek].filter(c => c.tempId !== data.tempId)
      ScheduleList.value = ScheduleList.value.filter(c => c.tempId !== data.tempId)
      courseSlotDialogVisible.value = false
      await slotStore.deleteById(data.courseSlotId)
      ElMessage.success('删除成功')
    }

    /* --------------- 原生拖拽 --------------- */
    let dragCache = null

    function onDrag(e, card, fromSlot, fromDay) {
      dragCache = { card, fromSlot, fromDay }
    }

    function onDrop(e, toSlot, toDay) {
      if (!dragCache) return
      const { card, fromSlot, fromDay } = dragCache
      const newCard = {
        ...card,
        tempId: Date.now() + Math.random(),
        slot: toSlot + 1,
        dayOfWeek: toDay,
        weekMonday: displayWeek.value,
        courseSlotId: undefined
      }
      cardList.value[toSlot][toDay] = [...cardList.value[toSlot][toDay], newCard]
      ScheduleList.value.push(newCard)
      dragCache = null}
    </script>

    <style scoped>
    /* ======  仅样式：更大气、通透、质感  ====== */

    /* --- 整体页 --- */
    .detail-page {
      height: 150vh;
      display: flex;
      flex-direction: column;
      background: linear-gradient(135deg, #f5f7fa 0%, #e4e7f1 100%);
      gap: 16px;
    }

    /* --- 顶部栏 --- */
    .header {
      flex: 0 0 auto;
      display: flex;
      align-items: center;
      padding: 12px 20px;
      background: #ffffff;
      border-radius: 12px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
    }
    .header .title {
      margin-left: 12px;
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }

    /* --- 信息卡片 --- */
    .info-box {
      flex: 0 0 auto;
      padding: 4px;
      background: #ffffff;
      border-radius: 2px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
    }

    /* --- 表格区域 --- */
    .table-box {
      flex: 1 1 auto;
      padding: 4px;
      background: #ffffff;
      border-radius: 5px;
      box-shadow: 0 2px 2px rgba(0, 0, 0, 0.06);
      overflow: hidden;
    }

    ::v-deep(.el-table) {
      --el-table-header-bg-color: #f0f2f5;
      --el-table-row-hover-bg-color: #f5f7fa;
      --el-table-border-radius: 12px;
      --el-table-padding: 4px 0;
      --el-table-font-size: 15px;
      --el-table-header-font-size: 16px;
    }

    /* --- 卡片池 --- */
    .card-pool {
      min-height: 80px;
      padding: 4px;
      display: flex;
      flex-direction: column;
      gap: 8px;
      background: #ffffff;
      border: 1px solid #ebeef5;
      border-radius: 2px;
      transition: border-color 0.3s;
    }
    .card-pool:hover {
      border-color: #409eff;
    }
    .card-pool.empty {
      border-style: dashed;
      opacity: 0.6;
    }

    /* --- 课程卡片：固定 120×64 --- */
    .course-card {
      background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
      color: #fff;
      width: 120px;
      height: 64px;
      border-radius: 6px;
      padding: 6px 8px;
      cursor: move;
      user-select: none;
      box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
      transition: transform 0.2s, box-shadow 0.2s;
      font-size: 15px;
      line-height: 1.4;
      display: flex;
      flex-direction: column;
      justify-content: center;
    }
    .course-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(64, 158, 255, 0.4);
    }
    .card-name {
      font-weight: 600;
      letter-spacing: 0.5px;
    }
    .card-teacher {
      font-size: 13px;
      opacity: 0.9;
      margin-top: 2px;
    }
    </style>