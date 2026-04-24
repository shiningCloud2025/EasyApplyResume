import React, { useEffect, useMemo, useState } from 'react'
import { Alert, Button, Card, Checkbox, Empty, Image, Input, Radio, Spin, message } from 'antd'
import { ArrowLeftOutlined } from '@ant-design/icons'
import { useMutation, useQuery, useQueryClient } from 'react-query'
import { useLocation, useNavigate, useParams } from 'react-router-dom'
import { questionBankAPI } from '@api/questionBank'
import { useUserStore } from '@stores/userStore'
import type { QuestionBankAnswerResult, QuestionBankDetail } from '@types/index'
import './QuestionBankDetailPage.scss'

const QuestionBankDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const location = useLocation()
  const queryClient = useQueryClient()
  const { user, isLoggedIn, fetchUserInfo } = useUserStore()
  const [singleAnswer, setSingleAnswer] = useState('')
  const [multipleAnswers, setMultipleAnswers] = useState<string[]>([])
  const [textAnswer, setTextAnswer] = useState('')
  const [submitResult, setSubmitResult] = useState<QuestionBankAnswerResult | null>(null)
  const [showAnalysis, setShowAnalysis] = useState(false)

  const fromPath = useMemo(() => {
    const state = location.state as { from?: string } | null
    return state?.from || '/question-bank'
  }, [location.state])

  useEffect(() => {
    if (isLoggedIn && !user?.userId) {
      fetchUserInfo(true)
    }
  }, [fetchUserInfo, isLoggedIn, user?.userId])

  const {
    data: detail,
    isLoading,
    error,
    refetch,
  } = useQuery<QuestionBankDetail>(
    ['questionBankDetail', id],
    () => questionBankAPI.getQuestionBankDetail(Number(id)),
    {
      enabled: !!id,
    },
  )

  const choiceOptions = useMemo(() => {
    if (!detail) {
      return []
    }

    if (detail.questionBankType === 3) {
      return [
        { value: 'A', content: detail.questionBankOptionA || '正确' },
        { value: 'B', content: detail.questionBankOptionB || '错误' },
      ]
    }

    return [
      { value: 'A', content: detail.questionBankOptionA || '' },
      { value: 'B', content: detail.questionBankOptionB || '' },
      { value: 'C', content: detail.questionBankOptionC || '' },
      { value: 'D', content: detail.questionBankOptionD || '' },
    ].filter((item) => item.content.trim())
  }, [detail])

  const currentAnswers = useMemo(() => {
    if (!detail) {
      return []
    }

    const normalizedTextAnswer = textAnswer.trim()

    if (detail.questionBankType === 1 || detail.questionBankType === 3) {
      return singleAnswer ? [singleAnswer] : []
    }

    if (detail.questionBankType === 2) {
      return multipleAnswers
    }

    if (detail.questionBankType === 4 || detail.questionBankType === 5) {
      return normalizedTextAnswer ? [normalizedTextAnswer] : []
    }

    return []
  }, [detail, multipleAnswers, singleAnswer, textAnswer])

  useEffect(() => {
    setSingleAnswer('')
    setMultipleAnswers([])
    setTextAnswer('')
    setSubmitResult(null)
    setShowAnalysis(false)
  }, [detail?.questionBankId])

  useEffect(() => {
    setSubmitResult(null)
    setShowAnalysis(false)
  }, [currentAnswers])

  const handleBack = (needRefresh = false) => {
    if (!needRefresh) {
      navigate(fromPath)
      return
    }

    const [pathname, search = ''] = fromPath.split('?')
    const searchParams = new URLSearchParams(search)
    searchParams.set('refreshToken', String(Date.now()))
    const nextSearch = searchParams.toString()
    navigate(`${pathname}${nextSearch ? `?${nextSearch}` : ''}`)
  }

  const submitAnswerMutation = useMutation(
    (userAnswers: string[]) => questionBankAPI.submitQuestionBankAnswer({
      userId: Number(user?.userId),
      questionBankId: Number(id),
      userAnswers,
    }),
    {
      onSuccess: async (result) => {
        setSubmitResult(result)
        setShowAnalysis(false)
        await queryClient.invalidateQueries(['questionBankPage'])
      },
      onError: () => {
        message.error('提交失败，请稍后重试。')
      },
    },
  )

  const handleSubmit = () => {
    if (!detail) {
      return
    }

    if (!user?.userId) {
      fetchUserInfo(true)
      message.warning('用户信息恢复中，请稍后再试。')
      return
    }

    if (!currentAnswers.length) {
      if (detail.questionBankType === 2) {
        message.warning('请至少选择一个答案。')
        return
      }

      if (detail.questionBankType === 4) {
        message.warning('请输入填空答案。')
        return
      }

      if (detail.questionBankType === 5) {
        message.warning('请输入作答内容。')
        return
      }

      message.warning('请选择答案。')
      return
    }

    submitAnswerMutation.mutate(currentAnswers)
  }

  const handleReset = () => {
    setSingleAnswer('')
    setMultipleAnswers([])
    setTextAnswer('')
    setSubmitResult(null)
    setShowAnalysis(false)
  }

  if (isLoading) {
    return (
      <div className="question-bank-detail-page">
        <div className="loading-wrapper">
          <Spin size="large" tip="加载中..." />
        </div>
      </div>
    )
  }

  if (error || !detail) {
    return (
      <div className="question-bank-detail-page">
        <Empty description="题目详情不存在或加载失败。">
          <Button type="primary" onClick={() => refetch()}>
            重新加载
          </Button>
          <Button onClick={() => handleBack()}>
            返回题库
          </Button>
        </Empty>
      </div>
    )
  }

  const isSubjectiveQuestion = detail.questionBankType === 5
  const submitResultType = isSubjectiveQuestion ? 'success' : submitResult?.correct ? 'success' : 'error'
  const submitResultMessage = isSubjectiveQuestion
    ? '答案已提交，当前题目已记录为已作答。'
    : submitResult?.correct
      ? '回答正确，继续保持。'
      : '回答错误，再试一次。'

  const answerLabel = submitResult?.referenceAnswer ? '参考答案：' : '正确答案：'
  const answerText = submitResult?.referenceAnswer || submitResult?.correctAnswer
  const analysisText = submitResult?.questionBankAnalysis?.trim()
  const answerResultDescription = answerText
    ? (
      <div className="answer-result-detail">
        <div>
          <strong>{answerLabel}</strong>
          <span>{answerText}</span>
        </div>
      </div>
    )
    : undefined

  return (
    <div className="question-bank-detail-page">
      <Card className="detail-card">
        <Button type="link" icon={<ArrowLeftOutlined />} onClick={() => handleBack(!!submitResult)} className="back-btn">
          返回题库
        </Button>

        <div className="detail-section">
          <div className="detail-section-title">题目信息</div>
          <div className="question-description">{detail.questionBankDescription || '-'}</div>
        </div>

        {detail.questionBankCode ? (
          <div className="detail-section">
            <div className="detail-section-title">代码段</div>
            <pre className="detail-code-block"><code>{detail.questionBankCode}</code></pre>
          </div>
        ) : null}

        {detail.questionBankImage ? (
          <div className="detail-section">
            <div className="detail-section-title">题目图片</div>
            <Image src={detail.questionBankImage} alt="question" className="detail-image" />
          </div>
        ) : null}

        <div className="detail-section">
          <div className="detail-section-title">开始答题</div>

          {detail.questionBankType === 2 ? (
            <Alert
              className="answer-tip"
              type="info"
              showIcon
              message="多选题支持多选，提交前请确认所选答案。"
            />
          ) : null}

          {isSubjectiveQuestion ? (
            <Alert
              className="answer-tip"
              type="info"
              showIcon
              message="当前题目为主观题，提交后会记录本题已作答。"
            />
          ) : null}

          {(detail.questionBankType === 1 || detail.questionBankType === 3) ? (
            <Radio.Group
              value={singleAnswer}
              onChange={(event) => {
                setSingleAnswer(String(event.target.value))
                setSubmitResult(null)
              }}
              className="choice-answer-group"
            >
              {choiceOptions.map((option) => (
                <Radio key={option.value} value={option.value} className="choice-answer-item">
                  <span className="choice-answer-key">{option.value}.</span>
                  <span className="choice-answer-text">{option.content}</span>
                </Radio>
              ))}
            </Radio.Group>
          ) : null}

          {detail.questionBankType === 2 ? (
            <Checkbox.Group
              value={multipleAnswers}
              onChange={(values) => {
                setMultipleAnswers(values.map(String))
                setSubmitResult(null)
              }}
              className="choice-answer-group"
            >
              {choiceOptions.map((option) => (
                <Checkbox key={option.value} value={option.value} className="choice-answer-item">
                  <span className="choice-answer-key">{option.value}.</span>
                  <span className="choice-answer-text">{option.content}</span>
                </Checkbox>
              ))}
            </Checkbox.Group>
          ) : null}

          {detail.questionBankType === 4 ? (
            <Input
              value={textAnswer}
              onChange={(event) => {
                setTextAnswer(event.target.value)
                setSubmitResult(null)
              }}
              placeholder="请输入填空答案"
              className="answer-input"
            />
          ) : null}

          {detail.questionBankType === 5 ? (
            <Input.TextArea
              value={textAnswer}
              onChange={(event) => {
                setTextAnswer(event.target.value)
                setSubmitResult(null)
              }}
              placeholder="请输入你的作答内容"
              rows={8}
              className="answer-textarea"
            />
          ) : null}

          {!user?.userId ? (
            <Alert
              className="answer-tip"
              type="info"
              showIcon
              message="正在恢复用户信息，恢复后即可提交答案。"
            />
          ) : null}

          <div className="answer-actions">
            <Button type="primary" onClick={handleSubmit} loading={submitAnswerMutation.isLoading} disabled={!user?.userId}>
              提交答案
            </Button>
            <Button onClick={handleReset} disabled={submitAnswerMutation.isLoading}>
              清空作答
            </Button>
          </div>
        </div>

        {submitResult ? (
          <>
            <Alert
              className="detail-section answer-result-alert"
              type={submitResultType}
              showIcon
              message={submitResultMessage}
              description={answerResultDescription}
            />
            {analysisText ? (
              <div className="detail-section answer-analysis-section">
                <Button type="link" className="answer-analysis-toggle" onClick={() => setShowAnalysis((prev) => !prev)}>
                  {showAnalysis ? '收起题目解析' : '查看题目解析'}
                </Button>
                {showAnalysis ? (
                  <Alert
                    className="answer-analysis-alert"
                    type="info"
                    showIcon
                    message="题目解析"
                    description={<div className="answer-result-detail">{analysisText}</div>}
                  />
                ) : null}
              </div>
            ) : null}
          </>
        ) : null}
      </Card>
    </div>
  )
}

export default QuestionBankDetailPage
