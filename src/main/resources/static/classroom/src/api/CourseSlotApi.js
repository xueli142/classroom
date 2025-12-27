// src/api/CourseSlotApi.js
import http from '@/api/http.js'

export const CourseSlotApi = {

    /* ===== 单条 / 批量 更新 ===== */
    updateOne: dto =>
        http.put('/api/course-slot/updateById', dto),

    updateBatch: dtoList =>
        http.put('/api/course-slot/updateByIds', dtoList),

    /* ===== 删除 ===== */
    deleteOne: courseSlotId =>
        http.delete(`/api/course-slot/${courseSlotId}`),

    deleteBatch: courseSlotIds =>
        http.delete('/api/course-slot/deleteByIds', { data: courseSlotIds }),

    /* ===== 批量新增（一张公共图） ===== */
    insertBatch: dtoList =>
        http.post('/api/course-slot/insertBatch', dtoList),

    /* ===== 分页 & 视图 ===== */
    // 通用分页（管理后台）
    page: dto =>
        http.post('/api/course-slot/page', dto),

    // 老师：看我本周时段
    teacherSlotPage: dto =>
        http.post('/api/course-slot/teacherSlotPage', dto),

    //学生:看本周时段
    studentSlotPage:dto=>
        http.post('/api/course-slot/studentSlotPage', dto),
    // 教室：占用明细
    classroomOccupyPage: dto =>
        http.post('/api/course-slot/classroomOccupyPage', dto),

    // 学生/老师：个人课表
    userSlotPage: dto =>
        http.post('/api/course-slot/userSlotPage', dto),

    // 列表查询（无分页）
    selectList: dto =>
        http.post('/api/course-slot/selectList', dto),

    /* ===== 以下接口后端已删除，若前端已无用可清理 ===== */
    // canBookPage: dto => http.post('/api/course-slot/canBookPage', dto),
    // weekTable:   dto => http.post('/api/course-slot/weekTable', dto),
}