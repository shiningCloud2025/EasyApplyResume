import React, { useEffect, useMemo, useRef, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { Button, Space, Dropdown, message, Input } from 'antd'
import type { MenuProps } from 'antd'
import { UserOutlined, LogoutOutlined, SearchOutlined, DownOutlined } from '@ant-design/icons'
import { useQuery, useQueryClient } from 'react-query'
import { useUserStore } from '@stores/userStore'
import { userAPI } from '@api/feedback'
import { questionBankAPI } from '@api/questionBank'
import type { QuestionFirstCategory, QuestionSecondCategory } from '@types/index'
import './PortalHeader.scss'

interface PortalHeaderProps {
  activeMenu?: string
  onMenuClick?: (menu: string) => void
}

interface PortalMenuItem {
  key: string
  label: string
  path: string
  requireLogin?: boolean
  activePrefixes?: string[]
  menuItems?: MenuProps['items']
}

const PortalHeader: React.FC<PortalHeaderProps> = ({ activeMenu, onMenuClick }) => {
  const navigate = useNavigate()
  const location = useLocation()
  const queryClient = useQueryClient()
  const { user, isLoggedIn, logout, getUserById } = useUserStore()
  const [isSticky, setIsSticky] = useState(false)
  const [secondCategoryCache, setSecondCategoryCache] = useState<Record<number, QuestionSecondCategory[]>>({})
  const [loadingFirstCategoryIds, setLoadingFirstCategoryIds] = useState<number[]>([])
  const [allSecondCategoriesLoaded, setAllSecondCategoriesLoaded] = useState(false)
  const [allSecondCategoriesLoading, setAllSecondCategoriesLoading] = useState(false)
  const menuOpenKeysRef = useRef<string[]>([])

  useEffect(() => {
    const handleScroll = () => {
      setIsSticky(window.scrollY > 50)
    }

    window.addEventListener('scroll', handleScroll)
    return () => window.removeEventListener('scroll', handleScroll)
  }, [])

  useEffect(() => {
    const fetchUserInfo = async () => {
      if (isLoggedIn && user?.userId) {
        try {
          await getUserById(String(user.userId))
        } catch (error) {
          console.error('❌ 自动获取用户信息失败:', error)
        }
      }
    }

    fetchUserInfo()
  }, [isLoggedIn, user?.userId, getUserById])

  const { data: firstCategories = [], isLoading: isFirstCategoriesLoading } = useQuery<QuestionFirstCategory[]>(
    ['questionFirstCategories'],
    questionBankAPI.getAllFirstCategories,
    {
      staleTime: 10 * 60 * 1000,
    },
  )

  const handleGoToProfile = async () => {
    if (user?.userId) {
      try {
        const res = await userAPI.getUserById(String(user.userId))
        if (res.data) {
          await getUserById(String(user.userId))
        }
      } catch (error) {
        console.error('❌ 获取用户信息失败:', error)
      }
    }

    navigate('/profile')
  }

  const userMenuItems = [
    {
      key: 'profile',
      icon: <UserOutlined />,
      label: '个人中心',
      onClick: handleGoToProfile,
    },
    {
      type: 'divider' as const,
    },
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: () => {
        logout()
        navigate('/auth/login')
      },
    },
  ]

  const handleNavigate = (path: string, menuKey?: string, requireLogin?: boolean) => {
    if (onMenuClick && menuKey) {
      onMenuClick(menuKey)
    }

    if (requireLogin && !isLoggedIn) {
      message.info('请先登录')
      navigate('/auth/login')
      return
    }

    if (path === '/my-resumes' && !isLoggedIn) {
      message.info('请先登录')
      navigate('/auth/login')
    } else if (path === '/my-resumes' && isLoggedIn) {
      navigate('/resume/my-resumes')
    } else if (
      path.startsWith('/resume') ||
      path.startsWith('/jobs') ||
      path.startsWith('/advice') ||
      path.startsWith('/ai') ||
      path.startsWith('/question-bank')
    ) {
      navigate(path)
    } else {
      navigate(path)
    }
  }

  const buildQuestionBankPath = (params?: {
    firstCategoryId?: number
    secondCategoryId?: number
    firstCategoryName?: string
    secondCategoryName?: string
  }) => {
    const searchParams = new URLSearchParams()

    if (params?.firstCategoryId) {
      searchParams.set('firstCategoryId', String(params.firstCategoryId))
    }

    if (params?.secondCategoryId) {
      searchParams.set('secondCategoryId', String(params.secondCategoryId))
    }

    if (params?.firstCategoryName) {
      searchParams.set('firstCategoryName', params.firstCategoryName)
    }

    if (params?.secondCategoryName) {
      searchParams.set('secondCategoryName', params.secondCategoryName)
    }

    const search = searchParams.toString()
    return search ? `/question-bank?${search}` : '/question-bank'
  }

  const handleQuestionBankNavigate = (params?: {
    firstCategoryId?: number
    secondCategoryId?: number
    firstCategoryName?: string
    secondCategoryName?: string
  }) => {
    if (onMenuClick) {
      onMenuClick('question-bank')
    }

    if (!isLoggedIn) {
      message.info('请先登录')
      navigate('/auth/login')
      return
    }

    navigate(buildQuestionBankPath(params))
  }

  const ensureSecondCategoriesLoaded = async (firstCategory: QuestionFirstCategory) => {
    const { questionFirstCategoryId, questionFirstCategoryName } = firstCategory

    if (secondCategoryCache[questionFirstCategoryId] || loadingFirstCategoryIds.includes(questionFirstCategoryId)) {
      return
    }

    setLoadingFirstCategoryIds((current) => [...current, questionFirstCategoryId])

    try {
      const categories = await queryClient.fetchQuery(
        ['questionSecondCategories', questionFirstCategoryId],
        () => questionBankAPI.getSecondCategoriesByFirstCategoryId(questionFirstCategoryId),
        {
          staleTime: 10 * 60 * 1000,
        },
      )

      setSecondCategoryCache((current) => ({
        ...current,
        [questionFirstCategoryId]: categories.map((item) => ({
          ...item,
          questionFirstCategoryName: questionFirstCategoryName,
        })),
      }))
    } catch (error) {
      console.error('❌ 获取题库小类失败:', error)
    } finally {
      setLoadingFirstCategoryIds((current) => current.filter((id) => id !== questionFirstCategoryId))
    }
  }

  const ensureAllSecondCategoriesLoaded = async () => {
    if (allSecondCategoriesLoaded || allSecondCategoriesLoading) {
      return
    }

    setAllSecondCategoriesLoading(true)

    try {
      const categories = await queryClient.fetchQuery(
        ['allQuestionSecondCategories'],
        questionBankAPI.getAllSecondCategories,
        {
          staleTime: 10 * 60 * 1000,
        },
      )

      const grouped = categories.reduce<Record<number, QuestionSecondCategory[]>>((accumulator, item) => {
        const key = item.questionFirstCategoryId
        if (!accumulator[key]) {
          accumulator[key] = []
        }
        accumulator[key].push(item)
        return accumulator
      }, {})

      setSecondCategoryCache((current) => ({
        ...grouped,
        ...current,
      }))
      setAllSecondCategoriesLoaded(true)
    } catch (error) {
      console.error('❌ 获取全部题库小类失败:', error)
    } finally {
      setAllSecondCategoriesLoading(false)
    }
  }

  const handleQuestionBankMenuOpenChange: MenuProps['onOpenChange'] = (openKeys) => {
    const latestOpenedKey = openKeys.find((key) => !menuOpenKeysRef.current.includes(key))
    menuOpenKeysRef.current = [...openKeys]

    if (latestOpenedKey === 'question-bank-by-second') {
      void ensureAllSecondCategoriesLoaded()
      return
    }

    if (latestOpenedKey?.startsWith('question-bank-second-first-')) {
      const questionFirstCategoryId = Number(latestOpenedKey.replace('question-bank-second-first-', ''))
      const firstCategory = firstCategories.find((item) => item.questionFirstCategoryId === questionFirstCategoryId)
      if (firstCategory) {
        void ensureSecondCategoriesLoaded(firstCategory)
      }
    }
  }

  const aiMenuItems: MenuProps['items'] = [
    {
      key: 'ai-chat',
      label: 'AI智能问答助手',
      onClick: () => handleNavigate('/ai/chat', 'ai-chat'),
    },
    {
      key: 'ai-agent',
      label: 'AI智能体助手',
      onClick: () => handleNavigate('/ai/agent', 'ai-agent'),
    },
  ]

  const questionBankMenuItems: MenuProps['items'] = [
    {
      key: 'question-bank-by-first',
      label: '笔试大类',
      children: isFirstCategoriesLoading
        ? [{ key: 'question-bank-first-loading', label: '加载中...', disabled: true }]
        : firstCategories.length
          ? firstCategories.map((firstCategory) => ({
              key: `question-bank-first-${firstCategory.questionFirstCategoryId}`,
              label: firstCategory.questionFirstCategoryName,
              onClick: () => handleQuestionBankNavigate({
                firstCategoryId: firstCategory.questionFirstCategoryId,
                firstCategoryName: firstCategory.questionFirstCategoryName,
              }),
            }))
          : [{ key: 'question-bank-first-empty', label: '暂无大类', disabled: true }],
    },
    {
      key: 'question-bank-by-second',
      label: '笔试小类',
      children: isFirstCategoriesLoading
        ? [{ key: 'question-bank-second-loading', label: '加载中...', disabled: true }]
        : firstCategories.length
          ? firstCategories.map((firstCategory) => {
              const secondCategories = secondCategoryCache[firstCategory.questionFirstCategoryId] || []
              const isLoadingSecondCategories = loadingFirstCategoryIds.includes(firstCategory.questionFirstCategoryId)

              return {
                key: `question-bank-second-first-${firstCategory.questionFirstCategoryId}`,
                label: firstCategory.questionFirstCategoryName,
                children: secondCategories.length
                  ? secondCategories.map((secondCategory) => ({
                      key: `question-bank-second-${secondCategory.questionSecondCategoryId}`,
                      label: secondCategory.questionSecondCategoryName,
                      onClick: () => handleQuestionBankNavigate({
                        firstCategoryId: firstCategory.questionFirstCategoryId,
                        secondCategoryId: secondCategory.questionSecondCategoryId,
                        firstCategoryName: firstCategory.questionFirstCategoryName,
                        secondCategoryName: secondCategory.questionSecondCategoryName,
                      }),
                    }))
                  : isLoadingSecondCategories
                    ? [{ key: `question-bank-second-${firstCategory.questionFirstCategoryId}-loading`, label: '加载中...', disabled: true }]
                    : [{ key: `question-bank-second-${firstCategory.questionFirstCategoryId}-placeholder`, label: '展开后加载小类', disabled: true }],
              }
            })
          : [{ key: 'question-bank-second-empty', label: '暂无小类', disabled: true }],
    },
    {
      type: 'divider',
    },
    {
      key: 'question-bank-all',
      label: '所有题库',
      onClick: () => handleQuestionBankNavigate(),
    },
  ]

  const helpMenuItems: MenuProps['items'] = [
    {
      key: 'help-guide',
      label: '使用指南',
      onClick: () => handleNavigate('/help/guide', 'help-guide'),
    },
    {
      key: 'help-faq',
      label: '常见问题',
      onClick: () => handleNavigate('/help/faq', 'help-faq'),
    },
    {
      key: 'help-contact',
      label: '联系客服',
      onClick: () => handleNavigate('/help/contact', 'help-contact'),
    },
  ]

  const aboutMenuItems: MenuProps['items'] = [
    {
      key: 'about-company',
      label: '项目介绍',
      onClick: () => handleNavigate('/about/company', 'about-company'),
    },
    {
      key: 'about-team',
      label: '团队介绍',
      onClick: () => handleNavigate('/about/team', 'about-team'),
    },
    {
      key: 'about-history',
      label: '发展历程',
      onClick: () => handleNavigate('/about/history', 'about-history'),
    },
    {
      key: 'about-join-us',
      label: '加入我们',
      onClick: () => handleNavigate('/about/join-us', 'about-join-us'),
    },
    {
      key: 'about-partners',
      label: '合作伙伴',
      onClick: () => handleNavigate('/about/partners', 'about-partners'),
    },
    {
      key: 'about-media',
      label: '媒体报道',
      onClick: () => handleNavigate('/about/media', 'about-media'),
    },
  ]

  const portalMenuItems: PortalMenuItem[] = [
    { key: 'home', label: '首页', path: '/home' },
    {
      key: 'my-resumes',
      label: '我的简历',
      path: '/resume/my-resumes',
      requireLogin: true,
      activePrefixes: ['/resume/my-resumes', '/resume/edit/', '/resume/recycle-bin'],
    },
    {
      key: 'templates',
      label: '简历模板',
      path: '/resume/templates',
      activePrefixes: ['/resume/templates', '/resume/template/'],
    },
    {
      key: 'jobs',
      label: '招聘信息',
      path: '/jobs',
      activePrefixes: ['/jobs', '/job/'],
    },
    {
      key: 'advice',
      label: '求职攻略',
      path: '/advice',
      activePrefixes: ['/advice'],
    },
    {
      key: 'ai',
      label: 'AI简历助手',
      path: '/ai/chat',
      activePrefixes: ['/ai'],
      menuItems: aiMenuItems,
    },
    {
      key: 'question-bank',
      label: '笔试专项',
      path: '/question-bank',
      activePrefixes: ['/question-bank'],
      menuItems: questionBankMenuItems,
    },
    {
      key: 'feedback',
      label: '反馈',
      path: '/feedback/submit',
      activePrefixes: ['/feedback'],
    },
    {
      key: 'help',
      label: '帮助中心',
      path: '/help/guide',
      activePrefixes: ['/help'],
      menuItems: helpMenuItems,
    },
    {
      key: 'about',
      label: '关于我们',
      path: '/about/company',
      activePrefixes: ['/about'],
      menuItems: aboutMenuItems,
    },
  ]

  const isActiveRoute = (path: string, item?: PortalMenuItem) => {
    if (item?.activePrefixes?.some((prefix) => location.pathname.startsWith(prefix))) {
      return true
    }

    return location.pathname === path || (activeMenu && portalMenuItems.find((menuItem) => menuItem.key === activeMenu)?.path === path)
  }

  return (
    <header className={`portal-header ${isSticky ? 'sticky' : ''}`}>
      <div className="header-content">
        <div className="header-left">
          <div className="logo" onClick={() => handleNavigate('/')}>
            📄 易投简历
          </div>
        </div>

        <nav className="header-nav">
          <ul className="nav-list">
            {portalMenuItems.map((item) => (
              <li key={item.key} className={`nav-item ${isActiveRoute(item.path, item) ? 'active' : ''}`}>
                {item.menuItems ? (
                  <Dropdown
                    menu={{ items: item.menuItems, onOpenChange: item.key === 'question-bank' ? handleQuestionBankMenuOpenChange : undefined }}
                    placement="bottom"
                  >
                    <button type="button" className="nav-link nav-dropdown">
                      {item.label} <DownOutlined style={{ fontSize: 10, marginLeft: 4 }} />
                    </button>
                  </Dropdown>
                ) : (
                  <button
                    type="button"
                    className="nav-link"
                    onClick={() => handleNavigate(item.path, item.key, item.requireLogin)}
                  >
                    {item.label}
                  </button>
                )}
              </li>
            ))}
          </ul>
        </nav>

        <div className="header-right">
          <div className="header-actions">
            <Input
              placeholder="搜索..."
              className="header-search"
              prefix={<SearchOutlined style={{ color: '#999' }} />}
              onPressEnter={(e) => {
                const value = (e.target as HTMLInputElement).value
                if (value) {
                  message.info('搜索功能开发中...')
                }
              }}
            />

            {isLoggedIn ? (
              <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
                <div className="user-profile">
                  <span className="username">{user?.userUsername || '用户'}</span>
                  <div className="avatar">
                    {user?.userImage ? (
                      <img src={user.userImage} alt="avatar" />
                    ) : (
                      <UserOutlined />
                    )}
                  </div>
                </div>
              </Dropdown>
            ) : (
              <Space>
                <Button onClick={() => handleNavigate('/auth/login', 'login')} type="text">
                  登录
                </Button>
                <Button onClick={() => handleNavigate('/auth/register', 'register')} type="primary">
                  注册
                </Button>
              </Space>
            )}
          </div>
        </div>
      </div>
    </header>
  )
}

export default PortalHeader
