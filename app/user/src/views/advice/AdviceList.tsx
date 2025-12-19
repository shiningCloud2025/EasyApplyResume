import React, { useState } from 'react'
import {
  Card,
  Table,
  Row,
  Col,
  Input,
  Button,
  Tag,
  Empty,
  message
} from 'antd'
import { SearchOutlined, ReloadOutlined, ReadOutlined } from '@ant-design/icons'
import { useQuery } from 'react-query'
import { adviceAPI } from '@api/job'
import { useNavigate } from 'react-router-dom'
import './AdviceList.scss'

const AdviceList: React.FC = () => {
  const navigate = useNavigate()
  const [filters, setFilters] = useState({
    jobAdviceArticleTitle: '',
    jobAdviceArticleCategory: '',
    jobAdviceArticleTags: '',
    jobAdviceArticleAuthorName: ''
  })
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10
  })

  // 获取攻略列表
  const {
    data: articlesData,
    isLoading,
    error,
    refetch
  } = useQuery(
    ['adviceArticles', pagination, filters],
    () => adviceAPI.getArticles({
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
      query: filters
    }),
    {
      keepPreviousData: true,
      select: (response) => response.data,
      onError: () => {
        message.error('获取攻略列表失败')
      }
    }
  )

  const handleSearch = (value: string) => {
    setFilters({ ...filters, jobAdviceArticleTitle: value })
    setPagination({ ...pagination, current: 1 })
  }

  const handleFilterChange = (key: string, value: any) => {
    setFilters({ ...filters, [key]: value || '' })
    setPagination({ ...pagination, current: 1 })
  }

  const handlePageChange = (page: number, pageSize: number) => {
    setPagination({ current: page, pageSize })
  }

  const handleArticleClick = (articleId: number) => {
    navigate(`/advice/${articleId}`)
  }

  const formatDate = (dateString: string) => {
    if (!dateString) return '-'
    return new Date(dateString).toLocaleDateString('zh-CN')
  }

  return (
    <div className="advice-list-page">
      <div className="page-header">
        <h1>求职攻略</h1>
        <p>专业的求职指导，助您顺利拿到offer</p>
      </div>

      <Card className="filter-card">
        <Row gutter={24} align="middle">
          <Col>
            <span className="filter-label">攻略标题</span>
          </Col>
          <Col>
            <Input
              placeholder="请输入标题"
              value={filters.jobAdviceArticleTitle}
              onChange={(e) => handleFilterChange('jobAdviceArticleTitle', e.target.value)}
              allowClear
              style={{ width: 180 }}
            />
          </Col>
          <Col>
            <span className="filter-label">分类</span>
          </Col>
          <Col>
            <Input
              placeholder="请输入分类"
              value={filters.jobAdviceArticleCategory}
              onChange={(e) => handleFilterChange('jobAdviceArticleCategory', e.target.value)}
              allowClear
              style={{ width: 120 }}
            />
          </Col>
          <Col>
            <span className="filter-label">作者</span>
          </Col>
          <Col>
            <Input
              placeholder="请输入作者"
              value={filters.jobAdviceArticleAuthorName}
              onChange={(e) => handleFilterChange('jobAdviceArticleAuthorName', e.target.value)}
              allowClear
              style={{ width: 120 }}
            />
          </Col>
          <Col>
            <Button type="primary" icon={<SearchOutlined />} onClick={() => refetch()}>
              搜索
            </Button>
          </Col>
          <Col>
            <Button icon={<ReloadOutlined />} onClick={() => {
              setFilters({
                jobAdviceArticleTitle: '',
                jobAdviceArticleCategory: '',
                jobAdviceArticleTags: '',
                jobAdviceArticleAuthorName: ''
              })
              setPagination({ current: 1, pageSize: 10 })
            }}>
              重置
            </Button>
          </Col>
        </Row>
      </Card>

      <Card className="table-card">
        {error ? (
          <Empty
            description="加载失败，请重试"
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          >
            <Button type="primary" onClick={() => refetch()}>
              重试
            </Button>
          </Empty>
        ) : (
          <Table
            dataSource={articlesData?.records || []}
            loading={isLoading}
            rowKey="jobAdviceArticleId"
            onRow={(record: any) => ({
              onClick: () => handleArticleClick(record.jobAdviceArticleId),
              style: { cursor: 'pointer' }
            })}
            pagination={{
              current: pagination.current,
              pageSize: pagination.pageSize,
              total: articlesData?.total || 0,
              onChange: handlePageChange,
              showSizeChanger: true,
              showQuickJumper: true,
              pageSizeOptions: ['10', '20', '50'],
              showTotal: (total, range) => `第 ${range[0]}-${range[1]} 条，共 ${total} 条`
            }}
            columns={[
              {
                title: '标题',
                dataIndex: 'jobAdviceArticleTitle',
                key: 'title',
                render: (text: string) => (
                  <span className="article-title-cell">
                    <ReadOutlined style={{ marginRight: 8, color: '#1890ff' }} />
                    {text}
                  </span>
                )
              },
              {
                title: '分类',
                dataIndex: 'jobAdviceArticleCategory',
                key: 'category',
                width: 120,
                render: (text: string) => text ? <Tag color="blue">{text}</Tag> : '-'
              },
              {
                title: '标签',
                dataIndex: 'jobAdviceArticleTags',
                key: 'tags',
                width: 200,
                render: (text: string) => (
                  text ? (
                    <span>
                      {text.split(',').slice(0, 2).map((tag: string, idx: number) => (
                        <Tag key={idx} color="cyan">{tag.trim()}</Tag>
                      ))}
                    </span>
                  ) : '-'
                )
              },
              {
                title: '作者',
                dataIndex: 'jobAdviceArticleAuthorName',
                key: 'author',
                width: 120,
                render: (text: string) => text || '佚名'
              },
              {
                title: '发布时间',
                dataIndex: 'jobAdviceArticlePublishedTime',
                key: 'publishedTime',
                width: 120,
                render: (text: string) => formatDate(text)
              },
              {
                title: '最后更新',
                dataIndex: 'jobAdviceArticleUpdatedTime',
                key: 'updatedTime',
                width: 120,
                render: (text: string) => formatDate(text)
              }
            ]}
          />
        )}
      </Card>
    </div>
  )
}

export default AdviceList