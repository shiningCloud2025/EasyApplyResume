import React, { useState, useCallback, useRef, useEffect } from 'react'
import { 
  Button, 
  Space, 
  message,
  Tooltip,
  Spin,
  Dropdown,
  Segmented
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
  CompressOutlined
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

const ResumeEditor: React.FC = () => {
  const { sortedNum } = useParams<{ sortedNum: string }>()
  const navigate = useNavigate()
  const { user } = useUserStore()
  const [saving, setSaving] = useState(false)
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
  const handleSave = async () => {
    if (!user) return
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
      message.success('保存成功')
    } catch (error: any) {
      console.error('保存失败:', error)
      const errorMsg = error?.response?.data?.message || error?.message || '保存失败'
      message.error(errorMsg)
    } finally {
      setSaving(false)
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
          <Space>
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
              onClick={handleSave}
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
  )
}

export default ResumeEditor
