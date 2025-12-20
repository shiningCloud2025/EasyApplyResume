import React, { useState, useEffect } from 'react'
import { Card, Form, Input, Button, InputNumber, Select, message, Avatar, Upload } from 'antd'
import { ArrowLeftOutlined, UserOutlined, UploadOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useMutation } from 'react-query'
import { useUserStore } from '@stores/userStore'
import request from '@utils/request'

const { TextArea } = Input
const { Option } = Select

// 更新用户信息的API
const updateUserAPI = (data: any) => {
  return request.put('/user/user/updateUser', data)
}

const ProfileEdit: React.FC = () => {
  const navigate = useNavigate()
  const { user, setUser } = useUserStore()
  const [form] = Form.useForm()

  useEffect(() => {
    if (user) {
      form.setFieldsValue({
        userId: user.userId,
        userUsername: user.userUsername,
        userEmail: user.userEmail,
        userPhone: user.userPhone,
        userIntroduce: user.userIntroduce,
        userDreamPosition: user.userDreamPosition,
        userDreamWeekWorkDayNum: user.userDreamWeekWorkDayNum,
        userDreamMinMonthSalary: user.userDreamMinMonthSalary,
        userDreamMaxMonthSalary: user.userDreamMaxMonthSalary,
        userDreamGoodWelfare: user.userDreamGoodWelfare,
      })
    }
  }, [user, form])

  const updateMutation = useMutation(updateUserAPI, {
    onSuccess: (response) => {
      message.success('资料更新成功')
      // 更新本地用户信息
      if (user) {
        const formValues = form.getFieldsValue()
        setUser({ ...user, ...formValues })
      }
      navigate('/profile')
    },
    onError: (error: any) => {
      const errorMsg = error?.response?.data?.message || error?.message || '更新失败'
      message.error(errorMsg)
    }
  })

  const handleSubmit = (values: any) => {
    updateMutation.mutate({
      ...values,
      userId: user?.userId
    })
  }

  const jobPositions = [
    { value: 1, label: '前端开发工程师' },
    { value: 2, label: '后端开发工程师' },
    { value: 3, label: '全栈开发工程师' },
    { value: 4, label: '产品经理' },
    { value: 5, label: 'UI设计师' },
    { value: 6, label: '数据分析师' },
    { value: 7, label: '运营专员' },
    { value: 8, label: '其他' },
  ]

  return (
    <div className="profile-edit-page" style={{ padding: 24 }}>
      <Card
        title={
          <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
            <Button 
              icon={<ArrowLeftOutlined />} 
              onClick={() => navigate('/profile')}
            >
              返回
            </Button>
            <span>编辑个人资料</span>
          </div>
        }
      >
        <div style={{ display: 'flex', justifyContent: 'center', marginBottom: 24 }}>
          <Avatar
            src={user?.userImage}
            icon={<UserOutlined />}
            size={100}
          />
        </div>

        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          style={{ maxWidth: 600, margin: '0 auto' }}
        >
          <Form.Item
            label="用户名"
            name="userUsername"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input placeholder="请输入用户名" maxLength={20} />
          </Form.Item>

          <Form.Item
            label="邮箱"
            name="userEmail"
            rules={[{ type: 'email', message: '请输入正确的邮箱格式' }]}
          >
            <Input placeholder="请输入邮箱" disabled />
          </Form.Item>

          <Form.Item
            label="手机号"
            name="userPhone"
          >
            <Input placeholder="请输入手机号" maxLength={11} />
          </Form.Item>

          <Form.Item
            label="个人介绍"
            name="userIntroduce"
          >
            <TextArea 
              placeholder="介绍一下自己吧" 
              rows={4} 
              maxLength={200}
              showCount
            />
          </Form.Item>

          <Card title="求职意向" size="small" style={{ marginBottom: 24 }}>
            <Form.Item
              label="目标岗位"
              name="userDreamPosition"
            >
              <Select placeholder="请选择目标岗位" allowClear>
                {jobPositions.map(pos => (
                  <Option key={pos.value} value={pos.value}>{pos.label}</Option>
                ))}
              </Select>
            </Form.Item>

            <Form.Item
              label="期望工作天数（每周）"
              name="userDreamWeekWorkDayNum"
            >
              <InputNumber 
                min={1} 
                max={7} 
                placeholder="请输入" 
                style={{ width: '100%' }}
                addonAfter="天/周"
              />
            </Form.Item>

            <div style={{ display: 'flex', gap: 16 }}>
              <Form.Item
                label="最低月薪"
                name="userDreamMinMonthSalary"
                style={{ flex: 1 }}
              >
                <InputNumber 
                  min={0} 
                  placeholder="最低" 
                  style={{ width: '100%' }}
                  addonAfter="元"
                />
              </Form.Item>
              <Form.Item
                label="最高月薪"
                name="userDreamMaxMonthSalary"
                style={{ flex: 1 }}
              >
                <InputNumber 
                  min={0} 
                  placeholder="最高" 
                  style={{ width: '100%' }}
                  addonAfter="元"
                />
              </Form.Item>
            </div>

            <Form.Item
              label="期望福利"
              name="userDreamGoodWelfare"
            >
              <Input placeholder="如：五险一金、带薪年假、餐补等" />
            </Form.Item>
          </Card>

          <Form.Item>
            <div style={{ display: 'flex', gap: 16, justifyContent: 'center' }}>
              <Button onClick={() => navigate('/profile')}>
                取消
              </Button>
              <Button type="primary" htmlType="submit" loading={updateMutation.isLoading}>
                保存修改
              </Button>
            </div>
          </Form.Item>
        </Form>
      </Card>
    </div>
  )
}

export default ProfileEdit
