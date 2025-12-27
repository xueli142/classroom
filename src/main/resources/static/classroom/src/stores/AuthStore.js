    // stores/auth.js
    import { defineStore } from 'pinia';
    import { ref, computed } from 'vue';
    import router from '@/router/index.js';
    import { AuthApi } from '@/api/AuthApi.js';

    /* ---------- 持久化小工具 ---------- */
    const persist = {
        save(user, token) {
            if (user)  localStorage.setItem('auth.user',  JSON.stringify(user));
            if (token) localStorage.setItem('auth.token', token);
        },
        load() {
            const userStr = localStorage.getItem('auth.user');
            const token   = localStorage.getItem('auth.token');
            return {
                user : userStr ? JSON.parse(userStr) : null,
                token: token || '',
            };
        },
        clear() {
            localStorage.removeItem('auth.user');
            localStorage.removeItem('auth.token');
        },
    };

    /* ---------- courseStore 定义 ---------- */
    export const useAuthStore = defineStore('auth', () => {
        /* ----- state ----- */
        const user    = ref(null);        // { uid, role, name, phone, email, message … }
        const token   = ref('');
        const loading = ref(false);
        const error   = ref(null);

        /* ----- getters ----- */
        const isLogin = computed(() => !!token.value);

        /* ----- actions ----- */

        // 1. 恢复登录态（在 main.js 或 App.vue 里调用一次）
        function loadLocalAuth() {
            const { user: localUser, token: localToken } = persist.load();
            if (localToken && localUser) {
                user.value  = localUser;
                token.value = localToken;
            }
        }

        // 2. 登录
        async function login(payload) {
            loading.value = true;
            error.value   = '';
            try {
                const res = await AuthApi.loginUser(payload);
                user.value  = res.user;
                token.value = res.token;
                persist.save(res.user, res.token);
                return res;
            } catch (e) {
                error.value = e?.response?.data?.msg || '登录失败';
                throw e;                       // 交给组件继续处理
            } finally {
                loading.value = false;
            }
        }

        // 3. 退出
        async function logout() {
            if (!token.value) return;
            loading.value = true;
            error.value   = '';
            try {
                await AuthApi.logout(token.value);
            } catch (e) {
                // 即使接口失败也强制清本地
            } finally {
                user.value  = null;
                token.value = '';
                persist.clear();
                loading.value = false;
                await router.replace('/login');
            }
        }

        /* 以下简单转发，保持与后端接口一致，组件可直接调用 */
        const changePassword = payload => AuthApi.changePassword(payload);
        const changeUsername = payload => AuthApi.changeUid(payload);
        const changePhone    = payload => AuthApi.changePhone(payload);
        const insertImage    = file    => AuthApi.insertImage(file);
        const changeImage    = (file, oldName) => AuthApi.changeImage(file, oldName);

        /* ---------- 导出 ---------- */
        return {
            // state
            user,
            token,
            loading,
            error,
            // getters
            isLogin,
            // actions
            loadLocalAuth,
            login,
            logout,
            changePassword,
            changeUsername,
            changePhone,
            insertImage,
            changeImage,
        };
    });