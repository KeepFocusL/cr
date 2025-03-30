import { createRouter, createWebHistory } from 'vue-router'
import AppLayout from '@/layout/AppLayout.vue'
import {useUserStore} from '@/stores/user.js'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login.vue'),
      meta: {
        title: '登录',
        requiresAuth: false  // 登录页不需要验证
      }
    },
    {
      path: '/',
      component: AppLayout,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('../views/Dashboard.vue'),
          meta: { title: '仪表盘' }
        },
        {
          path: 'empty',
          name: 'Empty',
          component: () => import('../views/Empty.vue'),
          meta: { title: '空白页面' }
        },
        {
          path: 'sb',
          name: 'Sb',
          component: () => import('../views/user/Sb.vue'),
          meta: { title: '傻逼管理' }
        },
        {
          path: 'course',
          name: 'Course',
          component: () => import('../views/user/Course.vue'),
          meta: { title: '课程管理' }
        },
        {
          path: 'station',
          name: 'Station',
          component: () => import('../views/business/Station.vue'),
          meta: { title: '车站管理' }
        },
        {
          path: 'train',
          name: 'Train',
          component: () => import('../views/business/Train.vue'),
          meta: { title: '车次管理' }
        },
        {
          path: 'train-station',
          name: 'TrainStation',
          component: () => import('../views/business/TrainStation.vue'),
          meta: { title: '火车车站管理' }
        },
      ]
    },
  ],
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  // 如果页面需要认证（默认都需要认证，除非明确标记不需要）
  if (to.meta.requiresAuth !== false) {
    // 检查是否有 token
    if (!userStore.token) {
      // 如果没有 token，重定向到登录页
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
      return
    }
  }

  // 如果已登录且要访问登录页，重定向到首页
  if (to.path === '/login' && userStore.token) {
    next({ path: '/dashboard' })
    return
  }

  next()
})

export default router
