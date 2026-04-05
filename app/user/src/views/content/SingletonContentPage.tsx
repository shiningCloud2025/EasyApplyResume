import React from 'react'
import { Button, Empty, Spin } from 'antd'
import { CalendarOutlined, ReloadOutlined } from '@ant-design/icons'
import { useQuery } from 'react-query'
import PublicContentLayout from '@components/PublicContentLayout'
import RichTextContent from '@components/RichTextContent'
import { formatContentDate, stripHtml } from '@utils/content'
import type { SingletonContentPageConfig } from './contentPageConfigs'
import './ContentPages.scss'

interface SingletonContentPageProps {
  config: SingletonContentPageConfig
}

const SingletonContentPage: React.FC<SingletonContentPageProps> = ({ config }) => {
  const { data, isLoading, error, refetch } = useQuery(config.key, config.fetcher)

  const hasContent = Boolean(data?.title || data?.content)
  const displayTitle = stripHtml(data?.title) || config.title

  return (
    <PublicContentLayout
      section={config.section}
      title={config.title}
      description={config.description}
    >
      {isLoading ? (
        <div className="content-pages__state flex-center">
          <Spin size="large" tip="加载中..." />
        </div>
      ) : error ? (
        <div className="content-pages__state">
          <Empty description="内容加载失败，请稍后重试。">
            <Button type="primary" icon={<ReloadOutlined />} onClick={() => refetch()}>
              重新加载
            </Button>
          </Empty>
        </div>
      ) : !hasContent ? (
        <div className="content-pages__state">
          <Empty description={config.emptyDescription} />
        </div>
      ) : (
        <article className="content-pages__card fade-in">
          <div className="content-pages__meta">
            {(data?.updatedTime || data?.createdTime) && (
              <span className="content-pages__meta-item">
                <CalendarOutlined />
                最后更新：{formatContentDate(data?.updatedTime || data?.createdTime)}
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

export default SingletonContentPage
