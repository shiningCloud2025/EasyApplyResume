import React, { useState } from 'react'
import { Card, Input, Select, Button, Tag, Empty, Spin, Pagination, message, Tooltip, Row, Col, Modal } from 'antd'
import { SearchOutlined, ReloadOutlined, HeartOutlined, HeartFilled, EyeOutlined } from '@ant-design/icons'
import { useQuery, useQueryClient } from 'react-query'
import { resumeAPI } from '@api/resume'
import { jobAPI } from '@api/job'
import useUserStore from '@stores/userStore'
import { useNavigate } from 'react-router-dom'
import { LiveProvider, LivePreview, LiveError } from 'react-live'
import './ResumeTemplates.scss'

const { Option } = Select

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

interface ResumeTemplateQuery {
  resumeTemplateName?: string
  resumeTemplateIndustry?: number
}

const ResumeTemplates: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useUserStore()
  const queryClient = useQueryClient()
  const [query, setQuery] = useState<ResumeTemplateQuery>({
    resumeTemplateName: '',
    resumeTemplateIndustry: undefined
  })
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 8  // 一页8个（2行x4列）
  })

  // 收藏状态（悬停时查询）
  const [collectedStatuses, setCollectedStatuses] = useState<Record<number, boolean | 'loading'>>({})
  const [hoveredTemplateId, setHoveredTemplateId] = useState<number | null>(null)

  // 使用模板弹窗
  const [useModalVisible, setUseModalVisible] = useState(false)
  const [selectedTemplate, setSelectedTemplate] = useState<any>(null)
  const [resumeName, setResumeName] = useState('')
  const [creating, setCreating] = useState(false)

  // 获取行业列表
  const { data: industries } = useQuery(
    ['industries'],
    () => jobAPI.getAllIndustries(),
    {
      select: (response) => response.data || [],
      staleTime: 1000 * 60 * 10
    }
  )

  // 获取简历模板列表
  const { data: templatesData, isLoading, error, refetch } = useQuery(
    ['templates', pagination, query],
    () => resumeAPI.getTemplates({
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
      query
    }),
    {
      keepPreviousData: true,
      select: (response) => response.data,
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '获取模板列表失败'
        message.error(errorMsg)
      }
    }
  )

  const handleIndustryChange = (value: number | undefined) => {
    setQuery({ ...query, resumeTemplateIndustry: value })
    setPagination({ ...pagination, current: 1 })
  }

  const handleReset = () => {
    setQuery({ resumeTemplateName: '', resumeTemplateIndustry: undefined })
    setPagination({ current: 1, pageSize: 8 })
  }

  const handleCardHover = async (templateId: number) => {
    setHoveredTemplateId(templateId)
    if (collectedStatuses[templateId] !== undefined || !user) return
    
    setCollectedStatuses(prev => ({ ...prev, [templateId]: 'loading' }))
    try {
      const res = await resumeAPI.checkTemplateCollected(user.userId, templateId)
      console.log('收藏状态接口返回:', res)
      // res 已经是 { code, message, data } 结构，直接取 data
      setCollectedStatuses(prev => ({ ...prev, [templateId]: res.data === true }))
    } catch {
      setCollectedStatuses(prev => ({ ...prev, [templateId]: false }))
    }
  }

  const handleCollect = async (templateId: number, isCollected: boolean) => {
    if (!user) {
      message.warning('请先登录')
      navigate('/auth/login')
      return
    }
    try {
      await resumeAPI.collectTemplate(user.userId, templateId, !isCollected)
      message.success(isCollected ? '取消收藏成功' : '收藏成功')
      setCollectedStatuses(prev => ({ ...prev, [templateId]: !isCollected }))
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || '操作失败'
      message.error(errorMsg)
    }
  }

  const handlePreview = (templateId: number) => {
    navigate(`/resume/template/${templateId}`)
  }

  const handleUseTemplate = (template: any) => {
    if (!user) {
      message.warning('请先登录')
      navigate('/auth/login')
      return
    }
    setSelectedTemplate(template)
    setResumeName('')
    setUseModalVisible(true)
  }

  const handleConfirmUse = async () => {
    if (!user || !selectedTemplate) return
    setCreating(true)
    try {
      await resumeAPI.createResumeFromTemplate(user.userId, {
        resumeTemplateId: selectedTemplate.resumeTemplateId,
        resumeTemplateName: selectedTemplate.resumeTemplateName,
        resumeTemplateReactCode: selectedTemplate.resumeTemplateReactCode,
        industryMapIndustryName: selectedTemplate.industryMapIndustryName,
        isEnable: selectedTemplate.isEnable,
        createTime: selectedTemplate.resumeTemplateCreatedTime,
        updateTime: selectedTemplate.resumeTemplateUpdatedTime
      }, resumeName || undefined)
      message.success('创建成功')
      setUseModalVisible(false)
      // 使简历列表缓存失效，等待完成后再跳转
      await queryClient.invalidateQueries(['user-resumes', user.userId])
      navigate('/resume/my-resumes')
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || '创建失败'
      message.error(errorMsg)
    } finally {
      setCreating(false)
    }
  }

  const formatDate = (date: string | Date) => {
    if (!date) return '-'
    return new Date(date).toLocaleDateString('zh-CN')
  }

  return (
    <div className="resume-templates-page">
      {/* 页面标题 */}
      <div className="page-header">
        <h1>简历模板</h1>
        <p>浏览并选择适合您的简历模板</p>
      </div>

      {/* 筛选条件 */}
      <Card className="filter-card">
        <Row gutter={24} align="middle">
          <Col>
            <span className="filter-label">模板名称</span>
          </Col>
          <Col>
            <Input
              placeholder="请输入模板名称"
              value={query.resumeTemplateName}
              onChange={(e) => {
                setQuery({ ...query, resumeTemplateName: e.target.value })
                setPagination({ ...pagination, current: 1 })
              }}
              allowClear
              style={{ width: 200 }}
            />
          </Col>
          <Col>
            <span className="filter-label">行业</span>
          </Col>
          <Col>
            <Select
              placeholder="请选择行业"
              style={{ width: 160 }}
              value={query.resumeTemplateIndustry}
              onChange={handleIndustryChange}
              allowClear
            >
              {industries?.map((industry: any) => (
                <Option key={industry.industryMapIndustryCode} value={industry.industryMapIndustryCode}>
                  {industry.industryMapIndustryName}
                </Option>
              ))}
            </Select>
          </Col>
          <Col>
            <Button type="primary" icon={<SearchOutlined />} onClick={() => refetch()}>
              搜索
            </Button>
          </Col>
          <Col>
            <Button icon={<ReloadOutlined />} onClick={handleReset}>
              重置
            </Button>
          </Col>
        </Row>
      </Card>

      {/* 模板网格 */}
      <div className="templates-container">
        {isLoading ? (
          <div className="loading-wrapper"><Spin size="large" /></div>
        ) : error ? (
          <Empty description="加载失败">
            <Button type="primary" onClick={() => refetch()}>重试</Button>
          </Empty>
        ) : !templatesData?.records?.length ? (
          <Empty description="暂无模板" />
        ) : (
          <div className="template-grid">
            {templatesData.records.map((template: any) => {
              const templateId = template.resumeTemplateId
              const status = collectedStatuses[templateId]
              const isHovered = hoveredTemplateId === templateId
              const isCollected = status === true
              const isLoading = status === 'loading'

              return (
                <Card
                  key={templateId}
                  className="template-card"
                  hoverable
                  onMouseEnter={() => handleCardHover(templateId)}
                  onMouseLeave={() => setHoveredTemplateId(null)}
                  cover={
                    <div className="card-cover" onClick={() => handlePreview(templateId)}>
                      <ReactCodePreview code={template.resumeTemplateReactCode} />
                      <div className="cover-overlay">
                        <EyeOutlined /> 预览
                      </div>
                    </div>
                  }
                  actions={
                    isHovered && status !== undefined
                      ? [
                          <Tooltip title={isCollected ? '取消收藏' : '收藏'} key="collect">
                            <span onClick={() => !isLoading && handleCollect(templateId, isCollected)}>
                              {isLoading ? <HeartOutlined style={{ opacity: 0.5 }} /> 
                                : isCollected ? <HeartFilled style={{ color: '#ff4d4f' }} /> 
                                : <HeartOutlined />}
                            </span>
                          </Tooltip>,
                          <Button type="link" size="small" key="use" onClick={() => handleUseTemplate(template)}>
                            使用
                          </Button>
                        ]
                      : [
                          <Button type="link" size="small" key="use" onClick={() => handleUseTemplate(template)}>
                            使用
                          </Button>
                        ]
                  }
                >
                  <Card.Meta
                    title={<span className="card-title">{template.resumeTemplateName}</span>}
                    description={
                      <div className="card-desc">
                        <div className="tags">
                          {template.industryMapIndustryName && (
                            <Tag color="blue">{template.industryMapIndustryName}</Tag>
                          )}
                        </div>
                        <div className="date">创建：{formatDate(template.resumeTemplateCreatedTime)}</div>
                        <div className="date">更新：{formatDate(template.resumeTemplateUpdatedTime)}</div>
                      </div>
                    }
                  />
                </Card>
              )
            })}
          </div>
        )}

        {/* 分页 */}
        {templatesData?.total > 0 && (
          <div className="pagination-wrapper">
            <Pagination
              current={pagination.current}
              pageSize={pagination.pageSize}
              total={templatesData.total}
              onChange={(page, size) => setPagination({ current: page, pageSize: size })}
              showSizeChanger
              pageSizeOptions={['8', '12', '16', '24']}
              showTotal={(total) => `共 ${total} 个模板`}
            />
          </div>
        )}
      </div>

      {/* 使用模板弹窗 */}
      <Modal
        title="保存简历"
        open={useModalVisible}
        onCancel={() => setUseModalVisible(false)}
        onOk={handleConfirmUse}
        confirmLoading={creating}
        okText="确定"
        cancelText="取消"
      >
        <div style={{ marginBottom: 16 }}>
          <p>请输入简历名称（可选，不填则自动命名）：</p>
          <Input
            placeholder="请输入简历名称"
            value={resumeName}
            onChange={(e) => setResumeName(e.target.value)}
            maxLength={50}
          />
        </div>
      </Modal>
    </div>
  )
}

export default ResumeTemplates
