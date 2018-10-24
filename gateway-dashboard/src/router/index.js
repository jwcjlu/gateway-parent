import Vue from 'vue'
import Router from 'vue-router'
const explain = r => require.ensure([], () => r(require('../page/explain')), 'explain')
const adminSet = r => require.ensure([], () => r(require('../page/adminSet')), 'adminSet')
const home = r => require.ensure([], () => r(require('../page/home')), 'home')
const login = r => require.ensure([], () => r(require('../page/login')), 'login')
const manage = r => require.ensure([], () => r(require('../page/manage')), 'manage')
const appAuthManage = r => require.ensure([], () => r(require('../page/appAuthManage')), 'appAuthManage')
const userManage = r => require.ensure([], () => r(require('../page/userManage')), 'userManage')
const pluginManage = r => require.ensure([], () => r(require('../page/pluginManage')), 'pluginManage')

const concurrency = r => require.ensure([], () => r(require('../page/concurrency')), 'rate_limiter')

const divide = r => require.ensure([], () => r(require('../page/divide')), 'divide')
const resourceManage = r => require.ensure([], () => r(require('../page/resourceManage')), 'resourceManage')
const roleManage = r => require.ensure([], () => r(require('../page/roleManage')), 'ruleManage')
const menuManage = r => require.ensure([], () => r(require('../page/menuManage')), 'menuManage')
const serviceManage = r => require.ensure([], () => r(require('../page/serviceManage')), 'serviceManage')


Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            component: login
        },
        {
            path: '/manage',
            component: manage,
            name: '',
            children: [{
                path: '',
                component: home,
                meta: [],
            }, {
                path: '/pluginManage',
                component: pluginManage,
                meta: ['插件管理'],
            },  {
                path: '/concurrency',
                component: concurrency,
                meta: ['concurrency'],
            },

                {
                    path: '/serviceManage',
                    component: serviceManage,
                    meta: ['服务管理'],
                }, {
                path: '/menuManage',
                component: menuManage,
                meta: ['菜单管理'],
            },{
                    path: '/divide',
                    component: divide,
                    meta: ['代理&分流'],
                },
                {
                    path: '/resourceManage',
                    component: resourceManage,
                    meta: ['权限管理'],
                },
                {
                path: '/appAuthManage',
                component: appAuthManage,
                meta: ['接入管理'],
            }, {
                    path: '/roleManage',
                    component: roleManage,
                    meta: ['角色管理'],
                }, {
                    path: '/userManage',
                    component: userManage,
                    meta: ['用户管理'],
            }, {
                    path: '/adminSet',
                    component: adminSet,
                    meta: ['设置', '管理员设置'],
            }, {
                    path: '/explain',
                    component: explain,
                    meta: ['说明', '说明'],
            }]
        }
    ]
})
