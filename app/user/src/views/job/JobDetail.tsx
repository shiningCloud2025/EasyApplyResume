import React from 'react'
import { Card, Tag, Spin, Button, Empty, Divider, Descriptions, message } from 'antd'
import { ArrowLeftOutlined, BankOutlined, EnvironmentOutlined, CalendarOutlined, LinkOutlined, EditOutlined } from '@ant-design/icons'
import { useQuery } from 'react-query'
import { useParams, useNavigate } from 'react-router-dom'
import { jobAPI } from '@api/job'
import { formatRecruitPositionName } from '@utils/index'

const JobDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()

  const {
    data: job,
    isLoading,
    error
  } = useQuery(
    ['jobDetail', id],
    () => jobAPI.getJobDetail(Number(id)),
    {
      enabled: !!id,
      select: (response) => response.data,
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '获取招聘信息失败'
        message.error(errorMsg)
      }
    }
  )

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

  if (isLoading) {
    return (
      <div className="job-detail-page">
        <div className="loading-wrapper">
          <Spin size="large" tip="加载中..." />
        </div>
      </div>
    )
  }

  if (error || !job) {
    return (
      <div className="job-detail-page">
        <Empty
          description="招聘信息不存在或加载失败"
          image={Empty.PRESENTED_IMAGE_SIMPLE}
        >
          <Button type="primary" onClick={() => navigate('/jobs')}>
            返回列表
          </Button>
        </Empty>
      </div>
    )
  }

  return (
    <div className="job-detail-page">
      <Card className="detail-card">
        <Button
          type="link"
          icon={<ArrowLeftOutlined />}
          onClick={() => navigate('/jobs')}
          className="back-btn"
        >
          返回列表
        </Button>

        <div className="job-header">
          <h1 className="job-title">
            <BankOutlined style={{ marginRight: 12 }} />
            {job.employmentInformationCompanyName}
          </h1>
          <div className="job-meta">
            {job.employmentInformationIndustryCategoriesName && (
              <Tag color="blue">{job.employmentInformationIndustryCategoriesName}</Tag>
            )}
            {job.employmentInformationOnlineApplicationStatus && (
              <Tag color={getStatusColor(job.employmentInformationOnlineApplicationStatus)}>
                {job.employmentInformationOnlineApplicationStatus}
              </Tag>
            )}
          </div>
        </div>

        <Divider />

        <Descriptions title="基本信息" column={{ xs: 1, sm: 2, md: 3 }} bordered>
          <Descriptions.Item label="招聘信息编号">
            {job.employmentInformationCode || '-'}
          </Descriptions.Item>
          <Descriptions.Item label="行业大类">
            {job.employmentInformationIndustryCategoriesName || '-'}
          </Descriptions.Item>
          <Descriptions.Item label="企业性质">
            {getCompanyTypeName(job.employmentInformationCompanyType)}
          </Descriptions.Item>
          <Descriptions.Item label="招聘批次">
            {getBatchName(job.employmentInformationBatch)}
          </Descriptions.Item>
          <Descriptions.Item label="招聘岗位">
            {formatRecruitPositionName(job.employmentInformationRecruitPositionName)}
          </Descriptions.Item>
          <Descriptions.Item label="招聘对象">
            {getRecruitObjectName(job.employmentInformationRecruitObject)}
          </Descriptions.Item>
          <Descriptions.Item label="招聘地址(省)">
            <EnvironmentOutlined style={{ marginRight: 4 }} />
            {job.employmentInformationRecruitLocationFirstName?.join('、') || '-'}
          </Descriptions.Item>
          <Descriptions.Item label="招聘地址(市)">
            {job.employmentInformationRecruitLocationSecondName?.join('、') || '-'}
          </Descriptions.Item>
          <Descriptions.Item label="详细地址">
            {job.employmentInformationRecruitLocationDetail?.join('、') || '-'}
          </Descriptions.Item>
          <Descriptions.Item label="网申状态">
            {job.employmentInformationOnlineApplicationStatus ? 
              <Tag color={getStatusColor(job.employmentInformationOnlineApplicationStatus)}>
                {job.employmentInformationOnlineApplicationStatus}
              </Tag> : '-'
            }
          </Descriptions.Item>
          <Descriptions.Item label="投递方式">
            {job.employmentInformationSubmissionWay ? (
              <a href={job.employmentInformationSubmissionWay} target="_blank" rel="noopener noreferrer">
                <LinkOutlined style={{ marginRight: 4 }} />
                点击跳转
              </a>
            ) : '-'}
          </Descriptions.Item>
          <Descriptions.Item label="内推码">
            {job.employmentInformationEmployeeReferralCode || '-'}
          </Descriptions.Item>
        </Descriptions>

        <Descriptions title="时间信息" column={{ xs: 1, sm: 2, md: 3 }} bordered style={{ marginTop: 24 }}>
          <Descriptions.Item label="创建时间">
            <CalendarOutlined style={{ marginRight: 4 }} />
            {formatDate(job.employmentInformationStartTime)}
          </Descriptions.Item>
          <Descriptions.Item label="截止时间">
            {formatDate(job.employmentInformationStopTime)}
          </Descriptions.Item>
          <Descriptions.Item label="更新时间">
            <EditOutlined style={{ marginRight: 4 }} />
            {formatDate(job.employmentInformationUpdatedTime)}
          </Descriptions.Item>
        </Descriptions>

        {job.employmentInformationOfficialAnnouncement && (
          <>
            <Divider orientation="left">官方公告</Divider>
            <div
              className="announcement-content"
              dangerouslySetInnerHTML={{ __html: job.employmentInformationOfficialAnnouncement }}
            />
          </>
        )}
      </Card>
    </div>
  )
}

export default JobDetail