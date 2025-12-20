import React, { useState, useEffect } from 'react'
import { 
  Card, 
  Button, 
  Input, 
  Empty, 
  Modal, 
  message,
  Tag,
  Tooltip,
  Spin,
  Tabs
} from 'antd'
import { 
  PlusOutlined, 
  EditOutlined, 
  DeleteOutlined, 
  CopyOutlined, 
  EyeOutlined,
  HeartOutlined,
  RestOutlined
} from '@ant-design/icons'
import { useQuery, useMutation, useQueryClient } from 'react-query'
import { resumeAPI, ResumeSearchQuery } from '@api/resume'
import { useNavigate } from 'react-router-dom'
import type { UserResume } from '@types/index'
import { useUserStore } from '@stores/userStore'
import { LiveProvider, LivePreview, LiveError } from 'react-live'
import './MyResumes.scss'

const { Search } = Input

// React代码预览组件（小尺寸）
const ReactCodePreview: React.FC<{ code: string }> = ({ code }) => {
  if (!code) {
    return (
      <div className="preview-placeholder">
        <span>暂无预览</span>
      </div>
    )
  }

  const processCode = (rawCode: string): string => {
    let processed = rawCode
      .replace(/import\s+.*?from\s+['"].*?['"]\s*;?/g, '')
      .replace(/import\s+['"].*?['"]\s*;?/g, '')
      .replace(/export\s+default\s+/g, '')
      .replace(/export\s+/g, '')
      .trim()

    if (processed.match(/^(const|function|class)\s+\w+/)) {
      const componentMatch = processed.match(/^(?:const|function|class)\s+(\w+)/)
      if (componentMatch) {
        const componentName = componentMatch[1]
        processed = `${processed}\n\nrender(<${componentName} />)`
      }
    }
    return processed
  }

  const scope = {
    React,
    useState: React.useState,
    useEffect: React.useEffect,
  }

  return (
    <LiveProvider code={processCode(code)} scope={scope} noInline={true}>
      <div className="mini-preview">
        <LivePreview />
      </div>
      <LiveError className="preview-error" />
    </LiveProvider>
  )
}

const MyResumes: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useUserStore()
  const queryClient = useQueryClient()
  const [searchKeyword, setSearchKeyword] = useState('')
  const [searchQuery, setSearchQuery] = useState<ResumeSearchQuery>({})
  const [deleteModalVisible, setDeleteModalVisible] = useState(false)
  const [selectedResume, setSelectedResume] = useState<UserResume | null>(null)
  const [activeTab, setActiveTab] = useState('my-resumes')

  // 获取用户简历列表（支持搜索）
  const {
    data: resumesData = [],
    isLoading,
    error,
    refetch
  } = useQuery(
    ['user-resumes', user?.userId, searchQuery],
    () => {
      if (!user?.userId) return Promise.resolve([])
      return resumeAPI.getUserResumes(user.userId, searchQuery)
    },
    {
      enabled: !!user?.userId,
      select: (response) => response.data || [],
      onSuccess: (data) => {
        console.log('获取简历列表成功:', data)
      },
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '获取简历列表失败'
        message.error(errorMsg)
        console.error('API错误:', error)
      }
    }
  )

  // 获取用户收藏的模板
  const {
    data: collectionsData = [],
    isLoading: collectionsLoading,
    refetch: refetchCollections
  } = useQuery(
    ['user-collections', user?.userId],
    () => {
      if (!user?.userId) return Promise.resolve([])
      return resumeAPI.getUserCollections(user.userId)
    },
    {
      enabled: !!user?.userId && activeTab === 'my-collections',
      select: (response) => response.data || []
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
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '删除简历失败'
        message.error(errorMsg)
        console.error('删除失败:', error)
      }
    }
  )

  const handleSearch = (value: string) => {
    setSearchKeyword(value)
    // 更新搜索参数
    const newQuery = value ? { userSaveResumeResumeName: value } : {}
    setSearchQuery(newQuery)
    // 强制重新获取数据
    setTimeout(() => refetch(), 0)
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
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || '复制简历失败'
      message.error(errorMsg)
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

  // 后端已经处理搜索，直接使用返回数据
  const filteredResumes = Array.isArray(resumesData) ? resumesData : []

  const handleOpenRecycleBin = () => {
    navigate('/resume/recycle-bin')
  }

  return (
    <div className="my-resumes">
      {/* 页面标题 + Tab切换 */}
      <div className="page-header">
        <Tabs 
          activeKey={activeTab} 
          onChange={setActiveTab}
          items={[
            { key: 'my-resumes', label: '我的简历' },
            { key: 'my-collections', label: '我的收藏' }
          ]}
        />
      </div>

      {/* 筛选卡片 */}
      <Card className="filter-card" style={{ marginBottom: 24 }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Search
            placeholder="搜索简历名称"
            onSearch={handleSearch}
            style={{ width: 300 }}
            allowClear
          />
          <Button
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => navigate('/resume/templates')}
          >
            创建新简历
          </Button>
        </div>
      </Card>

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
      ) : activeTab === 'my-resumes' ? (
        filteredResumes.length === 0 ? (
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
        ) : (
          <div className="resume-grid">
            {filteredResumes.map((resume) => (
              <Card
                key={resume.userSaveResumeSortedNum}
                hoverable
                className="resume-card"
                cover={
                  <div 
                    className="resume-preview-container"
                    onClick={() => handleEdit(resume)}
                  >
                    <ReactCodePreview code={resume.userSaveResumeResumeReactCode} />
                  </div>
                }
              >
                <div className="resume-info" onClick={() => handleEdit(resume)}>
                  <h3 className="resume-name">
                    <span className="resume-order">#{resume.userSaveResumeSortedNum}</span>
                    {resume.userSaveResumeResumeName}
                  </h3>
                  <div className="resume-meta">
                    <Tag color={getIndustryColor(resume.userSaveResumeIndustry)}>
                      {resume.userSaveResumeIndustryName || getIndustryName(resume.userSaveResumeIndustry)}
                    </Tag>
                  </div>
                  <div className="resume-times">
                    <span>创建：{new Date(resume.userSaveResumeCreatedTime).toLocaleDateString('zh-CN')}</span>
                    <span>更新：{new Date(resume.userSaveResumeUpdatedTime).toLocaleDateString('zh-CN')}</span>
                  </div>
                </div>
                <div className="resume-actions">
                  <Tooltip title="编辑">
                    <Button type="text" icon={<EditOutlined />} onClick={() => handleEdit(resume)} />
                  </Tooltip>
                  <Tooltip title="删除">
                    <Button type="text" danger icon={<DeleteOutlined />} onClick={() => handleDelete(resume)} />
                  </Tooltip>
                </div>
              </Card>
            ))}
          </div>
        )
      ) : (
        // 我的收藏 Tab
        collectionsLoading ? (
          <div style={{ textAlign: 'center', padding: '50px' }}>
            <Spin size="large" />
          </div>
        ) : collectionsData.length === 0 ? (
          <Empty
            description="暂无收藏"
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          >
            <Button type="primary" onClick={() => navigate('/resume/templates')}>
              去收藏模板
            </Button>
          </Empty>
        ) : (
          <div className="resume-grid">
            {collectionsData.map((collection: any) => (
              <Card
                key={collection.userCollectionsSortedNum}
                hoverable
                className="resume-card"
                cover={
                  <div className="resume-preview-container">
                    <ReactCodePreview code={collection.resumeTemplateReactCode} />
                  </div>
                }
                onClick={() => navigate(`/resume/template/${collection.resumeTemplateId}`)}
              >
                <div className="resume-info">
                  <h3 className="resume-name">{collection.resumeTemplateName || '收藏模板'}</h3>
                  <div className="resume-meta">
                    <Tag color="purple"><HeartOutlined /> 已收藏</Tag>
                  </div>
                  <div className="resume-times">
                    <span>收藏时间：{new Date(collection.createdTime).toLocaleDateString('zh-CN')}</span>
                  </div>
                </div>
              </Card>
            ))}
          </div>
        )
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

      {/* 回收站入口 */}
      <div 
        className="recycle-bin-entry"
        onClick={handleOpenRecycleBin}
        style={{
          position: 'fixed',
          right: 32,
          bottom: 100,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          padding: '16px 20px',
          background: '#fff',
          borderRadius: 12,
          boxShadow: '0 4px 16px rgba(0,0,0,0.15)',
          cursor: 'pointer',
          transition: 'all 0.3s',
          zIndex: 999
        }}
      >
        <RestOutlined style={{ fontSize: 28, color: '#666' }} />
        <span style={{ fontSize: 13, color: '#666', marginTop: 6, fontWeight: 500 }}>回收站</span>
      </div>
    </div>
  )
}

export default MyResumes
