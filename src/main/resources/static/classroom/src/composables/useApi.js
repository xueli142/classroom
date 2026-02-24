/**
 * 通用的 API 调用 composable
 * 简化组件中的 API 调用逻辑
 *
 * @author Classroom Team
 * @since 2025-02-23
 */

import { ref } from 'vue';

/**
 * 通用的 API 调用 hook
 * @param {Function} apiFunc - API 函数
 * @returns {Object} { loading, error, execute, data }
 */
export function useApi(apiFunc) {
	const loading = ref(false);
	const error = ref(null);
	const data = ref(null);

	/**
	 * 执行 API 调用
	 * @param {...any} args - 传递给 API 函数的参数
	 * @returns {Promise}
	 */
	const execute = async (...args) => {
		loading.value = true;
		error.value = null;
		data.value = null;

		try {
			const result = await apiFunc(...args);
			data.value = result;
			return result;
		} catch (e) {
			error.value = e;
			throw e;
		} finally {
			loading.value = false;
		}
	};

	return {
		loading,
		error,
		data,
		execute
	};
}

/**
 * 用于分页查询的 composable
 * @param {Function} apiFunc - 分页查询 API 函数
 * @returns {Object} { loading, error, execute, data, pagination }
 */
export function usePageApi(apiFunc) {
	const { loading, error, data, execute } = useApi(apiFunc);

	const pagination = ref({
		page: 1,
		size: 10,
		total: 0
	});

	/**
	 * 执行分页查询
	 * @param {Object} params - 查询参数
	 * @returns {Promise}
	 */
	const executePage = async (params = {}) => {
		const result = await execute({
			...params,
			page: params.page || pagination.value.page,
			size: params.size || pagination.value.size
		});

		if (result) {
			pagination.value.total = result.total || 0;
			pagination.value.page = result.current || pagination.value.page;
			pagination.value.size = result.size || pagination.value.size;
		}

		return result;
	};

	/**
	 * 重置到第一页
	 */
	const resetPage = () => {
		pagination.value.page = 1;
	};

	return {
		loading,
		error,
		data,
		pagination,
		execute,
		executePage,
		resetPage
	};
}

/**
 * 用于表单提交的 composable
 * @param {Function} apiFunc - 提交 API 函数
 * @returns {Object} { loading, error, execute, reset }
 */
export function useSubmit(apiFunc) {
	const { loading, error, execute } = useApi(apiFunc);

	return {
		loading,
		error,
		execute
	};
}

export default {
	useApi,
	usePageApi,
	useSubmit
};
