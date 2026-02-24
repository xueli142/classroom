const WHITE_LIST = ['/login'];

// 不同角色对应的“首页”
const ROLE_HOME = {
    admin:   '/adminDashboard',
    teacher: '/teacherDashboard',
    student: '/studentDashboard'
};

function getToken() {
    return localStorage.getItem('auth.token') || '';
}

function getRole() {
    try {
        return JSON.parse(localStorage.getItem('auth.user') || '{}')?.role || '';
    } catch { return ''; }
}

export function setupGuard(router) {
    router.beforeEach((to, from, next) => {
        /* ---------- 404 兜底 ---------- */
        if (to.matched.length === 0) {
            next('/login');
            return;
        }

        /* ---------- 白名单 ---------- */
        if (WHITE_LIST.includes(to.path)) {
            next();
            return;
        }

        /* ---------- 登录态 ---------- */
        const token = getToken();
        if (!token) {
            next('/login');
            return;
        }

        /* ---------- 角色分流 ---------- */
        const role = getRole();
        // 第一次进系统（或手动输 /Dashboard）时按角色跳转
        if (to.path === '/' || to.path === '/Dashboard') {
            const target = ROLE_HOME[role];
            if (target) {
                next(target);   // 跳到各自 Dashboard
            } else {
                next('/login'); // 没角色就回登录
            }
            return;
        }

        /* ---------- 细粒度鉴权可继续在这里扩展 ---------- */
        // if (!isRoleAllowed(to.path, role)) { next(ROLE_HOME[role]); return; }

        next();
    });
}