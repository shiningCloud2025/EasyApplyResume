import { Routes, Route, Navigate } from 'react-router-dom'
import { useUserStore } from '@stores/userStore'
import MainLayout from '@components/MainLayout'
import AuthLayout from '@components/AuthLayout'
import ProtectedRoute from '@components/ProtectedRoute'
import LoadingSpinner from '@components/LoadingSpinner'

// 页面组件懒加载
const HomePage = React.lazy(() => import('@views/home/HomePage'))
const LoginPage = React.lazy(() => import('@views/auth/LoginPage'))
const RegisterPage = React.lazy(() => import('@views/auth/RegisterPage'))
const ProfilePage = React.lazy(() => import('@views/user/ProfilePage'))
const ResumeTemplates = React.lazy(() => import('@views/resume/ResumeTemplates'))
const MyResumes = React.lazy(() => import('@views/resume/MyResumes'))
const ResumeEditor = React.lazy(() => import('@views/resume/ResumeEditor'))
const JobList = React.lazy(() => import('@views/job/JobList'))
const JobDetail = React.lazy(() => import('@views/job/JobDetail'))
const AdviceList = React.lazy(() => import('@views/advice/AdviceList'))
const AdviceDetail = React.lazy(() => import('@views/advice/AdviceDetail'))
const AIAssistant = React.lazy(() => import('@views/ai/AIAssistant'))
const FeedbackSubmit = React.lazy(() => import('@views/feedback/FeedbackSubmit'))
const MyFeedback = React.lazy(() => import('@views/feedback/MyFeedback'))
const FeedbackDetail = React.lazy(() => import('@views/feedback/FeedbackDetail'))
const NotFound = React.lazy(() => import('@views/error/NotFound'))

function App() {
  const { isLoggedIn, initAuth } = useUserStore()

  // 临时调试信息
  console.log('🏠 App 组件渲染')
  console.log('🔐 登录状态:', isLoggedIn)

  // 初始化认证状态
  React.useEffect(() => {
    console.log('🔄 初始化认证状态')
    initAuth()
  }, [initAuth])

  return (
    <Routes>
      {/* 未登录默认重定向到登录页 */}
      <Route path="/" element={<Navigate to={isLoggedIn ? "/home" : "/auth/login"} replace />} />
      
      {/* 认证布局 */}
      <Route path="/auth" element={<AuthLayout />}>
        <Route path="login" element={<LoginPage />} />
        <Route path="register" element={<RegisterPage />} />
      </Route>

      {/* 主应用布局 */}  
      <Route path="/" element={
        <ProtectedRoute isAuthenticated={isLoggedIn}>
          <MainLayout />
        </ProtectedRoute>
      }>
        <Route path="home" element={<HomePage />} />
        <Route path="profile" element={<ProfilePage />} />
        <Route path="resume" element={<Navigate to="/resume/templates" replace />}>
          <Route path="templates" element={<ResumeTemplates />} />
          <Route path="my-resumes" element={<MyResumes />} />
          <Route path="edit/:sortedNum" element={<ResumeEditor />} />
        </Route>
        <Route path="jobs" element={<JobList />} />
        <Route path="job/:id" element={<JobDetail />} />
        <Route path="advice" element={<AdviceList />} />
        <Route path="advice/:id" element={<AdviceDetail />} />
        <Route path="ai" element={<AIAssistant />} />
        <Route path="feedback" element={<Navigate to="/feedback/submit" replace />}>
          <Route path="submit" element={<FeedbackSubmit />} />
          <Route path="my" element={<MyFeedback />} />
          <Route path=":id" element={<FeedbackDetail />} />
        </Route>
      </Route>

      {/* 404页面 */}
      <Route path="*" element={<NotFound />} />
    </Routes>
  )
}

export default App