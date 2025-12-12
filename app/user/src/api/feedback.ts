import request from '@utils/request'

// 用户管理相关API
export const userAPI = {
  // 更新用户信息
  updateUser: (userData: any) => {
    return request.post('/user/user/updateUser', userData)
  },

  // 根据用户ID查询用户信息
  getUserById: (userId: string) => {
    return request.get('/user/user/getUserByUserId', {
      params: { userId }
    })
  }
}

// 反馈相关API
export const feedbackAPI = {
  // 添加反馈
  addFeedback: (feedbackData: any) => {
    return request.post('/user/feedback/addFeedback', feedbackData)
  },

  // 分页查询反馈
  getFeedbacks: (params: any) => {
    return request.post('/user/feedback/getFeedbackPage', null, {
      params: {
        size: params.size,
        page: params.page,
      },
      data: params.query
    })
  },

  // 查询反馈详情
  getFeedbackDetail: (feedbackId: number) => {
    return request.get('/user/feedback/findFeedbackById', {
      params: { feedbackId }
    })
  },

  // 更新反馈阶段
  updateFeedbackStep: (feedbackId: number, operationCode: number, updateForm: any, operationPersonId: number) => {
    return request.post('/user/feedback/updateFeedbackStep', updateForm, {
      params: {
        feedbackId,
        OperationCode: operationCode,
        operationPersonId
      }
    })
  }
}