import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    // 门户欢迎页
    {
      path: '/',
      name: 'Portal',
      component: () => import('@/views/Portal.vue'),
      meta: { title: '欢迎' }
    },
    // 登录页
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/auth/Login.vue'),
      meta: { title: '登录' }
    },
    // 主系统
    {
      path: '/main',
      component: () => import('@/layout/MainLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        // 首页
        {
          path: '',
          redirect: '/main/home'
        },
        {
          path: 'home',
          name: 'Home',
          component: () => import('@/views/main/Home.vue'),
          meta: { title: '首页' }
        },
        // 公告管理
        {
          path: 'notice/admin',
          name: 'NoticeAdmin',
          component: () => import('@/views/main/notice/NoticeManage.vue'),
          meta: { title: '管理端公告管理' }
        },
        {
          path: 'notice/user',
          name: 'NoticeUser',
          component: () => import('@/views/main/notice/NoticeManage.vue'),
          meta: { title: '用户端公告管理' }
        },
        {
          path: 'notice/monitor',
          name: 'NoticeMonitor',
          component: () => import('@/views/main/notice/NoticeManage.vue'),
          meta: { title: '监测端公告管理' }
        },
        // 图片广告管理
        {
          path: 'ad/image/admin',
          name: 'ImageAdAdmin',
          component: () => import('@/views/main/ad/ImageAdManage.vue'),
          meta: { title: '管理端图片广告' }
        },
        {
          path: 'ad/image/user',
          name: 'ImageAdUser',
          component: () => import('@/views/main/ad/ImageAdManage.vue'),
          meta: { title: '用户端图片广告' }
        },
        {
          path: 'ad/image/monitor',
          name: 'ImageAdMonitor',
          component: () => import('@/views/main/ad/ImageAdManage.vue'),
          meta: { title: '监测端图片广告' }
        },
        // 视频广告管理（暂未开放）
        {
          path: 'ad/video',
          name: 'VideoAd',
          component: () => import('@/views/main/coming-soon/VideoAd.vue'),
          meta: { title: '视频广告管理' }
        },
        // 用户监测管理
        {
          path: 'user-monitor/website',
          name: 'UserWebsite',
          component: () => import('@/views/main/monitor/WebsiteMonitor.vue'),
          meta: { title: '用户端网站管理' }
        },
        {
          path: 'user-monitor/log',
          name: 'UserLog',
          component: () => import('@/views/main/monitor/LogMonitor.vue'),
          meta: { title: '用户端日志管理' }
        },
        // 管理监测管理
        {
          path: 'admin-monitor/website',
          name: 'AdminWebsite',
          component: () => import('@/views/main/monitor/WebsiteMonitor.vue'),
          meta: { title: '管理端网站管理' }
        },
        {
          path: 'admin-monitor/log',
          name: 'AdminLog',
          component: () => import('@/views/main/monitor/LogMonitor.vue'),
          meta: { title: '管理端日志管理' }
        },
        // 中间件监测管理
        {
          path: 'middleware/mysql',
          name: 'MySql',
          component: () => import('@/views/main/coming-soon/MySql.vue'),
          meta: { title: 'MySQL管理' }
        },
        {
          path: 'middleware/redis',
          name: 'Redis',
          component: () => import('@/views/main/coming-soon/Redis.vue'),
          meta: { title: 'Redis管理' }
        },
        {
          path: 'middleware/minio',
          name: 'Minio',
          component: () => import('@/views/main/middleware/MiddlewareMonitor.vue'),
          meta: { title: 'MinIO管理' }
        },
        // 网站安全管理
        {
          path: 'security/spring-boot-admin',
          name: 'SpringBootAdmin',
          component: () => import('@/views/main/security/SpringBootAdmin.vue'),
          meta: { title: 'Spring Boot Admin' }
        },
        {
          path: 'security/prometheus',
          name: 'Prometheus',
          component: () => import('@/views/main/security/Prometheus.vue'),
          meta: { title: 'Prometheus' }
        },
        {
          path: 'security/grafana',
          name: 'Grafana',
          component: () => import('@/views/main/security/Grafana.vue'),
          meta: { title: 'Grafana' }
        }
      ]
    },
    // 404
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  // 检查token
  const token = localStorage.getItem('monitor_token')
  if (token) {
    try {
      const parts = token.split('.')
      if (parts.length === 3) {
        const payload = parts[1].replace(/-/g, '+').replace(/_/g, '/')
        const jsonPayload = decodeURIComponent(
          atob(payload + '='.repeat((4 - (payload.length % 4)) % 4))
            .split('')
            .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
            .join('')
        )
        const parsedPayload = JSON.parse(jsonPayload)
        const currentTime = Math.floor(Date.now() / 1000)

        if (parsedPayload.exp && parsedPayload.exp < currentTime) {
          authStore.clearAuth()
          next('/')
          return
        }
      }
    } catch (e) {
      authStore.clearAuth()
      next('/')
      return
    }
  }

  // 已登录访问门户/登录页，跳转首页
  if ((to.path === '/' || to.path === '/login') && authStore.isLoggedIn) {
    next('/main/home')
    return
  }

  // 需要登录的页面
  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    next('/login')
    return
  }

  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 易投简历监测与广告端`
  }

  next()
})

export default router
