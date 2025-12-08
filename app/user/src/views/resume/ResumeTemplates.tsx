import React, { useState, useEffect } from 'react'
import { Card, Input, Select, Button, Tag, Image, Empty, Spin, Pagination, message, Tooltip } from 'antd'
import { SearchOutlined, HeartOutlined, HeartFilled, EyeOutlined, StarOutlined } from '@ant-design/icons'
import { useQuery } from 'react-query'
import { resumeAPI } from '@api/resume'
import useUserStore from '@stores/userStore'
import type { ResumeTemplate, ResumeTemplateQuery } from '@types/index'
import { useNavigate } from 'react-router-dom'

const { Option } = Select
const { Search } = Input
const { Meta } = Card

const ResumeTemplates: React.FC = () => {
  const navigate = useNavigate()
  const { user, isLoggedIn } = useUserStore()
  const [query, setQuery] = useState<ResumeTemplateQuery>({
    resumeTemplateName: '',
    resumeTemplateIndustry: undefined
  })
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 12
  })

  // 获取简历模板列表
  const {
    data: templatesData,
    isLoading,
    error,
    refetch
  } = useQuery(
    ['templates', pagination, query],
    () => resumeAPI.getTemplates({
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
      query
    }),
    {
      keepPreviousData: true,
      select: (response) => response.data,
      onSuccess: (data) => {
        console.log('获取模板成功:', data)
      },
      onError: (error) => {
        message.error('获取模板列表失败')
        console.error('API错误:', error)
      }
    }
  )

  // 检查收藏状态
  const { data: collectedStatuses } = useQuery(
    ['collection-status', templatesData?.records?.map(t => t.resumeTemplateId)],
    () => {
      if (!user || !templatesData?.records?.length) return null
      return Promise.all(
        templatesData.records.map(template =>
          resumeAPI.checkTemplateCollected(user.userId, template.resumeTemplateId)
            .then(response => response.data)
            .catch(() => false)
        )
      )
    },
    {
      enabled: !!user && !!templatesData?.records?.length,
      select: (results: any[]) => {
        const statusMap: Record<number, boolean> = {}
        templatesData.records.forEach((template: ResumeTemplate, index: number) => {
          statusMap[template.resumeTemplateId] = results[index]?.data || false
        })
        return statusMap
      },
      onError: (error) => {
        console.error('获取收藏状态失败:', error)
      }
    }
  )

  // 行业数据
  const industries = [
    { label: '全部', value: undefined },
    { label: '互联网', value: 1 },
    { label: '金融', value: 2 },
    { label: '教育', value: 3 },
    { label: '医疗', value: 4 },
    { label: '制造业', value: 5 }
  ]

  const handleSearch = (value: string) => {
    setQuery({ ...query, resumeTemplateName: value })
    setPagination({ ...pagination, current: 1 })
  }

  const handleIndustryChange = (value: number) => {
    setQuery({ ...query, resumeTemplateIndustry: value })
    setPagination({ ...pagination, current: 1 })
  }

  const handlePageChange = (page: number, pageSize: number) => {
    setPagination({ current: page, pageSize })
  }

  const handleCollect = async (templateId: number, isCurrentlyCollected: boolean) => {
    if (!user) {
      message.warning('请先登录')
      return
    }

    try {
      await resumeAPI.collectTemplate(user.userId, templateId, !isCurrentlyCollected)
      message.success(isCurrentlyCollected ? '取消收藏成功' : '收藏成功')
      refetch()
    } catch (error) {
      message.error('操作失败')
    }
  }

  const handleUseTemplate = async (template: ResumeTemplate) => {
    if (!user) {
      message.warning('请先登录')
      navigate('/auth/login')
      return
    }

    try {
      await resumeAPI.createResumeFromTemplate(user.userId, {
        resumeTemplateId: template.resumeTemplateId,
        resumeTemplateName: template.resumeTemplateName,
        resumeTemplateIndustry: template.resumeTemplateIndustry,
        resumeTemplateStyle: template.resumeTemplateStyle,
        resumeTemplatePreviewImage: template.resumeTemplatePreviewImage,
        resumeTemplateDescription: template.resumeTemplateDescription,
        resumeTemplateReactCode: template.resumeTemplateReactCode
      })
      message.success('正在创建简历...')
      setTimeout(() => {
        navigate('/resume/my-resumes')
      }, 1000)
    } catch (error) {
      message.error('创建简历失败')
    }
  }

  const handlePreview = (templateId: number) => {
    navigate(`/resume/template/${templateId}`)
  }

  const getIndustryName = (industryId: number) => {
    const industry = industries.find(item => item.value === industryId)
    return industry?.label || '其他'
  }

  const getStyleTagColor = (style: string) => {
    const colors: Record<string, string> = {
      modern: 'blue',
      classic: 'green',
      creative: 'orange',
      technical: 'purple'
    }
    return colors[style] || 'default'
  }

  const getStyleName = (style: string) => {
    const names: Record<string, string> = {
      modern: '现代',
      classic: '传统',
      creative: '创意',
      technical: '技术'
    }
    return names[style] || '默认'
  }

  const renderTemplateCard = (template: ResumeTemplate) => {
    const isCollected = collectedStatuses?.[template.resumeTemplateId] || false

    return (
      <Card
        hoverable
        className="template-card"
        cover={
          <div className="template-cover">
            <Image
              src={template.resumeTemplatePreviewImage}
              alt={template.resumeTemplateName}
              preview={false}
              className="template-image"
              onError={(e) => {
                const target = e.target as HTMLImageElement
                target.src = `https://via.placeholder.com/300x400/1890ff/ffffff?text=${encodeURIComponent(template.resumeTemplateName)}`
              }}
            />
            <div className="template-overlay">
              <Button
                type="primary"
                icon={<EyeOutlined />}
                onClick={() => handlePreview(template.resumeTemplateId)}
              >
                预览
              </Button>
            </div>
          </div>
        }
        actions={[
          <Tooltip title={isCollected ? '取消收藏' : '收藏'}>
            <Button
              type="text"
              icon={isCollected ? 
                <HeartFilled style={{ color: '#ff4d4f' }} /> : 
                <HeartOutlined />
              }
              onClick={() => handleCollect(template.resumeTemplateId, isCollected)}
              loading={isLoading}
            />
          </Tooltip>,
          <Button
            type="primary"
            onClick={() => handleUseTemplate(template)}
            disabled={isLoading}
          >
            使用模板
          </Button>
        ]}
      >
        <Meta
          title={
            <div className="template-title">
              <span>{template.resumeTemplateName}</span>
              <Tooltip title={`预览量：${Math.floor(Math.random() * 1000) + 100}`}>
                <StarOutlined style={{ marginLeft: 8 }} />
              </Tooltip>
            </div>
          }
          description={
            <div className="template-meta">
              <Tag color={getStyleTagColor(template.resumeTemplateStyle)}>
                {getStyleName(template.resumeTemplateStyle)}
              </Tag>
              <Tag color="cyan">
                {getIndustryName(template.resumeTemplateIndustry)}
              </Tag>
              <p className="template-desc">
                {template.resumeTemplateDescription}
              </p>
            </div>
          }
        />
      </Card>
    )
  }

  return (
    <div className="resume-templates">
      <Card className="filter-card" title="筛选条件">
        <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', alignItems: 'center' }}>
          <Search
            placeholder="搜索模板名称"
            onSearch={handleSearch}
            allowClear
            style={{ width: 200 }}
          />
          <Select
            placeholder="选择行业"
            style={{ width: 120 }}
            value={query.resumeTemplateIndustry}
            onChange={handleIndustryChange}
            allowClear
          >
            {industries.map(industry => (
              <Option key={industry.value} value={industry.value}>
                {industry.label}
              </Option>
            ))}
          </Select>
          <Button onClick={() => {
            setQuery({ resumeTemplateName: '', resumeTemplateIndustry: undefined })
            setPagination({ current: 1, pageSize: 12 })
          }}>
            重置筛选
          </Button>
        </div>
      </Card>

      {isLoading ? (
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
          {!templatesData?.records?.length ? (
            <Empty
              description="暂无匹配的模板"
              image={Empty.PRESENTED_IMAGE_SIMPLE}
            />
          ) : (
            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '24px' }}>
              {templatesData.records.map(template => (
                <div key={template.resumeTemplateId}>
                  {renderTemplateCard(template)}
                </div>
              ))}
            </div>
          )}

          {templatesData?.total && templatesData.total > pagination.pageSize && (
            <div className="pagination-wrapper">
              <Pagination
                current={pagination.current}
                pageSize={pagination.pageSize}
                total={templatesData.total}
                onChange={handlePageChange}
                showSizeChanger
                showQuickJumper
                pageSizeOptions={['12', '24', '36', '48']}
                showTotal={(total, range) =>
                  `第 ${range[0]}-${range[1]} 条，共 ${total} 条`
                }
              />
            </div>
          )}
        </>
      )}
    </div>
  )
}

export default ResumeTemplates