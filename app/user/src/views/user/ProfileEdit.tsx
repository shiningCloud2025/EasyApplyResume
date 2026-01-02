import React, { useState, useEffect } from 'react'
import { Card, Form, Input, Button, InputNumber, Select, message, Avatar, Row, Col, Upload } from 'antd'
import { ArrowLeftOutlined, UserOutlined, UploadOutlined } from '@ant-design/icons'
import type { UploadFile, UploadProps } from 'antd'
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
  const { user, updateUser } = useUserStore()
  const [form] = Form.useForm()
  const [uploading, setUploading] = useState(false)
  
  // 监听头像链接变化，实时预览
  const userImageValue = Form.useWatch('userImage', form)
  
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
        userAccount: user.userAccount,
        userUsername: user.userUsername,
        userEmail: user.userEmail,
        userPhone: user.userPhone,
        userImage: user.userImage,
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
      
      // 初始化时加载岗位、省份、大学数据
      loadPositions()
      loadProvinces()
      handleUniversitySearch('')
      
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

  // 处理头像上传
  const handleAvatarUpload: UploadProps['customRequest'] = async (options) => {
    const { file, onSuccess, onError } = options
    
    if (!user?.userId) {
      message.error('未获取到用户信息')
      return
    }

    const uploadFile = file as File
    
    // 验证文件类型
    if (!uploadFile.type.startsWith('image/')) {
      message.error('只能上传图片文件')
      return
    }

    // 验证文件大小（5MB）
    if (uploadFile.size / 1024 / 1024 > 5) {
      message.error('图片大小不能超过 5MB')
      return
    }

    setUploading(true)
    try {
      const formData = new FormData()
      formData.append('file', uploadFile)
      formData.append('userId', user.userId.toString())

      console.log('📤 开始上传头像，文件:', uploadFile.name)
      
      const response = await request.post('/user/file/uploadUserHeadImg', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      
      console.log('✅ 上传成功，URL:', response.data)
      
      // 自动填充到表单
      form.setFieldValue('userImage', response.data)
      message.success('上传成功')
      
      onSuccess?.(response.data)
    } catch (error: any) {
      console.error('❌ 上传失败:', error)
      message.error(error.message || '上传失败')
      onError?.(error)
    } finally {
      setUploading(false)
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
        
        updateUser({ 
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
    // 移除确认密码字段，不传给后端
    const { confirmPassword, ...submitData } = values
    
    updateMutation.mutate({
      ...submitData,
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
          <Avatar src={userImageValue || user?.userImage} icon={<UserOutlined />} size={100} />
        </div>

        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
          style={{ maxWidth: 600, margin: '0 auto' }}
        >
          <Form.Item
            label="头像"
            name="userImage"
            rules={[{ required: true, message: '请上传头像' }]}
          >
            <div>
              <Upload
                customRequest={handleAvatarUpload}
                showUploadList={false}
                accept="image/*"
              >
                <Button icon={<UploadOutlined />} loading={uploading}>
                  选择图片
                </Button>
              </Upload>
              <div style={{ fontSize: 12, color: '#999', marginTop: 8 }}>
                支持 JPG/PNG/GIF 格式，文件大小不超过 5MB
              </div>
            </div>
          </Form.Item>

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

          <Form.Item 
            label="密码" 
            name="userPassword"
            rules={[
              { required: true, message: '请输入密码' },
              { min: 6, message: '密码至少6位' },
              { max: 30, message: '密码最多30位' }
            ]}
          >
            <Input.Password placeholder="请输入密码" maxLength={30} />
          </Form.Item>

          <Form.Item 
            label="确认密码" 
            name="confirmPassword"
            dependencies={['userPassword']}
            rules={[
              { required: true, message: '请确认密码' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('userPassword') === value) {
                    return Promise.resolve()
                  }
                  return Promise.reject(new Error('两次密码不一致'))
                },
              }),
            ]}
          >
            <Input.Password placeholder="请再次输入密码" maxLength={30} />
          </Form.Item>

          <Form.Item 
            label="手机号" 
            name="userPhone"
            rules={[
              { required: true, message: '请输入手机号' },
              { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确' }
            ]}
          >
            <Input placeholder="请输入手机号" maxLength={11} />
          </Form.Item>

          <Form.Item 
            label="个人介绍" 
            name="userIntroduce"
            rules={[{ required: true, message: '请输入个人介绍' }]}
          >
            <TextArea placeholder="介绍一下自己吧" rows={4} maxLength={200} showCount />
          </Form.Item>

          <Card title="求职意向" size="small" style={{ marginBottom: 24 }}>
            <Form.Item 
              label="目标岗位" 
              name="userDreamPosition"
              rules={[{ required: true, message: '请选择目标岗位' }]}
            >
              <Select 
                placeholder="请选择目标岗位" 
                allowClear
                showSearch
                optionFilterProp="children"
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
                <Form.Item 
                  label="期望工作省份" 
                  name="userRecruitLocationFirst"
                  rules={[{ required: true, message: '请选择省份' }]}
                >
                  <Select
                    placeholder="请选择省份"
                    allowClear
                    showSearch
                    optionFilterProp="children"
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
                <Form.Item 
                  label="期望工作城市" 
                  name="userRecruitLocationSecond"
                  rules={[{ required: true, message: '请选择城市' }]}
                >
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

            <Form.Item 
              label="毕业院校" 
              name="userUniversityCode"
              rules={[{ required: true, message: '请选择毕业院校' }]}
            >
              <Select
                placeholder="请输入大学名称搜索"
                allowClear
                showSearch
                loading={universitySearchLoading}
                onSearch={handleUniversitySearch}
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

            <Form.Item 
              label="期望工作天数（每周）" 
              name="userDreamWeekWorkDayNum"
              rules={[{ required: true, message: '请输入每周工作天数' }]}
            >
              <InputNumber min={1} max={7} placeholder="请输入" style={{ width: '100%' }} addonAfter="天/周" />
            </Form.Item>

            <Row gutter={16}>
              <Col span={12}>
                <Form.Item 
                  label="最低月薪" 
                  name="userDreamMinMonthSalary"
                  rules={[{ required: true, message: '请输入最低月薪' }]}
                >
                  <InputNumber min={0} max={100000} placeholder="最低" style={{ width: '100%' }} addonAfter="元" />
                </Form.Item>
              </Col>
              <Col span={12}>
                <Form.Item 
                  label="最高月薪" 
                  name="userDreamMaxMonthSalary"
                  rules={[{ required: true, message: '请输入最高月薪' }]}
                >
                  <InputNumber min={0} placeholder="最高" style={{ width: '100%' }} addonAfter="元" />
                </Form.Item>
              </Col>
            </Row>

            <Form.Item 
              label="期望福利" 
              name="userDreamGoodWelfare"
              rules={[{ required: true, message: '请输入期望福利' }]}
            >
              <Input placeholder="如：五险一金、带薪年假、餐补等" maxLength={200} />
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
