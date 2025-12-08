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
          redirect: '/admin/system/links',
          meta: { title: '内部系统' },
          children: [
            {
              path: 'links',
              name: 'SystemLinks',
              component: () => import('@/views/admin/system/SystemLinks.vue'),
              meta: { title: '系统链接' }
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
          component: () => import('@/views/admin/APIDocs.vue'),
          meta: { title: 'API文档中心' }
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
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/404.vue')
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
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