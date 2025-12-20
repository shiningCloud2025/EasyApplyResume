import request from '@utils/request'
import type { PaginationParams } from '@types/index'

// 简历搜索查询参数类型
export interface ResumeSearchQuery {
  userSaveResumeResumeName?: string
  userSaveResumeIndustry?: number
}

// 简历相关API
export const resumeAPI = {
  // 分页查询简历模板
  getTemplates: (params: PaginationParams & { query?: any }) => {
    return request.post('/user/resumeTemplate/findResumeTemplateByPage', params.query || {}, {
      params: {
        pageNum: params.pageNum,
        pageSize: params.pageSize
      }
    })
  },

  // 查询模板详情
  getTemplateById: (resumeTemplateId: number) => {
    return request.get('/user/resumeTemplate/findResumeTemplateById', {
      params: { resumeTemplateId }
    })
  },

  // 检查是否收藏模板
  checkTemplateCollected: (userId: number, rtid: number) => {
    return request.get('/user/userCollections/isUserCollectResumeTemplate', {
      params: { userId, rtid }
    })
  },

  // 收藏或取消收藏模板
  collectTemplate: (userId: number, rtid: number, isCollect: boolean) => {
    return request.get('/user/userCollections/saveResumeTemplateByUserId', {
      params: { userId, rtid, isCollect }
    })
  },

  // 获取用户收藏的模板列表
  getUserCollections: (userId: number) => {
    return request.get('/user/userCollections/findUserCollectionsByUserId', {
      params: { userId }
    })
  },

  // 通过模板创建简历
  createResumeFromTemplate: (userId: number, templateData: any, resumeName?: string) => {
    return request.post('/user/saveResume/saveUserSaveResumeInfoFirst', templateData, {
      params: { userId, resumeName }
    })
  },

  // 获取用户所有简历（支持搜索）
  getUserResumes: (userId: number, query?: ResumeSearchQuery) => {
    return request.post('/user/saveResume/getUserSaveResumeInfoByUserId', query || {}, {
      params: { userId }
    })
  },

  // 获取指定简历
  getUserResume: (userId: number, sortedNum: number) => {
    return request.get('/user/saveResume/getUserSaveResumeInfoByUserIdAndResumeId', {
      params: { userId, userSaveResumeSortedNum: sortedNum }
    })
  },

  // 保存简历
  saveResume: (resumeData: any) => {
    return request.post('/user/saveResume/saveUserSaveResumeInfo', resumeData)
  },

  // 删除简历
  deleteResume: (userId: number, sortedNum: number) => {
    return request.delete('/user/saveResume/deleteUserSaveResumeInfoByUserIdAndResumeId', {
      params: { userId, userSaveResumeSortedNum: sortedNum }
    })
  },

  // 获取回收站简历（支持搜索）
  getDeletedResumes: (userId: number, query?: ResumeSearchQuery) => {
    return request.post('/user/deleteResume/getUserDeleteResumeInfoByUserId', query || {}, {
      params: { userId }
    })
  },

  // 获取回收站单个简历详情
  getDeletedResume: (userId: number, resumeSortedNum: number) => {
    return request.get('/user/deleteResume/getUserDeleteResumeInfoByUserIdAndResumeSortedNum', {
      params: { userId, resumeSortedNum }
    })
  },

  // 恢复简历
  restoreResume: (resumeData: any) => {
    return request.post('/user/deleteResume/addUserDeleteResumeToUserSaveResume', resumeData)
  },

  // 清空回收站
  clearTrash: (userId: number) => {
    return request.delete('/user/deleteResume/clearUserAllDeleteResume', {
      params: { userId }
    })
  },

  // 修改回收站简历名称
  updateDeletedResumeName: (userId: number, resumeSortedNum: number, resumeName: string) => {
    return request.post('/user/deleteResume/updateUserDeleteResumeName', null, {
      params: { userId, resumeSortedNum, resumeName }
    })
  },

  // 修改用户简历名称
  updateResumeName: (userId: number, resumeSortedNum: number, resumeName: string) => {
    return request.post('/user/saveResume/updateUserDeleteResumeName', null, {
      params: { userId, resumeSortedNum, resumeName }
    })
  }
}
