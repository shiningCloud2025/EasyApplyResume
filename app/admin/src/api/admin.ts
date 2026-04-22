import axios from 'axios'
import { api } from '@/utils/request'
import request from '@/utils/request'
import type { AdminUser, LoginForm, PhoneLoginForm, EmailLoginForm } from '@/store/auth'
import type { 
  PageQuery, 
  PageResult,
  AdminPageQuery,
  AdminPageVO,
  AdminInfoVO,
  AdminForm,
  AdminFeedbackForm,
  AdminFeedbackQuery,
  AdminFeedbackPageVO,
  AdminFeedbackInfoVO,
  AdminFeedbackRecordQuery,
  AdminFeedbackRecordPageVO,
  AdminFeedbackRecordInfoVO,
  UserFeedbackQuery,
  UserFeedbackPageVO,
  UserFeedbackInfoVO,
  UserUpdateFeedbackForm,
  RoleForm,
  RolePageQuery,
  RolePageVO,
  RoleInfoVO,
  PermissionForm,
  PermissionPageQuery,
  PermissionPageVO,
  PermissionInfoVO,
  ResumeTemplateForm,
  ResumeTemplateQuery,
  ResumeTemplatePageVO,
  ResumeTemplateInfoVO,
  JobAdviceArticleForm,
  JobAdviceArticleQuery,
  RecruitPositionForm,
  RecruitPositionQuery,
  RecruitPositionPageVO,
  RecruitPositionInfoVO,
  EmploymentInformationForm,
  EmploymentInformationQuery,
  EmploymentInformationPageVO,
  EmploymentInformationInfoVO,
  IndustryMapForm,
  IndustryMapQuery,
  IndustryMapPageVO,
  UniversityMap,
  UniversityMapQuery,
  UniversityMapPageVO,
  UniversityMapInfoVO,
  ProvinceMap,
  ProvinceMapQuery,
  ProvinceMapPageVO,
  ProvinceMapInfoVO,
  CityMap,
  CityMapQuery,
  CityMapPageVO,
  CityMapInfoVO,
  AreaMap,
  AreaMapQuery,
  AreaMapPageVO,
  AreaMapInfoVO,
  StreetMap,
  StreetMapQuery,
  StreetMapPageVO,
  StreetMapInfoVO,
  UserDeleteResumeQuery,
  UserDeleteResumeBySystemPageVO,
  UserDeleteResumeBySystemInfoVO,
  UserDeleteResumeInfoVO,
  AdminScoreTrainingDataQuery,
  AdminScoreTrainingDataForm,
  AdminScoreTrainingDataPageVO,
  AdminScoreTrainingDataInfoVO,
  AdminLlmUtilsInfoQuery,
  AdminLlmUtilsInfoPageVO,
  AdminLlmUtilsInfoVO
} from '@/types/admin'
import type {
  ProjectIntroduceForm,
  ProjectIntroduceInfoVO,
  TeamIntroduceForm,
  TeamIntroduceInfoVO,
  DevelopHistoryForm,
  DevelopHistoryInfoVO,
  JoinUsForm,
  JoinUsInfoVO,
  PartnerIntroduceForm,
  PartnerIntroduceInfoVO,
  MediaReportForm,
  MediaReportInfoVO,
  CustomerServiceForm,
  CustomerServiceInfoVO,
  FaqForm,
  FaqQuery,
  FaqPageVO,
  FaqInfoVO,
  UserGuideForm,
  UserGuideQuery,
  UserGuidePageVO,
  UserGuideInfoVO
} from '@/types/admin'

// 认证相关API
export const authApi = {
  // 账号密码登录
  login: (data: LoginForm) => api.post<string>('/admin/auth/formalLogin', null, { params: data }),
  
  // 手机验证码登录
  loginByPhone: (data: PhoneLoginForm) => api.post<string>('/admin/auth/phoneLogin', null, { params: data }),
  
  // 邮箱验证码登录
  loginByEmail: (data: EmailLoginForm) => api.post<string>('/admin/auth/emailLogin', null, { params: data }),
  
  // 退出登录
  logout: (adminId: number) => api.get('/admin/admin/logout', { params: { adminId } }),
  
  // 发送邮箱验证码
  sendEmailCode: (email: string) => api.post('/admin/email/loginandregister/send', null, { params: { email } }),
  
  // 校验邮箱验证码
  checkEmailCode: (email: string, code: string) => api.post('/admin/email/loginandregister/check', null, { params: { email, code } })
}

// 管理员相关API
export const adminApi = {
  // 新增管理员
  addAdmin: (data: AdminForm) => api.post<number>('/admin/admin/add', data),
  
  // 修改管理员
  updateAdmin: (data: AdminForm) => api.post<number>('/admin/admin/update', data),
  
  // 删除管理员
  deleteAdmin: (adminId: number) => api.delete<number>('/admin/admin/delete', { params: { adminId } }),
  
  // 查询管理员详情
  getAdminInfo: (adminId: number) => api.get<AdminInfoVO>(`/admin/admin/findById?adminId=${adminId}`),
  
  // 获取当前登录管理员信息（通过 JWT）
  getCurrentAdminInfo: () => api.post<{userId: number, userEmail: string, username: string, authorities: string[]}>('/admin/auth/getAdminInfo'),
  
  // 分页查询管理员
  getAdminPage: (pageNum: number, pageSize: number, query: AdminPageQuery) => 
    api.post<PageResult<AdminPageVO>>('/admin/admin/findByPage', query, { 
      params: { pageNum, pageSize } 
    }),
  
  // 查看管理员拥有的角色
  getAdminRoles: (adminId: number) => api.get(`/admin/admin/findRoleByAdmin?adminId=${adminId}`),
  
  // 为管理员分配角色
  assignRoleToAdmin: (adminId: number, roleIds: number[]) => {
    let params = `adminId=${adminId}`
    if (roleIds.length > 0) {
      roleIds.forEach(id => {
        params += `&roleIds=${id}`
      })
    } else {
      // 如果没有角色，传一个空数组
      params += '&roleIds='
    }
    return api.post<number>(`/admin/admin/assignRoleToAdmin?${params}`)
  },
  
  // 生成随机账号
  generateRandomAccount: () => api.get<string>('/admin/admin/generateRandomAccount')
}

// 角色相关API
export const roleApi = {
  // 新增角色
  addRole: (data: RoleForm) => api.post<number>('/admin/role/add', data),
  
  // 修改角色
  updateRole: (data: RoleForm) => api.post<number>('/admin/role/update', data),
  
  // 删除角色
  deleteRole: (roleId: number) => api.delete<number>('/admin/role/delete', { params: { roleId } }),
  
  // 查询角色详情
  getRoleInfo: (roleId: number) => api.get<RoleInfoVO>(`/admin/role/findById?roleId=${roleId}`),
  
  // 查询所有角色
  getRolePage: (pageNum: number, pageSize: number, query: RolePageQuery) => 
    api.post<PageResult<RolePageVO>>('/admin/role/findByPage', query, { 
      params: { pageNum, pageSize } 
    }),
  
  // 查询所有角色
  getAllRoles: () => api.get('/admin/role/findAllRole'),
  
  // 查看角色拥有的权限
  getRolePermissions: (roleId: number) => api.get(`/admin/role/findPermissionByRole?roleId=${roleId}`),
  
  // 为角色分配权限
  assignPermissionToRole: (roleId: number, permissionIds: number[]) => 
    api.post<number>('/admin/role/assignPermissionToRole', null, { params: { roleId, permissionIds } })
}

// 权限相关API
export const permissionApi = {
  // 新增权限
  addPermission: (data: PermissionForm) => api.post<number>('/admin/permission/add', data),
  
  // 修改权限
  updatePermission: (data: PermissionForm) => api.post<number>('/admin/permission/update', data),
  
  // 删除权限
  deletePermission: (permissionId: number) => api.get<number>(`/admin/permission/delete?permissionId=${permissionId}`),
  
  // 查询权限详情
  getPermissionInfo: (permissionId: number) => api.get<PermissionInfoVO>(`/admin/permission/findById?permissionId=${permissionId}`),
  
  // 分页查询权限
  getPermissionPage: (pageNum: number, pageSize: number, query: PermissionPageQuery) => 
    api.post<PageResult<PermissionPageVO>>('/admin/permission/findByPage', query, { 
      params: { pageNum, pageSize } 
    }),
  
  // 查询所有权限
  getAllPermissions: () => api.get<PermissionInfoVO[]>('/admin/permission/findAll')
}

// 简历模板相关API
export const resumeTemplateApi = {
  // 新增简历模板
  addResumeTemplate: (data: ResumeTemplateForm) => api.post<number>('/admin/resumeTemplate/addResumeTemplate', data),
  
  // 修改简历模板
  updateResumeTemplate: (data: ResumeTemplateForm) => api.post<number>('/admin/resumeTemplate/updateResumeTemplate', data),
  
  // 删除简历模板
  deleteResumeTemplate: (resumeTemplateId: number) => 
    api.delete<number>('/admin/resumeTemplate/deleteResumeTemplate', { params: { resumeTemplateId } }),
  
  // 查询简历模板详情
  getResumeTemplateInfo: (resumeTemplateId: number) => 
    api.get<ResumeTemplateInfoVO>('/admin/resumeTemplate/findResumeTemplateById', { params: { resumeTemplateId } }),
  
  // 分页查询简历模板
  getResumeTemplatePage: (pageNum: number, pageSize: number, query: ResumeTemplateQuery) => 
    api.post<PageResult<ResumeTemplatePageVO>>('/admin/resumeTemplate/findResumeTemplateByPage', query, { 
      params: { pageNum, pageSize } 
    }),
  
  // 查询所有简历模板
  getAllResumeTemplates: () => api.get<ResumeTemplatePageVO[]>('/admin/resumeTemplate/findAllResumeTemplate')
}

// 求职攻略文章相关API
export const jobAdviceArticleApi = {
  // 新增求职攻略
  addJobAdviceArticle: (data: JobAdviceArticleForm) => api.post<number>('/admin/jobAdviceArticle/addJobAdviceArticle', data),
  
  // 修改求职攻略
  updateJobAdviceArticle: (data: JobAdviceArticleForm) => api.post<number>('/admin/jobAdviceArticle/updateJobAdviceArticle', data),
  
  // 删除求职攻略
  deleteJobAdviceArticle: (jobAdviceArticleId: number) => 
    api.delete<number>('/admin/jobAdviceArticle/deleteJobAdviceArticle', { params: { jobAdviceArticleId } }),
  
  // 查询求职攻略详情
  getJobAdviceArticleInfo: (jobAdviceArticleId: number) => 
    api.get<any>('/admin/jobAdviceArticle/getJobAdviceArticleInfo', { params: { jobAdviceArticleId } }),
  
  // 分页查询求职攻略
  getJobAdviceArticlePage: (pageNum: number, pageSize: number, query: JobAdviceArticleQuery) => 
    api.post<any>('/admin/jobAdviceArticle/getJobAdviceArticlePage', query, { params: { pageNum, pageSize } }),
  
  // 查询所有求职攻略
  getAllJobAdviceArticles: () => api.get<any[]>('/admin/jobAdviceArticle/getAllJobAdviceArticle')
}

// 招聘岗位相关API
export const recruitPositionApi = {
  // 新增招聘岗位
  addRecruitPosition: (data: RecruitPositionForm) => api.post<number>('/admin/recruitPosition/addRecruitPosition', data),
  
  // 修改招聘岗位
  updateRecruitPosition: (data: RecruitPositionForm) => api.post<number>('/admin/recruitPosition/updateRecruitPosition', data),
  
  // 删除招聘岗位
  deleteRecruitPosition: (recruitPositionId: number) => 
    api.delete<number>('/admin/recruitPosition/deleteRecruitPosition', { params: { recruitPositionId } }),
  
  // 查询招聘岗位详情
  getRecruitPositionInfo: (recruitPositionId: number) => 
    api.get<RecruitPositionInfoVO>('/admin/recruitPosition/queryRecruitPosition', { params: { recruitPositionId } }),
  
  // 分页查询招聘岗位
  getRecruitPositionPage: (pageNum: number, pageSize: number, query: RecruitPositionQuery) => 
    api.post<PageResult<RecruitPositionPageVO>>('/admin/recruitPosition/queryRecruitPositionPage', query, { 
      params: { pageNum, pageSize } 
    }),
  
  // 查询所有招聘岗位
  getAllRecruitPositions: () => api.get<RecruitPositionInfoVO[]>('/admin/recruitPosition/queryAllRecruitPositionPage')
}

// 招聘信息相关API
export const employmentInformationApi = {
  // 新增招聘信息
  addEmploymentInformation: (data: EmploymentInformationForm) => api.post<number>('/admin/employmentInformation/addEmploymentInformation', data),
  
  // 修改招聘信息
  updateEmploymentInformation: (data: EmploymentInformationForm) => api.post<number>('/admin/employmentInformation/updateEmploymentInformation', data),
  
  // 删除招聘信息
  deleteEmploymentInformation: (employmentInformationId: number) =>
    api.delete<number>('/admin/employmentInformation/deleteEmploymentInformation', { employmentInformationId }),
  
  // 查询招聘信息详情
  getEmploymentInformationInfo: (employmentInformationId: number) =>
    api.get<EmploymentInformationInfoVO>('/admin/employmentInformation/getEmploymentInformationInfo', { employmentInformationId }),
  
  // 分页查询招聘信息
  getEmploymentInformationPage: (pageNum: number, pageSize: number, query: EmploymentInformationQuery) => 
    api.post<PageResult<EmploymentInformationPageVO>>('/admin/employmentInformation/getEmploymentInformationPage', query, { 
      params: { pageNum, pageSize } 
    }),
  
  // 查询所有招聘信息
  getAllEmploymentInformation: () => api.get<string[]>('/admin/employmentInformation/getAllEmploymentInformation')
}

// 行业Map相关API
export const industryMapApi = {
  // 新增行业
  addIndustryMap: (data: IndustryMapForm) => api.post<number>('/admin/industryMap/addIndustryMap', data),
  
  // 修改行业
  updateIndustryMap: (data: IndustryMapForm) => api.post<number>('/admin/industryMap/updateIndustryMap', data),
  
  // 查询行业详情
  getIndustryMapInfo: (industryMapIndustryCode: number) => 
    api.get<IndustryMapInfoVO>('/admin/industryMap/findIndustryMapById', { params: { industryMapId: industryMapIndustryCode } }),
  
  // 分页查询行业
  getIndustryMapPage: (pageNum: number, pageSize: number, query: IndustryMapQuery) => 
    api.post<PageResult<IndustryMapPageVO>>('/admin/industryMap/findIndustryMapByPage', query, { 
      params: { pageNum, pageSize } 
    }),
  
  // 查询所有行业
  findAllIndustryMap: () => api.get<IndustryMapInfoVO[]>('/admin/industryMap/findAllIndustryMap')
}

// Map相关API
export const universityMapApi = {
  getUniversityMapPage: (pageNum: number, pageSize: number, query: UniversityMapQuery) =>
    api.post<PageResult<UniversityMapPageVO>>('/admin/universityMap/findUniversityMapByPage', query, {
      params: { pageNum, pageSize }
    }),
  getUniversityMapInfo: (universityMapId: number) =>
    api.get<UniversityMapInfoVO>('/admin/universityMap/findUniversityMapById', { universityMapId })
}

export const provinceMapApi = {
  getProvinceMapPage: (pageNum: number, pageSize: number, query: ProvinceMapQuery) =>
    api.post<PageResult<ProvinceMapPageVO>>('/admin/provinceMap/findProvinceMapByPage', query, {
      params: { pageNum, pageSize }
    }),
  getProvinceMapInfo: (provinceMapId: number) =>
    api.get<ProvinceMapInfoVO>('/admin/provinceMap/findProvinceMapById', { provinceMapId })
}

export const cityMapApi = {
  getCityMapPage: (pageNum: number, pageSize: number, query: CityMapQuery) =>
    api.post<PageResult<CityMapPageVO>>('/admin/cityMap/findCityMapByPage', query, {
      params: { pageNum, pageSize }
    }),
  getCityMapInfo: (cityMapId: number) =>
    api.get<CityMapInfoVO>('/admin/cityMap/findCityMapById', { cityMapId })
}

export const areaMapApi = {
  getAreaMapPage: (pageNum: number, pageSize: number, query: AreaMapQuery) =>
    api.post<PageResult<AreaMapPageVO>>('/admin/areaMap/findAreaMapByPage', query, {
      params: { pageNum, pageSize }
    }),
  getAreaMapInfo: (areaMapId: number) =>
    api.get<AreaMapInfoVO>('/admin/areaMap/findAreaMapById', { areaMapId })
}

export const streetMapApi = {
  getStreetMapPage: (pageNum: number, pageSize: number, query: StreetMapQuery) =>
    api.post<PageResult<StreetMapPageVO>>('/admin/streetMap/findStreetMapByPage', query, {
      params: { pageNum, pageSize }
    }),
  getStreetMapInfo: (streetMapId: number) =>
    api.get<StreetMapInfoVO>('/admin/streetMap/findStreetMapById', { streetMapId })
}

// 邮件发送相关API
export const emailApi = {
  // 发送HTML邮件（指定发送者）
  sendHtmlEmailSelfDef: (fromEmail: string, toEmail: string, subject: string, htmlContent: string) =>
    request.post('/admin/email/communication/selfde/sendHtml', null, {
      params: { fromEmail, toEmail, subject, htmlContent }
    }),
  
  // 发送HTML邮件（使用默认发送者）
  sendHtmlEmailUsuallyDef: (toEmail: string, subject: string, htmlContent: string) =>
    request.post('/admin/email/communication/usallyde/sendHtml', null, {
      params: { toEmail, subject, htmlContent }
    }),
  
  // 发送纯文本邮件（指定发送者）
  sendTextEmailSelfDef: (fromEmail: string, toEmail: string, subject: string, content: string) =>
    request.post('/admin/email/communication/selfde/sendText', null, {
      params: { fromEmail, toEmail, subject, content }
    }),
  
  // 发送纯文本邮件（使用默认发送者）
  sendTextEmailUsuallyDef: (toEmail: string, subject: string, content: string) =>
    request.post('/admin/email/communication/usallyde/sendText', null, {
      params: { toEmail, subject, content }
    })
}

// LLM调用日志相关API
export const llmUtilsInfoApi = {
  // 查询LLM调用日志详情
  getAdminLlmUtilsInfoById: (llmUtilsInfoId: number) =>
    api.get<AdminLlmUtilsInfoVO>('/admin/llmUtilsInfo/getInfo', { params: { llmUtilsInfoId } }),

  // 分页查询LLM调用日志
  getAdminLlmUtilsInfoPage: (pageNum: number, pageSize: number, query: AdminLlmUtilsInfoQuery) =>
    api.post<PageResult<AdminLlmUtilsInfoPageVO>>('/admin/llmUtilsInfo/getPage', query, {
      params: { pageNum, pageSize }
    })
}

// AI管理相关API
export const aiApi = {
  // AI系统管理助手 - 应用对话（流式）- 直接返回fetch响应，不经过拦截器
  aiSystemManagerApplicationChat: async (message: string, chatId?: string) => {
    const baseURL = import.meta.env.VITE_API_BASE_URL || '/api'
    const token = localStorage.getItem('admin_token')
    let url = `${baseURL}/admin/aiSystemManagerAssistant/application/chat`
    
    if (chatId) {
      url += `?chatId=${encodeURIComponent(chatId)}`
    }
    
    return fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'text/plain',
        'Admin-Authorization': `Admin ${token}`,
        'Accept': 'text/event-stream'
      },
      body: message
    })
  },
  
  // AI系统管理助手 - Agent对话（流式）
  // 注意：需要后端修改才能正常工作
  // 
  // 后端需要修改（修改后此接口可正常使用）：
  // 1. 添加 produces = MediaType.TEXT_EVENT_STREAM_VALUE
  // 2. 直接返回 SseEmitter（不用 BaseResult 包装）
  // 
  // 修改前：
  // @PostMapping(value = "/agent/chat")
  // public BaseResult<SseEmitter> agentChat(...) { return BaseResult.ok(emitter); }
  // 
  // 修改后：
  // @PostMapping(value = "/agent/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  // public SseEmitter agentChat(...) { return emitter; }
  aiSystemManagerAgentChat: async (message: string, chatId?: string) => {
    const baseURL = import.meta.env.VITE_API_BASE_URL || '/api'
    const token = localStorage.getItem('admin_token')
    
    if (!chatId) {
      chatId = 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
    }
    
    let url = `${baseURL}/admin/aiSystemManagerAssistant/agent/chat?chatId=${encodeURIComponent(chatId)}`
    
    console.log('📤 [API] 调用 Agent Chat 接口')
    console.log('   URL:', url)
    console.log('   ChatId:', chatId)
    
    // 发送 SSE 请求
    return fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'text/plain',
        'Admin-Authorization': `Admin ${token}`,
        'Accept': 'text/event-stream'  // 明确要求 SSE 响应
      },
      body: message
    })
  }
}

// 反馈相关API
export const feedbackApi = {
  // 添加反馈
  addFeedback: (data: AdminFeedbackForm) => api.post('/admin/feedback/addFeedback', data),
  
  // 更新反馈阶段
  updateFeedbackStep: (feedbackId: number, operationCode: number, title: string, content: string, operationPersonId: number) =>
    api.put('/admin/feedback/updateFeedbackStep', { title, content }, {
      params: { feedbackId, OperationCode: operationCode, operationPersonId }
    }),
  
  // 查询反馈详情
  getFeedbackDetail: (feedbackId: number) => 
    api.get<AdminFeedbackInfoVO>(`/admin/feedback/findFeedbackById?feedbackId=${feedbackId}`),
  
  // 分页查询反馈
  getFeedbackPage: (pageNum: number, pageSize: number, query: AdminFeedbackQuery) =>
    api.post<PageResult<AdminFeedbackPageVO>>('/admin/feedback/getFeedbackPage', query, {
      params: { pageNum, pageSize }
    })
}

// 反馈记录相关API
export const feedbackRecordApi = {
  // 分页查询反馈记录
  getRecordPage: (pageNum: number, pageSize: number, query: AdminFeedbackRecordQuery) =>
    api.post<PageResult<AdminFeedbackRecordPageVO>>('/admin/adminFeedbackRecord/findAdminFeedbackRecordPage', query, {
      params: { pageNum, pageSize }
    }),
  
  // 查询反馈记录详情
  getRecordDetail: (feedbackRecordId: number) =>
    api.get<AdminFeedbackRecordInfoVO>('/admin/adminFeedbackRecord/findAdminFeedbackRecordByFeedbackRecordId', {
      params: { feedbackRecordId }
    })
}

// 用户反馈相关API
export const userFeedbackApi = {
  // 分页查询用户反馈
  getUserFeedbackPage: (size: number, page: number, query: UserFeedbackQuery) =>
    api.post<PageResult<UserFeedbackPageVO>>('/admin/userFeedback/getFeedbackPage', query, {
      params: { size, page }
    }),
  
  // 查询用户反馈详情
  getUserFeedbackDetail: (feedbackId: number) => 
    api.get<UserFeedbackInfoVO>(`/admin/userFeedback/findFeedbackById?feedbackId=${feedbackId}`),
  
  // 更新用户反馈阶段
  updateUserFeedbackStep: (feedbackId: number, operationCode: number, title: string, content: string, operationPersonId: number) =>
    api.post('/admin/userFeedback/updateFeedbackStep', { title, content }, {
      params: { feedbackId, OperationCode: operationCode, operationPersonId }
    })
}

// 用户反馈记录相关API
export const userFeedbackRecordApi = {
  // 分页查询用户反馈记录
  getUserFeedbackRecordPage: (pageNum: number, pageSize: number, query: UserFeedbackRecordQuery) =>
    api.post<PageResult<UserFeedbackRecordPageVO>>('/admin/userfeedbackRecord/findUserFeedbackRecordPage', query, {
      params: { pageNum, pageSize }
    }),
  
  // 查询用户反馈记录详情
  getUserFeedbackRecordDetail: (feedbackRecordId: number) =>
    api.get<UserFeedbackRecordInfoVO>('/admin/userfeedbackRecord/findUserFeedbackRecordByFeedbackRecordId', {
      params: { feedbackRecordId }
    })
}

// 短信相关API
export const smsApi = {
  // 发送短信验证码
  sendPhoneCode: (phone: string) => api.post('/admin/sms/send', null, { params: { phone } }),
  
  // 校验短信验证码
  checkPhoneCode: (phone: string, code: string) => api.post('/admin/sms/check', null, { params: { phone, code } })
}

// 系统删除简历相关API
export const systemDeleteResumeApi = {
  // 分页查询系统删除简历
  getDeleteResumePage: (pageNum: number, pageSize: number, query: UserDeleteResumeQuery) =>
    api.post<PageResult<UserDeleteResumeBySystemPageVO>>('/admin/userDeleteResumeBySystemService/getUserDeleteResumeInfoPage', query, {
      params: { pageNum, pageSize }
    }),

  // 查询系统删除简历详情
  getDeleteResumeDetail: (userDeleteResumeId: number) =>
    api.get<UserDeleteResumeBySystemInfoVO>('/admin/userDeleteResumeBySystemService/getUserDeleteResumeInfoById', {
      params: { userDeleteResumeId }
    })
}

// 简历评分训练数据相关API
export const scoreTrainingDataApi = {
  addScoreTrainingData: (data: AdminScoreTrainingDataForm) =>
    api.post<number>('/admin/scoreTrainingData/addScoreTrainingData', data),

  deleteScoreTrainingData: (scoreTrainingDataId: number) =>
    api.delete<number>('/admin/scoreTrainingData/deleteScoreTrainingData', { scoreTrainingDataId }),

  getScoreTrainingDataInfo: (scoreTrainingDataId: number) =>
    api.get<AdminScoreTrainingDataInfoVO>('/admin/scoreTrainingData/findScoreTrainingDataById', { scoreTrainingDataId }),

  getScoreTrainingDataPage: (pageNum: number, pageSize: number, query: AdminScoreTrainingDataQuery) =>
    api.post<PageResult<AdminScoreTrainingDataPageVO>>('/admin/scoreTrainingData/findScoreTrainingDataByPage', query, {
      params: { pageNum, pageSize }
    }),

  getAllScoreTrainingData: () =>
    api.get<AdminScoreTrainingDataPageVO[]>('/admin/scoreTrainingData/findAllScoreTrainingData'),

  exportScoreTrainingData: async (query: AdminScoreTrainingDataQuery) => {
    const token = localStorage.getItem('admin_token')
    return axios.post('/admin/scoreTrainingData/exportScoreTrainingData', query, {
      baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
      responseType: 'blob',
      headers: {
        'Content-Type': 'application/json',
        ...(token ? { 'Admin-Authorization': `Admin ${token}` } : {})
      }
    })
  }
}

// 公告相关API
export interface AnnouncementInfo {
  announcementId: number
  announcementTitle: string
  announcementContent: string
  announcementUpdatedTime: string
}

export const announcementApi = {
  // 获取管理端公告信息
  getAdminAnnouncement: () => api.get<AnnouncementInfo>('/admonitor/admin/announcement/getInfo')
}

// 广告相关API
export interface AdvertisementInfo {
  advertisementId: number
  advertisementName: string
  advertisementUrl: string      // 广告图片URL
  advertisementLink: string     // 点击跳转链接
  advertisementStartedTime: string
  advertisementEndTime: string
}

export const advertisementApi = {
  // 获取管理端所有广告
  getAllAdminAdvertisements: () => api.get<AdvertisementInfo[]>('/admonitor/admin/advertisement/findAllAdmonitorAdminAdvertisement')
}

// 内容管理相关API
export const projectIntroduceApi = {
  add: (data: ProjectIntroduceForm) => api.post<number>('/admin/projectIntroduce/add', data),
  update: (data: ProjectIntroduceForm) => api.post<number>('/admin/projectIntroduce/update', data),
  getInfo: () => api.get<ProjectIntroduceInfoVO>('/admin/projectIntroduce/getInfo')
}

export const teamIntroduceApi = {
  add: (data: TeamIntroduceForm) => api.post<number>('/admin/teamIntroduce/add', data),
  update: (data: TeamIntroduceForm) => api.post<number>('/admin/teamIntroduce/update', data),
  getInfo: () => api.get<TeamIntroduceInfoVO>('/admin/teamIntroduce/getInfo')
}

export const developHistoryApi = {
  add: (data: DevelopHistoryForm) => api.post<number>('/admin/developHistory/add', data),
  update: (data: DevelopHistoryForm) => api.post<number>('/admin/developHistory/update', data),
  getInfo: () => api.get<DevelopHistoryInfoVO>('/admin/developHistory/getInfo')
}

export const joinUsApi = {
  add: (data: JoinUsForm) => api.post<number>('/admin/joinUs/add', data),
  update: (data: JoinUsForm) => api.post<number>('/admin/joinUs/update', data),
  getInfo: () => api.get<JoinUsInfoVO>('/admin/joinUs/getInfo')
}

export const partnerIntroduceApi = {
  add: (data: PartnerIntroduceForm) => api.post<number>('/admin/partnerIntroduce/add', data),
  update: (data: PartnerIntroduceForm) => api.post<number>('/admin/partnerIntroduce/update', data),
  getInfo: () => api.get<PartnerIntroduceInfoVO>('/admin/partnerIntroduce/getInfo')
}

export const mediaReportApi = {
  add: (data: MediaReportForm) => api.post<number>('/admin/mediaReport/add', data),
  update: (data: MediaReportForm) => api.post<number>('/admin/mediaReport/update', data),
  getInfo: () => api.get<MediaReportInfoVO>('/admin/mediaReport/getInfo')
}

export const customerServiceApi = {
  add: (data: CustomerServiceForm) => api.post<number>('/admin/customerService/add', data),
  update: (data: CustomerServiceForm) => api.post<number>('/admin/customerService/update', data),
  getInfo: () => api.get<CustomerServiceInfoVO>('/admin/customerService/getInfo')
}

export const faqApi = {
  add: (data: FaqForm) => api.post<number>('/admin/faq/add', data),
  update: (data: FaqForm) => api.post<number>('/admin/faq/update', data),
  remove: (faqId: number) => api.delete<number>('/admin/faq/delete', { faqId }),
  getInfo: (faqId: number) => api.get<FaqInfoVO>('/admin/faq/getInfo', { faqId }),
  getPage: (pageNum: number, pageSize: number, query: FaqQuery) =>
    api.post<PageResult<FaqPageVO>>('/admin/faq/getPage', query, { params: { pageNum, pageSize } })
}

export const userGuideApi = {
  add: (data: UserGuideForm) => api.post<number>('/admin/userGuide/add', data),
  update: (data: UserGuideForm) => api.post<number>('/admin/userGuide/update', data),
  remove: (userGuideId: number) => api.delete<number>('/admin/userGuide/delete', { userGuideId }),
  getInfo: (userGuideId: number) => api.get<UserGuideInfoVO>('/admin/userGuide/getInfo', { userGuideId }),
  getPage: (pageNum: number, pageSize: number, query: UserGuideQuery) =>
    api.post<PageResult<UserGuidePageVO>>('/admin/userGuide/getPage', query, { params: { pageNum, pageSize } })
}
