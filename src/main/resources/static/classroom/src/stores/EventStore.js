// stores/eventStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { EventApi } from '@/api/EventApi.js';

export const useEventStore = defineStore('event', () => {
    /* ===== 活动 ===== */

    const eventList    = ref([]);
    const eventTotal   = ref(0);
    const eventLoading = ref(false);
    const eventError   = ref(null);

    /* ===== 活动管理 ===== */

    // 分页查询活动
    async function eventIPage(payload = { page: 1, size: 10 }) {
        eventLoading.value = true;
        eventError.value   = null;
        try {
            const res = await EventApi.eventIPage(payload);
            const { records, total: count } = res.data.data;
            console.log(res.data.data);
            eventList.value  = records;
            eventTotal.value = count;
        } catch (e) {
            eventError.value = e.message || '获取活动列表失败';
            eventList.value  = [];
            eventTotal.value = 0;
            throw e;
        } finally {
            eventLoading.value = false;
        }
    }

    // 新增活动（含图片）
    async function insertEvent(dto, file) {
        eventLoading.value = true;
        eventError.value   = null;
        try {
            await EventApi.insertEvent(dto, file);
        } catch (err) {
            eventError.value = err.message || '新增活动失败';
            throw err;
        } finally {
            eventLoading.value = false;
        }
    }

    // 更新活动（含图片）
    async function updateEvent(dto, file, oldName) {
        eventLoading.value = true;
        eventError.value   = null;
        try {
            await EventApi.updateEvent(dto, file, oldName);
        } catch (err) {
            eventError.value = err.message || '修改活动失败';
            throw err;
        } finally {
            eventLoading.value = false;
        }
    }

    // 删除单个活动
    async function deleteByEventId(eventId) {
        eventLoading.value = true;
        eventError.value   = null;
        try {
            await EventApi.deleteByEventId(eventId);
        } catch (err) {
            eventError.value = err.message || '删除活动失败';
            throw err;
        } finally {
            eventLoading.value = false;
        }
    }

    // 批量删除活动
    async function deleteByEventIds(eventIds) {
        eventLoading.value = true;
        eventError.value   = null;
        try {
            await EventApi.deleteByEventIds(eventIds);
        } catch (err) {
            eventError.value = err.message || '批量删除活动失败';
            throw err;
        } finally {
            eventLoading.value = false;
        }
    }

    return {
        /* 状态 */
        eventList,
        eventTotal,
        eventLoading,
        eventError,

        /* 活动 */
        eventIPage,
        insertEvent,
        updateEvent,
        deleteByEventId,
        deleteByEventIds,
    };
});