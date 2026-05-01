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
  Select,
  message
} from 'antd'
import { SearchOutlined, ReloadOutlined, BankOutlined } from '@ant-design/icons'
import { useQuery } from 'react-query'
import { jobAPI } from '@api/job'
import { useNavigate } from 'react-router-dom'
import { formatRecruitPositionName } from '@utils/index'

const JobList: React.FC = () => {
  const navigate = useNavigate()
  const [filters, setFilters] = useState({
    employmentInformationCompanyName: '',
    employmentInformationIndustryCategories: undefined as number | undefined,
    employmentInformationRecruitLocationDetail: ''
  })
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10
  })
  const [allJobs, setAllJobs] = useState<any[]>([])

  // 获取行业列表
  const { data: industries } = useQuery(
    ['industries'],
    () => jobAPI.getAllIndustries(),
    {
      select: (response) => response.data || [],
      staleTime: 1000 * 60 * 10 // 10分钟缓存
    }
  )

  // 获取招聘信息列表(一次性获取所有数据)
  const {
    data: jobsData,
    isLoading,
    error,
    refetch
  } = useQuery(
    ['jobs', filters],
    () => jobAPI.getJobs({
      pageNum: 1,
      pageSize: 10000,
      query: filters
    }),
    {
      keepPreviousData: true,
      select: (response) => response.data,
      onSuccess: (data) => {
        setAllJobs(data?.records || [])
        setPagination({ ...pagination, current: 1 })
      },
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '获取招聘信息失败'
        message.error(errorMsg)
      }
    }
  )

  // 前端分页:计算当前页数据
  const paginatedJobs = React.useMemo(() => {
    const start = (pagination.current - 1) * pagination.pageSize
    const end = start + pagination.pageSize
    return allJobs.slice(start, end)
  }, [allJobs, pagination.current, pagination.pageSize])

  const handleFilterChange = (key: string, value: any) => {
    setFilters({ ...filters, [key]: value === undefined ? undefined : value })
    setPagination({ ...pagination, current: 1 })
  }

  const handlePageChange = (page: number, pageSize: number) => {
    setPagination({ current: page, pageSize })
  }

  const handleJobClick = (jobId: number) => {
    navigate(`/job/${jobId}`)
  }

  const formatDate = (dateString: string) => {
    if (!dateString) return '-'
    return new Date(dateString).toLocaleDateString('zh-CN')
  }

  // 网申状态标签颜色
  const getStatusColor = (status: string) => {
    if (!status) return 'default'
    if (status.includes('进行中') || status.includes('开放')) return 'green'
    if (status.includes('已结束') || status.includes('关闭')) return 'red'
    return 'blue'
  }

  // 企业性质映射
  const getCompanyTypeName = (type: number) => {
    const typeMap: Record<number, string> = {
      1: '央企', 2: '国企', 3: '国企控股', 4: '私企', 
      5: '外企', 6: '合资', 7: '公务员', 8: '事业编'
    }
    return typeMap[type] || '-'
  }

  // 招聘批次映射
  const getBatchName = (batch: number) => {
    const batchMap: Record<number, string> = {
      1: '春招', 2: '暑期实习', 3: '秋招', 4: '寒假实习', 5: '日常实习'
    }
    return batchMap[batch] || '-'
  }

  // 招聘对象映射
  const getRecruitObjectName = (obj: number) => {
    const objMap: Record<number, string> = {
      1: '应届生', 2: '社会招聘', 3: '实习生'
    }
    return objMap[obj] || '-'
  }

  return (
    <div className="job-list-page">
      <div className="page-header">
        <h1>招聘信息</h1>
        <p>浏览最新招聘信息，找到适合您的职位</p>
      </div>

      <Card className="filter-card">
        <Row gutter={24} align="middle">
          <Col>
            <span className="filter-label">公司名称</span>
          </Col>
          <Col>
            <Input
              placeholder="请输入公司名称"
              value={filters.employmentInformationCompanyName}
              onChange={(e) => handleFilterChange('employmentInformationCompanyName', e.target.value)}
              allowClear
              style={{ width: 180 }}
            />
          </Col>
          <Col>
            <span className="filter-label">行业</span>
          </Col>
          <Col>
            <Select
              placeholder="请选择行业"
              style={{ width: 150 }}
              value={filters.employmentInformationIndustryCategories}
              onChange={(value) => handleFilterChange('employmentInformationIndustryCategories', value)}
              allowClear
            >
              {industries?.map((item: any) => (
                <Select.Option key={item.industryMapIndustryCode} value={item.industryMapIndustryCode}>
                  {item.industryMapIndustryName}
                </Select.Option>
              ))}
            </Select>
          </Col>
          <Col>
            <span className="filter-label">详细地址</span>
          </Col>
          <Col>
            <Input
              placeholder="请输入地址"
              value={filters.employmentInformationRecruitLocationDetail}
              onChange={(e) => handleFilterChange('employmentInformationRecruitLocationDetail', e.target.value)}
              allowClear
              style={{ width: 150 }}
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
                employmentInformationCompanyName: '',
                employmentInformationIndustryCategories: undefined,
                employmentInformationRecruitLocationDetail: ''
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
            dataSource={paginatedJobs}
            loading={isLoading}
            rowKey="employmentInformationId"
            scroll={{ x: 1800 }}
            onRow={(record: any) => ({
              onClick: () => handleJobClick(record.employmentInformationId),
              style: { cursor: 'pointer' }
            })}
            pagination={{
              current: pagination.current,
              pageSize: pagination.pageSize,
              total: allJobs.length,
              onChange: handlePageChange,
              showSizeChanger: true,
              showQuickJumper: true,
              pageSizeOptions: ['10', '20', '50'],
              showTotal: (total, range) => `第 ${range[0]}-${range[1]} 条，共 ${total} 条`
            }}
            columns={[
              {
                title: '公司名称',
                dataIndex: 'employmentInformationCompanyName',
                key: 'companyName',
                fixed: 'left',
                width: 180,
                render: (text: string) => (
                  <span className="company-name-cell">
                    <BankOutlined style={{ marginRight: 8, color: '#1890ff' }} />
                    {text}
                  </span>
                )
              },
              {
                title: '行业大类',
                dataIndex: 'employmentInformationIndustryCategoriesName',
                key: 'industry',
                width: 120,
                render: (text: string) => text || '-'
              },
              {
                title: '企业性质',
                dataIndex: 'employmentInformationCompanyType',
                key: 'companyType',
                width: 100,
                render: (val: number) => getCompanyTypeName(val)
              },
              {
                title: '招聘批次',
                dataIndex: 'employmentInformationBatch',
                key: 'batch',
                width: 100,
                render: (val: number) => getBatchName(val)
              },
              {
                title: '招聘对象',
                dataIndex: 'employmentInformationRecruitObject',
                key: 'object',
                width: 100,
                render: (val: number) => getRecruitObjectName(val)
              },
              {
                title: '招聘岗位',
                dataIndex: 'employmentInformationRecruitPositionName',
                key: 'position',
                width: 120,
                render: (text: string) => formatRecruitPositionName(text)
              },
              {
                title: '招聘地址(省)',
                dataIndex: 'employmentInformationRecruitLocationFirstName',
                key: 'locationProvince',
                width: 150,
                render: (list: string[]) => list?.join('、') || '-'
              },
              {
                title: '招聘地址(市)',
                dataIndex: 'employmentInformationRecruitLocationSecondName',
                key: 'locationCity',
                width: 150,
                render: (list: string[]) => list?.join('、') || '-'
              },
              {
                title: '详细地址',
                dataIndex: 'employmentInformationRecruitLocationDetail',
                key: 'locationDetail',
                width: 200,
                render: (list: string[]) => list?.join('、') || '-'
              },
              {
                title: '网申状态',
                dataIndex: 'employmentInformationOnlineApplicationStatus',
                key: 'status',
                width: 100,
                render: (text: string) => (
                  text ? <Tag color={getStatusColor(text)}>{text}</Tag> : '-'
                )
              },
              {
                title: '创建时间',
                dataIndex: 'employmentInformationStartTime',
                key: 'startTime',
                width: 110,
                render: (text: string) => formatDate(text)
              },
              {
                title: '截止时间',
                dataIndex: 'employmentInformationStopTime',
                key: 'stopTime',
                width: 110,
                render: (text: string) => formatDate(text)
              },
              {
                title: '更新时间',
                dataIndex: 'employmentInformationUpdatedTime',
                key: 'updatedTime',
                width: 110,
                render: (text: string) => formatDate(text)
              }
            ]}
          />
        )}
      </Card>
    </div>
  )
}

export default JobList