import request from '@utils/request'
import type {
  PaginatedResponse,
  PaginationParams,
  QuestionBankAnswerForm,
  QuestionBankAnswerResult,
  QuestionBankDetail,
  QuestionBankListItem,
  QuestionBankQuery,
  QuestionFirstCategory,
  QuestionSecondCategory,
} from '@types/index'

const mapFirstCategory = (item: Record<string, any>): QuestionFirstCategory => ({
  questionFirstCategoryId: Number(item.questionFirstCategoryId ?? 0),
  questionFirstCategoryName: String(item.questionFirstCategoryName ?? ''),
  questionFirstCategoryIntroduce: String(item.questionFirstCategoryIntroduce ?? ''),
  questionFirstCategoryCreateTime: String(item.questionFirstCategoryCreateTime ?? ''),
})

const mapSecondCategory = (item: Record<string, any>): QuestionSecondCategory => ({
  questionSecondCategoryId: Number(item.questionSecondCategoryId ?? 0),
  questionFirstCategoryId: Number(item.questionFirstCategoryId ?? 0),
  questionFirstCategoryName: item.questionFirstCategoryName ? String(item.questionFirstCategoryName) : undefined,
  questionSecondCategoryName: String(item.questionSecondCategoryName ?? ''),
  questionSecondCategoryIntroduce: String(item.questionSecondCategoryIntroduce ?? ''),
  questionSecondCategoryCreateTime: String(item.questionSecondCategoryCreateTime ?? ''),
})

const mapQuestionBank = (item: Record<string, any>): QuestionBankListItem => ({
  questionBankId: Number(item.questionBankId ?? 0),
  questionBankDescription: String(item.questionBankDescription ?? ''),
  questionBankType: Number(item.questionBankType ?? 0),
  questionFirstCategoryName: String(item.questionFirstCategoryName ?? ''),
  questionSecondCategoryName: String(item.questionSecondCategoryName ?? ''),
  questionBankDifficulty: Number(item.questionBankDifficulty ?? 0),
  questionBankState: Number(item.questionBankState ?? 0),
  questionBankAnswerStatus: Number(item.questionBankAnswerStatus ?? 0),
  questionBankCreateTime: String(item.questionBankCreateTime ?? ''),
  questionBankUpdateTime: String(item.questionBankUpdateTime ?? ''),
})

const mapQuestionBankDetail = (item: Record<string, any>): QuestionBankDetail => ({
  questionBankId: Number(item.questionBankId ?? 0),
  questionBankDescription: String(item.questionBankDescription ?? ''),
  questionBankOptionA: item.questionBankOptionA ? String(item.questionBankOptionA) : undefined,
  questionBankOptionB: item.questionBankOptionB ? String(item.questionBankOptionB) : undefined,
  questionBankOptionC: item.questionBankOptionC ? String(item.questionBankOptionC) : undefined,
  questionBankOptionD: item.questionBankOptionD ? String(item.questionBankOptionD) : undefined,
  questionBankImage: item.questionBankImage ? String(item.questionBankImage) : undefined,
  questionBankCode: item.questionBankCode ? String(item.questionBankCode) : undefined,
  questionBankReferenceAnswer: item.questionBankReferenceAnswer ? String(item.questionBankReferenceAnswer) : undefined,
  questionBankAnalysis: item.questionBankAnalysis ? String(item.questionBankAnalysis) : undefined,
  questionBankType: Number(item.questionBankType ?? 0),
  questionFirstCategoryName: String(item.questionFirstCategoryName ?? ''),
  questionSecondCategoryName: String(item.questionSecondCategoryName ?? ''),
  questionBankDifficulty: Number(item.questionBankDifficulty ?? 0),
})

const mapQuestionBankAnswerResult = (item: Record<string, any>): QuestionBankAnswerResult => ({
  questionBankId: Number(item.questionBankId ?? 0),
  correct: Boolean(item.correct),
  correctAnswer: item.correctAnswer ? String(item.correctAnswer) : undefined,
  referenceAnswer: item.referenceAnswer ? String(item.referenceAnswer) : undefined,
  questionBankAnalysis: item.questionBankAnalysis ? String(item.questionBankAnalysis) : undefined,
})

const mapPageResponse = <TInput extends Record<string, any>, TOutput>(
  response: any,
  mapper: (item: TInput) => TOutput,
): PaginatedResponse<TOutput> => ({
  records: (response?.data?.records || []).map(mapper),
  total: Number(response?.data?.total ?? 0),
  size: Number(response?.data?.size ?? 0),
  current: Number(response?.data?.current ?? 1),
  pages: Number(response?.data?.pages ?? 0),
})

export const questionBankAPI = {
  getAllFirstCategories: async (): Promise<QuestionFirstCategory[]> => {
    const response = await request.get('/user/questionFirstCategory/findAllQuestionFirstCategory')
    return Array.isArray(response?.data) ? response.data.map(mapFirstCategory) : []
  },

  getSecondCategoriesByFirstCategoryId: async (questionFirstCategoryId: number): Promise<QuestionSecondCategory[]> => {
    const response = await request.get('/user/questionSecondCategory/findQuestionSecondCategoryByFirstCategoryId', {
      params: { questionFirstCategoryId },
    })

    return Array.isArray(response?.data) ? response.data.map(mapSecondCategory) : []
  },

  getAllSecondCategories: async (): Promise<QuestionSecondCategory[]> => {
    const response = await request.get('/user/questionSecondCategory/findAllQuestionSecondCategory')
    return Array.isArray(response?.data) ? response.data.map(mapSecondCategory) : []
  },

  getQuestionBankPage: async (
    params: PaginationParams & { userId: number, query?: QuestionBankQuery },
  ): Promise<PaginatedResponse<QuestionBankListItem>> => {
    const response = await request.post('/user/questionBank/findQuestionBankByPage', params.query || {}, {
      params: {
        userId: params.userId,
        pageNum: params.pageNum,
        pageSize: params.pageSize,
      },
    })

    return mapPageResponse(response, mapQuestionBank)
  },

  getQuestionBankDetail: async (questionBankId: number): Promise<QuestionBankDetail> => {
    const response = await request.get('/user/questionBank/findQuestionBankById', {
      params: { questionBankId },
    })

    return mapQuestionBankDetail(response.data)
  },

  submitQuestionBankAnswer: async (payload: QuestionBankAnswerForm): Promise<QuestionBankAnswerResult> => {
    const response = await request.post('/user/questionBank/submitQuestionBankAnswer', payload)
    return mapQuestionBankAnswerResult(response.data)
  },
}
