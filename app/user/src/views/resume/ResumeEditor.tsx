import React, { useState, useCallback, useRef, useEffect } from 'react'
import {
  Button,
  Space,
  message,
  Tooltip,
  Spin,
  Dropdown,
  Segmented,
  Modal,
  Input,
  Tag
} from 'antd'
import {
  SaveOutlined,
  DownloadOutlined,
  ArrowLeftOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  FilePdfOutlined,
  FileWordOutlined,
  FileImageOutlined,
  ReloadOutlined,
  BoldOutlined,
  ItalicOutlined,
  UnderlineOutlined,
  FontColorsOutlined,
  BgColorsOutlined,
  FontSizeOutlined,
  CodeOutlined,
  EditOutlined,
  AlignLeftOutlined,
  AlignCenterOutlined,
  AlignRightOutlined,
  OrderedListOutlined,
  UnorderedListOutlined,
  ColumnWidthOutlined,
  LineHeightOutlined,
  CompressOutlined,
  RobotOutlined,
  StarOutlined,
  CommentOutlined,
  ThunderboltOutlined
} from '@ant-design/icons'
import { useParams, useNavigate } from 'react-router-dom'
import { useQuery } from 'react-query'
import { resumeAPI } from '@api/resume'
import { useUserStore } from '@stores/userStore'
import { LiveProvider, LivePreview, LiveError } from 'react-live'
import Editor from '@monaco-editor/react'
import html2canvas from 'html2canvas'
import { jsPDF } from 'jspdf'
import './ResumeEditor.scss'

const { TextArea } = Input

const ResumeEditor: React.FC = () => {
  const { sortedNum } = useParams<{ sortedNum: string }>()
  const navigate = useNavigate()
  const { user } = useUserStore()
  const [saving, setSaving] = useState(false)
  const [extractingKeywords, setExtractingKeywords] = useState(false)
  const [scoringResume, setScoringResume] = useState(false)
  const [generatingFeedback, setGeneratingFeedback] = useState(false)
  const [assistingReactCode, setAssistingReactCode] = useState(false)
  const [aiAssistantModalOpen, setAiAssistantModalOpen] = useState(false)
  const [aiAssistantRequirement, setAiAssistantRequirement] = useState('请帮我优化这份 React 简历代码，保持原有内容语义不变，重点优化结构、可读性、样式一致性和可维护性。')
  const [exporting, setExporting] = useState(false)
  const [lastSaved, setLastSaved] = useState('')
  const [hasChanges, setHasChanges] = useState(false)
  const [showCode, setShowCode] = useState(true)
  const [codeReady, setCodeReady] = useState(false)
  const previewRef = useRef<HTMLDivElement>(null)
  
  const [code, setCode] = useState<string>('')
  const [resumeName, setResumeName] = useState<string>('')
  const [resumeId, setResumeId] = useState<number | null>(null) // 简历主键ID
  const [editMode, setEditMode] = useState<'visual' | 'code'>('visual') // 编辑模式
  const savedRangeRef = useRef<Range | null>(null) // 保存选区

  // 获取简历数据
  const { isLoading, refetch } = useQuery(
    ['resume-detail', user?.userId, sortedNum],
    () => resumeAPI.getUserResume(user!.userId, Number(sortedNum)),
    {
      enabled: !!user?.userId && !!sortedNum,
      onSuccess: (response) => {
        const data = response.data
        if (data) {
          setResumeName(data.userSaveResumeResumeName || '简历编辑')
          setResumeId(data.userSaveResumeId) // 保存主键ID
          let reactCode = data.userSaveResumeResumeReactCode || ''
          if (reactCode && !reactCode.includes('render(')) {
            const match = reactCode.match(/(?:const|function|class)\s+(\w+)/)
            if (match) {
              reactCode = `${reactCode}\n\nrender(<${match[1]} />)`
            }
          }
          setCode(reactCode)
          setCodeReady(true)
        }
      }
    }
  )

  // 处理代码变化
  const handleCodeChange = useCallback((value: string | undefined) => {
    if (value !== undefined) {
      setCode(value)
      setHasChanges(true)
    }
  }, [])

  // 刷新
  const handleRefresh = () => {
    setCodeReady(false)
    refetch()
  }

  // 保存
  const handleSave = async (options?: { showSuccessMessage?: boolean }) => {
    if (!user) return false
    const showSuccessMessage = options?.showSuccessMessage ?? true
    setSaving(true)
    try {
      let codeToSave = code

      // 如果是可视化编辑模式，从预览区获取内容并转换为 React 代码
      if (editMode === 'visual' && previewRef.current) {
        const htmlContent = previewRef.current.innerHTML
        // 转义特殊字符（只转义模板字符串中的特殊字符）
        const escapedHtml = htmlContent
          .replace(/\\/g, '\\\\')
          .replace(/`/g, '\\`')
          .replace(/\$\{/g, '\\${') // 只转义 ${ 组合

        // 转换为简单的 React 组件
        codeToSave = 'const ResumeTemplate = () => {\n' +
          '  return (\n' +
          '    <div dangerouslySetInnerHTML={{ __html: `' + escapedHtml + '` }} />\n' +
          '  )\n' +
          '}\n\n' +
          'render(<ResumeTemplate />)'
      }

      console.log('保存的代码:', codeToSave.substring(0, 200)) // 调试

      await resumeAPI.saveResume({
        userSaveResumeId: resumeId, // 关键：传主键ID
        userSaveResumeSortedNum: Number(sortedNum),
        userSaveResumeUserId: user.userId,
        userSaveResumeResumeReactCode: codeToSave,
        userSaveResumeUpdatedTime: new Date().toISOString()
      })
      setCode(codeToSave)
      setLastSaved(new Date().toLocaleTimeString('zh-CN'))
      setHasChanges(false)
      if (showSuccessMessage) {
        message.success('保存成功')
      }
      return true
    } catch (error: any) {
      console.error('保存失败:', error)
      const errorMsg = error?.response?.data?.message || error?.message || '保存失败'
      message.error(errorMsg)
      return false
    } finally {
      setSaving(false)
    }
  }

  const ensureResumeSaved = async () => {
    if (!resumeId) {
      message.warning('当前简历信息未加载完成，请稍后重试')
      return false
    }

    if (!hasChanges) {
      return true
    }

    message.loading({ content: '正在先保存简历...', key: 'resume-ai-save' })
    const saved = await handleSave({ showSuccessMessage: false })
    message.destroy('resume-ai-save')

    if (saved) {
      message.success('简历已保存，正在执行AI分析')
    }

    return saved
  }

  const formatAIResultToChinese = (result: any) => {
    const keywordLabelMap: Record<string, string> = {
      skills: '技术关键词',
      keywords: '高频关键词',
      education: '教育关键词',
      experience: '工作经验关键词',
      strengths: '核心优势关键词'
    }

    if (!result || typeof result !== 'object' || Array.isArray(result)) {
      return result
    }

    return Object.entries(result).reduce((acc, [key, value]) => {
      const label = keywordLabelMap[key] || key
      acc[label] = value
      return acc
    }, {} as Record<string, any>)
  }

  const formatAIScoreResultToChineseText = (result: any) => {
    const scoreLabelMap: Record<string, string> = {
      totalScore: '总分',
      completenessScore: '完整度得分',
      experienceScore: '经验质量得分',
      skillsScore: '技能匹配度得分',
      presentationScore: '呈现质量得分',
      strengths: '优势亮点',
      weaknesses: '不足之处',
      suggestions: '优化建议',
      level: '综合等级'
    }

    const formatScoreObject = (scoreData: Record<string, any>) => {
      return Object.entries(scoreLabelMap)
        .filter(([key]) => scoreData[key] !== undefined && scoreData[key] !== null)
        .map(([key, label]) => {
          const value = scoreData[key]
          if (Array.isArray(value)) {
            return `${label}：\n${value.map((item) => `- ${item}`).join('\n')}`
          }
          return `${label}：${value}`
        })
        .join('\n\n')
    }

    if (typeof result === 'string') {
      try {
        const parsed = JSON.parse(result)
        if (parsed && typeof parsed === 'object' && !Array.isArray(parsed)) {
          return formatScoreObject(parsed)
        }
        return result
      } catch {
        return result
      }
    }

    if (result && typeof result === 'object' && !Array.isArray(result)) {
      return formatScoreObject(result)
    }

    return String(result || '')
  }

  const getPriorityTagColor = (priority?: string) => {
    if (priority === '高') return 'red'
    if (priority === '中') return 'orange'
    if (priority === '低') return 'blue'
    return 'default'
  }

  const getPriorityLabel = (priority?: string) => {
    if (priority === '高') return '建议优先修改'
    if (priority === '中') return '建议尽快优化'
    if (priority === '低') return '可进一步完善'
    return priority || '未标注'
  }

  const openAIResultModal = (title: string, content: React.ReactNode, width = 720) => {
    Modal.info({
      title,
      width,
      okText: '知道了',
      content,
    })
  }

  const handleExtractKeywords = async () => {
    if (!user?.userId) {
      message.warning('请先登录')
      return
    }

    if (!resumeId) {
      message.warning('简历ID不存在，暂时无法提取关键词')
      return
    }

    const saved = await ensureResumeSaved()
    if (!saved) {
      return
    }

    setExtractingKeywords(true)
    try {
      const response = await resumeAPI.extractKeywordsByAI(user.userId, resumeId)
      const result = response?.data
      const formattedResult = formatAIResultToChinese(result)
      const resultText = typeof formattedResult === 'string'
        ? formattedResult
        : JSON.stringify(formattedResult, null, 2)

      openAIResultModal(
        'AI关键词提取结果',
        <div className="ai-result-modal">
          <pre>{resultText || '未返回关键词内容'}</pre>
        </div>
      )
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || 'AI关键词提取失败'
      message.error(errorMsg)
    } finally {
      setExtractingKeywords(false)
    }
  }

  const handleScoreResume = async () => {
    if (!user?.userId) {
      message.warning('请先登录')
      return
    }

    if (!resumeId) {
      message.warning('简历ID不存在，暂时无法进行AI评分')
      return
    }

    const saved = await ensureResumeSaved()
    if (!saved) {
      return
    }

    setScoringResume(true)
    try {
      const response = await resumeAPI.scoreResumeByAI(user.userId, resumeId)
      const result = response?.data
      const resultText = formatAIScoreResultToChineseText(result)

      openAIResultModal(
        'AI简历评分结果',
        <div className="ai-result-modal">
          <pre>{resultText || '未返回评分内容'}</pre>
        </div>
      )
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || 'AI简历评分失败'
      message.error(errorMsg)
    } finally {
      setScoringResume(false)
    }
  }

  const handleGenerateResumeFeedback = async () => {
    if (!user?.userId) {
      message.warning('请先登录')
      return
    }

    if (!resumeId) {
      message.warning('简历ID不存在，暂时无法生成AI反馈')
      return
    }

    const saved = await ensureResumeSaved()
    if (!saved) {
      return
    }

    setGeneratingFeedback(true)
    try {
      const response = await resumeAPI.getResumeFeedbackByAI(user.userId, resumeId)
      const result = response?.data

      openAIResultModal(
        'AI简历反馈',
        <div className="ai-feedback-modal">
          <section className="feedback-section">
            <h3>整体评价</h3>
            <p>{result?.overallFeedback || '暂无整体评价'}</p>
          </section>

          <section className="feedback-section">
            <h3>详细修改建议</h3>
            {result?.detailedFeedback?.length ? (
              result.detailedFeedback.map((item, index) => (
                <div className="feedback-card" key={`${item.section}-${index}`}>
                  <div className="feedback-card-header">
                    <strong>{item.section || `建议 ${index + 1}`}</strong>
                    <Tag color={getPriorityTagColor(item.priority)}>{getPriorityLabel(item.priority)}</Tag>
                  </div>
                  <p><span>当前问题：</span>{item.currentIssue || '暂无'}</p>
                  <p><span>优化建议：</span>{item.suggestion || '暂无'}</p>
                </div>
              ))
            ) : (
              <p>暂无详细修改建议</p>
            )}
          </section>

          <section className="feedback-section">
            <h3>模块化改进方向</h3>
            {result?.improvementAreas?.length ? (
              result.improvementAreas.map((area, index) => (
                <div className="feedback-card" key={`${area.area}-${index}`}>
                  <strong>{area.area || `模块 ${index + 1}`}</strong>
                  <ul>
                    {(area.improvements || []).map((improvement, improvementIndex) => (
                      <li key={improvementIndex}>{improvement}</li>
                    ))}
                  </ul>
                </div>
              ))
            ) : (
              <p>暂无模块化改进方向</p>
            )}
          </section>

          <section className="feedback-section two-column">
            <div className="feedback-card compact">
              <h3>简历亮点</h3>
              {result?.strengths?.length ? (
                <ul>
                  {result.strengths.map((item, index) => (
                    <li key={index}>{item}</li>
                  ))}
                </ul>
              ) : (
                <p>暂无亮点总结</p>
              )}
            </div>

            <div className="feedback-card compact">
              <h3>快速优化项</h3>
              {result?.quickWins?.length ? (
                <ul>
                  {result.quickWins.map((item, index) => (
                    <li key={index}>{item}</li>
                  ))}
                </ul>
              ) : (
                <p>暂无快速优化项</p>
              )}
            </div>
          </section>
        </div>,
        880
      )
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || 'AI简历反馈生成失败'
      message.error(errorMsg)
    } finally {
      setGeneratingFeedback(false)
    }
  }

  const handleOpenAiAssistant = () => {
    if (!showCode) {
      setShowCode(true)
    }
    if (editMode !== 'code') {
      setEditMode('code')
      message.info('已为你切换到代码编辑模式')
    }
    setAiAssistantModalOpen(true)
  }

  const handleAssistReactCode = async () => {
    if (editMode !== 'code') {
      message.warning('请先切换到代码编辑模式再使用 React 代码 AI 助手')
      return
    }

    if (!code.trim()) {
      message.warning('当前没有可优化的 React 代码')
      return
    }

    const requirement = aiAssistantRequirement.trim()
    if (!requirement) {
      message.warning('请输入优化需求')
      return
    }

    setAssistingReactCode(true)
    try {
      const response = await resumeAPI.assistReactCodeByAI(requirement, code)
      const optimizedCode = response?.data

      if (!optimizedCode || !String(optimizedCode).trim()) {
        message.warning('AI 未返回可用代码')
        return
      }

      setCode(String(optimizedCode))
      setHasChanges(true)
      setAiAssistantModalOpen(false)
      message.success('AI 优化后的 React 代码已生成到代码编辑区')
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || 'React 代码 AI 助手调用失败'
      message.error(errorMsg)
    } finally {
      setAssistingReactCode(false)
    }
  }

  // 导出PDF
  const handleExportPDF = async () => {
    if (!previewRef.current) return
    setExporting(true)
    try {
      const canvas = await html2canvas(previewRef.current, {
        scale: 2,
        useCORS: true,
        allowTaint: true
      })
      const imgData = canvas.toDataURL('image/png')
      const pdf = new jsPDF('p', 'mm', 'a4')
      const pdfWidth = pdf.internal.pageSize.getWidth()
      const pdfHeight = (canvas.height * pdfWidth) / canvas.width
      pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight)
      pdf.save(`${resumeName || '简历'}.pdf`)
      message.success('导出PDF成功')
    } catch {
      message.error('导出失败')
    } finally {
      setExporting(false)
    }
  }

  // 导出PNG
  const handleExportPNG = async () => {
    if (!previewRef.current) return
    setExporting(true)
    try {
      const canvas = await html2canvas(previewRef.current, {
        scale: 2,
        useCORS: true,
        allowTaint: true
      })
      const link = document.createElement('a')
      link.download = `${resumeName || '简历'}.png`
      link.href = canvas.toDataURL('image/png')
      link.click()
      message.success('导出PNG成功')
    } catch {
      message.error('导出失败')
    } finally {
      setExporting(false)
    }
  }

  // 导出Word
  const handleExportWord = async () => {
    if (!previewRef.current) return
    setExporting(true)
    try {
      const htmlContent = previewRef.current.innerHTML
      const blob = new Blob([`
        <html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:w='urn:schemas-microsoft-com:office:word'>
        <head><meta charset='utf-8'><title>${resumeName || '简历'}</title></head>
        <body>${htmlContent}</body>
        </html>
      `], { type: 'application/msword' })
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `${resumeName || '简历'}.doc`
      link.click()
      URL.revokeObjectURL(link.href)
      message.success('导出Word成功')
    } catch {
      message.error('导出失败')
    } finally {
      setExporting(false)
    }
  }

  // 导出菜单
  const exportMenuItems = [
    { key: 'pdf', icon: <FilePdfOutlined />, label: '导出 PDF', onClick: handleExportPDF },
    { key: 'png', icon: <FileImageOutlined />, label: '导出 PNG', onClick: handleExportPNG },
    { key: 'word', icon: <FileWordOutlined />, label: '导出 Word', onClick: handleExportWord },
  ]

  // 保存选区（在点击下拉菜单前调用）
  const saveSelection = () => {
    const selection = window.getSelection()
    if (selection && selection.rangeCount > 0) {
      savedRangeRef.current = selection.getRangeAt(0).cloneRange()
    }
  }

  // 恢复选区
  const restoreSelection = () => {
    if (savedRangeRef.current) {
      const selection = window.getSelection()
      if (selection) {
        selection.removeAllRanges()
        selection.addRange(savedRangeRef.current)
      }
    }
  }

  // 获取选中的文本范围
  const getSelectedRange = () => {
    const selection = window.getSelection()
    if (!selection || selection.rangeCount === 0) return null
    const range = selection.getRangeAt(0)
    if (!previewRef.current?.contains(range.commonAncestorContainer)) return null
    return range
  }

  // 包装选中文本
  const wrapSelection = (styleFn: (el: HTMLSpanElement) => void) => {
    // 先恢复选区
    restoreSelection()
    
    const range = getSelectedRange()
    if (!range || range.collapsed) {
      message.warning('请先选中要修改的文字')
      return
    }
    
    const span = document.createElement('span')
    styleFn(span)
    
    try {
      range.surroundContents(span)
      setHasChanges(true)
    } catch (e) {
      // 如果选区跨越多个元素，使用替代方法
      const fragment = range.extractContents()
      styleFn(span)
      span.appendChild(fragment)
      range.insertNode(span)
      setHasChanges(true)
    }
  }

  // 应用样式到选中文本或父元素
  const applyStyleToSelection = (style: Partial<CSSStyleDeclaration>) => {
    restoreSelection()
    const range = getSelectedRange()
    if (!range) {
      message.warning('请先选中要修改的文字')
      return
    }
    
    if (range.collapsed) {
      // 没有选中文字，应用到最近的块级父元素
      const parent = range.commonAncestorContainer.parentElement
      if (parent) {
        Object.assign(parent.style, style)
        setHasChanges(true)
      }
    } else {
      // 选中了文字，包装并应用样式
      wrapSelection((span) => {
        Object.assign(span.style, style)
      })
    }
  }

  // 格式化命令
  const formatBold = () => wrapSelection((span) => { span.style.fontWeight = 'bold' })
  const formatItalic = () => wrapSelection((span) => { span.style.fontStyle = 'italic' })
  const formatUnderline = () => wrapSelection((span) => { span.style.textDecoration = 'underline' })
  const formatColor = (color: string) => wrapSelection((span) => { span.style.color = color })
  const formatBgColor = (color: string) => wrapSelection((span) => { span.style.backgroundColor = color })
  const formatFontSize = (size: string) => wrapSelection((span) => { span.style.fontSize = size })
  const formatFontFamily = (font: string) => wrapSelection((span) => { span.style.fontFamily = font })
  
  // 对齐方式（应用到块级元素）
  const formatAlign = (align: string) => {
    restoreSelection()
    const range = getSelectedRange()
    if (!range) return
    let block = range.commonAncestorContainer as HTMLElement
    while (block && block.nodeType !== 1) block = block.parentElement as HTMLElement
    if (block) {
      block.style.textAlign = align
      setHasChanges(true)
    }
  }

  // 插入列表
  const insertList = (ordered: boolean) => {
    restoreSelection()
    const range = getSelectedRange()
    if (!range) return
    const text = range.toString()
    const lines = text.split('\n').filter(l => l.trim())
    if (lines.length === 0) {
      message.warning('请先选中要转换为列表的文字')
      return
    }
    const list = document.createElement(ordered ? 'ol' : 'ul')
    lines.forEach(line => {
      const li = document.createElement('li')
      li.textContent = line.trim()
      list.appendChild(li)
    })
    range.deleteContents()
    range.insertNode(list)
    setHasChanges(true)
  }

  // 字体颜色菜单
  const fontColorMenuItems = [
    { key: 'black', label: <span style={{ color: '#000' }}>● 黑色</span>, onClick: () => formatColor('#000000') },
    { key: 'red', label: <span style={{ color: '#ff4d4f' }}>● 红色</span>, onClick: () => formatColor('#ff4d4f') },
    { key: 'blue', label: <span style={{ color: '#1890ff' }}>● 蓝色</span>, onClick: () => formatColor('#1890ff') },
    { key: 'green', label: <span style={{ color: '#52c41a' }}>● 绿色</span>, onClick: () => formatColor('#52c41a') },
    { key: 'orange', label: <span style={{ color: '#fa8c16' }}>● 橙色</span>, onClick: () => formatColor('#fa8c16') },
    { key: 'purple', label: <span style={{ color: '#722ed1' }}>● 紫色</span>, onClick: () => formatColor('#722ed1') },
  ]

  // 背景色菜单
  const bgColorMenuItems = [
    { key: 'none', label: '无背景', onClick: () => formatBgColor('transparent') },
    { key: 'yellow', label: <span style={{ background: '#fffb8f', padding: '2px 8px' }}>黄色</span>, onClick: () => formatBgColor('#fffb8f') },
    { key: 'green', label: <span style={{ background: '#b7eb8f', padding: '2px 8px' }}>绿色</span>, onClick: () => formatBgColor('#b7eb8f') },
    { key: 'blue', label: <span style={{ background: '#91d5ff', padding: '2px 8px' }}>蓝色</span>, onClick: () => formatBgColor('#91d5ff') },
    { key: 'pink', label: <span style={{ background: '#ffadd2', padding: '2px 8px' }}>粉色</span>, onClick: () => formatBgColor('#ffadd2') },
  ]

  // 字号菜单
  const fontSizeMenuItems = [
    { key: '10', label: '10px', onClick: () => formatFontSize('10px') },
    { key: '12', label: '12px', onClick: () => formatFontSize('12px') },
    { key: '14', label: '14px', onClick: () => formatFontSize('14px') },
    { key: '16', label: '16px', onClick: () => formatFontSize('16px') },
    { key: '18', label: '18px', onClick: () => formatFontSize('18px') },
    { key: '24', label: '24px', onClick: () => formatFontSize('24px') },
    { key: '32', label: '32px', onClick: () => formatFontSize('32px') },
  ]

  // 字体菜单
  const fontFamilyMenuItems = [
    { key: 'yahei', label: '微软雅黑', onClick: () => formatFontFamily('Microsoft YaHei, sans-serif') },
    { key: 'simsun', label: '宋体', onClick: () => formatFontFamily('SimSun, serif') },
    { key: 'simhei', label: '黑体', onClick: () => formatFontFamily('SimHei, sans-serif') },
    { key: 'arial', label: 'Arial', onClick: () => formatFontFamily('Arial, sans-serif') },
    { key: 'times', label: 'Times New Roman', onClick: () => formatFontFamily('Times New Roman, serif') },
    { key: 'georgia', label: 'Georgia', onClick: () => formatFontFamily('Georgia, serif') },
  ]

  // 行距菜单
  const lineHeightMenuItems = [
    { key: '1', label: '1.0 紧凑', onClick: () => applyLineHeight('1') },
    { key: '1.2', label: '1.2', onClick: () => applyLineHeight('1.2') },
    { key: '1.5', label: '1.5 正常', onClick: () => applyLineHeight('1.5') },
    { key: '1.8', label: '1.8', onClick: () => applyLineHeight('1.8') },
    { key: '2', label: '2.0 宽松', onClick: () => applyLineHeight('2') },
  ]

  // 边距菜单
  const marginMenuItems = [
    { key: 'narrow', label: '窄 (10px)', onClick: () => applyMargin('10px') },
    { key: 'normal', label: '正常 (20px)', onClick: () => applyMargin('20px') },
    { key: 'wide', label: '宽 (40px)', onClick: () => applyMargin('40px') },
    { key: 'extra', label: '特宽 (60px)', onClick: () => applyMargin('60px') },
  ]

  // 应用行距
  const applyLineHeight = (value: string) => {
    restoreSelection()
    if (previewRef.current) {
      const selection = window.getSelection()
      if (selection && selection.rangeCount > 0) {
        const range = selection.getRangeAt(0)
        const parent = range.commonAncestorContainer.parentElement
        if (parent) {
          parent.style.lineHeight = value
          setHasChanges(true)
        }
      } else {
        // 应用到整个内容
        previewRef.current.style.lineHeight = value
        setHasChanges(true)
      }
    }
  }

  // 应用边距
  const applyMargin = (value: string) => {
    restoreSelection()
    if (previewRef.current) {
      previewRef.current.style.padding = value
      setHasChanges(true)
    }
  }

  // 智能一页：调整内容使其适合A4页面
  const handleSmartOnePage = () => {
    if (!previewRef.current) return
    const content = previewRef.current
    const a4Height = 1123 // A4高度 (px, 约297mm)
    const currentHeight = content.scrollHeight
    
    if (currentHeight > a4Height) {
      // 内容超过一页，缩小字体和行距
      const scale = a4Height / currentHeight
      const currentFontSize = parseFloat(window.getComputedStyle(content).fontSize) || 14
      const newFontSize = Math.max(10, Math.floor(currentFontSize * scale))
      content.style.fontSize = `${newFontSize}px`
      content.style.lineHeight = '1.3'
      message.success('已调整为一页')
    } else {
      message.info('内容已在一页以内')
    }
    setHasChanges(true)
  }

  // 预览用的scope
  const scope = {
    React,
    useState: React.useState,
    useEffect: React.useEffect,
  }

  // 检查代码是否有效
  const isCodeValid = codeReady && code && code.includes('render(')

  if (isLoading) {
    return (
      <div className="resume-editor-loading">
        <Spin size="large" />
      </div>
    )
  }

  return (
    <>
      <div className="resume-editor fullscreen">
      {/* 顶部工具栏 */}
      <div className="editor-header">
        <div className="header-left">
          <Button icon={<ArrowLeftOutlined />} onClick={() => navigate('/resume/my-resumes')}>
            返回
          </Button>
          <span className="divider" />
          <h2>{resumeName}</h2>
          <span className={`save-status ${hasChanges ? 'unsaved' : 'saved'}`}>
            {hasChanges ? '未保存' : lastSaved ? `已于 ${lastSaved} 保存` : ''}
          </span>
        </div>
        
        <div className="header-right">
          <Space wrap>
            <Tooltip title="刷新">
              <Button
                icon={<ReloadOutlined />}
                onClick={handleRefresh}
                loading={isLoading}
              />
            </Tooltip>
            <Tooltip title={showCode ? '隐藏代码' : '显示代码'}>
              <Button
                icon={showCode ? <MenuFoldOutlined /> : <MenuUnfoldOutlined />}
                onClick={() => setShowCode(!showCode)}
              >
                {showCode ? '隐藏代码' : '显示代码'}
              </Button>
            </Tooltip>
            <Button
              icon={<ThunderboltOutlined />}
              onClick={handleOpenAiAssistant}
            >
              React代码AI助手
            </Button>
            <Button
              icon={<CommentOutlined />}
              loading={generatingFeedback}
              onClick={handleGenerateResumeFeedback}
            >
              AI简历反馈
            </Button>
            <Button
              icon={<RobotOutlined />}
              loading={extractingKeywords}
              onClick={handleExtractKeywords}
            >
              AI关键字提取
            </Button>
            <Button
              icon={<StarOutlined />}
              loading={scoringResume}
              onClick={handleScoreResume}
            >
              AI简历打分
            </Button>
            <Segmented
              value={editMode}
              onChange={(value) => setEditMode(value as 'visual' | 'code')}
              options={[
                { label: <><EditOutlined /> 可视化编辑</>, value: 'visual' },
                { label: <><CodeOutlined /> 代码编辑</>, value: 'code' },
              ]}
            />
            <Dropdown menu={{ items: exportMenuItems }} placement="bottom">
              <Button icon={<DownloadOutlined />} loading={exporting}>
                导出
              </Button>
            </Dropdown>
            <Button
              type="primary"
              icon={<SaveOutlined />}
              loading={saving}
              onClick={() => void handleSave()}
            >
              保存
            </Button>
          </Space>
        </div>
      </div>

      {/* 主体区域 */}
      <div className={`editor-body ${!showCode ? 'preview-only' : ''}`}>
        {/* 左侧：Monaco代码编辑器 */}
        {showCode && (
          <div className="editor-left">
            <div className="code-header">
              <span>代码编辑</span>
              <span className="code-tip">支持语法高亮、自动补全</span>
            </div>
            <div className="code-editor">
              <Editor
                height="100%"
                language="javascript"
                theme="vs-dark"
                value={code}
                onChange={handleCodeChange}
                options={{
                  minimap: { enabled: false },
                  fontSize: 14,
                  lineHeight: 22,
                  tabSize: 2,
                  wordWrap: 'on',
                  automaticLayout: true,
                  scrollBeyondLastLine: false,
                  folding: true,
                  lineNumbers: 'on',
                  renderWhitespace: 'selection',
                  quickSuggestions: true,
                  suggestOnTriggerCharacters: true,
                  formatOnPaste: true,
                  formatOnType: true,
                }}
              />
            </div>
          </div>
        )}

        {/* 右侧：实时预览/可视化编辑 */}
        <div className="editor-right">
          <div className="preview-header">
            {editMode === 'visual' && (
              <div className="edit-toolbar-pro">
                {/* 智能一页 */}
                <Tooltip title="智能调整为一页">
                  <Button size="small" onClick={handleSmartOnePage} style={{ background: '#1890ff', color: '#fff' }}>
                    <CompressOutlined /> 智能一页
                  </Button>
                </Tooltip>
                
                <span className="toolbar-divider" />
                
                {/* 对齐方式 */}
                <Tooltip title="左对齐">
                  <Button size="small" icon={<AlignLeftOutlined />} onMouseDown={saveSelection} onClick={() => formatAlign('left')} />
                </Tooltip>
                <Tooltip title="居中">
                  <Button size="small" icon={<AlignCenterOutlined />} onMouseDown={saveSelection} onClick={() => formatAlign('center')} />
                </Tooltip>
                <Tooltip title="右对齐">
                  <Button size="small" icon={<AlignRightOutlined />} onMouseDown={saveSelection} onClick={() => formatAlign('right')} />
                </Tooltip>
                
                <span className="toolbar-divider" />
                
                {/* 字体 */}
                <Dropdown menu={{ items: fontFamilyMenuItems }} onOpenChange={(open) => open && saveSelection()}>
                  <Button size="small">字体 ▼</Button>
                </Dropdown>
                
                {/* 字号 */}
                <Dropdown menu={{ items: fontSizeMenuItems }} onOpenChange={(open) => open && saveSelection()}>
                  <Button size="small" icon={<FontSizeOutlined />}>字号 ▼</Button>
                </Dropdown>
                
                <span className="toolbar-divider" />
                
                {/* 加粗/斜体/下划线 */}
                <Tooltip title="加粗">
                  <Button size="small" icon={<BoldOutlined />} onMouseDown={saveSelection} onClick={formatBold} />
                </Tooltip>
                <Tooltip title="斜体">
                  <Button size="small" icon={<ItalicOutlined />} onMouseDown={saveSelection} onClick={formatItalic} />
                </Tooltip>
                <Tooltip title="下划线">
                  <Button size="small" icon={<UnderlineOutlined />} onMouseDown={saveSelection} onClick={formatUnderline} />
                </Tooltip>
                
                <span className="toolbar-divider" />
                
                {/* 列表 */}
                <Tooltip title="有序列表">
                  <Button size="small" icon={<OrderedListOutlined />} onMouseDown={saveSelection} onClick={() => insertList(true)} />
                </Tooltip>
                <Tooltip title="无序列表">
                  <Button size="small" icon={<UnorderedListOutlined />} onMouseDown={saveSelection} onClick={() => insertList(false)} />
                </Tooltip>
                
                <span className="toolbar-divider" />
                
                {/* 颜色 */}
                <Dropdown menu={{ items: fontColorMenuItems }} onOpenChange={(open) => open && saveSelection()}>
                  <Button size="small" icon={<FontColorsOutlined />}>字色 ▼</Button>
                </Dropdown>
                <Dropdown menu={{ items: bgColorMenuItems }} onOpenChange={(open) => open && saveSelection()}>
                  <Button size="small" icon={<BgColorsOutlined />}>背景 ▼</Button>
                </Dropdown>
                
                <span className="toolbar-divider" />
                
                {/* 行距/边距 */}
                <Dropdown menu={{ items: lineHeightMenuItems }} onOpenChange={(open) => open && saveSelection()}>
                  <Button size="small" icon={<LineHeightOutlined />}>行距 ▼</Button>
                </Dropdown>
                <Dropdown menu={{ items: marginMenuItems }} onOpenChange={(open) => open && saveSelection()}>
                  <Button size="small" icon={<ColumnWidthOutlined />}>边距 ▼</Button>
                </Dropdown>
              </div>
            )}
            {editMode === 'code' && (
              <div className="preview-header-simple">
                <span>简历预览</span>
                <span className="edit-tip">修改左侧代码，右侧实时更新</span>
              </div>
            )}
          </div>
          <div className="preview-container">
            {isCodeValid ? (
              <LiveProvider code={code} scope={scope} noInline={true}>
                <div 
                  ref={previewRef} 
                  className="preview-content"
                  contentEditable={editMode === 'visual'}
                  suppressContentEditableWarning={true}
                  onInput={() => setHasChanges(true)}
                >
                  <LivePreview />
                </div>
                <LiveError className="preview-error" />
              </LiveProvider>
            ) : (
              <div className="preview-placeholder">
                <Spin />
                <p>正在加载简历...</p>
              </div>
            )}
          </div>
        </div>
      </div>
      </div>

      <Modal
        title="React代码AI助手"
        open={aiAssistantModalOpen}
        onCancel={() => setAiAssistantModalOpen(false)}
        onOk={() => void handleAssistReactCode()}
        okText="生成并应用到代码区"
        cancelText="取消"
        confirmLoading={assistingReactCode}
        width={760}
        destroyOnClose
      >
        <div className="ai-assistant-form">
          <p className="assistant-tip">
            该功能会自动切换到代码编辑模式，并优化左侧代码编辑区中的 React 简历代码。AI 生成成功后，会直接覆盖到代码编辑区，请先确认当前代码已保存或可接受变更。
          </p>
          <TextArea
            value={aiAssistantRequirement}
            onChange={(e) => setAiAssistantRequirement(e.target.value)}
            rows={6}
            maxLength={500}
            placeholder="请输入你希望 AI 如何优化这份 React 简历代码，例如：优化排版层级、统一标题样式、压缩重复结构、增强模块化。"
          />
        </div>
      </Modal>
    </>
  )
}

export default ResumeEditor
