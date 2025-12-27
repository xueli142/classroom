import {defineStore} from 'pinia';
import {ref} from 'vue';
import {UserManageApi} from '@/api/UserManageApi.js';


export const useStudentStore  = defineStore('student', () => {


    const user    = ref(null);        // { uid, role, name, phone, email, message … }
    const token   = ref('');
    const loading = ref(false);
    const error   = ref(null);
    const list    = ref([])
    const total   = ref(0)
    async function addStudent(payload) {
        loading.value = true;
        error.value   = null;
        try {
            await UserManageApi.addStudent(payload);
        } catch (err) {
            error.value = err.message || '新增失败';
            throw err;          // 让组件可以继续 catch
        } finally {
            loading.value = false;
        }

    }
    async function updateStudent(payload) {
        loading.value = true;
        error.value   = null;
        try {
            await UserManageApi.updateStudent(payload);
        } catch (err) {
            error.value = err.message || '修改失败';
            throw err;          // 让组件可以继续 catch
        } finally {
            loading.value = false;
        }
    }
    async function delStudent(userId) {
        loading.value = true;
        error.value   = null;
        try {
            await UserManageApi.delStudent(userId);
        } catch (err) {
            error.value = err.message || '删除失败';
            throw err;          // 让组件可以继续 catch
        } finally {
            loading.value = false;
        }
    }

    async function batchDelStudent(userIds) {

        loading.value = true;
        error.value   = null;
        try {
            await UserManageApi.batchDelStudent(userIds);
        } catch (err) {
            error.value = err.message || '批量删除失败';
            throw err;          // 让组件可以继续 catch
        } finally {
            loading.value = false;
        }
    }
    async function batchAddStudent(dtoList) {
        loading.value = true;
        error.value   = null;
        try {
            await UserManageApi.batchAddStudent(dtoList);
        } catch (err) {
            error.value = err.message || '批量新增失败';
            throw err;          // 让组件可以继续 catch
        } finally {
            loading.value = false;
        }
    }

    async function pageStudent(payload = { current: 1, size: 10 }) {
        loading.value = true
        error.value   = null

        try {
            // 1. 发请求
            const res = await UserManageApi.pageStudent(payload)

            // 2. 在 courseStore 里一次性剥掉两层 data
            const { records, total: count } = res.data.data
            list.value = records
            total.value = count
        } catch (e) {
            error.value = e.message || '获取数据失败'
            list.value = []
            total.value = 0
            throw e          // 组件若想继续处理错误可 catch
        } finally {
            loading.value = false
        }
    }





return {
    user,
    token,
    loading,
    error,
    addStudent,
    updateStudent,
    delStudent,
    batchDelStudent,
    batchAddStudent,
    pageStudent,
    list,
    total
};




});