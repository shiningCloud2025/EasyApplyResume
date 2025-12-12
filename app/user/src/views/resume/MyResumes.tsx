import React, { useState, useEffect } from 'react'
import { 
  Card, 
  List, 
  Button, 
  Input, 
  Empty, 
  Modal, 
  Popconfirm, 
  message,
  Tag,
  Tooltip,
  Avatar,
  Spin
} from 'antd'
import { 
  PlusOutlined, 
  EditOutlined, 
  DeleteOutlined, 
  CopyOutlined, 
  EyeOutlined,
  MoreOutlined
} from '@ant-design/icons'
import { useQuery, useMutation, useQueryClient } from 'react-query'
import { resumeAPI } from '@api/resume'
import { useNavigate } from 'react-router-dom'
import type { UserResume } from '@types/index'
import { useUserStore } from '@stores/userStore'

const { Search } = Input
const { TextArea, Text } = Input

const MyResumes: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useUserStore()
  const queryClient = useQueryClient()
  const [searchKeyword, setSearchKeyword] = useState('')
  const [deleteModalVisible, setDeleteModalVisible] = useState(false)
  const [selectedResume, setSelectedResume] = useState<UserResume | null>(null)

  // 获取用户简历列表
  const {
    data: resumesData = [],
    isLoading,
    error,
    refetch
  } = useQuery(
    ['user-resumes', user?.userId],
    () => {
      if (!user?.userId) return Promise.resolve([])
      return resumeAPI.getUserResumes(user.userId)
    },
    {
      enabled: !!user?.userId,
      select: (response) => response.data || [],
      onSuccess: (data) => {
        console.log('获取简历列表成功:', data)
      },
      onError: (error) => {
        message.error('获取简历列表失败')
        console.error('API错误:', error)
      }
    }
  )

  // 删除简历mutation
  const deleteResumeMutation = useMutation(
    (resumeId: number) => resumeAPI.deleteResume(user!.userId, resumeId),
    {
      onSuccess: () => {
        message.success('简历已移入回收站')
        setDeleteModalVisible(false)
        setSelectedResume(null)
        queryClient.invalidateQueries(['user-resumes', user?.userId])
      },
      onError: (error) => {
        message.error('删除简历失败')
        console.error('删除失败:', error)
      }
    }
  )

  const handleSearch = (value: string) => {
    setSearchKeyword(value)
  }

  const handleEdit = (resume: UserResume) => {
    navigate(`/resume/edit/${resume.userSaveResumeSortedNum}`)
  }

  const handlePreview = (resume: UserResume) => {
    Modal.info({
      title: '简历预览',
      width: 800,
      content: (
        <div style={{ padding: '20px' }}>
          <h3>{resume.userSaveResumeResumeName}</h3>
          <p>行业：{getIndustryName(resume.userSaveResumeIndustry)}</p>
          <p>创建时间：{new Date(resume.userSaveResumeCreatedTime).toLocaleDateString('zh-CN')}</p>
          <p>最后更新：{new Date(resume.userSaveResumeUpdatedTime).toLocaleDateString('zh-CN')}</p>
          <div style={{ 
            marginTop: 20, 
            padding: 20, 
            background: '#f5f5f5', 
            borderRadius: 8 
          }}>
            <p>简历内容将在这里显示...</p>
          </div>
        </div>
      )
    })
  }

  const handleDuplicate = async (resume: UserResume) => {
    try {
      const newResumeData = {
        ...resume,
        userSaveResumeResumeName: `${resume.userSaveResumeResumeName} (副本)`,
        userSaveResumeCreatedTime: new Date().toISOString(),
        userSaveResumeUpdatedTime: new Date().toISOString()
      }
      
      await resumeAPI.saveResume(newResumeData)
      message.success('简历复制成功')
      refetch()
    } catch (error) {
      message.error('复制简历失败')
      console.error('复制失败:', error)
    }
  }

  const handleDelete = (resume: UserResume) => {
    setSelectedResume(resume)
    setDeleteModalVisible(true)
  }

  const confirmDelete = () => {
    if (!selectedResume) return
    deleteResumeMutation.mutate(selectedResume.userSaveResumeSortedNum)
  }

  const getIndustryName = (industryId: number) => {
    const industries: Record<number, string> = {
      1: '互联网',
      2: '金融',
      3: '教育',
      4: '医疗',
      5: '制造业',
      6: '其他'
    }
    return industries[industryId] || '其他'
  }

  const getIndustryColor = (industryId: number) => {
    const colors: Record<number, string> = {
      1: 'blue',
      2: 'green',
      3: 'orange',
      4: 'red',
      5: 'purple',
      6: 'default'
    }
    return colors[industryId] || 'default'
  }

  const filteredResumes = resumesData.filter(resume =>
    resume.userSaveResumeResumeName.toLowerCase().includes(searchKeyword.toLowerCase())
  )

  return (
    <div className="my-resumes">
      <div className="page-header">
        <div className="header-content">
          <h2>我的简历</h2>
          <Button
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => navigate('/resume/templates')}
          >
            创建新简历
          </Button>
        </div>
        <div className="header-actions">
          <Search
            placeholder="搜索简历名称"
            onSearch={handleSearch}
            style={{ width: 300 }}
            allowClear
          />
        </div>
      </div>

      {!user ? (
        <div style={{ textAlign: 'center', padding: '50px' }}>
          <Empty
            description="请先登录"
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          >
            <Button type="primary" onClick={() => navigate('/auth/login')}>
              去登录
            </Button>
          </Empty>
        </div>
      ) : isLoading ? (
        <div style={{ textAlign: 'center', padding: '50px' }}>
          <Spin size="large" />
        </div>
      ) : error ? (
        <div style={{ textAlign: 'center', padding: '50px' }}>
          <Empty
            description="加载失败，请重试"
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          >
            <Button type="primary" onClick={() => refetch()}>
              重试
            </Button>
          </Empty>
        </div>
      ) : (
        <>
          <List
            grid={{ gutter: [24, 24], xs: 1, sm: 2, md: 2, lg: 3, xl: 3, xxl: 4 }}
            loading={isLoading}
            dataSource={filteredResumes}
            renderItem={(resume) => (
          <List.Item>
            <Card
              hoverable
              className="resume-card"
              actions={[
                <Tooltip title="预览">
                  <Button
                    type="text"
                    icon={<EyeOutlined />}
                    onClick={() => handlePreview(resume)}
                  />
                </Tooltip>,
                <Tooltip title="编辑">
                  <Button
                    type="text"
                    icon={<EditOutlined />}
                    onClick={() => handleEdit(resume)}
                  />
                </Tooltip>,
                <Tooltip title="复制">
                  <Button
                    type="text"
                    icon={<CopyOutlined />}
                    onClick={() => handleDuplicate(resume)}
                  />
                </Tooltip>,
                <Tooltip title="删除">
                  <Button
                    type="text"
                    danger
                    icon={<DeleteOutlined />}
                    onClick={() => handleDelete(resume)}
                  />
                </Tooltip>,
              ]}
            >
              <Card.Meta
                avatar={
                  <Avatar 
                    style={{ backgroundColor: '#1890ff' }}
                    size="large"
                  >
                    {resume.userSaveResumeResumeName.substring(0, 2)}
                  </Avatar>
                }
                title={
                  <div className="resume-card-title">
                    <Text strong>{resume.userSaveResumeResumeName}</Text>
                  </div>
                }
                description={
                  <div className="resume-card-desc">
                    <div className="resume-tags">
                      <Tag color={getIndustryColor(resume.userSaveResumeIndustry)}>
                        {getIndustryName(resume.userSaveResumeIndustry)}
                      </Tag>
                    </div>
                    <div className="resume-times">
                      <p>创建：{new Date(resume.userSaveResumeCreatedTime).toLocaleDateString('zh-CN')}</p>
                      <p>更新：{new Date(resume.userSaveResumeUpdatedTime).toLocaleDateString('zh-CN')}</p>
                    </div>
                  </div>
                }
              />
            </Card>
          </List.Item>
        )}
      />
          </>
      )}

      {user && !isLoading && !error && filteredResumes.length === 0 && (
        <Empty
          description={searchKeyword ? '未找到匹配的简历' : '暂无简历'}
          image={Empty.PRESENTED_IMAGE_SIMPLE}
        >
          {!searchKeyword && (
            <Button 
              type="primary" 
              icon={<PlusOutlined />}
              onClick={() => navigate('/resume/templates')}
            >
              创建简历
            </Button>
          )}
        </Empty>
      )}

      {/* 删除确认弹窗 */}
      <Modal
        title="确认删除"
        open={deleteModalVisible}
        onCancel={() => setDeleteModalVisible(false)}
        footer={[
          <Button key="cancel" onClick={() => setDeleteModalVisible(false)}>
            取消
          </Button>,
          <Button key="delete" type="primary" danger onClick={confirmDelete} loading={deleteResumeMutation.isLoading}>
            确认删除
          </Button>
        ]}
      >
        <p>确定要删除简历"{selectedResume?.userSaveResumeResumeName}"吗？</p>
        <p>删除后简历将移入回收站，仍可在30天内恢复。</p>
      </Modal>
    </div>
  )
}

export default MyResumes