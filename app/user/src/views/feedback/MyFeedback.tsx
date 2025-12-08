import React, { useState } from 'react'
import { 
  Card, 
  List, 
  Button, 
  Tag, 
  Empty, 
  Spin,
  message,
  Modal,
  Input
} from 'antd'
import { useQuery } from 'react-query'
import { feedbackAPI } from '@api/feedback'
import { useUserStore } from '@stores/userStore'
import type { FeedbackItem } from '@types/index'
import { useNavigate } from 'react-router-dom'

const MyFeedback: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useUserStore()
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10
  })

  // 获取反馈列表
  const {
    data: feedbacksData,
    isLoading,
    error,
    refetch
  } = useQuery(
    ['feedbacks', user?.userId, pagination],
    () => {
      if (!user?.userId) return Promise.resolve({ records: [], total: 0 })
      return feedbackAPI.getFeedbacks({
        size: pagination.pageSize,
        page: pagination.current,
        userId: user.userId
      })
    },
    {
      enabled: !!user?.userId,
      select: (response) => response.data || { records: [], total: 0 },
      onSuccess: (data) => {
        console.log('获取反馈列表成功:', data)
      },
      onError: (error) => {
        message.error('获取反馈列表失败')
        console.error('API错误:', error)
      }
    }
  )

  const getStatusTag = (status: string) => {
    const statusMap: Record<string, { color: string; text: string }> = {
      pending: { color: 'processing', text: '待处理' },
      processing: { color: 'warning', text: '处理中' },
      resolved: { color: 'success', text: '已解决' },
      closed: { color: 'default', text: '已关闭' }
    }
    const config = statusMap[status] || statusMap.pending
    return <Tag color={config.color}>{config.text}</Tag>
  }

  const getTypeTag = (type: string) => {
    const typeMap: Record<string, { color: string; text: string }> = {
      bug: { color: 'error', text: '问题反馈' },
      feature: { color: 'blue', text: '功能建议' },
      improvement: { color: 'orange', text: '改进建议' },
      other: { color: 'default', text: '其他' }
    }
    const config = typeMap[type] || typeMap.other
    return <Tag color={config.color}>{config.text}</Tag>
  }

  const handlePageChange = (page: number, pageSize: number) => {
    setPagination({ current: page, pageSize })
  }

  const handleViewDetail = (feedbackId: number) => {
    navigate(`/feedback/detail/${feedbackId}`)
  }

  if (!user) {
    return (
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
    )
  }

  return (
    <div className="my-feedback">
      <div className="page-header">
        <div className="header-content">
          <h2>我的反馈</h2>
          <Button
            type="primary"
            onClick={() => navigate('/feedback/submit')}
          >
            提交反馈
          </Button>
        </div>
      </div>

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
      ) : !feedbacksData?.records?.length ? (
        <Empty
          description="暂无反馈记录"
          image={Empty.PRESENTED_IMAGE_SIMPLE}
        >
          <Button type="primary" onClick={() => navigate('/feedback/submit')}>
            提交反馈
          </Button>
        </Empty>
      ) : (
        <>
          <List
            itemLayout="vertical"
            size="large"
            dataSource={feedbacksData.records}
            renderItem={(feedback: FeedbackItem) => (
              <List.Item
                key={feedback.feedbackId}
                actions={[
                  <Button 
                    type="link" 
                    onClick={() => handleViewDetail(feedback.feedbackId)}
                  >
                    查看详情
                  </Button>
                ]}
              >
                <List.Item.Meta
                  title={
                    <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                      <span>{feedback.feedbackTitle}</span>
                      {getStatusTag(feedback.feedbackStatus)}
                      {getTypeTag(feedback.feedbackType)}
                    </div>
                  }
                  description={
                    <div>
                      <p style={{ marginBottom: 8 }}>{feedback.feedbackContent}</p>
                      <div style={{ color: '#666', fontSize: '12px' }}>
                        提交时间: {new Date(feedback.feedbackCreatedTime).toLocaleString('zh-CN')}
                      </div>
                    </div>
                  }
                />
              </List.Item>
            )}
          />

          {feedbacksData?.total && feedbacksData.total > pagination.pageSize && (
            <div className="pagination-wrapper">
              {/* 这里可以添加分页组件 */}
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default MyFeedback