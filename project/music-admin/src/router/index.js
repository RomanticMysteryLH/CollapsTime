import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
//都看得见的路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
{
  path: '/404',
  component: () =>
    import ('@/views/404'),
  hidden: true
},

  /**
   * 数据图表
   */
  {
  path: '/',
  component: Layout,
  redirect: '/dashboard',
  children: [{
    path: 'dashboard',
    name: 'Dashboard',
    component: () =>
      import ('@/views/dashboard/index'),
    meta: { title: '后台数据', icon: 'dashboard' }
  }]
},

  /**
   * 会员管理模块
   */
  {
  path: '/UserManagment',
  component: Layout,
  name: 'UserManagment',
  meta: {
    title: '会员管理',
    icon: 'user'
  },
  children: [{
    path: 'index',
    component: () =>
      import ('@/views/UserManagment/index'),
    name: 'usermanagment',
    meta: {
      title: '管理会员信息'
    }
  },
  // {
  //   path: 'userExam',
  //   component: () =>
  //     import ('@/views/UserManagment/userExam'),
  //   name: 'userExam',
  //   meta: {
  //     title: '审核注销会员'
  //   }
  // }
  ]
},

  /**
   * 歌单管理模块
   */
  {
    path: '/SongListManagment',
    component: Layout,
    children: [{
      path: 'songlistManage',
      name: 'songlistManage',
      component: () =>
        import ('@/views/SongListManagment/songlistManage'),
      meta: { title: '歌单管理', icon: 'songlist' }
    }]
  },

  /**
   * 曲库管理模块
   */
  {
    path: '/SongManagment',
    component: Layout,
    name: 'SongMnagment',
    meta: {
      title: '曲库管理',
      icon: 'song'
    },
    children: [{
      path: 'songManage',
      component: () =>
        import ('@/views/SongManagment/songManage'),
      name: 'songManage',
      meta: {
        title: '歌曲管理'
      }
    },
      // {
      //   path: 'songExam',
      //   component: () =>
      //     import ('@/views/SongManagment/songExam'),
      //   name: 'songExam',
      //   meta: {
      //     title: '审核歌曲'
      //   }
      // }
    ]
  },

  /**
   * 歌手管理模块
   */
  {
    path: '/SingerManagment',
    component: Layout,
    children: [{
      path: 'singerManage',
      name: 'singerManage',
      component: () =>
        import ('@/views/SingerManagment/singerManage'),
      meta: { title: '歌手管理', icon: 'singer' }
    }]
  },

  /**
   * 主页管理模块
   */
  // {
  //   path: '/HomeManagment',
  //   component: Layout,
  //   name: 'HomeManagment',
  //   meta: {
  //     title: '主页管理',
  //     icon: 'form'
  //   },
  //   children: [{
  //     path: 'homeImgManage',
  //     component: () =>
  //       import ('@/views/HomeManagment/homeImgManage'),
  //     name: 'homeImgManage',
  //     meta: {
  //       title: '主页图片替换'
  //     }
  //   },
  //     {
  //       path: 'placardManage',
  //       component: () =>
  //         import ('@/views/HomeManagment/placardManage'),
  //       name: 'placardManage',
  //       meta: {
  //         title: '公告管理'
  //       }
  //     }
  //   ]
  // },

// 404 page must be placed at the end !!!
{ path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
