import React from 'react'
import { Card, Tag, Spin, Button, Empty, Divider, message } from 'antd'
import { ArrowLeftOutlined, UserOutlined, CalendarOutlined, TagOutlined, EditOutlined } from '@ant-design/icons'
import { useQuery } from 'react-query'
import { useParams, useNavigate } from 'react-router-dom'
import { adviceAPI } from '@api/job'
import RichTextContent from '@components/RichTextContent'
import './AdviceDetail.scss'

const AdviceDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()

  const {
    data: article,
    isLoading,
    error,
    refetch
  } = useQuery(
    ['adviceDetail', id],
    () => adviceAPI.getArticleDetail(Number(id)),
    {
      enabled: !!id,
      select: (response) => response.data,
      onError: () => {
        message.error('获取攻略详情失败')
      }
    }
  )

  const formatDate = (dateString: string) => {
    if (!dateString) return ''
    return new Date(dateString).toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    })
  }

  if (isLoading) {
    return (
      <div className="advice-detail-page">
        <div className="loading-wrapper">
          <Spin size="large" tip="加载中..." />
        </div>
      </div>
    )
  }

  if (error || !article) {
    return (
      <div className="advice-detail-page">
        <Empty
          description="攻略不存在或加载失败"
          image={Empty.PRESENTED_IMAGE_SIMPLE}
        >
          <Button type="primary" onClick={() => navigate('/advice')}>
            返回列表
          </Button>
        </Empty>
      </div>
    )
  }

  return (
    <div className="advice-detail-page">
      <Card className="detail-card">
        <Button 
          type="link" 
          icon={<ArrowLeftOutlined />} 
          onClick={() => navigate('/advice')}
          className="back-btn"
        >
          返回列表
        </Button>

        <div className="article-header">
          <h1 className="article-title">{article.jobAdviceArticleTitle}</h1>
          
          <div className="article-meta">
            <span className="meta-item">
              <UserOutlined />
              {article.jobAdviceArticleAuthorName || '佚名'}
            </span>
            <span className="meta-item">
              <CalendarOutlined />
              发布于 {formatDate(article.jobAdviceArticlePublishedTime)}
            </span>
            {article.jobAdviceArticleUpdatedTime && (
              <span className="meta-item">
                <EditOutlined />
                最后更新 {formatDate(article.jobAdviceArticleUpdatedTime)}
              </span>
            )}
          </div>

          <div className="article-tags-row">
            {article.jobAdviceArticleCategory && (
              <Tag color="blue">{article.jobAdviceArticleCategory}</Tag>
            )}
            {article.jobAdviceArticleTags && (
              <>
                <TagOutlined style={{ marginLeft: 8, marginRight: 4 }} />
                {article.jobAdviceArticleTags.split(',').map((tag: string, idx: number) => (
                  <Tag key={idx} color="cyan">{tag.trim()}</Tag>
                ))}
              </>
            )}
          </div>
        </div>

        <Divider />

        <RichTextContent
          className="article-content"
          html={article.jobAdviceArticleContent || ''}
        />
      </Card>
    </div>
  )
}

export default AdviceDetail