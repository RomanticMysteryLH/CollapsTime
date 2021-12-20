import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)
// 不重用的页面放在views下
import home from '@/views/home'
import playlist from '@/views/playlist'
import musician from '@/views/musician'
import myMusic from '@/views/mymusic'
import message from '@/views/message'
import hotSingle from '@/views/hotSingle'
import register from '@/views/register'
import detailPlaylist from '@/views/detailPlaylist'
import detailMusician from '@/views/detailMusician'
export default new VueRouter({
    routes: [{
            path: '/home',
            name: 'home',
            component: home,
            // children: [
            //     {
            //         // 当 /home/hotSingle 匹配成功，
            //         // UserProfile 会被渲染在 User 的 <router-view> 中
            //         path: 'hotSingle',
            //         component: hotSingle
            //     },
            // ]
        }, {
            path: '/',
            name: 'to_home',
            redirect: '/home' //重定向到home
        }, {

            path: '/playlist',
            component: playlist
        },
        {
            path: '/musician',
            component: musician
        },
        {
            path: '/myMusic',
            component: myMusic
        },
        {
            path: '/message',
            component: message,
        },
        {
            path: '/home/hotSingle',
            component: hotSingle
        },
        {
            path: '/register',
            component: register
        },
        {
            name: 'detailPlaylist',
            path: '/detailPlaylist/:playlistId',
            component: detailPlaylist,
        }, 
        {
            name: 'detailMusician',
            path: '/detailMusician/:musicianId',
            component:detailMusician,
        },
    ]
})