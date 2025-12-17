import request from '@utils/request'
import type { PaginationParams } from '@types/index'

// 职位信息相关API
export const jobAPI = {
  // 分页查询招聘信息 (POST 请求)
  getJobs: (params: PaginationParams & { query?: any }) => {
    return request.post('/user/employmentInformation/getEmploymentInformationPage', params.query || {}, {
      params: {
        pageNum: params.pageNum,
        pageSize: params.pageSize
      }
    })
  },

  // 获取职位详情
  getJobDetail: (employmentInformationId: number) => {
    return request.get('/user/employmentInformation/getEmploymentInformationInfo', {
      params: { employmentInformationId }
    })
  }
}

// 求职攻略相关API
export const adviceAPI = {
  // 分页查询求职攻略
  getArticles: (params: PaginationParams & { query?: any }) => {
    return request.get('/user/jobAdviceArticle/getJobAdviceArticlePage', {
      params: {
        size: params.pageSize,
        page: params.pageNum,
      },
      data: params.query
    })
  },

  // 获取攻略详情
  getArticleDetail: (jobAdviceArticleId: number) => {
    return request.get('/user/jobAdviceArticle/getJobAdviceArticleInfo', {
      params: { jobAdviceArticleId }
    })
  }
}