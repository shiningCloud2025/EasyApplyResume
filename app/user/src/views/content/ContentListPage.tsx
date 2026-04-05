import React, { useMemo, useState } from 'react'
import { Button, Empty, Input, Pagination, Spin } from 'antd'
import { ArrowRightOutlined, CalendarOutlined, ReloadOutlined, SearchOutlined } from '@ant-design/icons'
import { useQuery } from 'react-query'
import { useNavigate } from 'react-router-dom'
import PublicContentLayout from '@components/PublicContentLayout'
import { formatContentDate, getTextPreview, stripHtml } from '@utils/content'
import type { ContentListPageConfig } from './contentPageConfigs'
import './ContentPages.scss'

interface ContentListPageProps {
  config: ContentListPageConfig
}

const ContentListPage: React.FC<ContentListPageProps> = ({ config }) => {
  const navigate = useNavigate()
  const [keywordInput, setKeywordInput] = useState('')
  const [queryKeyword, setQueryKeyword] = useState('')
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10,
  })

  const queryKey = useMemo(
    () => [config.key, pagination.current, pagination.pageSize, queryKeyword],
    [config.key, pagination.current, pagination.pageSize, queryKeyword],
  )

  const { data, isLoading, error, refetch } = useQuery(queryKey, () =>
    config.fetchPage({
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
      query: { keyword: queryKeyword },
    }),
  )

  const handleSearch = (value: string) => {
    const nextKeyword = value.trim()
    setKeywordInput(value)
    setQueryKeyword(nextKeyword)
    setPagination((current) => ({ ...current, current: 1 }))
  }

  return (
    <PublicContentLayout
      section={config.section}
      title={config.title}
      description={config.description}
    >
      <div className="content-pages__toolbar fade-in">
        <div>
          <div className="content-pages__toolbar-title">{config.title}列表</div>
          <div className="content-pages__toolbar-desc">支持分页浏览与标题关键字检索。</div>
        </div>

        <Input.Search
          allowClear
          enterButton
          placeholder={config.searchPlaceholder}
          className="content-pages__search"
          prefix={<SearchOutlined />}
          value={keywordInput}
          onChange={(event) => setKeywordInput(event.target.value)}
          onSearch={handleSearch}
        />
      </div>

      {isLoading ? (
        <div className="content-pages__state flex-center">
          <Spin size="large" tip="加载中..." />
        </div>
      ) : error ? (
        <div className="content-pages__state">
          <Empty description="列表加载失败，请稍后重试。">
            <Button type="primary" icon={<ReloadOutlined />} onClick={() => refetch()}>
              重新加载
            </Button>
          </Empty>
        </div>
      ) : !data?.records?.length ? (
        <div className="content-pages__state">
          <Empty description={queryKeyword ? '未找到匹配内容，请调整关键词后重试。' : config.emptyDescription} />
        </div>
      ) : (
        <>
          <div className="content-pages__list fade-in">
            {data.records.map((item) => (
              <article
                key={item.id}
                className="content-pages__list-item"
                onClick={() => navigate(`${config.detailBasePath}/${item.id}`)}
              >
                <div className="content-pages__list-main">
                  <h2 className="content-pages__list-title">{stripHtml(item.title) || '未命名内容'}</h2>
                  <p className="content-pages__list-preview">
                    {getTextPreview(item.content) || '暂无内容摘要，点击查看详情。'}
                  </p>
                  <div className="content-pages__list-footer">
                    {(item.updatedTime || item.createdTime) && (
                      <span className="content-pages__meta-item">
                        <CalendarOutlined />
                        更新时间：{formatContentDate(item.updatedTime || item.createdTime)}
                      </span>
                    )}
                  </div>
                </div>

                <div className="content-pages__list-action">
                  <Button type="link" icon={<ArrowRightOutlined />}>
                    查看详情
                  </Button>
                </div>
              </article>
            ))}
          </div>

          <div className="content-pages__pagination">
            <Pagination
              current={data.current}
              pageSize={data.size || pagination.pageSize}
              total={data.total}
              showSizeChanger
              showQuickJumper
              pageSizeOptions={['10', '20', '50']}
              showTotal={(total, range) => `第 ${range[0]}-${range[1]} 条，共 ${total} 条`}
              onChange={(page, pageSize) => setPagination({ current: page, pageSize })}
            />
          </div>
        </>
      )}
    </PublicContentLayout>
  )
}

export default ContentListPage
