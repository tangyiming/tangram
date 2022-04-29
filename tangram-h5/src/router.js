import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)
// 避免重复同导航跳转报错
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

export default new Router({
    mode: 'hash',
    routes: [
        {
            path: '/',
            name: '/',
            redirect: '/index',
        },
        {
            path: '/index',
            redirect: '/index/scenes',
        },
        {
            path: '/index',
            name: 'index',
            component: () => import('./views/index'),
            children: [
                {
                    path: 'scenes',
                    // eslint-disable-next-line prettier/prettier
                    component: require('./views/index/scenes').default,
                },
                {
                    path: 'components',
                    // eslint-disable-next-line prettier/prettier
                    component: require('./views/index/components').default,
                },
                {
                    path: 'bizlines',
                    // eslint-disable-next-line prettier/prettier
                    component: require('./views/index/bizlines').default,
                },
            ],
        },
    ],
})
