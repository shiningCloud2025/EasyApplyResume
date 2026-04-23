import { contentAPI } from '@api/content'
import type { ContentBase, PaginatedResponse, PaginationParams, SingletonContent } from '@types/index'

export interface SingletonContentPageConfig {
  key: string
  section: string
  title: string
  description: string
  emptyDescription: string
  fetcher: () => Promise<SingletonContent>
}

export interface ContentListPageConfig {
  key: string
  section: string
  title: string
  description: string
  emptyDescription: string
  searchPlaceholder: string
  detailBasePath: string
  fetchPage: (params: PaginationParams & { query?: { keyword?: string } }) => Promise<PaginatedResponse<ContentBase>>
}

export interface ContentDetailPageConfig {
  key: string
  section: string
  title: string
  description: string
  emptyDescription: string
  listPath: string
  fetcher: (id: number) => Promise<ContentBase>
}

export const singletonContentPageConfigs: Record<string, SingletonContentPageConfig> = {
  company: {
    key: 'about-company',
    section: '关于我们',
    title: '项目介绍',
    description: '了解 EasyApplyResume 项目的定位、理念与面向求职者的服务愿景。',
    emptyDescription: '暂未发布项目介绍内容。',
    fetcher: contentAPI.getCompanyInfo,
  },
  team: {
    key: 'about-team',
    section: '关于我们',
    title: '团队介绍',
    description: '认识推动产品持续迭代的核心团队与服务支持团队。',
    emptyDescription: '暂未发布团队介绍内容。',
    fetcher: contentAPI.getTeamInfo,
  },
  history: {
    key: 'about-history',
    section: '关于我们',
    title: '发展历程',
    description: '查看平台从创立到成长的重要阶段与关键里程碑。',
    emptyDescription: '暂未发布发展历程内容。',
    fetcher: contentAPI.getHistoryInfo,
  },
  joinUs: {
    key: 'about-join-us',
    section: '关于我们',
    title: '加入我们',
    description: '探索团队文化、岗位机会与一起打造更好求职体验的可能。',
    emptyDescription: '暂未发布加入我们内容。',
    fetcher: contentAPI.getJoinUsInfo,
  },
  partners: {
    key: 'about-partners',
    section: '关于我们',
    title: '合作伙伴',
    description: '了解与我们携手合作的企业、机构与生态伙伴。',
    emptyDescription: '暂未发布合作伙伴内容。',
    fetcher: contentAPI.getPartnersInfo,
  },
  media: {
    key: 'about-media',
    section: '关于我们',
    title: '媒体报道',
    description: '查看平台相关报道、行业动态与公开发布的内容资讯。',
    emptyDescription: '暂未发布媒体报道内容。',
    fetcher: contentAPI.getMediaInfo,
  },
  contact: {
    key: 'help-contact',
    section: '帮助中心',
    title: '联系客服',
    description: '如需人工协助，可在这里查看客服渠道、服务方式与联系说明。',
    emptyDescription: '暂未发布客服信息。',
    fetcher: contentAPI.getContactInfo,
  },
}

export const guideListPageConfig: ContentListPageConfig = {
  key: 'help-guide-list',
  section: '帮助中心',
  title: '使用指南',
  description: '快速了解平台功能、使用步骤与常见操作流程。',
  emptyDescription: '暂未发布使用指南内容。',
  searchPlaceholder: '搜索指南标题',
  detailBasePath: '/help/guide',
  fetchPage: contentAPI.getGuidePage,
}

export const guideDetailPageConfig: ContentDetailPageConfig = {
  key: 'help-guide-detail',
  section: '帮助中心',
  title: '使用指南',
  description: '查看指南详情，获取更完整的操作说明与内容信息。',
  emptyDescription: '该使用指南不存在或暂未发布。',
  listPath: '/help/guide',
  fetcher: contentAPI.getGuideInfo,
}

export const faqListPageConfig: ContentListPageConfig = {
  key: 'help-faq-list',
  section: '帮助中心',
  title: '常见问题',
  description: '集中查看使用过程中的常见问题与解答，快速定位所需信息。',
  emptyDescription: '暂未发布常见问题内容。',
  searchPlaceholder: '搜索问题标题',
  detailBasePath: '/help/faq',
  fetchPage: contentAPI.getFaqPage,
}

export const faqDetailPageConfig: ContentDetailPageConfig = {
  key: 'help-faq-detail',
  section: '帮助中心',
  title: '常见问题',
  description: '查看问题详情，获取完整答案与更新时间。',
  emptyDescription: '该常见问题不存在或暂未发布。',
  listPath: '/help/faq',
  fetcher: contentAPI.getFaqInfo,
}
