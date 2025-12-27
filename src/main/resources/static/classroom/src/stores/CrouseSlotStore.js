// stores/courseSlotStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { CourseSlotApi } from '@/api/CourseSlotApi.js';

export const useCourseSlotStore = defineStore('courseSlot', () => {
    /* ===== 课程时段 ===== */
    const slotList    = ref([]);
    const slotTotal   = ref(0);
    const slotLoading = ref(false);
    const slotError   = ref(null);

    /* ----------------------------------------------------------
     * 通用分页（管理后台）
     * ---------------------------------------------------------- */
    async function slotPage(payload = { page: 1, size: 10 }) {
        slotLoading.value = true;
        slotError.value   = null;
        try {
            const res       = await CourseSlotApi.page(payload);
            const { records, total } = res.data.data;
            slotList.value  = records;
            slotTotal.value = total;
        } catch (e) {
            slotError.value = e.message || '获取时段列表失败';
            slotList.value  = [];
            slotTotal.value = 0;
            throw e;
        } finally {
            slotLoading.value = false;
        }
    }

    /* ----------------------------------------------------------
     * 老师：看我本周时段
     * ---------------------------------------------------------- */
    async function teacherSlotPage(dto) {
        slotLoading.value = true;
        slotError.value   = null;
        try {
            const res = await CourseSlotApi.teacherSlotPage(dto);
            return res.data.data;          // 返回数组
        } catch (e) {
            slotError.value = e.message || '获取老师时段失败';
            throw e;
        } finally {
            slotLoading.value = false;
        }
    }

    async function studentSlotPage(dto){
        slotLoading.value = true;
        slotError.value   = null;
        try {
            const res = await CourseSlotApi.studentSlotPage(dto);
            return res.data.data;          // 返回数组
        } catch (e) {
            slotError.value = e.message || '获取学生时段失败';
            throw e;
        } finally {
            slotLoading.value = false;
        }
    }
    /* ----------------------------------------------------------
     * 教室：占用明细
     * ---------------------------------------------------------- */
    async function classroomOccupyPage(dto) {
        slotLoading.value = true;
        slotError.value   = null;
        try {
            const res = await CourseSlotApi.classroomOccupyPage(dto);
            return res.data.data;
        } catch (e) {
            slotError.value = e.message || '获取教室占用明细失败';
            throw e;
        } finally {
            slotLoading.value = false;
        }
    }

    /* ----------------------------------------------------------
     * 学生/老师：个人课表
     * ---------------------------------------------------------- */
    async function userSlotPage(dto) {
        slotLoading.value = true;
        slotError.value   = null;
        try {
            const res = await CourseSlotApi.userSlotPage(dto);
            return res.data.data;
        } catch (e) {
            slotError.value = e.message || '获取个人课表失败';
            throw e;
        } finally {
            slotLoading.value = false;
        }
    }

    /* ----------------------------------------------------------
     * 列表查询（无分页）
     * ---------------------------------------------------------- */
    async function selectList(dto) {
        slotLoading.value = true;
        slotError.value   = null;
        try {
            const res = await CourseSlotApi.selectList(dto);
            return res.data.data;
        } catch (e) {
            slotError.value = e.message || '获取时段列表失败';
            throw e;
        } finally {
            slotLoading.value = false;
        }
    }

    /* ----------------------------------------------------------
     * 批量新增
     * ---------------------------------------------------------- */
    async function insertBatch(dtoList) {
        slotLoading.value = true;
        slotError.value   = null;
        try {
            await CourseSlotApi.insertBatch(dtoList);
        } catch (e) {
            slotError.value = e.message || '批量添加时段失败';
            throw e;
        } finally {
            slotLoading.value = false;
        }
    }

    /* ----------------------------------------------------------
     * 更新
     * ---------------------------------------------------------- */
    async function updateOne(dto) {
        slotLoading.value = true;
        slotError.value   = null;
        try {
            await CourseSlotApi.updateOne(dto);
        } catch (e) {
            slotError.value = e.message || '更新时段失败';
            throw e;
        } finally {
            slotLoading.value = false;
        }
    }

    async function updateBatch(dtoList) {
        slotLoading.value = true;
        slotError.value   = null;
        try {
            await CourseSlotApi.updateBatch(dtoList);
        } catch (e) {
            slotError.value = e.message || '批量更新时段失败';
            throw e;
        } finally {
            slotLoading.value = false;
        }
    }

    /* ----------------------------------------------------------
     * 删除
     * ---------------------------------------------------------- */
    async function deleteById(courseSlotId) {
        slotLoading.value = true;
        slotError.value   = null;
        try {
            await CourseSlotApi.deleteOne(courseSlotId);
        } catch (e) {
            slotError.value = e.message || '删除时段失败';
            throw e;
        } finally {
            slotLoading.value = false;
        }
    }

    async function deleteByIds(courseSlotIds) {
        slotLoading.value = true;
        slotError.value   = null;
        try {
            await CourseSlotApi.deleteBatch(courseSlotIds);
        } catch (e) {
            slotError.value = e.message || '批量删除时段失败';
            throw e;
        } finally {
            slotLoading.value = false;
        }
    }

    /* ===== 返回 courseStore ===== */
    return {
        /* 状态 */
        slotList,
        slotTotal,
        slotLoading,
        slotError,
        /* 方法 */
        slotPage,
        studentSlotPage,
        teacherSlotPage,
        classroomOccupyPage,
        userSlotPage,
        selectList,
        insertBatch,
        updateOne,
        updateBatch,
        deleteById,
        deleteByIds,
    };
});