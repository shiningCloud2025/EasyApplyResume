import request from '@utils/request'
import type { PaginationParams } from '@types/index'

// 招聘信息相关API
export const jobAPI = {
  // 分页查询招聘信息 (POST)
  getJobs: (params: PaginationParams & { query?: any }) => {
    return request.post('/user/employmentInformation/getEmploymentInformationPage', params.query || {}, {
      params: {
        size: params.pageSize,
        page: params.pageNum
      }
    })
  },

  // 获取招聘信息详情
  getJobDetail: (employmentInformationId: number) => {
    return request.get('/user/employmentInformation/getEmploymentInformationInfo', {
      params: { employmentInformationId }
    })
  },

  // 获取所有行业列表
  getAllIndustries: () => {
    return request.get('/user/industryMap/findAllIndustryMap')
  }
}

// 求职攻略相关API
export const adviceAPI = {
  // 分页查询求职攻略 (POST)
  getArticles: (params: PaginationParams & { query?: any }) => {
    return request.post('/user/jobAdviceArticle/getJobAdviceArticlePage', params.query || {}, {
      params: {
        size: params.pageSize,
        page: params.pageNum
      }
    })
  },

  // 获取攻略详情
  getArticleDetail: (jobAdviceArticleId: number) => {
    return request.get('/user/jobAdviceArticle/getJobAdviceArticleInfo', {
      params: { jobAdviceArticleId }
    })
  }
}