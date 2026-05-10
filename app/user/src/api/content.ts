import request from '@utils/request'
import type {
  ContentPageQuery,
  FaqDetail,
  FaqListItem,
  GuideDetail,
  GuideListItem,
  PaginatedResponse,
  PaginationParams,
  SingletonContent,
} from '@types/index'

const mapSingletonContent = <T extends Record<string, any>>(
  data: T | undefined,
  idKey: keyof T,
  titleKey: keyof T,
  contentKey: keyof T,
  updatedTimeKey: keyof T,
  createdTimeKey?: keyof T,
): SingletonContent => ({
  id: Number(data?.[idKey] ?? 0),
  title: String(data?.[titleKey] ?? ''),
  content: String(data?.[contentKey] ?? ''),
  createdTime: createdTimeKey ? String(data?.[createdTimeKey] ?? '') : undefined,
  updatedTime: String(data?.[updatedTimeKey] ?? ''),
})

const mapPageResponse = <TInput extends Record<string, any>, TOutput>(
  response: any,
  mapper: (item: TInput) => TOutput,
): PaginatedResponse<TOutput> => ({
  records: (response?.data?.records || []).map(mapper),
  total: response?.data?.total || 0,
  size: response?.data?.size || 0,
  current: response?.data?.current || 1,
  pages: response?.data?.pages || 0,
})

const mapGuide = (item: Record<string, any>): GuideListItem => ({
  id: Number(item.userGuideId ?? 0),
  title: String(item.userGuideTitle ?? ''),
  content: String(item.userGuideContent ?? ''),
  createdTime: String(item.userGuideCreatedTime ?? ''),
  updatedTime: String(item.userGuideUpdatedTime ?? ''),
})

const mapFaq = (item: Record<string, any>): FaqListItem => ({
  id: Number(item.faqId ?? 0),
  title: String(item.faqTitle ?? ''),
  content: String(item.faqContent ?? ''),
  createdTime: String(item.faqCreatedTime ?? ''),
  updatedTime: String(item.faqUpdatedTime ?? ''),
})

export const contentAPI = {
  getUserAnnouncement: async (): Promise<{ announcementTitle: string; announcementContent: string } | null> => {
    const response = await request.get('/admonitor/user/announcement/getInfo')
    return response?.data || response || null
  },
  getCompanyInfo: async (): Promise<SingletonContent> => {
    const response = await request.get('/user/projectIntroduce/getInfo')
    return mapSingletonContent(response.data, 'projectIntroduceId', 'projectIntroduceTitle', 'projectIntroduceContent', 'projectIntroduceUpdatedTime')
  },

  getTeamInfo: async (): Promise<SingletonContent> => {
    const response = await request.get('/user/teamIntroduce/getInfo')
    return mapSingletonContent(response.data, 'teamIntroduceId', 'teamIntroduceTitle', 'teamIntroduceContent', 'teamIntroduceUpdatedTime')
  },

  getHistoryInfo: async (): Promise<SingletonContent> => {
    const response = await request.get('/user/developHistory/getInfo')
    return mapSingletonContent(response.data, 'developHistoryId', 'developHistoryTitle', 'developHistoryContent', 'developHistoryUpdatedTime')
  },

  getJoinUsInfo: async (): Promise<SingletonContent> => {
    const response = await request.get('/user/joinUs/getInfo')
    return mapSingletonContent(response.data, 'joinUsId', 'joinUsTitle', 'joinUsContent', 'joinUsUpdatedTime')
  },

  getPartnersInfo: async (): Promise<SingletonContent> => {
    const response = await request.get('/user/partnerIntroduce/getInfo')
    return mapSingletonContent(response.data, 'partnerIntroduceId', 'partnerIntroduceTitle', 'partnerIntroduceContent', 'partnerIntroduceUpdatedTime')
  },

  getMediaInfo: async (): Promise<SingletonContent> => {
    const response = await request.get('/user/mediaReport/getInfo')
    return mapSingletonContent(response.data, 'mediaReportId', 'mediaReportTitle', 'mediaReportContent', 'mediaReportUpdatedTime')
  },

  getContactInfo: async (): Promise<SingletonContent> => {
    const response = await request.get('/user/customerService/getInfo')
    return mapSingletonContent(response.data, 'customerServiceId', 'customerServiceTitle', 'customerServiceContent', 'customerServiceUpdatedTime')
  },

  getGuidePage: async (
    params: PaginationParams & { query?: ContentPageQuery },
  ): Promise<PaginatedResponse<GuideListItem>> => {
    const response = await request.post('/user/userGuide/getPage', {
      userGuideTitle: params.query?.keyword || '',
    }, {
      params: {
        size: params.pageSize,
        page: params.pageNum,
      },
    })

    return mapPageResponse(response, mapGuide)
  },

  getGuideInfo: async (id: number): Promise<GuideDetail> => {
    const response = await request.get('/user/userGuide/getInfo', {
      params: { userGuideId: id },
    })

    return mapGuide(response.data)
  },

  getFaqPage: async (
    params: PaginationParams & { query?: ContentPageQuery },
  ): Promise<PaginatedResponse<FaqListItem>> => {
    const response = await request.post('/user/faq/getPage', {
      faqTitle: params.query?.keyword || '',
    }, {
      params: {
        size: params.pageSize,
        page: params.pageNum,
      },
    })

    return mapPageResponse(response, mapFaq)
  },

  getFaqInfo: async (id: number): Promise<FaqDetail> => {
    const response = await request.get('/user/faq/getInfo', {
      params: { faqId: id },
    })

    return mapFaq(response.data)
  },
}
