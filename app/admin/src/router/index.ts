import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore, writtenTestManagementPagePermissions } from '@/store/auth'

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
              meta: { title: '管理员管理', permission: '/admin/admin/findByPage' }
            },
            {
              path: 'role',
              name: 'RoleManagement',
              component: () => import('@/views/admin/user/RoleManagement.vue'),
              meta: { title: '角色管理', permission: '/admin/role/findByPage' }
            },
            {
              path: 'permission',
              name: 'PermissionManagement',
              component: () => import('@/views/admin/user/PermissionManagement.vue'),
              meta: { title: '权限管理', permission: ['/admin/permission/findByPage', '/admin/permission/findById', '/admin/permission/add', '/admin/permission/update', '/admin/permission/delete'] }
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
              meta: { title: '求职攻略文章管理', permission: '/admin/jobAdviceArticle/getJobAdviceArticlePage' }
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
              meta: { title: '招聘岗位管理', permission: '/admin/recruitPosition/queryRecruitPositionPage' }
            },
            {
              path: 'information',
              name: 'RecruitmentInformationManagement',
              component: () => import('@/views/admin/recruitment/InformationManagement.vue'),
              meta: { title: '招聘信息管理', permission: '/admin/employmentInformation/getEmploymentInformationPage' }
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
              meta: { title: '简历模版管理', permission: '/admin/resumeTemplate/findResumeTemplateByPage' }
            },
            {
              path: 'system-deleted',
              name: 'SystemResumeManagement',
              component: () => import('@/views/admin/resume/SystemResumeManagement.vue'),
              meta: { title: '系统删除简历管理', permission: '/admin/userDeleteResumeBySystemService/getUserDeleteResumeInfoPage' }
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
              meta: { title: '行业Map管理', permission: '/admin/industryMap/findIndustryMapByPage' }
            },
            {
              path: 'university',
              name: 'UniversityMapManagement',
              component: () => import('@/views/admin/map/UniversityMapManagement.vue'),
              meta: { title: '大学Map管理', permission: '/admin/universityMap/findUniversityMapByPage' }
            },
            {
              path: 'province',
              name: 'ProvinceMapManagement',
              component: () => import('@/views/admin/map/ProvinceMapManagement.vue'),
              meta: { title: '省份Map管理', permission: '/admin/provinceMap/findProvinceMapByPage' }
            },
            {
              path: 'city',
              name: 'CityMapManagement',
              component: () => import('@/views/admin/map/CityMapManagement.vue'),
              meta: { title: '城市Map管理', permission: '/admin/cityMap/findCityMapByPage' }
            },
            {
              path: 'area',
              name: 'AreaMapManagement',
              component: () => import('@/views/admin/map/AreaMapManagement.vue'),
              meta: { title: '区县Map管理', permission: '/admin/areaMap/findAreaMapByPage' }
            },
            {
              path: 'street',
              name: 'StreetMapManagement',
              component: () => import('@/views/admin/map/StreetMapManagement.vue'),
              meta: { title: '街道Map管理', permission: '/admin/streetMap/findStreetMapByPage' }
            }
          ]
        },
        {
          path: 'ai',
          name: 'AIManagement',
          redirect: '/admin/ai/chat',
          meta: { title: 'AI管理' },
          children: [
            {
              path: 'chat',
              name: 'AIChat',
              component: () => import('@/views/admin/ai/AIChat.vue'),
              meta: { title: 'AI智能问答助手', permission: '/admin/aiSystemManagerAssistant/application/chat' }
            },
            {
              path: 'agent',
              name: 'AIAgent',
              component: () => import('@/views/admin/ai/AIAgent.vue'),
              meta: { title: 'AI智能体助手', permission: '/admin/aiSystemManagerAssistant/agent/chat' }
            },
            {
              path: 'llm-utils-info',
              name: 'LlmUtilsInfoManagement',
              component: () => import('@/views/admin/ai/LlmUtilsInfoManagement.vue'),
              meta: { title: 'LLM调用日志管理', permission: '/admin/llmUtilsInfo/getPage' }
            }
          ]
        },
        {
          path: 'help-center',
          name: 'HelpCenterManagement',
          redirect: '/admin/help-center/faq',
          meta: { title: '帮助中心管理' },
          children: [
            {
              path: 'faq',
              name: 'FaqManagement',
              component: () => import('@/views/admin/content/HelpCenterManagement.vue'),
              meta: { title: 'FAQ管理', permission: '/admin/faq/getPage' }
            },
            {
              path: 'customer-service',
              name: 'CustomerServiceManagement',
              component: () => import('@/views/admin/content/HelpCenterManagement.vue'),
              meta: { title: '客服管理', permission: '/admin/customerService/getInfo' }
            },
            {
              path: 'user-guide',
              name: 'UserGuideManagement',
              component: () => import('@/views/admin/content/HelpCenterManagement.vue'),
              meta: { title: '使用指南管理', permission: '/admin/userGuide/getPage' }
            }
          ]
        },
        {
          path: 'written-test',
          name: 'WrittenTestManagement',
          redirect: '/admin/written-test/first-category',
          meta: { title: '笔试专项管理' },
          children: [
            {
              path: 'first-category',
              name: 'WrittenTestFirstCategoryManagement',
              component: () => import('@/views/admin/written-test/WrittenTestManagement.vue'),
              meta: { title: '题库大类管理', permission: writtenTestManagementPagePermissions.firstCategory }
            },
            {
              path: 'second-category',
              name: 'WrittenTestSecondCategoryManagement',
              component: () => import('@/views/admin/written-test/WrittenTestManagement.vue'),
              meta: { title: '题库小类管理', permission: writtenTestManagementPagePermissions.secondCategory }
            },
            {
              path: 'question-bank',
              name: 'WrittenTestQuestionBankManagement',
              component: () => import('@/views/admin/written-test/WrittenTestManagement.vue'),
              meta: { title: '题库题目管理', permission: writtenTestManagementPagePermissions.questionBank }
            },
            {
              path: 'user-answer',
              name: 'WrittenTestUserAnswerManagement',
              component: () => import('@/views/admin/written-test/WrittenTestManagement.vue'),
              meta: { title: '用户答题管理', permission: writtenTestManagementPagePermissions.userAnswer }
            }
          ]
        },
        {
          path: 'score-model',
          name: 'ScoreModelManagement',
          redirect: '/admin/score-model/training-data',
          meta: { title: '评分模型管理' },
          children: [
            {
              path: 'training-data',
              name: 'ScoreModelTrainingDataManagement',
              component: () => import('@/views/admin/score-model/ScoreTrainingDataManagement.vue'),
              meta: { title: '训练数据管理', permission: '/admin/scoreTrainingData/findScoreTrainingDataByPage' }
            },
            {
              path: 'train-code',
              name: 'ScoreModelTrainCodeManagement',
              component: () => import('@/views/admin/score-model/ScoreModelTrainCodeManagement.vue'),
              meta: { title: '训练代码管理', permission: '/admin/scoreModelTrainCode/findScoreModelTrainCodeByPage' }
            },
            {
              path: 'version',
              name: 'ScoreModelVersionManagement',
              component: () => import('@/views/admin/score-model/ScoreModelVersionManagement.vue'),
              meta: { title: '模型版本管理', permission: '/admin/scoreModelVersion/findScoreModelVersionByPage' }
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
              meta: { title: '易投简历用户端', permission: '/admin/internal-system/user-portal' }
            },
            {
              path: 'observation-portal',
              name: 'ObservationPortal',
              component: () => import('@/views/admin/system/ObservationPortal.vue'),
              meta: { title: '易投简历观测与广告端', permission: '/admin/internal-system/observation-portal' }
            },
            {
              path: 'nacos-platform',
              name: 'NacosPlatform',
              component: () => import('@/views/admin/system/NacosPlatform.vue'),
              meta: { title: 'Nacos配置平台', permission: '/admin/internal-system/nacos-platform' }
            },
            {
              path: 'yapi-platform',
              name: 'YApiPlatformInternal',
              component: () => import('@/views/admin/yapi/YApiEmbed.vue'),
              meta: { title: 'YApi测试平台', permission: '/admin/internal-system/yapi-platform' }
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
              meta: { title: '用户端反馈管理', permission: '/admin/userFeedback/getFeedbackPage' }
            },
            {
              path: 'management',
              name: 'FeedbackManage',
              component: () => import('@/views/admin/feedback/FeedbackManage.vue'),
              meta: { title: '管理端反馈管理', permission: '/admin/feedback/getFeedbackPage' }
            },
            {
              path: 'user-records',
              name: 'UserFeedbackRecords',
              component: () => import('@/views/admin/feedback/UserFeedbackRecords.vue'),
              meta: { title: '用户端反馈记录', permission: '/admin/userfeedbackRecord/findUserFeedbackRecordPage' }
            },
            {
              path: 'records',
              name: 'FeedbackRecords',
              component: () => import('@/views/admin/feedback/FeedbackRecords.vue'),
              meta: { title: '管理端反馈记录', permission: '/admin/adminFeedbackRecord/findAdminFeedbackRecordPage' }
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
            },
            {
              path: 'baidu-cloud',
              name: 'BaiduCloudAPI',
              component: () => import('@/views/admin/external-api/BaiduCloudAPI.vue'),
              meta: { title: '百度智能云平台' }
            },
            {
              path: 'qiniu',
              name: 'QiniuCloudAPI',
              component: () => import('@/views/admin/external-api/QiniuCloudAPI.vue'),
              meta: { title: '七牛云平台' }
            },
            {
              path: 'autodl',
              name: 'AutoDLAPI',
              component: () => import('@/views/admin/external-api/AutoDLAPI.vue'),
              meta: { title: 'AutoDL平台' }
            },
            {
              path: 'bigmodel',
              name: 'BigModelAPI',
              component: () => import('@/views/admin/external-api/BigModelAPI.vue'),
              meta: { title: '智谱开放平台' }
            },
            {
              path: 'volcengine',
              name: 'VolcengineAPI',
              component: () => import('@/views/admin/external-api/VolcengineAPI.vue'),
              meta: { title: '火山引擎平台' }
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
          meta: { title: '项目介绍', permission: '/admin/projectIntroduce/getInfo' }
        },
        {
          path: 'about-us/team-introduce',
          name: 'TeamIntroduceManagement',
          component: () => import('@/views/admin/content/AboutUsManagement.vue'),
          meta: { title: '团队介绍', permission: '/admin/teamIntroduce/getInfo' }
        },
        {
          path: 'about-us/develop-history',
          name: 'DevelopHistoryManagement',
          component: () => import('@/views/admin/content/AboutUsManagement.vue'),
          meta: { title: '发展历程', permission: '/admin/developHistory/getInfo' }
        },
        {
          path: 'about-us/join-us',
          name: 'JoinUsManagement',
          component: () => import('@/views/admin/content/AboutUsManagement.vue'),
          meta: { title: '加入我们', permission: '/admin/joinUs/getInfo' }
        },
        {
          path: 'about-us/partner-introduce',
          name: 'PartnerIntroduceManagement',
          component: () => import('@/views/admin/content/AboutUsManagement.vue'),
          meta: { title: '合作伙伴', permission: '/admin/partnerIntroduce/getInfo' }
        },
        {
          path: 'about-us/media-report',
          name: 'MediaReportManagement',
          component: () => import('@/views/admin/content/AboutUsManagement.vue'),
          meta: { title: '媒体报道', permission: '/admin/mediaReport/getInfo' }
        },
        {
          path: 'help-center',
          redirect: '/admin/help-center/faq'
        },
        {
          path: 'help-center/faq',
          name: 'FaqManagement',
          component: () => import('@/views/admin/content/HelpCenterManagement.vue'),
          meta: { title: 'FAQ管理', permission: '/admin/faq/getPage' }
        },
        {
          path: 'help-center/customer-service',
          name: 'CustomerServiceManagement',
          component: () => import('@/views/admin/content/HelpCenterManagement.vue'),
          meta: { title: '客服管理', permission: '/admin/customerService/getInfo' }
        },
        {
          path: 'help-center/user-guide',
          name: 'UserGuideManagement',
          component: () => import('@/views/admin/content/HelpCenterManagement.vue'),
          meta: { title: '使用指南管理', permission: '/admin/userGuide/getPage' }
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
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  const token = localStorage.getItem('admin_token')

  // 本地存在 token 时同步登录态，避免因前端自行解析 token 而误登出
  if (token && !authStore.isLoggedIn) {
    authStore.token = token
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
    next('/403')
    return
  }

  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 易投简历管理平台`
  }

  next()
})

export default router