// 分页查询参数
export interface PageQuery {
  pageNum?: number
  pageSize?: number
}

// 分页结果
export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

// API响应类型
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

// 管理员分页查询
export interface AdminPageQuery {
  adminUsername?: string
  adminEmail?: string
  adminPhone?: string
  adminState?: number
}

// 管理员分页VO
export interface AdminPageVO {
  adminId: number
  adminAccount: string
  adminUsername: string
  adminEmail: string
  adminPhone: string
  adminImage: string
  adminIntroduce: string
  adminState: number
  adminLoginTime: string
  roles?: RoleInfoVO[]
}

// 管理员详情VO
export interface AdminInfoVO {
  adminId: number
  adminAccount: string
  adminUsername: string
  adminEmail: string
  adminPhone: string
  adminImage: string
  adminIntroduce: string
  adminState: number
  adminLoginTime: string
  adminCreatedTime?: string
  roleInfoVOS?: RoleInfoVO[]
}

// 管理员表单
export interface AdminForm {
  adminId?: number
  adminAccount: string
  adminUsername: string
  adminEmail: string
  adminPhone: string
  adminPassword?: string
  adminImage?: string
  adminIntroduce?: string
  adminState?: number
}

// 角色信息VO
export interface RoleInfoVO {
  roleId: number
  roleName: string
  roleIntroduce: string
}

// 角色表单
export interface RoleForm {
  roleId?: number
  roleName: string
  roleIntroduce: string
}

// 角色分页查询
export interface RolePageQuery {
  roleName?: string
  roleIntroduce?: string
}

// 角色分页VO
export interface RolePageVO {
  roleId: number
  roleName: string
  roleIntroduce: string
  roleCreatedTime: string
}

// 权限信息VO
export interface PermissionInfoVO {
  permissionId: number
  permissionName: string
  permissionUrl: string
  permissionRequest: string
  permissionIntroduce: string
}

// 权限表单
export interface PermissionForm {
  permissionId?: number
  permissionName: string
  permissionUrl: string
  permissionRequest: string
  permissionIntroduce: string
}

// 权限分页查询
export interface PermissionPageQuery {
  permissionName?: string
  permissionUrl?: string
  permissionRequest?: string
}

// 权限分页VO
export interface PermissionPageVO {
  permissionId: number
  permissionName: string
  permissionUrl: string
  permissionRequest: string
  permissionIntroduce: string
  permissionCreatedTime: string
}

// 简历模板信息VO
export interface ResumeTemplateInfoVO {
  resumeTemplateId: number
  resumeTemplateUuid: string
  resumeTemplateName: string
  resumeTemplateDescribe: string
  resumeTemplateHtml: string
  resumeTemplateCss: string
  resumeTemplatePrice: number
  resumeTemplateState: number
  resumeTemplateCreatedTime: string
  resumeTemplateUpdatedTime: string
}

// 简历模板表单
export interface ResumeTemplateForm {
  resumeTemplateId?: number
  resumeTemplateUuid?: string
  resumeTemplateName: string
  resumeTemplateDescribe: string
  resumeTemplateHtml: string
  resumeTemplateCss: string
  resumeTemplatePrice: number
  resumeTemplateState?: number
}

// 简历模板查询
export interface ResumeTemplateQuery {
  resumeTemplateName?: string
  resumeTemplateState?: number
}

// 简历模板分页VO
export interface ResumeTemplatePageVO {
  resumeTemplateId: number
  resumeTemplateUuid: string
  resumeTemplateName: string
  resumeTemplateDescribe: string
  resumeTemplatePrice: number
  resumeTemplateState: number
  resumeTemplateCreatedTime: string
}

// 求职攻略文章表单
export interface JobAdviceArticleForm {
  jobAdviceArticleId?: number
  jobAdviceArticleTitle: string
  jobAdviceArticleContent: string
  jobAdviceArticleCategory: string
  jobAdviceArticleTag: string
  jobAdviceArticleState: number
}

// 求职攻略文章查询
export interface JobAdviceArticleQuery {
  jobAdviceArticleTitle?: string
  jobAdviceArticleCategory?: string
  jobAdviceArticleState?: number
}

// 招聘岗位信息VO
export interface RecruitPositionInfoVO {
  recruitPositionId: number
  recruitPositionName: string
  recruitPositionIndustryCode: number
  recruitPositionIndustryName?: string
  minMonthSalary: number
  maxMonthSalary: number
  weekWorkDayNum: number
  goodWelfare?: string
  createdTime?: string
  updatedTime?: string
}

// 招聘岗位表单
export interface RecruitPositionForm {
  recruitPositionId?: number
  recruitPositionName: string
  recruitPositionIndustryCode: number
  minMonthSalary: number
  maxMonthSalary: number
  weekWorkDayNum: number
  goodWelfare?: string
}

// 招聘岗位查询
export interface RecruitPositionQuery {
  recruitPositionName?: string
  recruitPositionIndustryCode?: number
  minMonthSalary?: number
  maxMonthSalary?: number
  weekWorkDayNum?: number
}

// 招聘岗位分页VO
export interface RecruitPositionPageVO {
  recruitPositionId: number
  recruitPositionName: string
  recruitPositionIndustryCode: number
  recruitPositionIndustryName?: string
  minMonthSalary: number
  maxMonthSalary: number
  weekWorkDayNum: number
  goodWelfare?: string
  createdTime?: string
}

// 招聘信息详情VO
export interface EmploymentInformationInfoVO {
  employmentInformationId: number
  employmentInformationCode?: number
  employmentInformationCompanyName: string
  employmentInformationIndustryCategoriesName?: string
  employmentInformationCompanyType: number
  employmentInformationBatch: number
  employmentInformationRecruitPosition: number
  employmentInformationRecruitObject: number
  employmentInformationRecruitLocationFirstName?: string[]
  employmentInformationRecruitLocationSecondName?: string[]
  employmentInformationRecruitLocationDetail?: string[]
  employmentInformationStartTime?: string
  employmentInformationStopTime: string
  employmentInformationUpdatedTime?: string
  employmentInformationOnlineApplicationStatus: string
  employmentInformationOfficialAnnouncement?: string
  employmentInformationSubmissionWay: string
  employmentInformationEmployeeReferralCode?: string
}

// 招聘信息表单
export interface EmploymentInformationForm {
  employmentInformationId?: number
  employmentInformationCode?: number
  employmentInformationCompanyName: string
  employmentInformationIndustryCategories: number
  employmentInformationCompanyType: number
  employmentInformationBatch: number
  employmentInformationRecruitPosition: number
  employmentInformationRecruitObject: number
  employmentInformationRecruitLocationFirstList: number[]
  employmentInformationRecruitLocationSecondList: number[]
  employmentInformationRecruitLocationDetail?: string
  employmentInformationStopTime: string
  employmentInformationOnlineApplicationStatus: string
  employmentInformationOfficialAnnouncement?: string
  employmentInformationSubmissionWay: string
  employmentInformationEmployeeReferralCode?: string
}

// 招聘信息查询
export interface EmploymentInformationQuery {
  employmentInformationCompanyName?: string
  employmentInformationIndustryCategories?: number
  employmentInformationCompanyType?: number
  employmentInformationBatch?: number
  employmentInformationRecruitPosition?: number
  employmentInformationRecruitObject?: number
  employmentInformationOnlineApplicationStatus?: string
}

// 招聘信息分页VO
export interface EmploymentInformationPageVO {
  employmentInformationId: number
  employmentInformationCompanyName: string
  employmentInformationIndustryCategoriesName?: string
  employmentInformationCompanyType: number
  employmentInformationBatch: number
  employmentInformationRecruitPosition: number
  employmentInformationRecruitObject: number
  employmentInformationRecruitLocationFirstName?: string[]
  employmentInformationRecruitLocationSecondName?: string[]
  employmentInformationRecruitLocationDetail?: string[]
  employmentInformationStartTime?: string
  employmentInformationStopTime: string
  employmentInformationUpdatedTime?: string
  employmentInformationOnlineApplicationStatus: string
  employmentInformationOfficialAnnouncement?: string
  employmentInformationSubmissionWay: string
  employmentInformationEmployeeReferralCode?: string
}

// 行业Map信息VO
export interface IndustryMapInfoVO {
  industryMapIndustryCode: number
  industryMapIndustryName: string
  createdTime: string
  updatedTime: string
}

// 行业Map表单
export interface IndustryMapForm {
  industryMapIndustryCode?: number
  industryMapIndustryName: string
}

// 行业Map查询
export interface IndustryMapQuery {
  industryMapIndustryCode?: number
  industryMapIndustryName?: string
}

// 行业Map分页VO
export interface IndustryMapPageVO {
  industryMapIndustryCode: number
  industryMapIndustryName: string
  createdTime: string
  updatedTime: string
}

// 管理员反馈查询
export interface AdminFeedbackQuery {
  adminFeedbackTitle?: string
  adminFeedbackContent?: string
}

// 管理员反馈分页VO
export interface AdminFeedbackPageVO {
  adminFeedbackId: number
  adminFeedbackTitle: string
  adminFeedbackContent: string
  adminFeedbackTime: string
  adminFeedbackRecentTime: string
  adminFeedbackCurStep: string
  adminFeedbackAdminId: number
  adminFeedbackAdminName: string
}

// 管理员反馈详情VO
export interface AdminFeedbackInfoVO {
  adminFeedbackId: number
  adminFeedbackTitle: string
  adminFeedbackContent: string
  adminFeedbackTime: string
  adminFeedbackRecentTime: string
  adminFeedbackCurStep: string
  adminFeedbackAdminId: number
  adminFeedbackAdminName: string
}

// 管理员反馈表单
export interface AdminFeedbackForm {
  adminFeedbackTitle: string
  adminFeedbackContent: string
  adminFeedbackAdminId: number
}

// 管理员反馈更新表单
export interface AdminUpdateFeedbackForm {
  operationCode: number
  title: string
  content: string
}

// 省份Map
export interface ProvinceMap {
  provinceMapPid: number
  provinceMapPname: string
}

// 城市Map
export interface CityMap {
  cityMapCid: number
  cityMapCname: string
  cityMapPid: number
}

// 区县Map
export interface AreaMap {
  areaMapAid: number
  areaMapAname: string
  areaMapCid: number
}