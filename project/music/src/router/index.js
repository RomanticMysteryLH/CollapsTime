import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)
// 不重用的页面放在views下
import home from '@/views/home'
import playlist from '@/views/playlist'
import musician from '@/views/musician'
import myMusic from '@/views/mymusic'
import message from '@/views/message'
import hitSingle from '@/views/hitSingle'
import register from '@/views/register'
import detailPlaylist from '@/views/detailPlaylist'
import detailMusician from '@/views/detailMusician'
import hitPlaylist from '@/views/hitPlaylist'
import userCenter from '@/views/userCenter'
import hitSinger from '@/views/hitSinger'
import collectedSong from '@/views/collectedSong'
import collectedPlaylist from '@/views/collectedPlaylist'
import followingSinger from '@/views/followingSinger'
import searchResult from '@/views/searchResult'
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
            component: myMusic,
            // beforeEnter: (to, from, next) => {
            //     // ...
            //     console.log(to);
            //     console.log(from);
            //     console.log(next);
            //     console.log(this.$root.userData)
            //     if(to.path ===myMusic&&this.$root.userData.login_ed==false){
            //         next({path:"/"});
            //     }else{
            //         next();
            //     }
            // }
        },
        {
            path: '/message',
            component: message,
        },
        {
            path: '/home/hitSingle',
            component: hitSingle
        },
        {
            path: '/home/hitPlaylist',
            component: hitPlaylist
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
            component: detailMusician,
        },
        {
            path: '/userCenter',
            component: userCenter
        },
        {
            path: '/home/hitSinger',
            component: hitSinger
        },
        {
            path: '/myMusic/collectedSong',
            component: collectedSong
        },
        {
            path: '/myMusic/collectedPlaylist',
            component: collectedPlaylist
        },
        {
            path: '/myMusic/followingSinger',
            component: followingSinger
        },
        {
            name:'searchResult',
            path: '/searchResult/:searchText/:searchMode',
            component: searchResult
        },
    ]
})