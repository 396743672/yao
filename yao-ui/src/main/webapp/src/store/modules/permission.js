import {constantRouterMap} from '@/router/routers'
import {isEmpty, validateURL} from '@/utils/validate'
import {getMenu} from '@/api/menu'
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
        GetMenu({commit, state}, userId) {
            return new Promise(resolve => {
                if (userId === undefined || userId === null) {
                    userId = this.getters.user.id;
                    location.reload();
                }
                getMenu(userId).then((res) => {
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

                    router.addRoutes([unFound]);
                    let menuArr = [];
                    if (menus !== null && menus.length > 0) {
                        menus.forEach(menu => {
                            menuArr.push(...menu)
                        })
                    }
                    router.addRoutes(menuArr);
                    this.commit('ADD_ROUTERS_COPY', menuArr);
                    let topNav = formatTopNav(menu);
                    let routers = state.routersCopy.filter(router => {
                        return router.parentId === topNav.currNav
                    });
                    this.commit('ADD_ROUTERS', routers);
                    this.commit('SET_TOP_NAV', topNav);
                    resolve(menu)
                })
            })
        },
        setTopNavCurrent({commit, state}, currNav) {
            state.topNav.currNav = currNav;
            let menus = state.routersCopy.filter(router => {
                return router.parentId === currNav
            });
            this.commit('ADD_ROUTERS', menus);
        }
    }
}

export default permission

let formatRoutes = (aMenu) => {
    const aRouter = [];
    aMenu.forEach(oMenu => {
        const {path, component, name, icon, type, parentId, url} = oMenu.data;
        const {children} = oMenu;
        if (type === -1) {
            //顶级菜单
            if (!isEmpty(children)) {
                aRouter.push(formatRoutes(children));
            }
        } else {
            if (!isEmpty(component)) {
                const oRouter = {
                    path: path,
                    component: () => {
                        let componentPath = ''
                        if (component === 'Layout') {
                            return import('@/views/layout/Layout')
                        } else if (component === 'Iframe') {
                            return import('@/views/layout/Iframe')
                        } else {
                            componentPath = component
                        }
                        return import(`@/${componentPath}.vue`)
                    },
                    name: name,
                    meta: {
                        icon: icon,
                        title: name,
                        url: url
                    },
                    icon: icon,
                    parentId: parentId,
                    children: isEmpty(children) ? [] : formatRoutes(children)
                };
                aRouter.push(oRouter)
            }
        }
    })
    return aRouter
};

let formatTopNav = (aMenu) => {
    const navList = [];
    let currNav = '';
    aMenu.forEach(oMenu => {
        const {type, component} = oMenu.data;
        if (type === -1) {
            //顶级菜单
            navList.push(oMenu.data);
        }
    });

    //对navList进行排序
    navList.sort(function (a, b) {
        return a.sort - b.sort
    });


    return {
        currNav: navList[0].id,
        navList
    }

};
