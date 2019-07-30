import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

/* Layout */
import Layout from '../views/layout/Layout'


export const constantRouterMap = [
    {
        path: '/login',
        meta: {title: '登录', noCache: true},
        component: () => import('@/views/login/index'),
        hidden: true
    },
    {
        path: '/404',
        component: () => import('@/views/error/404'),
        hidden: true
    },
    {
        path: '/401',
        component: () => import('@/views/error/401'),
        hidden: true
    },
    {
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '/redirect/:path*',
                component: () => import('@/views/redirect/index')
            }
        ]
    },
    {
        path: '/',
        component: Layout,
        redirect: 'dashboard',
        children: [
            {
                path: 'dashboard',
                component: () => import('@/views/dashboard/index'),
                name: '首页',
                meta: {title: '首页', icon: 'tachometer', noCache: true, affix: true}
            }
        ]
    },
    {
        path: '/',
        component: Layout,
        hidden: true,
        redirect: 'noredirect',
        children: [
            {
                path: 'userCenter',
                component: () => import('@/views/modules/admin/user/center/center'),
                name: '个人中心',
                meta: {title: '个人中心', icon: 'user'}
            }
        ]
    }
];

export default new Router({
    //mode: 'history',
    scrollBehavior: () => ({y: 0}),
    routes: constantRouterMap
})
