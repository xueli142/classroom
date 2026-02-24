/**
 * HTTP 状态码常量
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

export const HttpStatus = {
	// ==================== 成功响应 ====================
	OK: 200,
	CREATED: 201,
	NO_CONTENT: 204,

	// ==================== 客户端错误 ====================
	BAD_REQUEST: 400,
	UNAUTHORIZED: 401,
	FORBIDDEN: 403,
	NOT_FOUND: 404,
	METHOD_NOT_ALLOWED: 405,
	CONFLICT: 409,
	UNPROCESSABLE_ENTITY: 422,

	// ==================== 服务端错误 ====================
	INTERNAL_SERVER_ERROR: 500,
	BAD_GATEWAY: 502,
	SERVICE_UNAVAILABLE: 503,
	GATEWAY_TIMEOUT: 504,
};

export default HttpStatus;
