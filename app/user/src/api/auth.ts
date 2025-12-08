import request from '@utils/request'
import { 
  LoginForm, 
  PhoneLoginForm, 
  EmailLoginForm, 
  RegisterForm,
  BaseResponse 
} from '@types/index'

// 认证相关API
export const authAPI = {
  // 普通登录
  formalLogin: (data: LoginForm): Promise<BaseResponse<string>> => {
    return request.post('/user/auth/formalLogin', data)
  },

  // 手机验证码登录
  phoneLogin: (data: PhoneLoginForm): Promise<BaseResponse<string>> => {
    return request.post('/user/auth/phoneLogin', data)
  },

  // 邮箱验证码登录
  emailLogin: (data: EmailLoginForm): Promise<BaseResponse<string>> => {
    return request.post('/user/auth/emailLogin', data)
  },

  // 用户注册
  register: (data: RegisterForm): Promise<BaseResponse<string>> => {
    return request.post('/user/auth/formalRegister', data)
  },

  // 生成随机账号
  generateRandomAccount: (): Promise<BaseResponse<string>> => {
    return request.post('/user/auth/generateRandomAccount')
  },

  // 退出登录
  logout: (userId: number): Promise<BaseResponse<null>> => {
    return request.post('/user/auth/logout', { userId })
  },

  // 获取当前用户信息
  getUserInfo: (): Promise<BaseResponse<any>> => {
    return request.post('/user/auth/getUserInfo')
  }
}