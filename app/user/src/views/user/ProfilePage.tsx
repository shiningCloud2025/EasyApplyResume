import React from 'react'
import { Card, Avatar, Button, Descriptions, Tag } from 'antd'
import { UserOutlined, EditOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useUserStore } from '@stores/userStore'

const ProfilePage: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useUserStore()

  const formatSalary = (salary: number) => {
    return new Intl.NumberFormat('zh-CN', {
      style: 'currency',
      currency: 'CNY',
      minimumFractionDigits: 0,
    }).format(salary)
  }

  const getJobPositionName = (position: number) => {
    const positions = [
      '前端开发工程师', '后端开发工程师', '全栈开发工程师', '产品经理',
      'UI设计师', '数据分析师', '运营专员', '其他'
    ]
    return positions[position - 1] || '未知'
  }

  return (
    <div className="profile-page">
      <Card
        className="profile-card"
        title="个人信息"
        extra={
          <Button 
            type="primary" 
            icon={<EditOutlined />}
            onClick={() => navigate('/profile/edit')}
          >
            编辑资料
          </Button>
        }
      >
        <div className="profile-header">
          <Avatar
            src={user?.userImage}
            icon={<UserOutlined />}
            size={120}
            className="profile-avatar"
          />
          <div className="profile-info">
            <h2 className="profile-name">{user?.userUsername}</h2>
            <p className="profile-account">账号：{user?.userAccount}</p>
          </div>
        </div>

        <Descriptions
          title="基本信息"
          bordered
          column={{ xs: 1, sm: 2 }}
          className="profile-descriptions"
        >
          <Descriptions.Item label="邮箱">
            {user?.userEmail || '未填写'}
          </Descriptions.Item>
          <Descriptions.Item label="手机号">
            {user?.userPhone || '未填写'}
          </Descriptions.Item>
          <Descriptions.Item label="个人介绍" span={2}>
            {user?.userIntroduce || '这个人很懒，什么都没有留下~'}
          </Descriptions.Item>
        </Descriptions>

        <Descriptions
          title="求职意向"
          bordered
          column={{ xs: 1, sm: 2 }}
          className="profile-descriptions"
        >
          <Descriptions.Item label="目标岗位">
            <Tag color="blue">
              {user?.userDreamPosition ? getJobPositionName(user.userDreamPosition) : '未设置'}
            </Tag>
          </Descriptions.Item>
          <Descriptions.Item label="期望工作天数">
            {user?.userDreamWeekWorkDayNum ? `${user.userDreamWeekWorkDayNum}天/周` : '未设置'}
          </Descriptions.Item>
          <Descriptions.Item label="薪资范围">
            {user?.userDreamMinMonthSalary && user?.userDreamMaxMonthSalary
              ? `${formatSalary(user.userDreamMinMonthSalary)} - ${formatSalary(user.userDreamMaxMonthSalary)}`
              : '未设置'
            }
          </Descriptions.Item>
          <Descriptions.Item label="期望福利" span={2}>
            {user?.userDreamGoodWelfare || '未填写'}
          </Descriptions.Item>
        </Descriptions>

        <Descriptions
          title="账户信息"
          bordered
          column={{ xs: 1, sm: 2 }}
          className="profile-descriptions"
        >
          <Descriptions.Item label="注册时间">
            {user?.userCreatedTime ? new Date(user.userCreatedTime).toLocaleDateString('zh-CN') : '未知'}
          </Descriptions.Item>
          <Descriptions.Item label="最后登录">
            {user?.userLoginTime ? new Date(user.userLoginTime).toLocaleDateString('zh-CN') : '未知'}
          </Descriptions.Item>
        </Descriptions>
      </Card>
    </div>
  )
}

export default ProfilePage