import React, { useState, useEffect, useRef } from 'react'
import {
  Card,
  Button,
  Input,
  Empty,
  Modal,
  message,
  Tag,
  Tooltip,
  Spin,
  Tabs,
  Form,
  Select,
  Upload
} from 'antd'
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  CopyOutlined,
  EyeOutlined,
  HeartOutlined,
  RestOutlined,
  SendOutlined,
  ImportOutlined,
  InboxOutlined
} from '@ant-design/icons'
import type { UploadFile } from 'antd/es/upload/interface'
import { useQuery, useMutation, useQueryClient } from 'react-query'
import { resumeAPI, ResumeSearchQuery } from '@api/resume'
import { jobAPI } from '@api/job'
import { useNavigate } from 'react-router-dom'
import type { UserResume } from '@types/index'
import { useUserStore } from '@stores/userStore'
import { LiveProvider, LivePreview, LiveError } from 'react-live'
import { Editor, Toolbar } from '@wangeditor/editor-for-react'
import { IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor'
import html2canvas from 'html2canvas'
import jsPDF from 'jspdf'
import '@wangeditor/editor/dist/css/style.css'
import { formatIndustryMapName, isNormalIndustry } from '@utils/index'
import './MyResumes.scss'

const { Search } = Input
const { Dragger } = Upload

// React代码预览组件（小尺寸）
const ReactCodePreview: React.FC<{ code: string }> = ({ code }) => {
  if (!code) {
    return (
      <div className="preview-placeholder">
        <span>暂无预览</span>
      </div>
    )
  }

  const processCode = (rawCode: string): string => {
    let processed = rawCode
      .replace(/import\s+.*?from\s+['"].*?['"]\s*;?/g, '')
      .replace(/import\s+['"].*?['"]\s*;?/g, '')
      .replace(/export\s+default\s+/g, '')
      .replace(/export\s+/g, '')
      .trim()

    if (processed.match(/^(const|function|class)\s+\w+/)) {
      const componentMatch = processed.match(/^(?:const|function|class)\s+(\w+)/)
      if (componentMatch) {
        const componentName = componentMatch[1]
        processed = `${processed}\n\nrender(<${componentName} />)`
      }
    }
    return processed
  }

  const scope = {
    React,
    useState: React.useState,
    useEffect: React.useEffect,
  }

  return (
    <LiveProvider code={processCode(code)} scope={scope} noInline={true}>
      <div className="mini-preview">
        <LivePreview />
      </div>
      <LiveError className="preview-error" />
    </LiveProvider>
  )
}

const MyResumes: React.FC = () => {
  const navigate = useNavigate()
  const { user } = useUserStore()
  const queryClient = useQueryClient()
  const [searchKeyword, setSearchKeyword] = useState('')
  const [searchQuery, setSearchQuery] = useState<ResumeSearchQuery>({})
  const [deleteModalVisible, setDeleteModalVisible] = useState(false)
  const [selectedResume, setSelectedResume] = useState<UserResume | null>(null)
  const [activeTab, setActiveTab] = useState('my-resumes')
  const [editModalVisible, setEditModalVisible] = useState(false)
  const [editingResume, setEditingResume] = useState<UserResume | null>(null)
  const [newResumeName, setNewResumeName] = useState('')
  const [importModalVisible, setImportModalVisible] = useState(false)
  const [importResumeName, setImportResumeName] = useState('')
  const [importIndustryCode, setImportIndustryCode] = useState<number | undefined>(undefined)
  const [importFileList, setImportFileList] = useState<UploadFile[]>([])
  const [importing, setImporting] = useState(false)

  // 发送给HR相关状态
  const [sendModalVisible, setSendModalVisible] = useState(false)
  const [sendingResume, setSendingResume] = useState<UserResume | null>(null)
  const [sendForm, setSendForm] = useState({
    targetEmail: '',
    title: '',
    content: ''
  })
  const [sending, setSending] = useState(false)
  const [editor, setEditor] = useState<IDomEditor | null>(null)
  const [attachmentFormat, setAttachmentFormat] = useState<'png' | 'word' | 'pdf'>('pdf')
  const hiddenPreviewRef = useRef<HTMLDivElement>(null)

  // 获取行业列表
  const { data: industriesData = [] } = useQuery(
    ['industries'],
    () => jobAPI.getAllIndustries(),
    {
      select: (response: any) => response.data || [],
      staleTime: 1000 * 60 * 10
    }
  )

  const normalIndustries = React.useMemo(
    () => industriesData.filter((industry: any) => isNormalIndustry(industry.industryMapIndustryName)),
    [industriesData]
  )

  const toolbarConfig: Partial<IToolbarConfig> = {
    toolbarKeys: [
      'bold', 'italic', 'underline', 'color', 'bgColor', '|',
      'bulletedList', 'numberedList', '|',
      'insertLink', 'emotion', '|',
      'undo', 'redo'
    ]
  }
  const editorConfig: Partial<IEditorConfig> = {
    placeholder: '请输入邮件内容...'
  }

  // 组件卸载时销毁编辑器
  useEffect(() => {
    return () => {
      if (editor) {
        editor.destroy()
        setEditor(null)
      }
    }
  }, [editor])

  // 获取用户简历列表（支持搜索）
  const {
    data: resumesData = [],
    isLoading,
    error,
    refetch
  } = useQuery(
    ['user-resumes', user?.userId, searchQuery],
    () => {
      if (!user?.userId) return Promise.resolve([])
      return resumeAPI.getUserResumes(user.userId, searchQuery)
    },
    {
      enabled: !!user?.userId,
      select: (response) => response.data || [],
      onSuccess: (data) => {
        console.log('获取简历列表成功:', data)
      },
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '获取简历列表失败'
        message.error(errorMsg)
        console.error('API错误:', error)
      }
    }
  )

  // 获取用户收藏的模板
  const {
    data: collectionsData = [],
    isLoading: collectionsLoading,
    refetch: refetchCollections
  } = useQuery(
    ['user-collections', user?.userId, searchKeyword],
    () => {
      if (!user?.userId) return Promise.resolve([])
      console.log('调用收藏接口, userId:', user.userId, ', 搜索:', searchKeyword)
      // 传递搜索关键词作为 resumeTemplateName 参数（空字符串也传）
      return resumeAPI.getUserCollections(user.userId, searchKeyword)
    },
    {
      enabled: !!user?.userId && activeTab === 'my-collections',
      select: (response) => {
        console.log('收藏接口返回:', response)
        return response.data || []
      }
    }
  )

  // 切换到"我的收藏"时重新获取数据
  useEffect(() => {
    if (activeTab === 'my-collections' && user?.userId) {
      refetchCollections()
    }
  }, [activeTab, user?.userId])

  // 删除简历mutation
  const deleteResumeMutation = useMutation(
    (resumeId: number) => resumeAPI.deleteResume(user!.userId, resumeId),
    {
      onSuccess: () => {
        message.success('简历已移入回收站')
        setDeleteModalVisible(false)
        setSelectedResume(null)
        queryClient.invalidateQueries(['user-resumes', user?.userId])
      },
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '删除简历失败'
        message.error(errorMsg)
        console.error('删除失败:', error)
      }
    }
  )

  // 修改简历名称mutation
  const updateNameMutation = useMutation(
    (params: { resumeSortedNum: number; resumeName: string }) =>
      resumeAPI.updateResumeName(user!.userId, params.resumeSortedNum, params.resumeName),
    {
      onSuccess: () => {
        message.success('简历名称已修改')
        setEditModalVisible(false)
        setEditingResume(null)
        setNewResumeName('')
        queryClient.invalidateQueries(['user-resumes', user?.userId])
      },
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '修改简历名称失败'
        message.error(errorMsg)
      }
    }
  )

  const handleSearch = (value: string) => {
    setSearchKeyword(value)
    if (activeTab === 'my-resumes') {
      // 搜索我的简历
      const newQuery = value ? { userSaveResumeResumeName: value } : {}
      setSearchQuery(newQuery)
      setTimeout(() => refetch(), 0)
    } else {
      // 搜索我的收藏 - 手动触发重新查询
      setTimeout(() => refetchCollections(), 0)
    }
  }

  const handleImportResume = async () => {
    if (!user?.userId) {
      message.warning('请先登录')
      return
    }

    if (importIndustryCode === undefined) {
      message.warning('请选择所属行业')
      return
    }

    const currentFile = importFileList[0]?.originFileObj
    if (!currentFile) {
      message.warning('请先选择要导入的简历文件')
      return
    }

    setImporting(true)
    try {
      await resumeAPI.importResume({
        userId: user.userId,
        file: currentFile,
        industryCode: importIndustryCode,
        resumeName: importResumeName
      })
      message.success('简历导入成功')
      setImportModalVisible(false)
      setImportResumeName('')
      setImportIndustryCode(undefined)
      setImportFileList([])
      queryClient.invalidateQueries(['user-resumes', user.userId])
      refetch()
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || '导入简历失败'
      message.error(errorMsg)
    } finally {
      setImporting(false)
    }
  }

  const handleEditName = (resume: UserResume) => {
    setEditingResume(resume)
    setNewResumeName(resume.userSaveResumeResumeName)
    setEditModalVisible(true)
  }

  const confirmEditName = () => {
    if (!editingResume || !newResumeName.trim()) {
      message.warning('请输入简历名称')
      return
    }
    updateNameMutation.mutate({
      resumeSortedNum: editingResume.userSaveResumeSortedNum,
      resumeName: newResumeName.trim()
    })
  }

  const handleEdit = (resume: UserResume) => {
    navigate(`/resume/edit/${resume.userSaveResumeSortedNum}`)
  }

  const handleDuplicate = async (resume: UserResume) => {
    try {
      const newResumeData = {
        ...resume,
        userSaveResumeResumeName: `${resume.userSaveResumeResumeName} (副本)`,
        userSaveResumeCreatedTime: new Date().toISOString(),
        userSaveResumeUpdatedTime: new Date().toISOString()
      }
      await resumeAPI.saveResume(newResumeData)
      message.success('简历复制成功')
      refetch()
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || '复制简历失败'
      message.error(errorMsg)
    }
  }

  const handleDelete = (resume: UserResume) => {
    setSelectedResume(resume)
    setDeleteModalVisible(true)
  }

  const confirmDelete = () => {
    if (!selectedResume) return
    deleteResumeMutation.mutate(selectedResume.userSaveResumeSortedNum)
  }

  const getIndustryName = (industryId: number) => {
    const matchedIndustry = industriesData.find((item: any) => item.industryMapIndustryCode === industryId)
    return formatIndustryMapName(matchedIndustry?.industryMapIndustryName) || '其他'
  }

  const getIndustryColor = (industryId: number) => {
    const colors: Record<number, string> = {
      1: 'blue', 2: 'green', 3: 'orange', 4: 'red', 5: 'purple', 6: 'default'
    }
    return colors[industryId] || 'default'
  }

  const filteredResumes = Array.isArray(resumesData) ? resumesData : []

  const handleOpenRecycleBin = () => {
    navigate('/resume/recycle-bin')
  }

  // 打开发送给HR弹窗
  const handleSendToHr = (resume: UserResume) => {
    setSendingResume(resume)
    setSendForm({
      targetEmail: '',
      title: '',
      content: ''
    })
    setSendModalVisible(true)
  }

  // 将简历转换为指定格式的文件
  const generateResumeFile = async (resume: UserResume, format: 'png' | 'word' | 'pdf'): Promise<File> => {
    const resumeName = resume.userSaveResumeResumeName || '简历'

    if (format === 'png') {
      // PNG格式
      if (!hiddenPreviewRef.current) {
        throw new Error('预览区域未加载')
      }
      const canvas = await html2canvas(hiddenPreviewRef.current, {
        scale: 2,
        useCORS: true,
        allowTaint: true
      })
      const blob = await new Promise<Blob>((resolve) => {
        canvas.toBlob((b) => resolve(b!), 'image/png')
      })
      return new File([blob], `${resumeName}.png`, { type: 'image/png' })
    }

    if (format === 'word') {
      // Word格式 - 使用隐藏预览区域的HTML
      if (!hiddenPreviewRef.current) {
        throw new Error('预览区域未加载')
      }
      const htmlContent = hiddenPreviewRef.current.innerHTML
      const wordContent = `
        <html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:w='urn:schemas-microsoft-com:office:word'>
        <head><meta charset='utf-8'><title>${resumeName}</title></head>
        <body>${htmlContent}</body>
        </html>
      `
      const blob = new Blob([wordContent], { type: 'application/msword' })
      return new File([blob], `${resumeName}.doc`, { type: 'application/msword' })
    }

    if (format === 'pdf') {
      // PDF格式 - 使用html2canvas和jsPDF
      if (!hiddenPreviewRef.current) {
        throw new Error('预览区域未加载')
      }
      const canvas = await html2canvas(hiddenPreviewRef.current, {
        scale: 2,
        useCORS: true,
        allowTaint: true
      })
      const imgData = canvas.toDataURL('image/png')
      const pdf = new jsPDF('p', 'mm', 'a4')
      const pdfWidth = pdf.internal.pageSize.getWidth()
      const pdfHeight = (canvas.height * pdfWidth) / canvas.width
      pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight)
      const pdfBlob = pdf.output('blob')
      return new File([pdfBlob], `${resumeName}.pdf`, { type: 'application/pdf' })
    }

    throw new Error('不支持的格式')
  }

  // 确认发送
  const confirmSend = async () => {
    if (!sendingResume) return

    // 邮箱校验
    const email = sendForm.targetEmail.trim()
    if (!email) {
      message.warning('请输入目标邮箱')
      return
    }
    if (email.length > 25) {
      message.warning('邮箱长度不能超过25个字符')
      return
    }
    const emailRegex = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/
    if (!emailRegex.test(email)) {
      message.warning('邮箱格式不正确')
      return
    }

    // 标题校验
    const title = sendForm.title.trim()
    if (!title) {
      message.warning('请输入邮件标题')
      return
    }
    if (title.length > 35) {
      message.warning('标题长度不能超过35个字符')
      return
    }

    // 内容校验
    const content = sendForm.content.replace(/<[^>]+>/g, '').trim()
    if (!content) {
      message.warning('请输入邮件内容')
      return
    }

    setSending(true)
    try {
      // 根据选择的格式生成文件
      const resumeFile = await generateResumeFile(sendingResume, attachmentFormat)
      await resumeAPI.sendResumeToHr({
        targetEmail: email,
        title: title,
        content: sendForm.content,
        resumeFile
      })
      message.success('简历发送成功！')
      setSendModalVisible(false)
      setSendingResume(null)
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || '发送失败'
      message.error(errorMsg)
    } finally {
      setSending(false)
    }
  }

  return (
    <div className="my-resumes">
      <div className="page-header">
        <Tabs
          activeKey={activeTab}
          onChange={setActiveTab}
          items={[
            { key: 'my-resumes', label: '我的简历' },
            { key: 'my-collections', label: '我的收藏' }
          ]}
        />
      </div>

      <Card className="filter-card" style={{ marginBottom: 24 }}>
        <div className="resume-filter-bar">
          <Search placeholder="搜索简历名称" onSearch={handleSearch} style={{ width: 300 }} allowClear />
          <div className="resume-page-actions">
            <Button type="primary" size="middle" icon={<ImportOutlined />} onClick={() => setImportModalVisible(true)}>
              导入简历
            </Button>
            <Button type="primary" size="middle" icon={<PlusOutlined />} onClick={() => navigate('/resume/templates')}>
              创建新简历
            </Button>
          </div>
        </div>
      </Card>

      {!user ? (
        <div style={{ textAlign: 'center', padding: '50px' }}>
          <Empty description="请先登录" image={Empty.PRESENTED_IMAGE_SIMPLE}>
            <Button type="primary" onClick={() => navigate('/auth/login')}>去登录</Button>
          </Empty>
        </div>
      ) : isLoading ? (
        <div style={{ textAlign: 'center', padding: '50px' }}><Spin size="large" /></div>
      ) : error ? (
        <div style={{ textAlign: 'center', padding: '50px' }}>
          <Empty description="加载失败，请重试" image={Empty.PRESENTED_IMAGE_SIMPLE}>
            <Button type="primary" onClick={() => refetch()}>重试</Button>
          </Empty>
        </div>
      ) : activeTab === 'my-resumes' ? (
        filteredResumes.length === 0 ? (
          <Empty description={searchKeyword ? '未找到匹配的简历' : '暂无简历'} image={Empty.PRESENTED_IMAGE_SIMPLE}>
            {!searchKeyword && (
              <Button type="primary" icon={<PlusOutlined />} onClick={() => navigate('/resume/templates')}>
                创建简历
              </Button>
            )}
          </Empty>
        ) : (
          <div className="resume-grid">
            {filteredResumes.map((resume) => (
              <Card
                key={resume.userSaveResumeSortedNum}
                hoverable
                className="resume-card"
                cover={
                  <div className="resume-preview-container" onClick={() => handleEdit(resume)}>
                    <ReactCodePreview code={resume.userSaveResumeResumeReactCode} />
                  </div>
                }
              >
                <div className="resume-info" onClick={() => handleEdit(resume)}>
                  <h3 className="resume-name">
                    <span className="resume-order">#{resume.userSaveResumeSortedNum}</span>
                    {resume.userSaveResumeResumeName}
                  </h3>
                  <div className="resume-meta">
                    <Tag color={getIndustryColor(resume.userSaveResumeIndustry)}>
                      {formatIndustryMapName(resume.userSaveResumeIndustryName) || getIndustryName(resume.userSaveResumeIndustry)}
                    </Tag>
                  </div>
                  <div className="resume-times">
                    <span>创建：{new Date(resume.userSaveResumeCreatedTime).toLocaleDateString('zh-CN')}</span>
                    <span>更新：{new Date(resume.userSaveResumeUpdatedTime).toLocaleDateString('zh-CN')}</span>
                  </div>
                </div>
                <div className="resume-actions">
                  <Tooltip title="发送给HR">
                    <Button type="text" icon={<SendOutlined />} onClick={() => handleSendToHr(resume)} />
                  </Tooltip>
                  <Tooltip title="编辑名称">
                    <Button type="text" icon={<EditOutlined />} onClick={() => handleEditName(resume)} />
                  </Tooltip>
                  <Tooltip title="删除">
                    <Button type="text" danger icon={<DeleteOutlined />} onClick={() => handleDelete(resume)} />
                  </Tooltip>
                </div>
              </Card>
            ))}
          </div>
        )
      ) : (
        collectionsLoading ? (
          <div style={{ textAlign: 'center', padding: '50px' }}><Spin size="large" /></div>
        ) : collectionsData.length === 0 ? (
          <Empty description="暂无收藏" image={Empty.PRESENTED_IMAGE_SIMPLE}>
            <Button type="primary" onClick={() => navigate('/resume/templates')}>去收藏模板</Button>
          </Empty>
        ) : (
          <div className="resume-grid">
            {collectionsData.map((collection: any) => (
              <Card
                key={collection.resumeTemplateId}
                hoverable
                className="resume-card"
                cover={
                  <div className="resume-preview-container">
                    <ReactCodePreview code={collection.resumeTemplateReactCode} />
                  </div>
                }
                onClick={() => navigate(`/resume/template/${collection.resumeTemplateId}`)}
              >
                <div className="resume-info">
                  <h3 className="resume-name">{collection.resumeTemplateName || '收藏模板'}</h3>
                  <div className="resume-meta">
                    <Tag color="purple"><HeartOutlined /> 已收藏</Tag>
                  </div>
                </div>
              </Card>
            ))}
          </div>
        )
      )}

      {/* 导入简历弹窗 */}
      <Modal
        title="导入已有简历"
        open={importModalVisible}
        onCancel={() => {
          setImportModalVisible(false)
          setImportResumeName('')
          setImportIndustryCode(undefined)
          setImportFileList([])
        }}
        footer={[
          <Button
            key="cancel"
            onClick={() => {
              setImportModalVisible(false)
              setImportResumeName('')
              setImportIndustryCode(undefined)
              setImportFileList([])
            }}
          >
            取消
          </Button>,
          <Button key="import" type="primary" icon={<ImportOutlined />} onClick={handleImportResume} loading={importing}>
            开始导入
          </Button>
        ]}
      >
        <Form layout="vertical">
          <Form.Item label="简历名称">
            <Input
              placeholder="可选，不填则使用系统默认名称"
              value={importResumeName}
              onChange={(e) => setImportResumeName(e.target.value)}
              maxLength={50}
              showCount
            />
          </Form.Item>
          <Form.Item label="所属行业" required>
            <Select
              value={importIndustryCode}
              onChange={setImportIndustryCode}
              placeholder="请选择所属行业"
              options={normalIndustries.map((industry: any) => ({
                value: industry.industryMapIndustryCode,
                label: formatIndustryMapName(industry.industryMapIndustryName)
              }))}
            />
          </Form.Item>
          <Form.Item label="简历文件" required extra="支持上传已有简历文件，导入后将自动创建一份新的简历。">
            <Dragger
              multiple={false}
              accept=".pdf,.doc,.docx,.txt,.md"
              beforeUpload={(file) => {
                setImportFileList([
                  {
                    uid: file.uid,
                    name: file.name,
                    status: 'done',
                    size: file.size,
                    type: file.type,
                    originFileObj: file
                  }
                ])
                return false
              }}
              onRemove={() => {
                setImportFileList([])
              }}
              fileList={importFileList}
            >
              <p className="ant-upload-drag-icon">
                <InboxOutlined />
              </p>
              <p className="ant-upload-text">点击或拖拽文件到此区域上传</p>
              <p className="ant-upload-hint">建议上传 PDF / Word / TXT / Markdown 格式的简历文件</p>
            </Dragger>
          </Form.Item>
        </Form>
      </Modal>

      {/* 编辑简历名称弹窗 */}
      <Modal
        title="编辑简历名称"
        open={editModalVisible}
        onCancel={() => {
          setEditModalVisible(false)
          setEditingResume(null)
          setNewResumeName('')
        }}
        footer={[
          <Button key="cancel" onClick={() => {
            setEditModalVisible(false)
            setEditingResume(null)
            setNewResumeName('')
          }}>
            取消
          </Button>,
          <Button key="save" type="primary" onClick={confirmEditName} loading={updateNameMutation.isLoading}>
            保存
          </Button>
        ]}
      >
        <Input
          placeholder="请输入简历名称"
          value={newResumeName}
          onChange={(e) => setNewResumeName(e.target.value)}
          onPressEnter={confirmEditName}
          maxLength={50}
        />
      </Modal>

      {/* 删除确认弹窗 */}
      <Modal
        title="确认删除"
        open={deleteModalVisible}
        onCancel={() => setDeleteModalVisible(false)}
        footer={[
          <Button key="cancel" onClick={() => setDeleteModalVisible(false)}>取消</Button>,
          <Button key="delete" type="primary" danger onClick={confirmDelete} loading={deleteResumeMutation.isLoading}>
            确认删除
          </Button>
        ]}
      >
        <p>确定要删除简历"{selectedResume?.userSaveResumeResumeName}"吗？</p>
        <p>删除后简历将移入回收站，仍可在30天内恢复。</p>
      </Modal>

      {/* 发送给HR弹窗 */}
      <Modal
        title={`发送简历给HR - ${sendingResume?.userSaveResumeResumeName || ''}`}
        open={sendModalVisible}
        width={700}
        onCancel={() => {
          setSendModalVisible(false)
          setSendingResume(null)
        }}
        footer={[
          <Button key="cancel" onClick={() => {
            setSendModalVisible(false)
            setSendingResume(null)
          }}>
            取消
          </Button>,
          <Button key="send" type="primary" icon={<SendOutlined />} onClick={confirmSend} loading={sending}>
            发送
          </Button>
        ]}
      >
        <Form layout="vertical">
          <Form.Item label="目标邮箱" required>
            <Input
              placeholder="请输入HR邮箱"
              value={sendForm.targetEmail}
              onChange={(e) => setSendForm({ ...sendForm, targetEmail: e.target.value })}
              maxLength={25}
              showCount
            />
          </Form.Item>
          <Form.Item label="邮件标题" required>
            <Input
              placeholder="请输入邮件标题"
              value={sendForm.title}
              onChange={(e) => setSendForm({ ...sendForm, title: e.target.value })}
              maxLength={35}
              showCount
            />
          </Form.Item>
          <Form.Item label="邮件内容" required>
            <div style={{ border: '1px solid #d9d9d9', borderRadius: 6 }}>
              <Toolbar
                editor={editor}
                defaultConfig={toolbarConfig}
                mode="default"
                style={{ borderBottom: '1px solid #d9d9d9' }}
              />
              <Editor
                defaultConfig={editorConfig}
                value={sendForm.content}
                onCreated={setEditor}
                onChange={(editorInstance) => setSendForm(prev => ({ ...prev, content: editorInstance.getHtml() }))}
                mode="default"
                style={{ height: 200, overflowY: 'hidden' }}
              />
            </div>
          </Form.Item>
          <Form.Item label="附件格式" required>
            <Select
              value={attachmentFormat}
              onChange={setAttachmentFormat}
              options={[
                { value: 'pdf', label: 'PDF格式（推荐）' },
                { value: 'word', label: 'Word格式' },
                { value: 'png', label: 'PNG图片' }
              ]}
              style={{ width: 200 }}
            />
            <div style={{ fontSize: 12, color: '#999', marginTop: 4 }}>
              简历将以选择的格式作为附件发送
            </div>
          </Form.Item>
        </Form>
        {/* 隐藏的简历预览区域，用于生成PDF/Word/PNG */}
        {sendingResume && (
          <div
            ref={hiddenPreviewRef}
            style={{
              position: 'absolute',
              left: '-9999px',
              top: 0,
              width: '794px',
              background: '#fff'
            }}
          >
            <LiveProvider
              code={(() => {
                let processed = (sendingResume.userSaveResumeResumeReactCode || '')
                  .replace(/import\s+.*?from\s+['"].*?['"]\s*;?/g, '')
                  .replace(/import\s+['"].*?['"]\s*;?/g, '')
                  .replace(/export\s+default\s+/g, '')
                  .replace(/export\s+/g, '')
                  .trim()
                if (processed.match(/^(const|function|class)\s+\w+/)) {
                  const match = processed.match(/^(?:const|function|class)\s+(\w+)/)
                  if (match) processed = `${processed}\n\nrender(<${match[1]} />)`
                }
                return processed
              })()}
              scope={{ React, useState: React.useState, useEffect: React.useEffect }}
              noInline={true}
            >
              <LivePreview />
            </LiveProvider>
          </div>
        )}
      </Modal>

      {/* 回收站入口 */}
      <div
        className="recycle-bin-entry"
        onClick={handleOpenRecycleBin}
        style={{
          position: 'fixed',
          right: 32,
          bottom: 100,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          padding: '16px 20px',
          background: '#fff',
          borderRadius: 12,
          boxShadow: '0 4px 16px rgba(0,0,0,0.15)',
          cursor: 'pointer',
          transition: 'all 0.3s',
          zIndex: 999
        }}
      >
        <RestOutlined style={{ fontSize: 28, color: '#666' }} />
        <span style={{ fontSize: 13, color: '#666', marginTop: 6, fontWeight: 500 }}>回收站</span>
      </div>
    </div>
  )
}

export default MyResumes
