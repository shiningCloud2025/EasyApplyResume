// 通用类型定义
export interface BaseResponse<T = any> {
  code: number
  message: string
  data: T
}

// 用户相关类型
export interface User {
  userId: number
  userAccount: string
  userUsername: string
  userEmail: string
  userPhone: string
  userPassword: string
  userImage?: string
  userIntroduce?: string
  userCreatedTime: string
  userLoginTime: string
  userDreamPosition: number
  userDreamMinMonthSalary: number
  userDreamMaxMonthSalary: number
  userDreamWeekWorkDayNum: number
  userDreamGoodWelfare: string
}

// 登录表单类型
export interface LoginForm {
  accountOrPhoneOrEmail: string
  password: string
}

export interface PhoneLoginForm {
  phone: string
  verifyCode: string
}

export interface EmailLoginForm {
  email: string
  verifyCode: string
}

// 注册表单类型
export interface RegisterForm {
  userAccount: string
  userUsername: string
  userEmail: string
  userPhone: string
  userPassword: string
  userImage?: string
  userIntroduce?: string
  userDreamPosition?: number
  userDreamMinMonthSalary?: number
  userDreamMaxMonthSalary?: number
  userDreamWeekWorkDayNum?: number
  userDreamGoodWelfare?: string
  userRecruitLocationFirst?: string
  userRecruitLocationSecond?: string
  userUniversityCode?: number
  phoneMessageCode: string
  emailMessageCode: string
}

// 简历相关类型
export interface ResumeTemplate {
  resumeTemplateId: number
  resumeTemplateName: string
  resumeTemplateIndustry: number
  resumeTemplateStyle: string
  resumeTemplatePreviewImage: string
  resumeTemplateDescription: string
  resumeTemplateCreatedTime: string
  resumeTemplateUpdatedTime: string
}

export interface ResumeTemplateInfo extends ResumeTemplate {
  resumeTemplateReactCode: string
}

export interface UserResume {
  userSaveResumeId: number
  userSaveResumeResumeName: string
  userSaveResumeIndustry: number
  userSaveResumeResumeReactCode: string
  userSaveResumeCreatedTime: string
  userSaveResumeUpdatedTime: string
  userSaveResumeSortedNum: number
  userSaveResumeUserId: number
}

// 职位相关类型
export interface JobPosition {
  employmentInformationId: number
  employmentInformationCompanyName: string
  employmentInformationPositionName: string
  employmentInformationIndustry: number
  employmentInformationLocation: string
  employmentInformationSalary: string
  employmentInformationRequirements: string
  employmentInformationDescription: string
  employmentInformationCompanyLogo?: string
  employmentInformationCreatedTime: string
}

// 求职攻略类型
export interface JobAdviceArticle {
  jobAdviceArticleId: number
  jobAdviceArticleTitle: string
  jobAdviceArticleCategory: string
  jobAdviceArticleAuthor: string
  jobAdviceArticleContent: string
  jobAdviceArticleSummary: string
  jobAdviceArticleCoverImage?: string
  jobAdviceArticleTags?: string[]
  jobAdviceArticleViewCount: number
  jobAdviceArticleLikeCount: number
  jobAdviceArticleCreatedTime: string
  jobAdviceArticleUpdatedTime: string
}

// 反馈相关类型
export interface FeedbackForm {
  feedbackUserId: number
  feedbackTitle: string
  feedbackContent: string
  feedbackType: string
  feedbackPriority?: string
  feedbackImages?: string[]
}

export interface Feedback {
  feedbackId: number
  feedbackUserId: number
  feedbackTitle: string
  feedbackContent: string
  feedbackType: string
  feedbackStatus: string
  feedbackPriority: string
  feedbackImages?: string[]
  feedbackReply?: string
  feedbackCreatedTime: string
  feedbackUpdatedTime: string
}

// 大学相关类型
export interface University {
  universityCode: number
  universityName: string
  universityProvince: string
  universityCity: string
  universityLevel: string
}

// 分页类型
export interface PaginationParams {
  pageNum: number
  pageSize: number
}

export interface PaginatedResponse<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}