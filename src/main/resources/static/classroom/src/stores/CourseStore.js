// stores/courseStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { CourseApi } from '@/api/CourseApi.js';

export const useCourseStore = defineStore('course', () => {
    /* ===== 课程 ===== */

    const courseList    = ref([]);
    const courseTotal   = ref(0);
    const courseLoading = ref(false);
    const courseError   = ref(null);



    /* ===== 课程管理 ===== */

    // 分页查询课程
    async function courseIPage(payload = { page: 1, size: 10 }) {
        courseLoading.value = true;
        courseError.value   = null;
        try {
            const res = await CourseApi.courseIPage(payload);
            const { records, total: count } = res.data.data;
            courseList.value  = records;
            courseTotal.value = count;
        } catch (e) {
            courseError.value = e.message || '获取课程列表失败';
            courseList.value  = [];
            courseTotal.value = 0;
            throw e;
        } finally {
            courseLoading.value = false;
        }
    }

    // 新增课程（含图片）
    async function insertCourse(dto, file) {
        courseLoading.value = true;
        courseError.value   = null;
        try {
            await CourseApi.insertCourse(dto, file);
        } catch (err) {
            courseError.value = err.message || '新增课程失败';
            throw err;
        } finally {
            courseLoading.value = false;
        }
    }

    // 更新课程（含图片）
    async function updateCourse(dto, file, oldName) {
        courseLoading.value = true;
        courseError.value   = null;
        try {
            await CourseApi.updateCourse(dto, file, oldName);
        } catch (err) {
            courseError.value = err.message || '修改课程失败';
            throw err;
        } finally {
            courseLoading.value = false;
        }
    }

    // 删除单个课程
    async function deleteByCourseId(courseId) {
        courseLoading.value = true;
        courseError.value   = null;
        try {
            await CourseApi.deleteByCourseId(courseId);
        } catch (err) {
            courseError.value = err.message || '删除课程失败';
            throw err;
        } finally {
            courseLoading.value = false;
        }
    }

    // 批量删除课程
    async function deleteByCourseIds(courseIds) {
        courseLoading.value = true;
        courseError.value   = null;
        try {
            await CourseApi.deleteByCourseIds(courseIds);
        } catch (err) {
            courseError.value = err.message || '批量删除课程失败';
            throw err;
        } finally {
            courseLoading.value = false;
        }
    }



    return {



        /* 状态 */
        courseList,
        courseTotal,
        courseLoading,
        courseError,


        /* 课程 */
        courseIPage,
        insertCourse,
        updateCourse,
        deleteByCourseId,
        deleteByCourseIds,


    };
});