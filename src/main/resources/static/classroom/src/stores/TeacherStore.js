// stores/teacherStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { UserManageApi } from '@/api/UserManageApi.js';

export const useTeacherStore = defineStore('teacher', () => {
    const list    = ref([]);
    const total   = ref(0);
    const loading = ref(false);
    const error   = ref(null);

    async function pageTeacher(payload = { current: 1, size: 10 }) {
        loading.value = true;
        error.value   = null;
        try {
            const res = await UserManageApi.pageTeacher(payload);
            const { records, total: count } = res.data.data;
            list.value  = records;
            total.value = count;
        } catch (e) {
            error.value = e.message || '获取教师列表失败';
            list.value  = [];
            total.value = 0;
            throw e;
        } finally {
            loading.value = false;
        }
    }

    async function addTeacher(payload) {
        loading.value = true;
        error.value   = null;
        try {
            await UserManageApi.addTeacher(payload);
        } catch (err) {
            error.value = err.message || '新增教师失败';
            throw err;
        } finally {
            loading.value = false;
        }
    }

    async function updateTeacher(payload) {
        loading.value = true;
        error.value   = null;
        try {
            await UserManageApi.updateTeacher(payload);
        } catch (err) {
            error.value = err.message || '修改教师失败';
            throw err;
        } finally {
            loading.value = false;
        }
    }

    async function delTeacher(userId) {
        loading.value = true;
        error.value   = null;
        try {
            await UserManageApi.delTeacher(userId);
        } catch (err) {
            error.value = err.message || '删除教师失败';
            throw err;
        } finally {
            loading.value = false;
        }
    }

    async function batchDelTeacher(userIds) {
        loading.value = true;
        error.value   = null;
        try {
            await UserManageApi.batchDelTeacher(userIds);
        } catch (err) {
            error.value = err.message || '批量删除教师失败';
            throw err;
        } finally {
            loading.value = false;
        }
    }

    return {
        list,
        total,
        loading,
        error,
        pageTeacher,
        addTeacher,
        updateTeacher,
        delTeacher,
        batchDelTeacher
    };
});