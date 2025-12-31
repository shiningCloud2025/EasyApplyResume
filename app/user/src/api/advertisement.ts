import request from '@utils/request'

// 广告信息接口
export interface AdvertisementInfo {
  advertisementId: number
  advertisementName: string
  advertisementUrl: string      // 广告图片URL
  advertisementLink: string     // 点击跳转链接
  advertisementStartedTime: string
  advertisementEndTime: string
}

// 广告相关API
export const advertisementAPI = {
  // 获取用户端所有广告
  getAllUserAdvertisements: (): Promise<AdvertisementInfo[]> => {
    return request.get('/admonitor/user/advertisement/findAllAdmonitorUserAdvertisement') as any
  }
}
