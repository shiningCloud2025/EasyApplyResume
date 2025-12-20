import React, { useState } from 'react'
import { 
  Card, 
  Button, 
  Empty, 
  Modal, 
  message,
  Tag,
  Tooltip,
  Spin,
  Input,
  Pagination,
  Tabs,
  Drawer
} from 'antd'
import { 
  ArrowLeftOutlined,
  DeleteOutlined,
  UndoOutlined,
  ExclamationCircleOutlined,
  SearchOutlined,
  EyeOutlined,
  CodeOutlined
} from '@ant-design/icons'
import { useQuery, useMutation, useQueryClient } from 'react-query'
import { resumeAPI, ResumeSearchQuery } from '@api/resume'
import { useNavigate } from 'react-router-dom'
import { useUserStore } from '@stores/userStore'
import { LiveProvider, LivePreview, LiveError } from 'react-live'
import './MyResumes.scss'

const { Search } = Input

// React代码预览组件（复用）
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

const RecycleBin: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useUserStore()
  const queryClient = useQueryClient()
  const [restoreModalVisible, setRestoreModalVisible] = useState(false)
  const [selectedResume, setSelectedResume] = useState<any>(null)
  const [searchKeyword, setSearchKeyword] = useState('')
  const [searchQuery, setSearchQuery] = useState<ResumeSearchQuery>({})
  const [currentPage, setCurrentPage] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [previewVisible, setPreviewVisible] = useState(false)
  const [previewResume, setPreviewResume] = useState<any>(null)

  // 获取回收站简历列表（支持搜索）
  const {
    data: deletedResumes = [],
    isLoading,
    refetch
  } = useQuery(
    ['deleted-resumes', user?.userId, searchQuery],
    () => {
      if (!user?.userId) return Promise.resolve([])
      console.log('调用回收站接口: userId=', user.userId, 'query=', searchQuery)
      return resumeAPI.getDeletedResumes(user.userId, searchQuery)
    },
    {
      enabled: !!user?.userId,
      select: (response) => {
        console.log('回收站接口返回:', response)
        return response.data || []
      },
      staleTime: 0, // 每次都重新获取
      cacheTime: 0, // 不缓存
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '获取回收站列表失败'
        message.error(errorMsg)
      }
    }
  )

  // 恢复简历mutation
  const restoreMutation = useMutation(
    (resumeData: any) => resumeAPI.restoreResume(resumeData),
    {
      onSuccess: () => {
        message.success('简历已恢复')
        setRestoreModalVisible(false)
        setSelectedResume(null)
        queryClient.invalidateQueries(['deleted-resumes', user?.userId])
        queryClient.invalidateQueries(['user-resumes', user?.userId])
      },
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '恢复简历失败'
        message.error(errorMsg)
      }
    }
  )

  // 清空回收站mutation
  const clearTrashMutation = useMutation(
    () => resumeAPI.clearTrash(user!.userId),
    {
      onSuccess: () => {
        message.success('回收站已清空')
        queryClient.invalidateQueries(['deleted-resumes', user?.userId])
      },
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '清空回收站失败'
        message.error(errorMsg)
      }
    }
  )

  const handleRestore = (resume: any) => {
    setSelectedResume(resume)
    setRestoreModalVisible(true)
  }

  const confirmRestore = () => {
    if (!selectedResume) return
    restoreMutation.mutate(selectedResume)
  }

  const handleClearTrash = () => {
    Modal.confirm({
      title: '确认清空回收站',
      icon: <ExclamationCircleOutlined />,
      content: '清空后所有简历将被永久删除，无法恢复！',
      okText: '确认清空',
      okType: 'danger',
      cancelText: '取消',
      onOk: () => {
        clearTrashMutation.mutate()
      }
    })
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

  // 搜索过滤 - 调用后端接口
  const handleSearch = (value: string) => {
    setSearchKeyword(value)
    setCurrentPage(1) // 搜索时重置到第一页
    // 更新搜索参数
    const newQuery = value ? { userSaveResumeResumeName: value } : {}
    setSearchQuery(newQuery)
    // 强制重新获取数据
    setTimeout(() => refetch(), 0)
  }

  // 点击卡片预览
  const handlePreview = (resume: any) => {
    setPreviewResume(resume)
    setPreviewVisible(true)
  }

  // 后端已经处理搜索，直接使用返回数据
  const filteredResumes = Array.isArray(deletedResumes) ? deletedResumes : []

  // 分页数据
  const paginatedResumes = filteredResumes.slice(
    (currentPage - 1) * pageSize,
    currentPage * pageSize
  )

  const handlePageChange = (page: number, size?: number) => {
    setCurrentPage(page)
    if (size) setPageSize(size)
  }

  return (
    <div className="my-resumes recycle-bin-page">
      {/* 页面标题 */}
      <div className="page-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 16 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
          <Button icon={<ArrowLeftOutlined />} onClick={() => navigate('/resume/my-resumes')}>
            返回
          </Button>
          <h2 style={{ margin: 0 }}>回收站</h2>
          <span style={{ color: '#999', fontSize: 14 }}>
            删除的简历将在30天后自动清除
          </span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: 16 }}>
          <Search
            placeholder="搜索简历名称"
            allowClear
            onSearch={handleSearch}
            onChange={(e) => !e.target.value && handleSearch('')}
            style={{ width: 240 }}
            prefix={<SearchOutlined />}
          />
          {deletedResumes.length > 0 && (
            <Button 
              danger 
              icon={<DeleteOutlined />}
              onClick={handleClearTrash}
              loading={clearTrashMutation.isLoading}
            >
              清空回收站
            </Button>
          )}
        </div>
      </div>

      {/* 内容区域 */}
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
      ) : filteredResumes.length === 0 ? (
        <Empty
          description={searchKeyword ? `未找到包含"${searchKeyword}"的简历` : '回收站为空'}
          image={Empty.PRESENTED_IMAGE_SIMPLE}
          style={{ marginTop: 100 }}
        />
      ) : (
        <>
          <div className="resume-grid" style={{ marginTop: 24 }}>
            {paginatedResumes.map((resume: any) => (
              <Card
                key={resume.userDeleteResumeSortedNum}
                hoverable
                className="resume-card"
                cover={
                  <div className="resume-preview-container" onClick={() => handlePreview(resume)}>
                    <ReactCodePreview code={resume.userDeleteResumeResumeReactCode} />
                  </div>
                }
              >
                <div className="resume-info" onClick={() => handlePreview(resume)}>
                  <h3 className="resume-name">
                    <span className="resume-order">#{resume.userDeleteResumeSortedNum}</span>
                    {resume.userDeleteResumeResumeName}
                  </h3>
                  <div className="resume-meta">
                    <Tag color={getIndustryColor(resume.userDeleteResumeIndustry)}>
                      {resume.userDeleteResumeIndustryName || getIndustryName(resume.userDeleteResumeIndustry)}
                    </Tag>
                  </div>
                  <div className="resume-times">
                    <span>创建：{resume.userDeleteResumeCreatedTime ? new Date(resume.userDeleteResumeCreatedTime).toLocaleDateString('zh-CN') : '-'}</span>
                    <span>删除：{resume.userDeleteResumeDeleteTime ? new Date(resume.userDeleteResumeDeleteTime).toLocaleDateString('zh-CN') : '-'}</span>
                  </div>
                </div>
                <div className="resume-actions">
                  <Tooltip title="预览">
                    <Button type="text" icon={<EyeOutlined />} onClick={() => handlePreview(resume)} />
                  </Tooltip>
                  <Tooltip title="恢复">
                    <Button 
                      type="primary" 
                      icon={<UndoOutlined />} 
                      onClick={() => handleRestore(resume)}
                    >
                      恢复
                    </Button>
                  </Tooltip>
                </div>
              </Card>
            ))}
          </div>
          {filteredResumes.length > pageSize && (
            <div style={{ display: 'flex', justifyContent: 'center', marginTop: 24, marginBottom: 24 }}>
              <Pagination
                current={currentPage}
                pageSize={pageSize}
                total={filteredResumes.length}
                onChange={handlePageChange}
                showSizeChanger
                showQuickJumper
                showTotal={(total) => `共 ${total} 份简历`}
                pageSizeOptions={['10', '20', '30']}
              />
            </div>
          )}
        </>
      )}

      {/* 恢复确认弹窗 */}
      <Modal
        title="确认恢复"
        open={restoreModalVisible}
        onCancel={() => setRestoreModalVisible(false)}
        footer={[
          <Button key="cancel" onClick={() => setRestoreModalVisible(false)}>
            取消
          </Button>,
          <Button key="restore" type="primary" onClick={confirmRestore} loading={restoreMutation.isLoading}>
            确认恢复
          </Button>
        ]}
      >
        <p>确定要恢复简历"{selectedResume?.userDeleteResumeResumeName}"吗？</p>
        <p>恢复后简历将回到"我的简历"列表中。</p>
      </Modal>

      {/* 预览抽屉 */}
      <Drawer
        title={
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <span>#{previewResume?.userDeleteResumeSortedNum}</span>
            <span>{previewResume?.userDeleteResumeResumeName}</span>
          </div>
        }
        placement="right"
        open={previewVisible}
        onClose={() => setPreviewVisible(false)}
        width={700}
        extra={
          <Button 
            type="primary" 
            icon={<UndoOutlined />}
            onClick={() => {
              setPreviewVisible(false)
              handleRestore(previewResume)
            }}
          >
            恢复简历
          </Button>
        }
      >
        {previewResume && (
          <Tabs
            defaultActiveKey="preview"
            items={[
              {
                key: 'preview',
                label: '预览',
                children: (
                  <div style={{ 
                    border: '1px solid #e8e8e8', 
                    borderRadius: 8, 
                    padding: 20,
                    minHeight: 'calc(100vh - 200px)',
                    background: '#fafafa',
                    overflow: 'auto'
                  }}>
                    <LiveProvider 
                      code={(() => {
                        const code = previewResume.userDeleteResumeResumeReactCode || ''
                        let processed = code
                          .replace(/import\s+.*?from\s+['"].*?['"]\s*;?/g, '')
                          .replace(/import\s+['"].*?['"]\s*;?/g, '')
                          .replace(/export\s+default\s+/g, '')
                          .replace(/export\s+/g, '')
                          .trim()
                        if (processed.match(/^(const|function|class)\s+\w+/)) {
                          const match = processed.match(/^(?:const|function|class)\s+(\w+)/)
                          if (match) {
                            processed = `${processed}\n\nrender(<${match[1]} />)`
                          }
                        }
                        return processed
                      })()} 
                      scope={{ React, useState: React.useState, useEffect: React.useEffect }} 
                      noInline={true}
                    >
                      <LivePreview />
                      <LiveError style={{ color: 'red', marginTop: 10 }} />
                    </LiveProvider>
                  </div>
                )
              },
              {
                key: 'code',
                label: '代码',
                children: (
                  <div style={{ 
                    border: '1px solid #e8e8e8', 
                    borderRadius: 8, 
                    padding: 16,
                    background: '#1e1e1e',
                    maxHeight: 'calc(100vh - 200px)',
                    overflow: 'auto'
                  }}>
                    <pre style={{ 
                      margin: 0, 
                      color: '#d4d4d4', 
                      fontSize: 13,
                      lineHeight: 1.5,
                      whiteSpace: 'pre-wrap',
                      wordBreak: 'break-word'
                    }}>
                      {previewResume.userDeleteResumeResumeReactCode}
                    </pre>
                  </div>
                )
              },
              {
                key: 'info',
                label: '信息',
                children: (
                  <div style={{ padding: 16 }}>
                    <p><strong>简历名称：</strong>{previewResume.userDeleteResumeResumeName}</p>
                    <p><strong>行业：</strong>{previewResume.userDeleteResumeIndustryName || getIndustryName(previewResume.userDeleteResumeIndustry)}</p>
                    <p><strong>创建时间：</strong>{previewResume.userDeleteResumeCreatedTime ? new Date(previewResume.userDeleteResumeCreatedTime).toLocaleDateString('zh-CN') : '-'}</p>
                    <p><strong>更新时间：</strong>{previewResume.userDeleteResumeUpdatedTime ? new Date(previewResume.userDeleteResumeUpdatedTime).toLocaleDateString('zh-CN') : '-'}</p>
                    <p><strong>删除时间：</strong>{previewResume.userDeleteResumeDeleteTime ? new Date(previewResume.userDeleteResumeDeleteTime).toLocaleDateString('zh-CN') : '-'}</p>
                  </div>
                )
              }
            ]}
          />
        )}
      </Drawer>
    </div>
  )
}

export default RecycleBin
