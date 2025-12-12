import request from '@utils/request'

// 发送手机验证码
export const sendSmsCode = (phone: string): Promise<any> => {
  return request.post('/user/sms/send', null, {
    params: { phone }
  })
}

// 校验手机验证码
export const checkSmsCode = (phone: string, code: string): Promise<any> => {
  return request.post('/user/sms/check', null, {
    params: { phone, code }
  })
}

// 发送邮箱验证码
export const sendEmailCode = (email: string): Promise<any> => {
  return request.post('/user/email/loginandregister/send', null, {
    params: { email }
  })
}

// 校验邮箱验证码
export const checkEmailCode = (email: string, code: string): Promise<any> => {
  return request.post('/user/email/loginandregister/check', null, {
    params: { email, code }
  })
}