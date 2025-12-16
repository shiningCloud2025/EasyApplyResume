import request from '@utils/request'
import type { BaseResponse, ProvinceMap, CityMap, UniversityMap, RecruitPosition } from '@types/index'

// 省份相关API
export const provinceAPI = {
  // 获取所有省份（直接返回数组）
  getAllProvince: (): Promise<ProvinceMap[]> => {
    return request.get('/user/provinceMap/getAllProvince') as any
  },

  // 根据省份ID获取城市列表（直接返回数组）
  getCityByProvinceId: (provinceMapId: number): Promise<CityMap[]> => {
    return request.get('/user/provinceMap/getCityByProvinceId', {
      params: { provinceMapId }
    }) as any
  }
}

// 城市相关API
export const cityAPI = {
  // 获取所有城市
  getAllCity: (): Promise<CityMap[]> => {
    return request.get('/user/cityMap/getAllCity') as any
  },

  // 根据城市ID获取区县列表
  getAllAreaByCityId: (cityId: number) => {
    return request.get('/user/cityMap/getAllAreaByCityId', {
      params: { cityId }
    })
  }
}

// 招聘岗位相关API
export const recruitPositionAPI = {
  // 获取所有招聘岗位
  getAllRecruitPosition: (): Promise<BaseResponse<RecruitPosition[]>> => {
    return request.get('/user/recruitPosition/queryAllRecruitPositionPage')
  },

  // 根据ID查询岗位详情
  getRecruitPositionById: (recruitPositionId: number) => {
    return request.get('/user/recruitPosition/queryRecruitPosition', {
      params: { recruitPositionId }
    })
  }
}

// 大学相关API
export const universityAPI = {
  // 获取所有大学
  getAllUniversities: (): Promise<BaseResponse<UniversityMap[]>> => {
    return request.get('/user/universityMap/getAllUniversityMap')
  },

  // 模糊查询大学
  searchUniversities: (universityMapName: string): Promise<BaseResponse<UniversityMap[]>> => {
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