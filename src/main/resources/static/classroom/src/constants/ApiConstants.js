/**
 * API 端点常量
 * 集中管理所有 API 端点，便于维护和重构
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

export const ApiEndpoints = {
	// ==================== 认证相关 ====================
	AUTH: {
		LOGIN: '/auth/user/login',
		LOGOUT: '/auth/user/logout',
		CHANGE_PASSWORD: '/auth/user/changePassword',
		CHANGE_USERNAME: '/auth/user/changeUsername',
		CHANGE_PHONE: '/auth/user/changePhone',
		UPLOAD_IMAGE: '/auth/user/insertImage',
		CHANGE_IMAGE: '/auth/user/changeImage',
	},

	// ==================== 预约管理 ====================
	BOOKING: {
		LIST: '/api/booking/page',
		CREATE: '/api/booking/insert',
		UPDATE: '/api/booking/updateById',
		DELETE: '/api/booking/',
		BATCH_DELETE: '/api/booking/deleteByIds',
		BATCH_INSERT: '/api/booking/insertBatch',
	},

	// ==================== 教室管理 ====================
	CLASSROOM: {
		LIST: '/api/classroom/page',
		DETAIL: '/api/classroom/',
		CREATE: '/api/classroom/insert',
		UPDATE: '/api/classroom/updateById',
		DELETE: '/api/classroom/',
		BATCH_DELETE: '/api/classroom/deleteByIds',
	},

	// ==================== 课程管理 ====================
	COURSE: {
		LIST: '/api/course/page',
		DETAIL: '/api/course/',
		CREATE: '/api/course/insert',
		UPDATE: '/api/course/updateById',
		DELETE: '/api/course/',
		BATCH_DELETE: '/api/course/deleteByIds',
	},

	// ==================== 课程时段管理 ====================
	COURSE_SLOT: {
		LIST: '/api/courseSlot/page',
		CREATE: '/api/courseSlot/insert',
		UPDATE: '/api/courseSlot/updateById',
		DELETE: '/api/courseSlot/',
		BATCH_DELETE: '/api/courseSlot/deleteByIds',
		BY_CLASSROOM: '/api/courseSlot/byClassroom',
	},

	// ==================== 学期管理 ====================
	TERM: {
		LIST: '/api/term/list',
		CURRENT: '/api/term/current',
		CREATE: '/api/term/insert',
		UPDATE: '/api/term/updateById',
		DELETE: '/api/term/',
	},

	// ==================== 用户管理 ====================
	USER: {
		// 学生
		STUDENT_LIST: '/api/student/page',
		STUDENT_CREATE: '/api/student/insert',
		STUDENT_UPDATE: '/api/student/updateByUserId',
		STUDENT_DELETE: '/api/student/',
		STUDENT_BATCH_DELETE: '/api/student/deleteByUserIds',

		// 教师
		TEACHER_LIST: '/api/teacher/page',
		TEACHER_CREATE: '/api/teacher/insert',
		TEACHER_UPDATE: '/api/teacher/updateByUserId',
		TEACHER_DELETE: '/api/teacher/',
		TEACHER_BATCH_DELETE: '/api/teacher/deleteByUserIds',

		// 管理员
		ADMIN_LIST: '/api/admin/page',
		ADMIN_CREATE: '/api/admin/insert',
		ADMIN_UPDATE: '/api/admin/updateByUserId',
		ADMIN_DELETE: '/api/admin/',
		ADMIN_BATCH_DELETE: '/api/admin/deleteByUserIds',
	},

	// ==================== 设备管理 ====================
	THING: {
		LIST: '/api/thing/page',
		CREATE: '/api/thing/insert',
		UPDATE: '/api/thing/updateById',
		DELETE: '/api/thing/',
		BY_CLASSROOM: '/api/thing/byClassroom',
	},

	// ==================== 设备预约 ====================
	THING_BOOKING: {
		LIST: '/api/thingBooking/page',
		CREATE: '/api/thingBooking/insert',
		UPDATE: '/api/thingBooking/updateById',
		DELETE: '/api/thingBooking/',
		BY_STUDENT: '/api/thingBooking/byStudent',
	},

	// ==================== 学生选课 ====================
	STUDENT_COURSE: {
		LIST: '/api/studentCourse/page',
		CREATE: '/api/studentCourse/insert',
		DELETE: '/api/studentCourse/',
		BY_STUDENT: '/api/studentCourse/byStudent',
		BY_COURSE: '/api/studentCourse/byCourse',
	},

	// ==================== 事件管理 ====================
	EVENT: {
		LIST: '/api/event/page',
		CREATE: '/api/event/insert',
		UPDATE: '/api/event/updateById',
		DELETE: '/api/event/',
		BY_TEACHER: '/api/event/byTeacher',
	},

	// ==================== 反馈管理 ====================
	ADVICE: {
		LIST: '/api/advice/page',
		CREATE: '/api/advice/insert',
		UPDATE: '/api/advice/updateById',
		DELETE: '/api/advice/',
	},

	ADVICE_REPLY: {
		BY_ADVICE: '/api/adviceReply/byAdvice',
		CREATE: '/api/adviceReply/insert',
	},
};

export default ApiEndpoints;
