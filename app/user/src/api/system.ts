import request from '@utils/request'

// 大学相关API
export const universityAPI = {
  // 获取所有大学
  getAllUniversities: () => {
    return request.get('/user/universityMap/getAllUniversityMap')
  },

  // 模糊查询大学
  searchUniversities: (universityMapName: string) => {
    return request.get('/user/universityMap/getAllUniversityMapByName', {
      params: { universityMapName }
    })
  }
}

// 系统回收相关API
export const systemAPI = {
  // 添加系统回收的简历
  addExpiredResume: (resumes: any[]) => {
    return request.post('/user/userDeleteResumeBySystemService/addExpiredUserDeleteResume', resumes)
  },

  // 清理系统回收站
  clearSystemTrash: () => {
    return request.post('/user/userDeleteResumeBySystemService/clearExpiredUserDeleteResumeEveryThreeMonth')
  }
}