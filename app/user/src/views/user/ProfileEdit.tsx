import React, { useState, useEffect } from 'react'
import { Card, Form, Input, Button, InputNumber, Select, message, Avatar, Row, Col } from 'antd'
import { ArrowLeftOutlined, UserOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useMutation, useQuery } from 'react-query'
import { useUserStore } from '@stores/userStore'
import { provinceAPI, universityAPI, recruitPositionAPI } from '@api/system'
import request from '@utils/request'
import type { ProvinceMap, CityMap, UniversityMap, RecruitPosition } from '@types/index'

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
  
  // 省市大学选择状态
  const [provinces, setProvinces] = useState<ProvinceMap[]>([])
  const [cities, setCities] = useState<CityMap[]>([])
  const [universities, setUniversities] = useState<UniversityMap[]>([])
  const [positions, setPositions] = useState<RecruitPosition[]>([])
  const [universitySearchLoading, setUniversitySearchLoading] = useState(false)

  // 初始化表单值
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
        userRecruitLocationFirst: user.userRecruitLocationFirst,
        userRecruitLocationSecond: user.userRecruitLocationSecond,
        userUniversityCode: user.userUniversityCode,
      })
      
      // 如果有省份，加载对应的城市列表
      if (user.userRecruitLocationFirst) {
        loadCities(user.userRecruitLocationFirst)
      }
    }
  }, [user, form])

  // 加载省份列表
  const loadProvinces = async () => {
    try {
      const response = await provinceAPI.getAllProvince()
      const data = Array.isArray(response) ? response : (response as any)?.data || []
      setProvinces(data)
    } catch (error) {
      console.error('加载省份失败:', error)
    }
  }

  // 加载城市列表
  const loadCities = async (provinceId: number) => {
    try {
      const response = await provinceAPI.getCityByProvinceId(provinceId)
      const data = Array.isArray(response) ? response : (response as any)?.data || []
      setCities(data)
    } catch (error) {
      console.error('加载城市失败:', error)
    }
  }

  // 加载岗位列表
  const loadPositions = async () => {
    try {
      const response = await recruitPositionAPI.getAllRecruitPosition()
      setPositions(response.data || [])
    } catch (error) {
      console.error('加载岗位失败:', error)
    }
  }

  // 搜索大学
  const handleUniversitySearch = async (keyword: string) => {
    setUniversitySearchLoading(true)
    try {
      const response = keyword 
        ? await universityAPI.searchUniversities(keyword)
        : await universityAPI.getAllUniversities()
      setUniversities(response.data || [])
    } catch (error) {
      console.error('搜索大学失败:', error)
    } finally {
      setUniversitySearchLoading(false)
    }
  }

  // 省份变化时加载城市
  const handleProvinceChange = (provinceId: number) => {
    form.setFieldValue('userRecruitLocationSecond', undefined)
    if (provinceId) {
      loadCities(provinceId)
    } else {
      setCities([])
    }
  }

  const updateMutation = useMutation(updateUserAPI, {
    onSuccess: (response) => {
      message.success('资料更新成功')
      if (user) {
        const formValues = form.getFieldsValue()
        // 找到对应的名称
        const province = provinces.find(p => p.provinceMapPid === formValues.userRecruitLocationFirst)
        const city = cities.find(c => c.cityMapCid === formValues.userRecruitLocationSecond)
        const uni = universities.find(u => u.universityMapId === formValues.userUniversityCode)
        
        setUser({ 
          ...user, 
          ...formValues,
          userRecruitLocationFirstName: province?.provinceMapPname,
          userRecruitLocationSecondName: city?.cityMapCname,
          userUniversityCodeName: uni?.universityMapName,
        })
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

  return (
    <div className="profile-edit-page" style={{ padding: 24 }}>
      <Card
        title={
          <div style={{ display: 'flex', alignItems: 'center', gap: 12 }}>
            <Button icon={<ArrowLeftOutlined />} onClick={() => navigate('/profile')}>
              返回
            </Button>
            <span>编辑个人资料</span>
          </div>
        }
      >
        <div style={{ display: 'flex', justifyContent: 'center', marginBottom: 24 }}>
          <Avatar src={user?.userImage} icon={<UserOutlined />} size={100} />
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

          <Form.Item label="账号" name="userAccount">
            <Input disabled style={{ backgroundColor: '#f5f5f5' }} />
          </Form.Item>

          <Form.Item label="邮箱" name="userEmail">
            <Input disabled style={{ backgroundColor: '#f5f5f5' }} />
          </Form.Item>

          <Form.Item label="手机号" name="userPhone">
            <Input placeholder="请输入手机号" maxLength={11} />
          </Form.Item>

          <Form.Item label="个人介绍" name="userIntroduce">
            <TextArea placeholder="介绍一下自己吧" rows={4} maxLength={200} showCount />
          </Form.Item>

          <Card title="求职意向" size="small" style={{ marginBottom: 24 }}>
            <Form.Item label="目标岗位" name="userDreamPosition">
              <Select 
                placeholder="请选择目标岗位" 
                allowClear
                showSearch
                optionFilterProp="children"
                onDropdownVisibleChange={(open) => open && positions.length === 0 && loadPositions()}
              >
                {positions.map(pos => (
                  <Option key={pos.recruitPositionId} value={pos.recruitPositionId}>
                    {pos.recruitPositionName}
                  </Option>
                ))}
              </Select>
            </Form.Item>

            <Row gutter={16}>
              <Col span={12}>
                <Form.Item label="期望工作省份" name="userRecruitLocationFirst">
                  <Select
                    placeholder="请选择省份"
                    allowClear
                    showSearch
                    optionFilterProp="children"
                    onDropdownVisibleChange={(open) => open && provinces.length === 0 && loadProvinces()}
                    onChange={handleProvinceChange}
                  >
                    {provinces.map(province => (
                      <Option key={province.provinceMapPid} value={province.provinceMapPid}>
                        {province.provinceMapPname}
                      </Option>
                    ))}
                  </Select>
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item label="期望工作城市" name="userRecruitLocationSecond">
                  <Select
                    placeholder={cities.length === 0 ? "请先选择省份" : "请选择城市"}
                    allowClear
                    showSearch
                    optionFilterProp="children"
                    disabled={cities.length === 0}
                  >
                    {cities.map(city => (
                      <Option key={city.cityMapCid} value={city.cityMapCid}>
                        {city.cityMapCname}
                      </Option>
                    ))}
                  </Select>
                </Form.Item>
              </Col>
            </Row>

            <Form.Item label="毕业院校" name="userUniversityCode">
              <Select
                placeholder="请输入大学名称搜索"
                allowClear
                showSearch
                loading={universitySearchLoading}
                onSearch={handleUniversitySearch}
                onDropdownVisibleChange={(open) => open && universities.length === 0 && handleUniversitySearch('')}
                filterOption={false}
                notFoundContent={universitySearchLoading ? '搜索中...' : '未找到匹配的大学'}
              >
                {universities.map(uni => (
                  <Option key={uni.universityMapId} value={uni.universityMapId}>
                    {uni.universityMapName}
                  </Option>
                ))}
              </Select>
            </Form.Item>

            <Form.Item label="期望工作天数（每周）" name="userDreamWeekWorkDayNum">
              <InputNumber min={1} max={7} placeholder="请输入" style={{ width: '100%' }} addonAfter="天/周" />
            </Form.Item>

            <Row gutter={16}>
              <Col span={12}>
                <Form.Item label="最低月薪" name="userDreamMinMonthSalary">
                  <InputNumber min={0} placeholder="最低" style={{ width: '100%' }} addonAfter="元" />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item label="最高月薪" name="userDreamMaxMonthSalary">
                  <InputNumber min={0} placeholder="最高" style={{ width: '100%' }} addonAfter="元" />
                </Form.Item>
              </Col>
            </Row>

            <Form.Item label="期望福利" name="userDreamGoodWelfare">
              <Input placeholder="如：五险一金、带薪年假、餐补等" />
            </Form.Item>
          </Card>

          <Form.Item>
            <div style={{ display: 'flex', gap: 16, justifyContent: 'center' }}>
              <Button onClick={() => navigate('/profile')}>取消</Button>
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
