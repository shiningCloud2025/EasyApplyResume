import React from 'react'
import { Button, Empty, Spin } from 'antd'
import { ArrowLeftOutlined, CalendarOutlined, ReloadOutlined } from '@ant-design/icons'
import { useQuery } from 'react-query'
import { useNavigate, useParams } from 'react-router-dom'
import PublicContentLayout from '@components/PublicContentLayout'
import RichTextContent from '@components/RichTextContent'
import { formatContentDate, stripHtml } from '@utils/content'
import type { ContentDetailPageConfig } from './contentPageConfigs'
import './ContentPages.scss'

interface ContentDetailPageProps {
  config: ContentDetailPageConfig
}

const ContentDetailPage: React.FC<ContentDetailPageProps> = ({ config }) => {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()
  const numericId = Number(id)
  const isValidId = Number.isFinite(numericId) && numericId > 0

  const { data, isLoading, error, refetch } = useQuery(
    [config.key, numericId],
    () => config.fetcher(numericId),
    {
      enabled: isValidId,
    },
  )

  const hasContent = Boolean(data?.title || data?.content)
  const displayTitle = stripHtml(data?.title) || config.title

  return (
    <PublicContentLayout
      section={config.section}
      title={config.title}
      description={config.description}
    >
      {!isValidId ? (
        <div className="content-pages__state">
          <Empty description="无效的内容编号。">
            <Button type="primary" onClick={() => navigate(config.listPath)}>
              返回列表
            </Button>
          </Empty>
        </div>
      ) : isLoading ? (
        <div className="content-pages__state flex-center">
          <Spin size="large" tip="加载中..." />
        </div>
      ) : error ? (
        <div className="content-pages__state">
          <Empty description="详情加载失败，请稍后重试。">
            <Button type="primary" icon={<ReloadOutlined />} onClick={() => refetch()}>
              重新加载
            </Button>
          </Empty>
        </div>
      ) : !hasContent ? (
        <div className="content-pages__state">
          <Empty description={config.emptyDescription}>
            <Button type="primary" onClick={() => navigate(config.listPath)}>
              返回列表
            </Button>
          </Empty>
        </div>
      ) : (
        <article className="content-pages__card fade-in">
          <Button type="link" icon={<ArrowLeftOutlined />} onClick={() => navigate(config.listPath)}>
            返回列表
          </Button>

          <div className="content-pages__meta">
            {data?.createdTime && (
              <span className="content-pages__meta-item">
                <CalendarOutlined />
                发布时间：{formatContentDate(data.createdTime)}
              </span>
            )}
            {data?.updatedTime && (
              <span className="content-pages__meta-item">
                <CalendarOutlined />
                最后更新：{formatContentDate(data.updatedTime)}
              </span>
            )}
          </div>

          <h2 className="content-pages__title">{displayTitle}</h2>
          <RichTextContent html={data?.content} />
        </article>
      )}
    </PublicContentLayout>
  )
}

export default ContentDetailPage
