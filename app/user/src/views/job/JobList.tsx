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
import { formatRecruitPositionName, formatIndustryMapName, isRecruitIndustry } from '@utils/index'
import './JobList.scss'

const JobList: React.FC = () => {
  const navigate = useNavigate()
  const [filters, setFilters] = useState({
    employmentInformationCompanyName: '',
    employmentInformationIndustryCategories: undefined as number | undefined,
    employmentInformationRecruitLocationDetail: ''
  })
  const [selectedIndustryCode, setSelectedIndustryCode] = useState<number | undefined>(undefined)
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10
  })
  const [allJobs, setAllJobs] = useState<any[]>([])

  const { data: normalIndustries = [] } = useQuery(
    ['normal-industries'],
    () => jobAPI.getNormalIndustries(),
    {
      select: (response) => response.data || [],
      staleTime: 1000 * 60 * 10
    }
  )

  const { data: allIndustries = [] } = useQuery(
    ['employment-industries'],
    () => jobAPI.getAllIndustries(),
    {
      select: (response) => response.data || [],
      staleTime: 1000 * 60 * 10
    }
  )

  const recruitIndustryCodeMap = React.useMemo(() => {
    const recruitIndustryByName = new Map<string, number>()

    allIndustries
      .filter((item: any) => isRecruitIndustry(item.industryMapIndustryName))
      .forEach((item: any) => {
        recruitIndustryByName.set(
          formatIndustryMapName(item.industryMapIndustryName),
          item.industryMapIndustryCode
        )
      })

    return normalIndustries.reduce((map: Record<number, number>, item: any) => {
      const recruitCode = recruitIndustryByName.get(formatIndustryMapName(item.industryMapIndustryName))
      if (recruitCode !== undefined) {
        map[item.industryMapIndustryCode] = recruitCode
      }
      return map
    }, {})
  }, [allIndustries, normalIndustries])

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
        setPagination((current) => ({ ...current, current: 1 }))
      },
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '获取招聘信息失败'
        message.error(errorMsg)
      }
    }
  )

  const paginatedJobs = React.useMemo(() => {
    const start = (pagination.current - 1) * pagination.pageSize
    const end = start + pagination.pageSize
    return allJobs.slice(start, end)
  }, [allJobs, pagination.current, pagination.pageSize])

  const handleFilterChange = (key: string, value: any) => {
    setFilters({ ...filters, [key]: value === undefined ? undefined : value })
    setPagination({ ...pagination, current: 1 })
  }

  const handleIndustryChange = (value: number | undefined) => {
    setSelectedIndustryCode(value)
    setFilters({
      ...filters,
      employmentInformationIndustryCategories:
        value === undefined ? undefined : recruitIndustryCodeMap[value]
    })
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

  const getStatusColor = (status: string) => {
    if (!status) return 'default'
    if (status.includes('进行中') || status.includes('开放')) return 'green'
    if (status.includes('已结束') || status.includes('关闭')) return 'red'
    return 'blue'
  }

  const getCompanyTypeName = (type: number) => {
    const typeMap: Record<number, string> = {
      1: '央企', 2: '国企', 3: '国企控股', 4: '私企',
      5: '外企', 6: '合资', 7: '公务员', 8: '事业编'
    }
    return typeMap[type] || '-'
  }

  const getBatchName = (batch: number) => {
    const batchMap: Record<number, string> = {
      1: '春招', 2: '暑期实习', 3: '秋招', 4: '寒假实习', 5: '日常实习'
    }
    return batchMap[batch] || '-'
  }

  const getRecruitObjectName = (obj: number) => {
    const objMap: Record<number, string> = {
      1: '应届生', 2: '社会招聘', 3: '实习生'
    }
    return objMap[obj] || '-'
  }

  const resetFilters = () => {
    setFilters({
      employmentInformationCompanyName: '',
      employmentInformationIndustryCategories: undefined,
      employmentInformationRecruitLocationDetail: ''
    })
    setSelectedIndustryCode(undefined)
    setPagination({ current: 1, pageSize: 10 })
  }

  return (
    <div className="job-list-page">
      <div className="page-header">
        <h1>招聘信息</h1>
        <p>浏览最新招聘信息，找到适合您的职位</p>
      </div>

      <Card className="filter-card">
        <div className="job-filter-grid">
          <div className="job-filter-item">
            <span className="filter-label">公司名称</span>
            <Input
              placeholder="请输入公司名称"
              value={filters.employmentInformationCompanyName}
              onChange={(e) => handleFilterChange('employmentInformationCompanyName', e.target.value)}
              allowClear
            />
          </div>

          <div className="job-filter-item">
            <span className="filter-label">行业</span>
            <Select
              placeholder="请选择行业"
              value={selectedIndustryCode}
              onChange={handleIndustryChange}
              allowClear
            >
              {normalIndustries?.map((item: any) => (
                <Select.Option key={item.industryMapIndustryCode} value={item.industryMapIndustryCode}>
                  {formatIndustryMapName(item.industryMapIndustryName)}
                </Select.Option>
              ))}
            </Select>
          </div>

          <div className="job-filter-item">
            <span className="filter-label">详细地址</span>
            <Input
              placeholder="请输入地址"
              value={filters.employmentInformationRecruitLocationDetail}
              onChange={(e) => handleFilterChange('employmentInformationRecruitLocationDetail', e.target.value)}
              allowClear
            />
          </div>
        </div>

        <div className="job-filter-actions">
          <Button type="primary" icon={<SearchOutlined />} onClick={() => refetch()}>
            搜索
          </Button>
          <Button icon={<ReloadOutlined />} onClick={resetFilters}>
            重置
          </Button>
        </div>
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
                responsive: ['md'],
                render: (text: string) => formatIndustryMapName(text)
              },
              {
                title: '企业性质',
                dataIndex: 'employmentInformationCompanyType',
                key: 'companyType',
                width: 100,
                responsive: ['lg'],
                render: (val: number) => getCompanyTypeName(val)
              },
              {
                title: '招聘批次',
                dataIndex: 'employmentInformationBatch',
                key: 'batch',
                width: 100,
                responsive: ['lg'],
                render: (val: number) => getBatchName(val)
              },
              {
                title: '招聘对象',
                dataIndex: 'employmentInformationRecruitObject',
                key: 'object',
                width: 100,
                responsive: ['lg'],
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
                responsive: ['md'],
                render: (list: string[]) => list?.join('、') || '-'
              },
              {
                title: '招聘地址(市)',
                dataIndex: 'employmentInformationRecruitLocationSecondName',
                key: 'locationCity',
                width: 150,
                responsive: ['lg'],
                render: (list: string[]) => list?.join('、') || '-'
              },
              {
                title: '详细地址',
                dataIndex: 'employmentInformationRecruitLocationDetail',
                key: 'locationDetail',
                width: 200,
                responsive: ['lg'],
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
                responsive: ['md'],
                render: (text: string) => formatDate(text)
              },
              {
                title: '截止时间',
                dataIndex: 'employmentInformationStopTime',
                key: 'stopTime',
                width: 110,
                responsive: ['md'],
                render: (text: string) => formatDate(text)
              },
              {
                title: '更新时间',
                dataIndex: 'employmentInformationUpdatedTime',
                key: 'updatedTime',
                width: 110,
                responsive: ['lg'],
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
