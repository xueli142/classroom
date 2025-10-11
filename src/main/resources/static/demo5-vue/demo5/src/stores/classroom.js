import { reactive } from 'vue'
import {
    getPage,
    createClassroom,
    deleteClassroom,
    updateClassroom,
    createClassroomImage,
    updateClassroomImage
} from '@/api/classroom.js'

/* ---------- 状态 ---------- */
const state = reactive({
    page: 0,
    classrooms: [],
    urls: [],
    loading: false
})

/* ---------- 原有 ---------- */
async function load(p = 0) {
    state.loading = true
    try {
        state.page = p
        const { classrooms, urls } = await getPage(p)
        state.classrooms = classrooms
        state.urls = urls
    } finally {
        state.loading = false
    }
}

/* ---------- 新增 ---------- */
async function create(form) {
    await createClassroom(form)
    await load(state.page)   // 停留在当前页
}

async function remove(id) {
    await deleteClassroom(id)
    await load(state.page)
}

async function update(id, form) {
    await updateClassroom(id, form)
    await load(state.page)
}

/**
 * 上传图片
 * @param {string|number} id
 * @param {File} file
 * @param {boolean} isCreate  true=新增 false=修改
 */
async function uploadImage(id, file, isCreate = true) {
    isCreate
        ? await createClassroomImage(id, file)
        : await updateClassroomImage(id, file)
    await load(state.page)
}

/* ---------- 首次拉数据 ---------- */
load(0)

/* ---------- 导出给组件用 ---------- */


// 在 useClassroom 里追加
async function search(keyword) {
    state.loading = true
    try {
        // 如果后端支持 /api/classroom/search?key=xxx
        const { classrooms, urls } = await axios
            .get('http://localhost:9090/api/classroom/search', { params: { key: keyword } })
            .then(res => res.data.data)

        state.page   = 0          // 查询结果默认第一页
        state.classrooms = classrooms
        state.urls       = urls
    } finally {
        state.loading = false
    }
}

export function useClassroom() {
    return { state, load, create, remove, update, uploadImage, search }
}