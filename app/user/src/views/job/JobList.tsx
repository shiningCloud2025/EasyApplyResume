import React, { useState, useEffect } from 'react'
import { 
  Card, 
  List, 
  Row, 
  Col, 
  Input, 
  Select, 
  Button, 
  Tag, 
  Avatar, 
  Empty, 
  Spin, 
  Pagination,
  message
} from 'antd'
import { SearchOutlined, BankOutlined, EnvironmentOutlined, CalendarOutlined } from '@ant-design/icons'
import { useQuery } from 'react-query'
import { jobAPI } from '@api/job'
import { useNavigate } from 'react-router-dom'
import type { JobPosition } from '@types/index'

const { Search } = Input
const { Option } = Select

const JobList: React.FC = () => {
  const navigate = useNavigate()
  const [filters, setFilters] = useState({
    keyword: '',
    industry: undefined,
    location: undefined,
    salary: undefined
  })
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 12
  })

  // 获取职位列表
  const {
    data: jobsData,
    isLoading,
    error,
    refetch
  } = useQuery(
    ['jobs', pagination, filters],
    () => jobAPI.getJobs({
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
      query: filters
    }),
    {
      keepPreviousData: true,
      select: (response) => response.data,
      onSuccess: (data) => {
        console.log('获取职位列表成功:', data)
      },
      onError: (error) => {
        message.error('获取职位列表失败')
        console.error('API错误:', error)
      }
    }
  )

      const industries = [
    { label: '全部', value: undefined },
    { label: '互联网', value: 1 },
    { label: '金融', value: 2 },
    { label: '教育', value: 3 },
    { label: '医疗', value: 4 },
    { label: '制造业', value: 5 }
  ]

  const locations = [
    { label: '全部', value: undefined },
    { label: '北京', value: 'beijing' },
    { label: '上海', value: 'shanghai' },
    { label: '杭州', value: 'hangzhou' },
    { label: '深圳', value: 'shenzhen' },
    { label: '广州', value: 'guangzhou' }
  ]

  const salaryRanges = [
    { label: '全部', value: undefined },
    { label: '10k以下', value: '0-10' },
    { label: '10k-20k', value: '10-20' },
    { label: '20k-30k', value: '20-30' },
    { label: '30k以上', value: '30-999' }
  ]

  const handleSearch = (value: string) => {
    setFilters({ ...filters, keyword: value })
    setPagination({ ...pagination, current: 1 })
  }

  const handleFilterChange = (key: string, value: any) => {
    setFilters({ ...filters, [key]: value })
    setPagination({ ...pagination, current: 1 })
  }

  const handlePageChange = (page: number, pageSize: number) => {
    setPagination({ current: page, pageSize })
  }

  const handleJobClick = (jobId: number) => {
    navigate(`/job/${jobId}`)
  }

  const getIndustryName = (industryId: number) => {
    const industry = industries.find(item => item.value === industryId)
    return industry?.label || '其他'
  }

  const getDaysAgo = (dateString: string) => {
    const now = new Date()
    const date = new Date(dateString)
    const diffTime = Math.abs(now.getTime() - date.getTime())
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    
    if (diffDays === 0) return '今天'
    if (diffDays === 1) return '昨天'
    if (diffDays <= 7) return `${diffDays}天前`
    return date.toLocaleDateString('zh-CN')
  }

  return (
    <div className="job-list-page">
      <Card className="filter-card" title="职位筛选">
        <Row gutter={[16, 16]}>
          <Col xs={24} sm={12} md={6}>
            <Search
              placeholder="搜索职位或公司"
              onSearch={handleSearch}
              allowClear
            />
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Select
              placeholder="选择行业"
              style={{ width: '100%' }}
              value={filters.industry}
              onChange={(value) => handleFilterChange('industry', value)}
              allowClear
            >
              {industries.map(industry => (
                <Option key={industry.value} value={industry.value}>
                  {industry.label}
                </Option>
              ))}
            </Select>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Select
              placeholder="工作地点"
              style={{ width: '100%' }}
              value={filters.location}
              onChange={(value) => handleFilterChange('location', value)}
              allowClear
            >
              {locations.map(location => (
                <Option key={location.value} value={location.value}>
                  {location.label}
                </Option>
              ))}
            </Select>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Select
              placeholder="薪资范围"
              style={{ width: '100%' }}
              value={filters.salary}
              onChange={(value) => handleFilterChange('salary', value)}
              allowClear
            >
              {salaryRanges.map(range => (
                <Option key={range.value} value={range.value}>
                  {range.label}
                </Option>
              ))}
            </Select>
          </Col>
        </Row>
      </Card>

      <Spin spinning={isLoading}>
        {error ? (
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
        ) : !jobsData?.records?.length ? (
          <Empty
            description="暂无匹配的职位"
            image={Empty.PRESENTED_IMAGE_SIMPLE}
          />
        ) : (
          <>
            <List
              itemLayout="vertical"
              size="large"
              dataSource={jobsData.records}
              renderItem={(job) => (
                <List.Item
                  key={job.employmentInformationId}
                  className="job-item"
                  onClick={() => handleJobClick(job.employmentInformationId)}
                >
                  <List.Item.Meta
                    avatar={
                      <Avatar 
                        src={job.employmentInformationCompanyLogo}
                        icon={<BankOutlined />}
                        size={48}
                      />
                    }
                    title={
                      <div className="job-title">
                        <h3>{job.employmentInformationPositionName}</h3>
                        <Tag color="blue">{job.employmentInformationSalary}</Tag>
                      </div>
                    }
                    description={
                      <div className="job-description">
                        <div className="company-info">
                          <span className="company-name">{job.employmentInformationCompanyName}</span>
                          <Tag color="cyan">
                            {getIndustryName(job.employmentInformationIndustry)}
                          </Tag>
                        </div>
                        <p className="job-desc-text">{job.employmentInformationDescription}</p>
                        <div className="job-requirements">
                          <Tag color="orange">{job.employmentInformationRequirements}</Tag>
                        </div>
                        <div className="job-meta">
                          <span className="meta-item">
                            <EnvironmentOutlined />
                            {job.employmentInformationLocation}
                          </span>
                          <span className="meta-item">
                            <CalendarOutlined />
                            {getDaysAgo(job.employmentInformationCreatedTime)}
                          </span>
                        </div>
                      </div>
                    }
                  />
                </List.Item>
              )}
            />

            {jobsData?.total && jobsData.total > pagination.pageSize && (
              <div className="pagination-wrapper">
                <Pagination
                  current={pagination.current}
                  pageSize={pagination.pageSize}
                  total={total}
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
      </Spin>
    </div>
  )
}

export default JobList