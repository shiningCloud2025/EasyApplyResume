import React, { useEffect, useMemo, useState } from 'react'
import { Alert, Button, Card, Col, Empty, Input, Row, Select, Space, Table, Tag } from 'antd'
import { ReloadOutlined, SearchOutlined } from '@ant-design/icons'
import { useQuery } from 'react-query'
import { useLocation, useNavigate } from 'react-router-dom'
import { questionBankAPI } from '@api/questionBank'
import { useUserStore } from '@stores/userStore'
import type { QuestionBankListItem, QuestionBankQuery, QuestionFirstCategory, QuestionSecondCategory } from '@types/index'
import './QuestionBankListPage.scss'

const questionTypeMap: Record<number, string> = {
  1: '单选题',
  2: '多选题',
  3: '判断题',
  4: '填空题',
  5: '问答题',
}

const difficultyMap: Record<number, { text: string, color: string }> = {
  1: { text: '简单', color: 'green' },
  2: { text: '中等', color: 'blue' },
  3: { text: '困难', color: 'red' },
}

const answerStatusMap: Record<number, { text: string, color: string }> = {
  0: { text: '未作答', color: 'default' },
  1: { text: '已做错', color: 'red' },
  2: { text: '已做对', color: 'green' },
}

const QuestionBankListPage: React.FC = () => {
  const navigate = useNavigate()
  const location = useLocation()
  const { user, isLoggedIn, fetchUserInfo } = useUserStore()
  const searchParams = useMemo(() => new URLSearchParams(location.search), [location.search])

  const routeFirstCategoryId = useMemo(() => {
    const value = searchParams.get('firstCategoryId')
    return value ? Number(value) : undefined
  }, [searchParams])

  const routeSecondCategoryId = useMemo(() => {
    const value = searchParams.get('secondCategoryId')
    return value ? Number(value) : undefined
  }, [searchParams])

  const routeFirstCategoryName = searchParams.get('firstCategoryName') || ''
  const routeSecondCategoryName = searchParams.get('secondCategoryName') || ''
  const refreshToken = searchParams.get('refreshToken') || ''
  const routeKeyword = searchParams.get('keyword') || ''
  const routeQuestionBankType = searchParams.get('questionBankType')
  const routeQuestionBankDifficulty = searchParams.get('questionBankDifficulty')
  const routeQuestionBankAnswerStatus = searchParams.get('questionBankAnswerStatus')
  const routePageNum = searchParams.get('pageNum')
  const routePageSize = searchParams.get('pageSize')

  const [keywordInput, setKeywordInput] = useState(routeKeyword)
  const [firstCategoryName, setFirstCategoryName] = useState(routeFirstCategoryName)
  const [secondCategoryName, setSecondCategoryName] = useState(routeSecondCategoryName)
  const [filters, setFilters] = useState<QuestionBankQuery>({
    questionFirstCategoryId: routeFirstCategoryId,
    questionSecondCategoryId: routeSecondCategoryId,
    questionBankType: routeQuestionBankType ? Number(routeQuestionBankType) : undefined,
    questionBankDifficulty: routeQuestionBankDifficulty ? Number(routeQuestionBankDifficulty) : undefined,
    questionBankAnswerStatus: routeQuestionBankAnswerStatus ? Number(routeQuestionBankAnswerStatus) : undefined,
    questionBankDescription: routeKeyword.trim() || undefined,
  })
  const [pagination, setPagination] = useState({
    current: routePageNum ? Number(routePageNum) : 1,
    pageSize: routePageSize ? Number(routePageSize) : 10,
  })

  const getShortDescription = (text: string) => {
    if (!text) {
      return '-'
    }

    return text.length > 20 ? `${text.slice(0, 20)}...` : text
  }

  useEffect(() => {
    if (isLoggedIn && !user?.userId) {
      fetchUserInfo(true)
    }
  }, [fetchUserInfo, isLoggedIn, user?.userId])

  useEffect(() => {
    setKeywordInput(routeKeyword)
    setFirstCategoryName(routeFirstCategoryName)
    setSecondCategoryName(routeSecondCategoryName)
    setFilters({
      questionFirstCategoryId: routeFirstCategoryId,
      questionSecondCategoryId: routeSecondCategoryId,
      questionBankType: routeQuestionBankType ? Number(routeQuestionBankType) : undefined,
      questionBankDifficulty: routeQuestionBankDifficulty ? Number(routeQuestionBankDifficulty) : undefined,
      questionBankAnswerStatus: routeQuestionBankAnswerStatus ? Number(routeQuestionBankAnswerStatus) : undefined,
      questionBankDescription: routeKeyword.trim() || undefined,
    })
    setPagination({
      current: routePageNum ? Number(routePageNum) : 1,
      pageSize: routePageSize ? Number(routePageSize) : 10,
    })
  }, [
    routeFirstCategoryId,
    routeSecondCategoryId,
    routeQuestionBankType,
    routeQuestionBankDifficulty,
    routeQuestionBankAnswerStatus,
    routeKeyword,
    routePageNum,
    routePageSize,
    routeFirstCategoryName,
    routeSecondCategoryName,
  ])

  useEffect(() => {
    const nextSearchParams = new URLSearchParams()

    if (filters.questionFirstCategoryId) {
      nextSearchParams.set('firstCategoryId', String(filters.questionFirstCategoryId))
    }

    if (filters.questionSecondCategoryId) {
      nextSearchParams.set('secondCategoryId', String(filters.questionSecondCategoryId))
    }

    if (firstCategoryName) {
      nextSearchParams.set('firstCategoryName', firstCategoryName)
    }

    if (secondCategoryName) {
      nextSearchParams.set('secondCategoryName', secondCategoryName)
    }

    if (filters.questionBankType !== undefined) {
      nextSearchParams.set('questionBankType', String(filters.questionBankType))
    }

    if (filters.questionBankDifficulty !== undefined) {
      nextSearchParams.set('questionBankDifficulty', String(filters.questionBankDifficulty))
    }

    if (filters.questionBankAnswerStatus !== undefined) {
      nextSearchParams.set('questionBankAnswerStatus', String(filters.questionBankAnswerStatus))
    }

    if (filters.questionBankDescription) {
      nextSearchParams.set('keyword', filters.questionBankDescription)
    }

    if (pagination.current > 1) {
      nextSearchParams.set('pageNum', String(pagination.current))
    }

    if (pagination.pageSize !== 10) {
      nextSearchParams.set('pageSize', String(pagination.pageSize))
    }

    if (refreshToken) {
      nextSearchParams.set('refreshToken', refreshToken)
    }

    const nextSearch = nextSearchParams.toString()
    const currentSearch = location.search.startsWith('?') ? location.search.slice(1) : location.search

    if (nextSearch !== currentSearch) {
      navigate({ pathname: location.pathname, search: nextSearch ? `?${nextSearch}` : '' }, { replace: true })
    }
  }, [filters, pagination, firstCategoryName, secondCategoryName, refreshToken, location.pathname, location.search, navigate])

  const {
    data: firstCategories = [],
  } = useQuery<QuestionFirstCategory[]>(
    ['questionBankFirstCategories'],
    questionBankAPI.getAllFirstCategories,
    {
      enabled: isLoggedIn,
      staleTime: 5 * 60 * 1000,
    },
  )

  const {
    data: secondCategories = [],
    isFetching: isSecondCategoriesFetching,
  } = useQuery<QuestionSecondCategory[]>(
    ['questionBankSecondCategories', filters.questionFirstCategoryId],
    () => questionBankAPI.getSecondCategoriesByFirstCategoryId(Number(filters.questionFirstCategoryId)),
    {
      enabled: isLoggedIn && !!filters.questionFirstCategoryId,
      staleTime: 5 * 60 * 1000,
    },
  )

  const filterTitle = useMemo(() => {
    const selectedFirst = firstCategories.find((item) => item.questionFirstCategoryId === filters.questionFirstCategoryId)
    const selectedSecond = secondCategories.find((item) => item.questionSecondCategoryId === filters.questionSecondCategoryId)

    if (selectedSecond) {
      return `${selectedFirst?.questionFirstCategoryName || firstCategoryName || ''}${selectedFirst?.questionFirstCategoryName || firstCategoryName ? ' / ' : ''}${selectedSecond.questionSecondCategoryName}`
    }

    if (selectedFirst) {
      return selectedFirst.questionFirstCategoryName
    }

    if (secondCategoryName) {
      return `${firstCategoryName ? `${firstCategoryName} / ` : ''}${secondCategoryName}`
    }

    if (firstCategoryName) {
      return firstCategoryName
    }

    return '全部题库'
  }, [filters.questionFirstCategoryId, filters.questionSecondCategoryId, firstCategories, secondCategories, firstCategoryName, secondCategoryName])

  const {
    data,
    isLoading,
    error,
    refetch,
    isFetching,
  } = useQuery(
    [
      'questionBankPage',
      user?.userId,
      pagination.current,
      pagination.pageSize,
      filters.questionFirstCategoryId,
      filters.questionSecondCategoryId,
      filters.questionBankType,
      filters.questionBankDifficulty,
      filters.questionBankAnswerStatus,
      filters.questionBankDescription,
      refreshToken,
    ],
    () => questionBankAPI.getQuestionBankPage({
      userId: Number(user?.userId),
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
      query: filters,
    }),
    {
      enabled: isLoggedIn && !!user?.userId,
      keepPreviousData: true,
    },
  )

  const handleSearch = () => {
    const nextKeyword = keywordInput.trim()
    setFilters((current) => ({
      ...current,
      questionBankDescription: nextKeyword || undefined,
    }))
    setPagination((current) => ({ ...current, current: 1 }))
  }

  const handleFirstCategoryChange = (value?: number) => {
    const selectedFirstCategory = firstCategories.find((item) => item.questionFirstCategoryId === value)
    setFilters((current) => ({
      ...current,
      questionFirstCategoryId: value,
      questionSecondCategoryId: undefined,
    }))
    setFirstCategoryName(selectedFirstCategory?.questionFirstCategoryName || '')
    setSecondCategoryName('')
    setPagination((current) => ({ ...current, current: 1 }))
  }

  const handleResetFilters = () => {
    setKeywordInput('')
    setFirstCategoryName(routeFirstCategoryName)
    setSecondCategoryName(routeSecondCategoryName)
    setFilters({
      questionFirstCategoryId: routeFirstCategoryId,
      questionSecondCategoryId: routeSecondCategoryId,
      questionBankType: undefined,
      questionBankDifficulty: undefined,
      questionBankAnswerStatus: undefined,
      questionBankDescription: undefined,
    })
    setPagination({ current: 1, pageSize: 10 })
  }

  const columns = [
    {
      title: '题目描述',
      dataIndex: 'questionBankDescription',
      key: 'questionBankDescription',
      render: (text: string, record: QuestionBankListItem) => {
        if (!text) {
          return '-'
        }

        return (
          <Button
            type="link"
            className="question-bank-desc-btn"
            title={text}
            onClick={() => navigate(`/question-bank/${record.questionBankId}`, {
              state: { from: `${location.pathname}${location.search}` },
            })}
          >
            {getShortDescription(text)}
          </Button>
        )
      },
    },
    {
      title: '题目类型',
      dataIndex: 'questionBankType',
      key: 'questionBankType',
      width: 110,
      render: (value: number) => questionTypeMap[value] || `类型${value}`,
    },
    {
      title: '难度',
      dataIndex: 'questionBankDifficulty',
      key: 'questionBankDifficulty',
      width: 100,
      render: (value: number) => {
        const difficulty = difficultyMap[value]
        return <Tag color={difficulty?.color || 'default'}>{difficulty?.text || `等级${value}`}</Tag>
      },
    },
    {
      title: '大类',
      dataIndex: 'questionFirstCategoryName',
      key: 'questionFirstCategoryName',
      width: 140,
      render: (text: string) => text || '-',
    },
    {
      title: '小类',
      dataIndex: 'questionSecondCategoryName',
      key: 'questionSecondCategoryName',
      width: 140,
      render: (text: string) => text || '-',
    },
    {
      title: '作答状态',
      dataIndex: 'questionBankAnswerStatus',
      key: 'questionBankAnswerStatus',
      width: 120,
      render: (value: number) => {
        const answerStatus = answerStatusMap[value]
        return <Tag color={answerStatus?.color || 'default'}>{answerStatus?.text || `状态${value}`}</Tag>
      },
    },
    {
      title: '更新时间',
      dataIndex: 'questionBankUpdateTime',
      key: 'questionBankUpdateTime',
      width: 180,
      render: (text: string) => text || '-',
    },
    {
      title: '操作',
      key: 'actions',
      width: 110,
      render: (_: unknown, record: QuestionBankListItem) => (
        <Button
          type="link"
          onClick={() => navigate(`/question-bank/${record.questionBankId}`, {
            state: { from: `${location.pathname}${location.search}` },
          })}
        >
          去答题
        </Button>
      ),
    },
  ]

  if (!isLoggedIn) {
    return (
      <div className="question-bank-list-page">
        <Card className="question-bank-auth-card">
          <Empty description="查看题库需要先登录。">
            <Space>
              <Button type="primary" onClick={() => navigate('/auth/login')}>
                去登录
              </Button>
              <Button onClick={() => navigate('/home')}>
                返回首页
              </Button>
            </Space>
          </Empty>
        </Card>
      </div>
    )
  }

  if (!user?.userId) {
    return (
      <div className="question-bank-list-page">
        <Card className="question-bank-auth-card">
          <Empty description="正在恢复用户信息，请稍候..." />
        </Card>
      </div>
    )
  }

  return (
    <div className="question-bank-list-page">
      <div className="page-header">
        <h1>笔试题库</h1>
        <p>支持按大类、小类、题型、难度、作答状态和关键词组合筛选。</p>
      </div>

      <Card className="filter-card">
        <div className="question-bank-toolbar">
          <div className="question-bank-filter-summary">
            <div className="question-bank-filter-title">当前筛选</div>
            <Space wrap>
              <Tag color="blue">{filterTitle}</Tag>
              {filters.questionBankType ? <Tag color="purple">题型：{questionTypeMap[filters.questionBankType]}</Tag> : null}
              {filters.questionBankDifficulty ? <Tag color="gold">难度：{difficultyMap[filters.questionBankDifficulty]?.text}</Tag> : null}
              {filters.questionBankAnswerStatus !== undefined ? <Tag color="geekblue">状态：{answerStatusMap[filters.questionBankAnswerStatus]?.text}</Tag> : null}
              {filters.questionBankDescription ? <Tag color="cyan">关键词：{filters.questionBankDescription}</Tag> : null}
            </Space>
          </div>
        </div>

        <div className="question-bank-filter-grid">
          <Select
            allowClear
            placeholder="请选择题目大类"
            value={filters.questionFirstCategoryId}
            onChange={handleFirstCategoryChange}
            options={firstCategories.map((item) => ({
              label: item.questionFirstCategoryName,
              value: item.questionFirstCategoryId,
            }))}
          />

          <Select
            allowClear
            placeholder={filters.questionFirstCategoryId ? '请选择题目小类' : '请先选择题目大类'}
            value={filters.questionSecondCategoryId}
            onChange={(value) => {
              const selectedSecondCategory = secondCategories.find((item) => item.questionSecondCategoryId === value)
              setFilters((current) => ({ ...current, questionSecondCategoryId: value }))
              setSecondCategoryName(selectedSecondCategory?.questionSecondCategoryName || '')
              setPagination((current) => ({ ...current, current: 1 }))
            }}
            options={secondCategories.map((item) => ({
              label: item.questionSecondCategoryName,
              value: item.questionSecondCategoryId,
            }))}
            disabled={!filters.questionFirstCategoryId}
            loading={isSecondCategoriesFetching}
          />

          <Select
            allowClear
            placeholder="请选择题目类型"
            value={filters.questionBankType}
            onChange={(value) => {
              setFilters((current) => ({ ...current, questionBankType: value }))
              setPagination((current) => ({ ...current, current: 1 }))
            }}
            options={Object.entries(questionTypeMap).map(([value, label]) => ({
              label,
              value: Number(value),
            }))}
          />

          <Select
            allowClear
            placeholder="请选择题目难度"
            value={filters.questionBankDifficulty}
            onChange={(value) => {
              setFilters((current) => ({ ...current, questionBankDifficulty: value }))
              setPagination((current) => ({ ...current, current: 1 }))
            }}
            options={Object.entries(difficultyMap).map(([value, config]) => ({
              label: config.text,
              value: Number(value),
            }))}
          />

          <Select
            allowClear
            placeholder="请选择作答状态"
            value={filters.questionBankAnswerStatus}
            onChange={(value) => {
              setFilters((current) => ({ ...current, questionBankAnswerStatus: value }))
              setPagination((current) => ({ ...current, current: 1 }))
            }}
            options={Object.entries(answerStatusMap).map(([value, config]) => ({
              label: config.text,
              value: Number(value),
            }))}
          />

          <Input
            allowClear
            placeholder="搜索题目描述"
            prefix={<SearchOutlined />}
            value={keywordInput}
            onChange={(event) => setKeywordInput(event.target.value)}
            onPressEnter={handleSearch}
            className="question-bank-search"
          />
        </div>

        <div className="question-bank-filter-actions">
          <Row gutter={12} justify="end">
            <Col>
              <Button type="primary" icon={<SearchOutlined />} onClick={handleSearch}>
                查询
              </Button>
            </Col>
            <Col>
              <Button icon={<ReloadOutlined />} onClick={handleResetFilters}>
                重置筛选
              </Button>
            </Col>
          </Row>
        </div>
      </Card>

      <Card className="table-card">
        {error ? (
          <Empty description="题库加载失败，请稍后重试。">
            <Button type="primary" icon={<ReloadOutlined />} onClick={() => refetch()}>
              重新加载
            </Button>
          </Empty>
        ) : !isLoading && !data?.records?.length ? (
          <Empty description="当前筛选下暂无题目。" />
        ) : (
          <>
            {(isFetching && !isLoading) ? (
              <Alert className="question-bank-fetching" type="info" showIcon message="正在刷新题库列表..." />
            ) : null}
            <Table<QuestionBankListItem>
              dataSource={data?.records || []}
              loading={isLoading}
              rowKey="questionBankId"
              pagination={{
                current: data?.current || pagination.current,
                pageSize: data?.size || pagination.pageSize,
                total: data?.total || 0,
                onChange: (page, pageSize) => setPagination({ current: page, pageSize }),
                showSizeChanger: true,
                showQuickJumper: true,
                pageSizeOptions: ['10', '20', '50'],
                showTotal: (total, range) => `第 ${range[0]}-${range[1]} 条，共 ${total} 条`,
              }}
              columns={columns}
            />
          </>
        )}
      </Card>
    </div>
  )
}

export default QuestionBankListPage
