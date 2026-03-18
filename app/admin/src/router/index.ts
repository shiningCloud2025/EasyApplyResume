import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: () => import('@/views/Welcome.vue')
    },
    {
      path: '/login',
      component: () => import('@/views/auth/Login.vue')
    },
    {
      path: '/register',
      component: () => import('@/views/auth/Register.vue')
    },
    {
      path: '/admin',
      component: () => import('@/layout/AdminLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/admin/dashboard'
        },
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('@/views/admin/Dashboard.vue'),
          meta: { title: '首页' }
        },
        {
          path: 'user',
          name: 'UserManagement',
          redirect: '/admin/user/admin',
          meta: { title: '用户管理' },
          children: [
            {
              path: 'admin',
              name: 'AdminManagement',
              component: () => import('@/views/admin/user/AdminManagement.vue'),
              meta: { title: '管理员管理' }
            },
            {
              path: 'role',
              name: 'RoleManagement',
              component: () => import('@/views/admin/user/RoleManagement.vue'),
              meta: { title: '角色管理' }
            },
            {
              path: 'permission',
              name: 'PermissionManagement',
              component: () => import('@/views/admin/user/PermissionManagement.vue'),
              meta: { title: '权限管理' }
            }
          ]
        },
        {
          path: 'article',
          name: 'ArticleManagement',
          redirect: '/admin/article/job-advice',
          meta: { title: '文章管理' },
          children: [
            {
              path: 'job-advice',
              name: 'JobAdviceManagement',
              component: () => import('@/views/admin/article/JobAdviceManagement.vue'),
              meta: { title: '求职攻略文章管理' }
            }
          ]
        },
        {
          path: 'recruitment',
          name: 'RecruitmentManagement',
          redirect: '/admin/recruitment/positions',
          meta: { title: '招聘管理' },
          children: [
            {
              path: 'positions',
              name: 'RecruitmentPositionManagement',
              component: () => import('@/views/admin/recruitment/PositionManagement.vue'),
              meta: { title: '招聘岗位管理' }
            },
            {
              path: 'information',
              name: 'RecruitmentInformationManagement',
              component: () => import('@/views/admin/recruitment/InformationManagement.vue'),
              meta: { title: '招聘信息管理' }
            }
          ]
        },
        {
          path: 'resume',
          name: 'ResumeManagement',
          redirect: '/admin/resume/template',
          meta: { title: '简历管理' },
          children: [
            {
              path: 'template',
              name: 'ResumeTemplateManagement',
              component: () => import('@/views/admin/resume/TemplateManagement.vue'),
              meta: { title: '简历模版管理' }
            },
            {
              path: 'system-deleted',
              name: 'SystemResumeManagement',
              component: () => import('@/views/admin/resume/SystemResumeManagement.vue'),
              meta: { title: '系统删除简历管理' }
            }
          ]
        },
        {
          path: 'map',
          name: 'MapManagement',
          redirect: '/admin/map/industry',
          meta: { title: 'Map管理' },
          children: [
            {
              path: 'industry',
              name: 'IndustryMapManagement',
              component: () => import('@/views/admin/map/IndustryMapManagement.vue'),
              meta: { title: '行业Map管理' }
            }
          ]
        },
        {
          path: 'ai',
          name: 'AIAssistant',
          redirect: '/admin/ai/chat',
          meta: { title: 'AI助手' },
          children: [
            {
              path: 'chat',
              name: 'AIChat',
              component: () => import('@/views/admin/ai/AIChat.vue'),
              meta: { title: 'AI智能问答助手' }
            },
            {
              path: 'agent',
              name: 'AIAgent',
              component: () => import('@/views/admin/ai/AIAgent.vue'),
              meta: { title: 'AI智能体助手' }
            }
          ]
        },
        {
          path: 'system',
          name: 'SystemManagement',
          redirect: '/admin/system/user-portal',
          meta: { title: '内部系统' },
          children: [
            {
              path: 'user-portal',
              name: 'UserPortal',
              component: () => import('@/views/admin/system/UserPortal.vue'),
              meta: { title: '易投简历用户端' }
            },
            {
              path: 'observation-portal',
              name: 'ObservationPortal',
              component: () => import('@/views/admin/system/ObservationPortal.vue'),
              meta: { title: '易投简历观测与广告端' }
            }
          ]
        },
        {
          path: 'feedback',
          name: 'FeedbackManagement',
          redirect: '/admin/feedback/user-management',
          meta: { title: '反馈管理' },
          children: [
            {
              path: 'submit',
              name: 'FeedbackSubmit',
              component: () => import('@/views/admin/feedback/FeedbackSubmit.vue'),
              meta: { title: '意见反馈' }
            },
            {
              path: 'user-management',
              name: 'UserFeedbackManage',
              component: () => import('@/views/admin/feedback/UserFeedbackManage.vue'),
              meta: { title: '用户端反馈管理' }
            },
            {
              path: 'management',
              name: 'FeedbackManage',
              component: () => import('@/views/admin/feedback/FeedbackManage.vue'),
              meta: { title: '管理端反馈管理' }
            },
            {
              path: 'user-records',
              name: 'UserFeedbackRecords',
              component: () => import('@/views/admin/feedback/UserFeedbackRecords.vue'),
              meta: { title: '用户端反馈记录' }
            },
            {
              path: 'records',
              name: 'FeedbackRecords',
              component: () => import('@/views/admin/feedback/FeedbackRecords.vue'),
              meta: { title: '管理端反馈记录' }
            }
          ]
        },
        {
          path: 'api-docs',
          name: 'APIDocs',
          redirect: '/admin/api-docs/external',
          meta: { title: 'API文档中心' },
          children: [
            {
              path: 'external',
              name: 'ExternalAPIDocs',
              component: () => import('@/views/admin/APIDocs.vue'),
              meta: { title: 'API对外文档中心' }
            },
            {
              path: 'internal',
              name: 'InternalAPIDocs',
              component: () => import('@/views/admin/APIDocs.vue'),
              meta: { title: 'API对内文档中心' }
            }
          ]
        },
        {
          path: 'external-api',
          name: 'ExternalAPI',
          redirect: '/admin/external-api/bailian',
          meta: { title: '外部API' },
          children: [
            {
              path: 'bailian',
              name: 'BailianAPI',
              component: () => import('@/views/admin/external-api/BailianAPI.vue'),
              meta: { title: '阿里云百炼平台' }
            },
            {
              path: 'sms',
              name: 'SmsAPI',
              component: () => import('@/views/admin/external-api/SmsAPI.vue'),
              meta: { title: '阿里云短信平台' }
            },
            {
              path: 'searchapi',
              name: 'SearchAPI',
              component: () => import('@/views/admin/external-api/SearchAPI.vue'),
              meta: { title: 'SearchAPI平台' }
            },
            {
              path: 'amap',
              name: 'AmapAPI',
              component: () => import('@/views/admin/external-api/AmapAPI.vue'),
              meta: { title: '高德开放平台' }
            }
          ]
        },
        {
          path: 'about-us',
          redirect: '/admin/about-us/project-introduce'
        },
        {
          path: 'about-us/project-introduce',
          name: 'ProjectIntroduceManagement',
          component: () => import('@/views/admin/content/AboutUsManagement.vue'),
          meta: { title: '项目介绍' }
        },
        {
          path: 'about-us/team-introduce',
          name: 'TeamIntroduceManagement',
          component: () => import('@/views/admin/content/AboutUsManagement.vue'),
          meta: { title: '团队介绍' }
        },
        {
          path: 'about-us/develop-history',
          name: 'DevelopHistoryManagement',
          component: () => import('@/views/admin/content/AboutUsManagement.vue'),
          meta: { title: '发展历程' }
        },
        {
          path: 'about-us/join-us',
          name: 'JoinUsManagement',
          component: () => import('@/views/admin/content/AboutUsManagement.vue'),
          meta: { title: '加入我们' }
        },
        {
          path: 'about-us/partner-introduce',
          name: 'PartnerIntroduceManagement',
          component: () => import('@/views/admin/content/AboutUsManagement.vue'),
          meta: { title: '合作伙伴' }
        },
        {
          path: 'about-us/media-report',
          name: 'MediaReportManagement',
          component: () => import('@/views/admin/content/AboutUsManagement.vue'),
          meta: { title: '媒体报道' }
        },
        {
          path: 'help-center',
          redirect: '/admin/help-center/faq'
        },
        {
          path: 'help-center/faq',
          name: 'FaqManagement',
          component: () => import('@/views/admin/content/HelpCenterManagement.vue'),
          meta: { title: 'FAQ管理' }
        },
        {
          path: 'help-center/customer-service',
          name: 'CustomerServiceManagement',
          component: () => import('@/views/admin/content/HelpCenterManagement.vue'),
          meta: { title: '客服管理' }
        },
        {
          path: 'help-center/user-guide',
          name: 'UserGuideManagement',
          component: () => import('@/views/admin/content/HelpCenterManagement.vue'),
          meta: { title: '使用指南管理' }
        },
        {
          path: 'profile',
          name: 'Profile',
          component: () => import('@/views/Profile.vue'),
          meta: { title: '个人中心' }
        }
      ]
    },
    {
      path: '/403',
      name: 'Forbidden',
      component: () => import('@/views/403.vue')
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/404.vue')
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  // 检查token是否过期
  const token = localStorage.getItem('admin_token')
  if (token) {
    try {
      // 解析JWT token检查是否过期
      const parts = token.split('.')
      if (parts.length !== 3) {
        throw new Error('Invalid token format')
      }

      // Base64 URL解码
      const payload = parts[1].replace(/-/g, '+').replace(/_/g, '/')
      const jsonPayload = decodeURIComponent(atob(payload + '='.repeat((4 - payload.length % 4) % 4)).split('').map((c) => {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
      }).join(''))

      const parsedPayload = JSON.parse(jsonPayload)
      const currentTime = Math.floor(Date.now() / 1000)

      if (parsedPayload.exp && parsedPayload.exp < currentTime) {
        // Token已过期，清除认证状态并跳转登录
        authStore.clearAuth()
        next('/login')
        return
      }
    } catch (parseError) {
      // Token格式错误，清除认证状态
      authStore.clearAuth()
      next('/login')
      return
    }
  }

  // 如果已登录且访问登录页，跳转到首页
  if (to.path === '/login' && authStore.isLoggedIn) {
    next('/admin/dashboard')
    return
  }

  // 需要登录的页面
  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    next('/login')
    return
  }

  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 易投简历管理平台`
  }

  next()
})

export default router