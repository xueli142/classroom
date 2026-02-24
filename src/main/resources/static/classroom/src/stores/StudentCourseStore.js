// stores/studentCourseStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { StudentCourseApi } from '@/api/StudentCourseApi.js';
import {BookingApi} from "@/api/BookingApi.js";

export const useStudentCourseStore = defineStore('studentCourse', () => {
    /* ===== 学生选课 ===== */

    const studentCourseList    = ref([]);
    const studentCourseTotal   = ref(0);
    const studentCourseLoading = ref(false);
    const studentCourseError   = ref(null);



    /* ===== 学生选课管理 ===== */

    // 分页查询学生选课
    async function studentCourseIPage(payload = { page: 1, size: 10 }) {
        studentCourseLoading.value = true;
        studentCourseError.value   = null;
        try {
            const res = await StudentCourseApi.studentCourseIPage(payload);
            const { records, total: count } = res.data.data;
            studentCourseList.value  = records;
            studentCourseTotal.value = count;
        } catch (e) {
            studentCourseError.value = e.message || '获取学生选课列表失败';
            studentCourseList.value  = [];
            studentCourseTotal.value = 0;
            throw e;
        } finally {
            studentCourseLoading.value = false;
        }
    }

    // 新增学生选课
    async function insertStudentCourse(dto) {
        studentCourseLoading.value = true;
        studentCourseError.value   = null;
        try {
            await StudentCourseApi.insertStudentCourse(dto);
        } catch (err) {
            studentCourseError.value = err.message || '新增学生选课失败';
            throw err;
        } finally {
            studentCourseLoading.value = false;
        }
    }

    // 批量新增学生选课
    async function insertBatch(list) {
        studentCourseLoading.value = true;
        studentCourseError.value   = null;
        try {
            await StudentCourseApi.insertBatch(list);
        } catch (err) {
            studentCourseError.value = err.message || '批量新增学生选课失败';
            throw err;
        } finally {
            studentCourseLoading.value = false;
        }
    }

    // 更新学生选课
    async function updateStudentCourse(dto) {
        studentCourseLoading.value = true;
        studentCourseError.value   = null;
        try {
            await StudentCourseApi.updateStudentCourse(dto);
        } catch (err) {
            studentCourseError.value = err.message || '修改学生选课失败';
            throw err;
        } finally {
            studentCourseLoading.value = false;
        }
    }
    async function insertOne(userId, courseId) {
        studentCourseLoading.value = true;
        studentCourseError.value   = null;
        try {
            await StudentCourseApi.insertOne(userId, courseId);
            // 订完后刷新当前页
            await studentCourseIPage({ page: 1, size: 10 });
        } catch (err) {
            studentCourseError.value = err.message || '订课失败';
            throw err;
        } finally {
            studentCourseLoading.value = false;
        }
    }

    // 根据用户ID删除学生选课
    async function deleteByUserId(userId) {
        studentCourseLoading.value = true;
        studentCourseError.value   = null;
        try {
            await StudentCourseApi.deleteByUserId(userId);
        } catch (err) {
            studentCourseError.value = err.message || '删除学生选课失败';
            throw err;
        } finally {
            studentCourseLoading.value = false;
        }
    }

    // 根据课程ID删除学生选课
    async function deleteByCourseId(courseId) {
        studentCourseLoading.value = true;
        studentCourseError.value   = null;
        try {
            await StudentCourseApi.deleteByCourseId(courseId);
        } catch (err) {
            studentCourseError.value = err.message || '删除学生选课失败';
            throw err;
        } finally {
            studentCourseLoading.value = false;
        }
    }

    // 根据主键ID删除学生选课
    async function deleteById(id) {
        studentCourseLoading.value = true;
        studentCourseError.value   = null;
        try {
            await StudentCourseApi.deleteById(id);
        } catch (err) {
            studentCourseError.value = err.message || '删除学生选课失败';
            throw err;
        } finally {
            studentCourseLoading.value = false;
        }
    }

    // 批量根据用户IDs删除学生选课
    async function deleteByUserIds(userIds) {
        studentCourseLoading.value = true;
        studentCourseError.value   = null;
        try {
            await StudentCourseApi.deleteByUserIds(userIds);
        } catch (err) {
            studentCourseError.value = err.message || '批量删除学生选课失败';
            throw err;
        } finally {
            studentCourseLoading.value = false;
        }
    }

    // 批量根据课程IDs删除学生选课
    async function deleteByCourseIds(courseIds) {
        studentCourseLoading.value = true;
        studentCourseError.value   = null;
        try {
            await StudentCourseApi.deleteByCourseIds(courseIds);
        } catch (err) {
            studentCourseError.value = err.message || '批量删除学生选课失败';
            throw err;
        } finally {
            studentCourseLoading.value = false;
        }
    }



    return {



        /* 状态 */
        studentCourseList,
        studentCourseTotal,
        studentCourseLoading,
        studentCourseError,


        /* 学生选课 */
        insertOne,
        studentCourseIPage,
        insertStudentCourse,
        insertBatch,
        updateStudentCourse,
        deleteByUserId,
        deleteByCourseId,
        deleteById,
        deleteByUserIds,
        deleteByCourseIds,


    };
});