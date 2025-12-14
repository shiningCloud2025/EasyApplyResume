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
  ProvinceMap,
  CityMap,
  AreaMap
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
  logout: (adminId: number) => api.get('/admin/auth/logout', { adminId }),
  
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
  generateRandomAccount: () => api.get<string>('/admin/admin/generateRandomAccount'),
  
  // 获取当前管理员信息
  getCurrentAdminInfo: () => api.post<any>('/admin/admin/getAdminInfo')
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
    api.get<ResumeTemplateInfoVO>('/admin/resumeTemplate/findResumeTemplateById', { resumeTemplateId }),
  
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
    api.get<any>('/admin/jobAdviceArticle/getJobAdviceArticleInfo', { jobAdviceArticleId }),
  
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
    api.get<RecruitPositionInfoVO>('/admin/recruitPosition/queryRecruitPosition', { recruitPositionId }),
  
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
  deleteEmploymentInformation: (data: EmploymentInformationForm) => 
    api.post<number>('/admin/employmentInformation/deleteEmploymentInformation', data),
  
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
    api.get<IndustryMapInfoVO>('/admin/industryMap/findIndustryMapById', { industryMapId: industryMapIndustryCode }),
  
  // 分页查询行业
  getIndustryMapPage: (pageNum: number, pageSize: number, query: IndustryMapQuery) => 
    api.post<PageResult<IndustryMapPageVO>>('/admin/industryMap/findIndustryMapByPage', query, { 
      params: { pageNum, pageSize } 
    }),
  
  // 查询所有行业
  findAllIndustryMap: () => api.get<IndustryMapInfoVO[]>('/admin/industryMap/findAllIndustryMap')
}

// 地区相关API（注意：这些接口直接返回数据，没有BaseResult包装）
export const provinceMapApi = {
  // 查询所有省份
  getAllProvince: async () => {
    console.log('🔥 API层：开始调用 getAllProvince')
    try {
      const response = await request.get<ProvinceMap[]>('/admin/provinceMap/getAllProvince')
      console.log('🔥 API层：getAllProvince axios响应:', response)
      console.log('🔥 API层：getAllProvince 数据:', response.data)
      // 响应拦截器已处理数组包装，直接返回
      return response as any
    } catch (error) {
      console.error('🔥 API层：getAllProvince 失败:', error)
      throw error
    }
  },
  
  // 根据省份查询城市
  getCityByProvinceId: async (provinceMapId: number) => {
    console.log('🔥 API层：开始调用 getCityByProvinceId, 省份ID:', provinceMapId)
    try {
      const response = await request.get<CityMap[]>('/admin/provinceMap/getCityByProvinceId', { 
        params: { provinceMapId } 
      })
      console.log('🔥 API层：getCityByProvinceId axios响应:', response)
      console.log('🔥 API层：getCityByProvinceId 数据:', response.data)
      // 响应拦截器已处理数组包装，直接返回
      return response as any
    } catch (error) {
      console.error('🔥 API层：getCityByProvinceId 失败:', error)
      throw error
    }
  }
}

// 城市相关API（注意：这些接口直接返回数据，没有BaseResult包装）
export const cityMapApi = {
  // 查询所有城市
  getAllCity: async () => {
    const response = await request.get<CityMap[]>('/admin/cityMap/getAllCity')
    // 响应拦截器已处理数组包装，直接返回
    return response as any
  },
  
  // 根据城市查询区县
  getAllAreaByCityId: async (cityId: number) => {
    const response = await request.get<AreaMap[]>('/admin/cityMap/getAllAreaByCityId', { 
      params: { cityId } 
    })
    // 响应拦截器已处理数组包装，直接返回
    return response as any
  }
}

// 邮件发送相关API
export const emailApi = {
  // 发送纯文本邮件（指定发送者）
  sendTextEmailSpecifySelf: (fromEmail: string, toEmail: string, subject: string, content: string) =>
    api.post<void>('/admin/email/communication/selfde/sendText', null, {
      params: { fromEmail, toEmail, subject, content }
    }),
  
  // 发送纯文本邮件（使用默认发送者）
  sendTextEmail: (toEmail: string, subject: string, content: string) =>
    api.post<void>('/admin/email/communication/usallyde/sendText', null, {
      params: { toEmail, subject, content }
    }),
  
  // 发送HTML邮件（指定发送者）
  sendHtmlEmailSpecifySelf: (fromEmail: string, toEmail: string, subject: string, htmlContent: string) =>
    api.post<void>('/admin/email/communication/selfde/sendHtml', null, {
      params: { fromEmail, toEmail, subject, htmlContent }
    }),
  
  // 发送HTML邮件（使用默认发送者）
  sendHtmlEmail: (toEmail: string, subject: string, htmlContent: string) =>
    api.post<void>('/admin/email/communication/usallyde/sendHtml', null, {
      params: { toEmail, subject, htmlContent }
    })
}

// AI助手相关API
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
  
  // AI系统管理助手 - Agent对话（流式）- 直接返回fetch响应，不经过拦截器
  aiSystemManagerAgentChat: async (message: string, chatId?: string) => {
    const baseURL = import.meta.env.VITE_API_BASE_URL || '/api'
    const token = localStorage.getItem('admin_token')
    
    // 如果没有chatId，生成一个UUID
    if (!chatId) {
      chatId = 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
    }
    
    let url = `${baseURL}/admin/aiSystemManagerAssistant/agent/chat?chatId=${encodeURIComponent(chatId)}`
    
    console.log('🔧 [API] Agent Chat URL:', url)
    console.log('🔧 [API] Message:', message)
    
    return fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'text/plain',
        'Admin-Authorization': `Admin ${token}`,
        'Accept': 'text/event-stream',
        'Cache-Control': 'no-cache'
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
  getFeedbackDetail: (feedbackId: number) => api.get<AdminFeedbackInfoVO>('/admin/feedback/findFeedbackById', { feedbackId }),
  
  // 分页查询反馈
  getFeedbackPage: (pageNum: number, pageSize: number, query: AdminFeedbackQuery) =>
    api.post<PageResult<AdminFeedbackPageVO>>('/admin/feedback/getFeedbackPage', query, {
      params: { pageNum, pageSize }
    })
}

// 短信相关API
export const smsApi = {
  // 发送短信验证码
  sendPhoneCode: (phone: string) => api.post('/admin/sms/send', null, { params: { phone } }),
  
  // 校验短信验证码
  checkPhoneCode: (phone: string, code: string) => api.post('/admin/sms/check', null, { params: { phone, code } })
}