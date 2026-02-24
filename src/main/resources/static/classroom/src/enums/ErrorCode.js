/**
 * 错误码枚举
 * 与后端 ErrorCode 接口及各 ErrorCode 枚举类保持一致
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

// ==================== 通用响应码 ====================
export const ResponseCode = {
	SUCCESS: 200,
	CREATED: 201,
	NO_CONTENT: 204,
	BAD_REQUEST: 400,
	UNAUTHORIZED: 401,
	FORBIDDEN: 403,
	NOT_FOUND: 404,
	INTERNAL_ERROR: 500,
};

// ==================== 认证相关错误码 ====================
export const AuthErrorCode = {
	TOKEN_MISSING: 'AUTH_001',
	TOKEN_EXPIRED: 'AUTH_002',
	TOKEN_INVALID: 'AUTH_003',
	CREDENTIALS_INVALID: 'AUTH_004',
	USER_DISABLED: 'AUTH_005',
	ACCOUNT_LOCKED: 'AUTH_006',
};

// ==================== 用户相关错误码 ====================
export const UserErrorCode = {
	USER_NOT_FOUND: 'USER_001',
	UID_DUPLICATE: 'USER_002',
	PHONE_DUPLICATE: 'USER_003',
};

// ==================== 业务相关错误码 ====================
export const BusinessErrorCode = {
	RESOURCE_NOT_FOUND: 'BIZ_001',
	RESOURCE_CONFLICT: 'BIZ_002',
	OPERATION_NOT_ALLOWED: 'BIZ_003',
	DUPLICATE_RESOURCE: 'BIZ_004',
};

// ==================== 验证相关错误码 ====================
export const ValidationErrorCode = {
	VALIDATION_FAILED: 'VAL_001',
	INVALID_PARAMETER: 'VAL_002',
	INVALID_FORMAT: 'VAL_003',
};

/**
 * 错误码友好消息映射
 */
export const ErrorMessages = {
	// 认证错误
	[AuthErrorCode.TOKEN_MISSING]: '缺少认证令牌',
	[AuthErrorCode.TOKEN_EXPIRED]: '登录已过期，请重新登录',
	[AuthErrorCode.TOKEN_INVALID]: '认证令牌无效',
	[AuthErrorCode.CREDENTIALS_INVALID]: '用户名或密码错误',
	[AuthErrorCode.USER_DISABLED]: '账号已被禁用，请联系管理员',
	[AuthErrorCode.ACCOUNT_LOCKED]: '账号已被锁定，请联系管理员',

	// 用户错误
	[UserErrorCode.USER_NOT_FOUND]: '用户不存在',
	[UserErrorCode.UID_DUPLICATE]: '用户名已存在',
	[UserErrorCode.PHONE_DUPLICATE]: '手机号已被使用',

	// 业务错误
	[BusinessErrorCode.RESOURCE_NOT_FOUND]: '请求的资源不存在',
	[BusinessErrorCode.RESOURCE_CONFLICT]: '资源冲突',
	[BusinessErrorCode.OPERATION_NOT_ALLOWED]: '当前操作不允许',
	[BusinessErrorCode.DUPLICATE_RESOURCE]: '资源已存在',

	// 验证错误
	[ValidationErrorCode.VALIDATION_FAILED]: '数据验证失败',
	[ValidationErrorCode.INVALID_PARAMETER]: '参数错误',
	[ValidationErrorCode.INVALID_FORMAT]: '格式错误',
};

/**
 * 根据错误码获取友好消息
 * @param {string} code - 错误码
 * @param {string} defaultMessage - 默认消息
 * @returns {string} 友好消息
 */
export function getErrorMessage(code, defaultMessage = '操作失败') {
	return ErrorMessages[code] || defaultMessage;
}

export default {
	ResponseCode,
	AuthErrorCode,
	UserErrorCode,
	BusinessErrorCode,
	ValidationErrorCode,
	ErrorMessages,
	getErrorMessage,
};
