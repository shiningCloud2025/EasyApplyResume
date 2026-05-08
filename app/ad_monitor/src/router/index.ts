import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore, announcementManagementPermissions, imageAdvertisementManagementPermissions, userWebsiteManagementPermissions, adminWebsiteManagementPermissions, middlewareManagementPermissions, serviceMachineManagementPermissions } from '@/store/auth'

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
          meta: { title: '管理端公告管理', permission: announcementManagementPermissions.admin.getInfo }
        },
        {
          path: 'notice/user',
          name: 'NoticeUser',
          component: () => import('@/views/main/notice/NoticeManage.vue'),
          meta: { title: '用户端公告管理', permission: announcementManagementPermissions.user.getInfo }
        },
        {
          path: 'notice/monitor',
          name: 'NoticeMonitor',
          component: () => import('@/views/main/notice/NoticeManage.vue'),
          meta: { title: '监测端公告管理', permission: announcementManagementPermissions.monitor.getInfo }
        },
        // 图片广告管理
        {
          path: 'ad/image/admin',
          name: 'ImageAdAdmin',
          component: () => import('@/views/main/ad/ImageAdManage.vue'),
          meta: { title: '管理端图片广告', permission: imageAdvertisementManagementPermissions.admin.getByPage }
        },
        {
          path: 'ad/image/user',
          name: 'ImageAdUser',
          component: () => import('@/views/main/ad/ImageAdManage.vue'),
          meta: { title: '用户端图片广告', permission: imageAdvertisementManagementPermissions.user.getByPage }
        },
        {
          path: 'ad/image/monitor',
          name: 'ImageAdMonitor',
          component: () => import('@/views/main/ad/ImageAdManage.vue'),
          meta: { title: '监测端图片广告', permission: imageAdvertisementManagementPermissions.monitor.getByPage }
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
          meta: {
            title: '用户端网站管理',
            permission: [
              userWebsiteManagementPermissions.visitTrend,
              userWebsiteManagementPermissions.userTrend
            ]
          }
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
          meta: {
            title: '管理端网站管理',
            permission: [
              adminWebsiteManagementPermissions.visitTrend,
              adminWebsiteManagementPermissions.adminTrend
            ]
          }
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
          meta: { title: 'MinIO管理', permission: middlewareManagementPermissions.minio }
        },
        // 服务器管理
        {
          path: 'server/manage',
          name: 'ServerManage',
          component: () => import('@/views/main/server/ServiceMachineManage.vue'),
          meta: { title: '设备管理', permission: serviceMachineManagementPermissions.getByPage }
        },
        {
          path: 'server/monitor',
          name: 'ServerMonitor',
          component: () => import('@/views/main/server/ServiceMachineMonitor.vue'),
          meta: { title: '设备监控' }
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
        },
        // 内部系统
        {
          path: 'system/user-portal',
          name: 'SystemUserPortal',
          component: () => import('@/views/main/security/UserPortal.vue'),
          meta: { title: '易投简历用户端' }
        },
        {
          path: 'system/observation-portal',
          name: 'SystemObservationPortal',
          component: () => import('@/views/main/security/ObservationPortal.vue'),
          meta: { title: '易投简历监测与广告端' }
        },
        {
          path: 'system/nacos-platform',
          name: 'SystemNacosPlatform',
          component: () => import('@/views/main/security/NacosPlatform.vue'),
          meta: { title: 'Nacos配置平台' }
        },
        {
          path: 'system/yapi-platform',
          name: 'SystemYApiPlatform',
          component: () => import('@/views/main/security/YApiEmbed.vue'),
          meta: { title: 'YApi测试平台' }
        },
        // 外部系统
        {
          path: 'external-platform/bailian',
          name: 'ExternalBailianPlatform',
          component: () => import('@/views/main/security/BailianAPI.vue'),
          meta: { title: '阿里云百炼平台' }
        },
        {
          path: 'external-platform/sms',
          name: 'ExternalSmsPlatform',
          component: () => import('@/views/main/security/SmsAPI.vue'),
          meta: { title: '阿里云短信平台' }
        },
        {
          path: 'external-platform/searchapi',
          name: 'ExternalSearchAPIPlatform',
          component: () => import('@/views/main/security/SearchAPI.vue'),
          meta: { title: 'SearchAPI平台' }
        },
        {
          path: 'external-platform/amap',
          name: 'ExternalAmapPlatform',
          component: () => import('@/views/main/security/AmapAPI.vue'),
          meta: { title: '高德开放平台' }
        },
        {
          path: 'external-platform/baidu-cloud',
          name: 'ExternalBaiduCloudPlatform',
          component: () => import('@/views/main/security/BaiduCloudAPI.vue'),
          meta: { title: '百度智能云平台' }
        },
        {
          path: 'external-platform/qiniu',
          name: 'ExternalQiniuPlatform',
          component: () => import('@/views/main/security/QiniuCloudAPI.vue'),
          meta: { title: '七牛云平台' }
        },
        {
          path: 'external-platform/autodl',
          name: 'ExternalAutoDLPlatform',
          component: () => import('@/views/main/security/AutoDLAPI.vue'),
          meta: { title: 'AutoDL平台' }
        },
        {
          path: 'external-platform/bigmodel',
          name: 'ExternalBigModelPlatform',
          component: () => import('@/views/main/security/BigModelAPI.vue'),
          meta: { title: '智谱开放平台' }
        },
        {
          path: 'external-platform/volcengine',
          name: 'ExternalVolcenginePlatform',
          component: () => import('@/views/main/security/VolcengineAPI.vue'),
          meta: { title: '火山引擎平台' }
        },
        // API文档中心
        {
          path: 'api-docs/external',
          name: 'ExternalAPIDocs',
          component: () => import('@/views/main/security/APIDocs.vue'),
          meta: { title: 'API对外文档中心' }
        },
        {
          path: 'api-docs/internal',
          name: 'InternalAPIDocs',
          component: () => import('@/views/main/security/APIDocs.vue'),
          meta: { title: 'API对内文档中心' }
        },
        // 个人中心
        {
          path: 'profile',
          name: 'Profile',
          component: () => import('@/views/main/Profile.vue'),
          meta: { title: '个人中心' }
        },
        // 意见反馈
        {
          path: 'feedback/submit',
          name: 'FeedbackSubmit',
          component: () => import('@/views/main/FeedbackSubmit.vue'),
          meta: { title: '意见反馈' }
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
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  const token = localStorage.getItem('monitor_token')

  // 本地存在 token 时同步登录态，避免因前端自行解析 token 而误登出
  if (token && !authStore.isLoggedIn) {
    authStore.setToken(token)
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

  const requiredPermission = typeof to.meta.permission === 'string' || Array.isArray(to.meta.permission)
    ? to.meta.permission
    : undefined

  if (requiredPermission && authStore.isLoggedIn && !authStore.user) {
    await authStore.getUserInfo(true)

    if (!authStore.isLoggedIn) {
      next('/login')
      return
    }
  }

  if (requiredPermission && !authStore.canAccessRoute(requiredPermission)) {
    next('/main/home')
    return
  }

  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 易投简历监测与广告端`
  }

  next()
})

export default router
