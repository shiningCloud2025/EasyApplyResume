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
  adminCreatedTime: string
  roles?: RoleInfoVO[]
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
  permissions?: PermissionInfoVO[]
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
  resumeTemplateName: string
  resumeTemplateReactCode: string
  industryMapIndustryName: string
  isEnable: number
  createTime: string
  updateTime: string
}

// 简历模板表单
export interface ResumeTemplateForm {
  resumeTemplateId?: number
  resumeTemplateName: string
  resumeTemplateReactCode: string
  resumeTemplateIndustry: number
  resumeTemplateIsActive?: number
}

// 简历模板查询
export interface ResumeTemplateQuery {
  resumeTemplateName?: string
  resumeTemplateIndustry?: number
}

// 简历模板分页VO
export interface ResumeTemplatePageVO {
  resumeTemplateId: number
  resumeTemplateName: string
  resumeTemplateReactCode: string
  industryMapIndustryName: string
  resumeTemplateIsActive: number
  resumeTemplateCreatedTime: string
  resumeTemplateUpdatedTime: string
}

// 求职攻略文章表单
export interface JobAdviceArticleForm {
  jobAdviceArticleId?: number
  jobAdviceArticleTitle: string
  jobAdviceArticleContent: string
  jobAdviceArticleCategory: string
  jobAdviceArticleTags: string
  jobAdviceArticleAuthorName: string
  jobAdviceArticlePublishedStatus: number
}

// 求职攻略文章查询
export interface JobAdviceArticleQuery {
  jobAdviceArticleTitle?: string
  jobAdviceArticleContent?: string
  jobAdviceArticleCategory?: string
  jobAdviceArticleTags?: string
  jobAdviceArticleAuthorName?: string
}

// 求职攻略文章分页VO
export interface JobAdviceArticlePageVO {
  jobAdviceArticleId: number
  jobAdviceArticleTitle: string
  jobAdviceArticleContent: string
  jobAdviceArticleCategory: string
  jobAdviceArticleTags: string
  jobAdviceArticleAuthorName: string
  jobAdviceArticlePublishedStatus: number
  jobAdviceArticlePublishedTime: any
  jobAdviceArticleUpdatedTime: any
}

// 招聘岗位信息VO
export interface RecruitPositionInfoVO {
  recruitPositionId: number
  recruitPositionName: string
  recruitPositionSalary: string
  recruitPositionIntroduce: string
  recruitPositionRequirement: string
  recruitPositionState: number
  recruitPositionCreatedTime: string
  recruitPositionUpdatedTime: string
}

// 招聘岗位表单
export interface RecruitPositionForm {
  recruitPositionId?: number
  recruitPositionName: string
  recruitPositionSalary: string
  recruitPositionIntroduce: string
  recruitPositionRequirement: string
  recruitPositionState?: number
}

// 招聘岗位查询
export interface RecruitPositionQuery {
  recruitPositionName?: string
  recruitPositionSalary?: string
  recruitPositionState?: number
}

// 招聘岗位分页VO
export interface RecruitPositionPageVO {
  recruitPositionId: number
  recruitPositionName: string
  recruitPositionSalary: string
  recruitPositionIntroduce: string
  recruitPositionState: number
  recruitPositionCreatedTime: string
}

// 招聘信息信息VO
export interface EmploymentInformationInfoVO {
  employmentInformationId: number
  employmentInformationCode: number
  employmentInformationCompanyName: string
  employmentInformationIndustryCategoriesName: string
  employmentInformationCompanyType: number
  employmentInformationBatch: number
  employmentInformationRecruitPosition: number
  employmentInformationRecruitObject: number
  employmentInformationRecruitLocationFirstName: string[]
  employmentInformationRecruitLocationSecondName: string[]
  employmentInformationRecruitLocationDetail: string[]
  employmentInformationStartTime: string
  employmentInformationStopTime: string
  employmentInformationUpdatedTime: string
  employmentInformationOnlineApplicationStatus: string
  employmentInformationOfficialAnnouncement: string
  employmentInformationSubmissionWay: string
  employmentInformationEmployeeReferralCode: string
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
  employmentInformationRecruitLocationFirst: number[]
  employmentInformationRecruitLocationSecond: number[]
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
  employmentInformationRecruitLocationFirst?: number
  employmentInformationRecruitLocationSecond?: number
  employmentInformationRecruitLocationDetail?: string
  employmentInformationStopTime?: string
  employmentInformationOnlineApplicationStatus?: string
  employmentInformationOfficialAnnouncement?: string
  employmentInformationSubmissionWay?: string
  employmentInformationEmployeeReferralCode?: string
}

// 招聘信息分页VO
export interface EmploymentInformationPageVO {
  employmentInformationId: number
  employmentInformationCompanyName: string
  employmentInformationIndustryCategoriesName: string
  employmentInformationCompanyType: number
  employmentInformationBatch: number
  employmentInformationRecruitPosition: number
  employmentInformationRecruitObject: number
  employmentInformationRecruitLocationFirstName: string[]
  employmentInformationRecruitLocationSecondName: string[]
  employmentInformationRecruitLocationDetail: string[]
  employmentInformationStartTime: string
  employmentInformationStopTime: string
  employmentInformationUpdatedTime: string
  employmentInformationOnlineApplicationStatus: string
  employmentInformationOfficialAnnouncement: string
  employmentInformationSubmissionWay: string
  employmentInformationEmployeeReferralCode: string
}

// 行业Map信息VO
export interface IndustryMapInfoVO {
  industryMapId: number
  industryMapName: string
  industryMapIntroduce: string
  industryMapParentId: number
  industryMapLevel: number
  industryMapCreatedTime: string
}

// 行业Map表单
export interface IndustryMapForm {
  industryMapId?: number
  industryMapName: string
  industryMapIntroduce: string
  industryMapParentId?: number
  industryMapLevel?: number
}

// 行业Map查询
export interface IndustryMapQuery {
  industryMapName?: string
  industryMapParentId?: number
  industryMapLevel?: number
}

// 行业Map分页VO
export interface IndustryMapPageVO {
  industryMapId: number
  industryMapName: string
  industryMapIntroduce: string
  industryMapParentId: number
  industryMapLevel: number
  industryMapCreatedTime: string
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
  title: string
  content: string
}