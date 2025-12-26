import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 30000
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('monitor_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const res = response.data
    // 支持两种格式：{ code, data } 或直接返回数据
    if (res.code !== undefined) {
      if (res.code !== 200) {
        ElMessage.error(res.message || '请求失败')
        return Promise.reject(new Error(res.message || '请求失败'))
      }
      return res.data
    }
    return res
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('monitor_token')
      window.location.href = '/login'
    } else {
      ElMessage.error(error.response?.data?.message || error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

// 认证相关API
export const authApi = {
  // 账号密码登录
  login: (data: { accountOrPhoneOrEmail: string; password: string }) =>
    request.post('/admin/auth/formalLogin', data),
  
  // 手机验证码登录
  loginByPhone: (data: { phone: string; messageCode: string }) =>
    request.post('/admin/auth/phoneLogin', data),
  
  // 邮箱验证码登录
  loginByEmail: (data: { email: string; messageCode: string }) =>
    request.post('/admin/auth/emailLogin', data),
  
  // 发送邮箱验证码
  sendEmailCode: (email: string) =>
    request.get('/admin/email/sendEmailCode', { params: { email } }),
  
  // 获取用户信息
  getUserInfo: () => request.post('/admin/auth/getAdminInfo', {})
}

// 短信API
export const smsApi = {
  sendPhoneCode: (phone: string) =>
    request.get('/admin/sms/sendPhoneCode', { params: { phone } })
}

// 管理端数据统计API
export const adminStatisticsApi = {
  // 计算某天访问量
  calculateDailyVisitNum: (time: string) => {
    console.log('📤 calculateDailyVisitNum 发送参数:', time)
    return request.get('/admonitor/admin/dailyVisitNum/calculateAdmonitorAdminDailyVisitNum', { params: { time } })
  },
  
  // 总访问量
  getTotalVisitNum: () =>
    request.get('/admonitor/admin/dailyVisitNum/calculateAdmonitorAdminDailyVisitNumTotal'),
  
  // 今日新增访问量
  getTodayIncreaseVisitNum: () =>
    request.get('/admonitor/admin/dailyVisitNum/calculateDayIncreaseAdmonitorAdminDailyVisitNum'),
  
  // 查询时间段访问量
  getVisitNumByDateRange: (fromDate: string, endDate: string) =>
    request.get('/admonitor/admin/dailyVisitTotalNum/findFromTimeToEndTimeAdmonitorAdminDailyVisitTotalNum', 
      { params: { fromDate, endDate } }),
  
  // 查询时间段管理员数量
  getAdminNumByDateRange: (fromDate: string, endDate: string) =>
    request.get('/admonitor/adminDaliyAdminNum/findFromTimeToEndTimeAdmonitorAdminDaliyAdminNum', 
      { params: { fromDate, endDate } }),
  
  // 计算总管理员数量
  getTotalAdminNum: () =>
    request.get('/admonitor/adminDaliyAdminNum/calculateAdminTotalNum')
}

// 用户端数据统计API
export const userStatisticsApi = {
  // 计算某天访问量
  calculateDailyVisitNum: (time: string) =>
    request.get('/admonitor/user/dailyVisitNum/calculateAdmonitorUserDailyVisitNum', { params: { time } }),
  
  // 总访问量
  getTotalVisitNum: () =>
    request.get('/admonitor/user/dailyVisitNum/calculateAdmonitorUserDailyVisitNumTotal'),
  
  // 今日新增访问量
  getTodayIncreaseVisitNum: () =>
    request.get('/admonitor/user/dailyVisitNum/calculateDayIncreaseAdmonitorUserDailyVisitNum'),
  
  // 查询时间段访问量
  getVisitNumByDateRange: (fromDate: string, endDate: string) =>
    request.get('/admonitor/user/dailyVisitTotalNum/findFromTimeToEndTimeAdmonitorUserDailyVisitTotalNum', 
      { params: { fromDate, endDate } }),
  
  // 查询时间段用户数量
  getUserNumByDateRange: (fromDate: string, endDate: string) =>
    request.get('/admonitor/user/daliyUserNum/findFromTimeToEndTimeAdmonitorUserDaliyUserNum', 
      { params: { fromDate, endDate } }),
  
  // 计算总用户数量
  getTotalUserNum: () =>
    request.get('/admonitor/user/daliyUserNum/calculateUserTotalNum')
}

// 管理端广告API
export const adminAdvertisementApi = {
  // 添加广告
  add: (data: any) =>
    request.post('/admonitor/admin/advertisement/addAdmonitorAdminAdvertisement', data),
  
  // 更新广告
  update: (data: any) =>
    request.put('/admonitor/admin/advertisement/updateAdmonitorAdminAdvertisement', data),
  
  // 删除广告
  delete: (id: number) =>
    request.delete('/admonitor/admin/advertisement/deleteAdmonitorAdminAdvertisement', { params: { id } }),
  
  // 查询广告详情
  getById: (id: number) =>
    request.get('/admonitor/admin/advertisement/findAdmonitorAdminAdvertisementById', { params: { id } }),
  
  // 分页查询广告
  getByPage: (pageNum: number, pageSize: number, query?: any) =>
    request.post('/admonitor/admin/advertisement/findAdmonitorAdminAdvertisementByPage', query, 
      { params: { pageNum, pageSize } }),
  
  // 查询所有广告
  getAll: () =>
    request.get('/admonitor/admin/advertisement/findAllAdmonitorAdminAdvertisement')
}

// 用户端广告API
export const userAdvertisementApi = {
  // 添加广告
  add: (data: any) =>
    request.post('/admonitor/user/advertisement/addAdmonitorUserAdvertisement', data),
  
  // 更新广告
  update: (data: any) =>
    request.put('/admonitor/user/advertisement/updateAdmonitorUserAdvertisement', data),
  
  // 删除广告
  delete: (id: number) =>
    request.delete('/admonitor/user/advertisement/deleteAdmonitorUserAdvertisement', { params: { id } }),
  
  // 查询广告详情
  getById: (id: number) =>
    request.get('/admonitor/user/advertisement/findAdmonitorUserAdvertisementById', { params: { id } }),
  
  // 分页查询广告
  getByPage: (pageNum: number, pageSize: number, query?: any) =>
    request.post('/admonitor/user/advertisement/findAdmonitorUserAdvertisementByPage', query, 
      { params: { pageNum, pageSize } }),
  
  // 查询所有广告
  getAll: () =>
    request.get('/admonitor/user/advertisement/findAllAdmonitorUserAdvertisement')
}

// 监测端广告API
export const monitorAdvertisementApi = {
  // 添加广告
  add: (data: any) =>
    request.post('/admonitor/admonitor/advertisement/addAdmonitorAdvertisement', data),
  
  // 更新广告
  update: (data: any) =>
    request.put('/admonitor/admonitor/advertisement/updateAdmonitorAdvertisement', data),
  
  // 删除广告
  delete: (id: number) =>
    request.delete('/admonitor/admonitor/advertisement/deleteAdmonitorAdvertisement', { params: { id } }),
  
  // 查询广告详情
  getById: (id: number) =>
    request.get('/admonitor/admonitor/advertisement/findAdmonitorAdvertisementById', { params: { id } }),
  
  // 分页查询广告
  getByPage: (pageNum: number, pageSize: number, query?: any) =>
    request.post('/admonitor/admonitor/advertisement/findAdmonitorAdvertisementByPage', query, 
      { params: { pageNum, pageSize } }),
  
  // 查询所有广告
  getAll: () =>
    request.get('/admonitor/admonitor/advertisement/findAllAdmonitorAdvertisement')
}

// 管理端公告API
export const adminAnnouncementApi = {
  // 添加公告
  add: (data: any) =>
    request.post('/admonitor/admin/announcement/add', data),
  
  // 修改公告
  update: (data: any) =>
    request.put('/admonitor/admin/announcement/update', data),
  
  // 获取公告信息
  getInfo: () =>
    request.get('/admonitor/admin/announcement/getInfo')
}

// 用户端公告API
export const userAnnouncementApi = {
  // 添加公告
  add: (data: any) =>
    request.post('/admonitor/user/announcement/add', data),
  
  // 修改公告
  update: (data: any) =>
    request.put('/admonitor/user/announcement/update', data),
  
  // 获取公告信息
  getInfo: () =>
    request.get('/admonitor/user/announcement/getInfo')
}

// 监测端公告API
export const monitorAnnouncementApi = {
  // 添加公告
  add: (data: any) =>
    request.post('/admonitor/admonitor/announcement/add', data),
  
  // 修改公告
  update: (data: any) =>
    request.put('/admonitor/admonitor/announcement/update', data),
  
  // 获取公告信息
  getInfo: () =>
    request.get('/admonitor/admonitor/announcement/getInfo')
}

export default request
