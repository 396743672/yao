import {constantRouterMap} from '@/router/routers'
import {validatenull, validateURL} from '@/utils/validate'
import {GetMenu} from '@/api/menu'
import router from '@/router/routers'


const permission = {
    state: {
        routers: constantRouterMap,
        routersCopy: constantRouterMap,
        addRouters: [],
        topNav: {
            currNav: '',
            navList: []
        }
    },
    mutations: {
        ADD_ROUTERS: (state, addRouters) => {
            state.routers = constantRouterMap.concat(addRouters);
        },
        ADD_ROUTERS_COPY: (state, addRouters) => {
            state.routersCopy = constantRouterMap.concat(addRouters);
        },
        SET_TOP_NAV: (state, topNav) => {
            state.topNav = topNav;
        }
    },
    actions: {
        // 获取系统菜单
        GetMenu({commit}, userId) {
            return new Promise(resolve => {
                GetMenu(userId).then((res) => {
                    const menu = res.data;
                    if (menu.length === 0) {
                        return
                    }
                    const menus = formatRoutes(menu);
                    const unFound = {
                        path: '*',
                        redirect: '/404',
                        hidden: true
                    };
                    router.addRoutes(...menus);
                    router.addRoutes([unFound]);
                    this.commit('ADD_ROUTERS', ...menus);
                    this.commit('ADD_ROUTERS_COPY', ...menus);
                    this.commit('SET_TOP_NAV', formatTopNav(menu));
                    resolve(menu)
                })
            })
        },
        setTopNavCurrent({commit, state}, currNav) {
            state.topNav.currNav = currNav;
            let menus = state.routersCopy.filter(router => {
                return router.parentId === currNav
            });
            this.commit('ADD_ROUTERS',menus);
        }
    }
}

export default permission

let formatRoutes = (aMenu) => {
    const aRouter = [];
    aMenu.forEach(oMenu => {
        const {path, component, name, icon, children, type, parentId} = oMenu;
        if (type === -1) {
            //顶级菜单
            if (!validatenull(oMenu.children)) {
                aRouter.push(formatRoutes(oMenu.children));
            }
        } else {
            if (!validatenull(component)) {
                const oRouter = {
                    path: path,
                    component: () => {
                        let componentPath = ''
                        if (component === 'Layout') {
                            return import('@/views/layout/Layout')
                        } else {
                            componentPath = component
                        }
                        return import(`@/${componentPath}.vue`)
                    },
                    name: name,
                    meta: {
                        icon: icon,
                        title: name
                    },
                    icon: icon,
                    parentId: parentId,
                    children: validatenull(children) ? [] : formatRoutes(children)
                }
                aRouter.push(oRouter)
            }
        }
    })
    return aRouter
}

let formatTopNav = (aMenu) => {
    const navList = [];
    let currNav = '';
    aMenu.forEach(oMenu => {
        const {type} = oMenu;
        if (type === -1) {
            //顶级菜单
            if (oMenu.component === 'Layout') {
                currNav = oMenu.id;
            }
            navList.push(oMenu);
        }
    });
    return {
        currNav,
        navList
    }

}
