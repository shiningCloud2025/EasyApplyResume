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

// LLM调用日志查询
export interface AdminLlmUtilsInfoQuery {
  llmUtilsInfoToolDescription?: string
  llmUtilsInfoModelProvider?: string
  llmUtilsInfoModelName?: string
  llmUtilsInfoStatus?: string
}

// LLM调用日志分页VO
export interface AdminLlmUtilsInfoPageVO {
  llmUtilsInfoId: number
  llmUtilsInfoToolClass: string
  llmUtilsInfoToolDescription: string
  llmUtilsInfoModelProvider: string
  llmUtilsInfoModelName: string
  llmUtilsInfoInputContent: string
  llmUtilsInfoOutputResult: string
  llmUtilsInfoLatencyMs: number
  llmUtilsInfoStatus: string
  llmUtilsInfoErrorMessage: string
  llmUtilsInfoCreatedTime: string
}

// LLM调用日志详情VO
export interface AdminLlmUtilsInfoVO {
  llmUtilsInfoId: number
  llmUtilsInfoToolClass: string
  llmUtilsInfoToolDescription: string
  llmUtilsInfoModelProvider: string
  llmUtilsInfoModelName: string
  llmUtilsInfoInputContent: string
  llmUtilsInfoOutputResult: string
  llmUtilsInfoLatencyMs: number
  llmUtilsInfoStatus: string
  llmUtilsInfoErrorMessage: string
  llmUtilsInfoCreatedTime: string
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

// ============= 管理员反馈记录（FeedbackRecord）=============

// 管理员反馈记录查询条件
export interface AdminFeedbackRecordQuery {
  adminFeedbackRecordName?: string          // 反馈人姓名
  adminFeedbackRecordTitle?: string         // 反馈标题
  adminFeedbackRecordApprovalPersonName?: string  // 处理人姓名
}

// 管理员反馈记录分页VO
export interface AdminFeedbackRecordPageVO {
  adminFeedbackRecordId: number             // 反馈记录ID
  adminFeedbackRecordAdminId: number        // 反馈管理员ID
  adminFeedbackRecordName: string           // 反馈人
  adminFeedbackRecordTitle: string          // 标题
  adminFeedbackRecordContent: string        // 内容
  adminFeedbackRecordTime: Date             // 创建时间
  adminFeedbackRecordCurrentStepSolveTime: Date  // 处理时间
  adminFeedbackRecordOldStep: string        // 原节点
  adminFeedbackRecordNewStep: string        // 现阶段
  adminFeedbackRecordApprovalPersonId: number    // 处理人ID
  adminFeedbackRecordApprovalPersonName: string  // 处理人姓名
}

// 管理员反馈记录详情VO
export interface AdminFeedbackRecordInfoVO {
  adminFeedbackRecordId: number             // 反馈记录ID
  adminFeedbackRecordAdminId: number        // 反馈管理员ID
  adminFeedbackRecordName: string           // 反馈人
  adminFeedbackRecordTitle: string          // 标题
  adminFeedbackRecordContent: string        // 内容
  adminFeedbackRecordTime: Date             // 创建时间
  adminFeedbackRecordCurrentStepSolveTime: Date  // 处理时间
  adminFeedbackRecordOldStep: string        // 原节点
  adminFeedbackRecordNewStep: string        // 现阶段
  adminFeedbackRecordApprovalPersonId: number    // 处理人ID
  adminFeedbackRecordApprovalPersonName: string  // 处理人姓名
}

// 大学Map
export interface UniversityMap {
  universityMapId: number
  universityMapName: string
  universityMapAddress?: string
  universityMapLat?: string
  universityMapLng?: string
  universityMapStatus?: number
}

export interface UniversityMapQuery {
  universityMapName?: string
}

export interface UniversityMapPageVO {
  universityMapId: number
  universityMapName: string
  universityMapAddress?: string
  universityMapLat?: string
  universityMapLng?: string
  universityMapStatus?: number
}

export interface UniversityMapInfoVO {
  universityMapId: number
  universityMapName: string
  universityMapAddress?: string
  universityMapLat?: string
  universityMapLng?: string
  universityMapStatus?: number
}

// 省份Map
export interface ProvinceMap {
  provinceMapPid: number
  provinceMapPname: string
}

// 省份Map查询
export interface ProvinceMapQuery {
  provinceMapPname?: string
  provinceMapPid?: number
}

// 省份Map分页VO
export interface ProvinceMapPageVO {
  provinceMapPid: number
  provinceMapPname: string
}

// 城市Map
export interface CityMap {
  cityMapCid: number
  cityMapCname: string
  cityMapPid: number
}

// 城市Map查询
export interface CityMapQuery {
  cityMapCname?: string
  cityMapPid?: number
}

// 城市Map分页VO
export interface CityMapPageVO {
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

// 区县Map查询
export interface AreaMapQuery {
  areaMapAname?: string
  areaMapCid?: number
}

// 区县Map分页VO
export interface AreaMapPageVO {
  areaMapId: number
  areaMapAname: string
  areaMapCid: number
}

// 街道Map
export interface StreetMap {
  streetMapSid: number
  streetMapSname: string
  streetMapAid: number
}

// 街道Map查询
export interface StreetMapQuery {
  streetMapSname?: string
  streetMapAid?: number
}

// 街道Map分页VO
export interface StreetMapPageVO {
  streetMapSid: number
  streetMapSname: string
  streetMapAid: number
}

// ============= 用户反馈（UserFeedback）=============
// 用户反馈查询
export interface UserFeedbackQuery {
  userFeedbackTitle?: string
  userFeedbackContent?: string
}

// 用户反馈分页VO
export interface UserFeedbackPageVO {
  userFeedbackId: number
  userFeedbackTitle: string
  userFeedbackContent: string
  userFeedbackTime: string
  userFeedbackRecentTime: string
  userFeedbackCurStep: string
  userFeedbackUserId: number
  userFeedbackUserName: string
}

// 用户反馈详情VO
export interface UserFeedbackInfoVO {
  userFeedbackId: number
  userFeedbackTitle: string
  userFeedbackContent: string
  userFeedbackTime: string
  userFeedbackRecentTime: string
  userFeedbackCurStep: string
  userFeedbackUserId: number
  userFeedbackUserName: string
}

// 用户反馈更新表单
export interface UserUpdateFeedbackForm {
  operationCode: number
  title: string
  content: string
}

// ============= 用户反馈记录（UserFeedbackRecord）=============

// 用户反馈记录查询条件
export interface UserFeedbackRecordQuery {
  userFeedbackRecordName?: string          // 反馈人姓名
  userFeedbackRecordTitle?: string         // 反馈标题
  userFeedbackRecordApprovalPersonName?: string  // 处理人姓名
}

// 用户反馈记录分页VO
export interface UserFeedbackRecordPageVO {
  userFeedbackRecordId: number             // 反馈记录ID
  userFeedbackRecordUserId: number         // 反馈的用户ID
  userFeedbackRecordName: string           // 反馈人
  userFeedbackRecordTitle: string          // 标题
  userFeedbackRecordContent: string        // 内容
  userFeedbackRecordTime: string           // 创建时间
  userFeedbackRecordCurrentStepSolveTime: string  // 处理时间
  userFeedbackRecordOldStep: string        // 原节点
  userFeedbackRecordNewStep: string        // 现阶段
  userFeedbackRecordApprovalPersonId: number    // 处理人ID
  adminFeedbackRecordApprovalPersonName: string  // 处理人姓名
}

// 用户反馈记录详情VO
export interface UserFeedbackRecordInfoVO {
  userFeedbackRecordId: number             // 反馈记录ID
  userFeedbackRecordUserId: number         // 反馈的用户ID
  userFeedbackRecordName: string           // 反馈人
  userFeedbackRecordTitle: string          // 标题
  userFeedbackRecordContent: string        // 内容
  userFeedbackRecordTime: string           // 创建时间
  userFeedbackRecordCurrentStepSolveTime: string  // 处理时间
  userFeedbackRecordOldStep: string        // 原节点
  userFeedbackRecordNewStep: string        // 现阶段
  userFeedbackRecordApprovalPersonId: number    // 处理人ID
  adminFeedbackRecordApprovalPersonName: string  // 处理人姓名
}

// ============= 系统删除简历（UserDeleteResumeBySystem）=============

// 系统删除简历查询条件
export interface UserDeleteResumeQuery {
  userDeleteResumeResumeName?: string      // 简历名称
  userDeleteResumeIndustryName?: string    // 行业名称
  userDeleteResumeUserId?: number          // 用户ID
}

// 系统删除简历分页VO
export interface UserDeleteResumeBySystemPageVO {
  userDeleteResumeBySystemId: number       // 系统删除简历ID
  userDeleteResumeBySystemResumeName: string  // 简历名称
  userDeleteResumeBySystemIndustryName: string  // 行业
  userDeleteResumeBySystemResumeReactCode: string  // React组件代码
  userDeleteResumeBySystemCreatedTime: string  // 创建时间
  userDeleteResumeBySystemUpdatedTime: string  // 更新时间
  userDeleteResumeBySystemSortedNum: number  // 排序序号
  userDeleteResumeBySystemUserId: number   // 所属用户ID
  userDeleteResumeBySystemRecycleTime: string  // 回收时间
}

// 系统删除简历详情VO
export interface UserDeleteResumeBySystemInfoVO {
  userDeleteResumeBySystemId: number       // 系统删除简历ID
  userDeleteResumeBySystemResumeName: string  // 简历名称
  userDeleteResumeBySystemIndustryName: string  // 行业
  userDeleteResumeBySystemResumeReactCode: string  // React组件代码
  userDeleteResumeBySystemCreatedTime: string  // 创建时间
  userDeleteResumeBySystemUpdatedTime: string  // 更新时间
  userDeleteResumeBySystemSortedNum: number  // 排序序号
  userDeleteResumeBySystemUserId: number   // 所属用户ID
  userDeleteResumeBySystemRecycleTime: string  // 回收时间
}

// 系统删除简历详情VO（旧版本，保持兼容）
export interface UserDeleteResumeInfoVO {
  userDeleteResumeResumeName: string       // 简历名称
  userDeleteResumeIndustryName: string     // 行业
  userDeleteResumeResumeReactCode: string  // React组件代码
  userDeleteResumeCreatedTime: string      // 创建时间
  userDeleteResumeUpdatedTime: string      // 更新时间
  userDeleteResumeSortedNum: number        // 排序序号
  userDeleteResumeUserId: number           // 所属用户ID
  userDeleteResumeDeleteTime: string       // 删除时间
}

// ============= 简历评分训练数据（ScoreTrainingData）=============

export interface AdminScoreTrainingDataQuery {
  scoreTrainingDataResumeName?: string
  scoreTrainingDataIndustryName?: string
  scoreTrainingDataDataSource?: number
}

export interface AdminScoreTrainingDataForm {
  scoreTrainingDataId?: number
  scoreTrainingDataResumeName: string
  scoreTrainingDataIndustryName: string
  scoreTrainingDataResumeContent: string
  scoreTrainingDataLabelScore: number | null
  scoreTrainingDataDataSource: number
}

export interface AdminScoreTrainingDataPageVO {
  scoreTrainingDataId: number
  scoreTrainingDataResumeName: string
  scoreTrainingDataIndustryName: string
  scoreTrainingDataLabelScore: number
  scoreTrainingDataDataSource: number
  scoreTrainingDataCreateTime?: string
}

export interface AdminScoreTrainingDataInfoVO {
  scoreTrainingDataId: number
  scoreTrainingDataResumeName: string
  scoreTrainingDataIndustryName: string
  scoreTrainingDataResumeContent: string
  scoreTrainingDataLabelScore: number
  scoreTrainingDataDataSource: number
  scoreTrainingDataCreateTime?: string
}

// ============= 评分模型训练代码（ScoreModelTrainCode）=============

export interface AdminScoreModelTrainCodeQuery {
  scoreModelTrainCodeName?: string
  scoreModelTrainCodeLanguage?: string
}

export interface AdminScoreModelTrainCodeForm {
  scoreModelTrainCodeId?: number
  scoreModelTrainCodeName: string
  scoreModelTrainCodeVersion: string
  scoreModelTrainCodeLanguage: string
  scoreModelTrainCodeContent: string
  scoreModelTrainCodeDesc: string
}

export interface AdminScoreModelTrainCodePageVO {
  scoreModelTrainCodeId: number
  scoreModelTrainCodeName: string
  scoreModelTrainCodeVersion: string
  scoreModelTrainCodeLanguage: string
  scoreModelTrainCodeDesc: string
  scoreModelTrainCodeCreateTime?: string
}

export interface AdminScoreModelTrainCodeInfoVO {
  scoreModelTrainCodeId: number
  scoreModelTrainCodeName: string
  scoreModelTrainCodeVersion: string
  scoreModelTrainCodeLanguage: string
  scoreModelTrainCodeContent: string
  scoreModelTrainCodeDesc: string
  scoreModelTrainCodeCreateTime?: string
}

// ============= 笔试专项题库分类管理 =============

export interface AdminQuestionFirstCategoryForm {
  questionFirstCategoryId?: number
  questionFirstCategoryName: string
  questionFirstCategoryIntroduce: string
}

export interface AdminQuestionFirstCategoryQuery {
  questionFirstCategoryName?: string
  questionFirstCategoryIntroduce?: string
}

export interface AdminQuestionFirstCategoryPageVO {
  questionFirstCategoryId: number
  questionFirstCategoryName: string
  questionFirstCategoryIntroduce: string
  questionFirstCategoryCreateTime?: string
}

export interface AdminQuestionFirstCategoryInfoVO {
  questionFirstCategoryId: number
  questionFirstCategoryName: string
  questionFirstCategoryIntroduce: string
  questionFirstCategoryCreateTime?: string
}

export interface AdminQuestionSecondCategoryForm {
  questionSecondCategoryId?: number
  questionFirstCategoryId: number | undefined
  questionSecondCategoryName: string
  questionSecondCategoryIntroduce: string
}

export interface AdminQuestionSecondCategoryQuery {
  questionFirstCategoryId?: number
  questionSecondCategoryName?: string
  questionSecondCategoryIntroduce?: string
}

export interface AdminQuestionSecondCategoryPageVO {
  questionSecondCategoryId: number
  questionFirstCategoryId: number
  questionSecondCategoryName: string
  questionSecondCategoryIntroduce: string
  questionSecondCategoryCreateTime?: string
}

export interface AdminQuestionSecondCategoryInfoVO {
  questionSecondCategoryId: number
  questionFirstCategoryId: number
  questionSecondCategoryName: string
  questionSecondCategoryIntroduce: string
  questionSecondCategoryCreateTime?: string
}

// ============= 内容管理（Content Management）=============

export interface ProjectIntroduceForm {
  projectIntroduceId?: number
  projectIntroduceTitle: string
  projectIntroduceContent: string
}

export interface ProjectIntroduceInfoVO {
  projectIntroduceId?: number
  projectIntroduceTitle: string
  projectIntroduceContent: string
  projectIntroduceUpdatedTime?: string
}

export interface TeamIntroduceForm {
  teamIntroduceId?: number
  teamIntroduceTitle: string
  teamIntroduceContent: string
}

export interface TeamIntroduceInfoVO {
  teamIntroduceId?: number
  teamIntroduceTitle: string
  teamIntroduceContent: string
  teamIntroduceUpdatedTime?: string
}

export interface DevelopHistoryForm {
  developHistoryId?: number
  developHistoryTitle: string
  developHistoryContent: string
}

export interface DevelopHistoryInfoVO {
  developHistoryId?: number
  developHistoryTitle: string
  developHistoryContent: string
  developHistoryUpdatedTime?: string
}

export interface JoinUsForm {
  joinUsId?: number
  joinUsTitle: string
  joinUsContent: string
}

export interface JoinUsInfoVO {
  joinUsId?: number
  joinUsTitle: string
  joinUsContent: string
  joinUsUpdatedTime?: string
}

export interface PartnerIntroduceForm {
  partnerIntroduceId?: number
  partnerIntroduceTitle: string
  partnerIntroduceContent: string
}

export interface PartnerIntroduceInfoVO {
  partnerIntroduceId?: number
  partnerIntroduceTitle: string
  partnerIntroduceContent: string
  partnerIntroduceUpdatedTime?: string
}

export interface MediaReportForm {
  mediaReportId?: number
  mediaReportTitle: string
  mediaReportContent: string
}

export interface MediaReportInfoVO {
  mediaReportId?: number
  mediaReportTitle: string
  mediaReportContent: string
  mediaReportUpdatedTime?: string
}

export interface CustomerServiceForm {
  customerServiceId?: number
  customerServiceTitle: string
  customerServiceContent: string
}

export interface CustomerServiceInfoVO {
  customerServiceId?: number
  customerServiceTitle: string
  customerServiceContent: string
  customerServiceUpdatedTime?: string
}

export interface FaqForm {
  faqId?: number
  faqTitle: string
  faqContent: string
}

export interface FaqQuery {
  faqTitle?: string
}

export interface FaqPageVO {
  faqId: number
  faqTitle: string
  faqContent: string
  faqCreatedTime?: string
  faqUpdatedTime?: string
}

export interface FaqInfoVO {
  faqId?: number
  faqTitle: string
  faqContent: string
  faqCreatedTime?: string
  faqUpdatedTime?: string
}

export interface UserGuideForm {
  userGuideId?: number
  userGuideTitle: string
  userGuideContent: string
}

export interface UserGuideQuery {
  userGuideTitle?: string
}

export interface UserGuidePageVO {
  userGuideId: number
  userGuideTitle: string
  userGuideContent: string
  userGuideCreatedTime?: string
  userGuideUpdatedTime?: string
}

export interface UserGuideInfoVO {
  userGuideId?: number
  userGuideTitle: string
  userGuideContent: string
  userGuideCreatedTime?: string
  userGuideUpdatedTime?: string
}
