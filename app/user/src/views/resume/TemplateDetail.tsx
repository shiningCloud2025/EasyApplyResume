import React, { useState } from 'react'
import { Card, Button, Tag, Spin, Empty, message, Descriptions, Modal, Input } from 'antd'
import { 
  ArrowLeftOutlined, 
  HeartOutlined, 
  HeartFilled, 
  DownloadOutlined,
  FileWordOutlined,
  FullscreenOutlined,
  FullscreenExitOutlined
} from '@ant-design/icons'
import { useQuery, useQueryClient } from 'react-query'
import { useParams, useNavigate } from 'react-router-dom'
import { resumeAPI } from '@api/resume'
import useUserStore from '@stores/userStore'
import { LiveProvider, LivePreview, LiveError } from 'react-live'
import html2canvas from 'html2canvas'
import { jsPDF } from 'jspdf'
import './TemplateDetail.scss'

// React代码预览组件
const ReactCodePreview: React.FC<{ code: string }> = ({ code }) => {
  if (!code) {
    return (
      <div className="preview-placeholder">
        <span>暂无预览内容</span>
      </div>
    )
  }

  // 预处理代码
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

  const processedCode = processCode(code)

  const scope = {
    React,
    useState: React.useState,
    useEffect: React.useEffect,
    useMemo: React.useMemo,
    useCallback: React.useCallback,
  }

  return (
    <LiveProvider code={processedCode} scope={scope} noInline={true}>
      <div className="live-preview-wrapper">
        <LivePreview />
      </div>
      <LiveError className="live-error" />
    </LiveProvider>
  )
}

const TemplateDetail: React.FC = () => {
  const { templateId } = useParams<{ templateId: string }>()
  const navigate = useNavigate()
  const { user } = useUserStore()
  const queryClient = useQueryClient()
  const [isFullscreen, setIsFullscreen] = useState(false)
  const [exporting, setExporting] = useState(false)
  const [useModalVisible, setUseModalVisible] = useState(false)
  const [resumeName, setResumeName] = useState('')
  const [creating, setCreating] = useState(false)

  // 获取模板详情
  const { data: template, isLoading, error } = useQuery(
    ['template-detail', templateId],
    () => resumeAPI.getTemplateById(Number(templateId)),
    {
      enabled: !!templateId,
      select: (response) => response.data,
      onError: (error: any) => {
        const errorMsg = error?.response?.data?.message || error?.message || '获取模板详情失败'
        message.error(errorMsg)
      }
    }
  )

  // 检查收藏状态
  const { data: isCollected, refetch: refetchCollected } = useQuery(
    ['template-collected', user?.userId, templateId],
    () => resumeAPI.checkTemplateCollected(user!.userId, Number(templateId)),
    {
      enabled: !!user && !!templateId,
      select: (response) => response.data?.data || false
    }
  )

  const handleBack = () => {
    navigate(-1)
  }

  const handleCollect = async () => {
    if (!user) {
      message.warning('请先登录')
      navigate('/auth/login')
      return
    }

    try {
      await resumeAPI.collectTemplate(user.userId, Number(templateId), !isCollected)
      message.success(isCollected ? '取消收藏成功' : '收藏成功')
      refetchCollected()
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || '操作失败'
      message.error(errorMsg)
    }
  }

  const handleUseTemplate = () => {
    if (!user) {
      message.warning('请先登录')
      navigate('/auth/login')
      return
    }
    if (!template) return
    setResumeName('')
    setUseModalVisible(true)
  }

  const handleConfirmUse = async () => {
    if (!user || !template) return
    setCreating(true)
    try {
      await resumeAPI.createResumeFromTemplate(user.userId, {
        resumeTemplateId: template.resumeTemplateId,
        resumeTemplateName: template.resumeTemplateName,
        resumeTemplateReactCode: template.resumeTemplateReactCode,
        industryMapIndustryName: template.industryMapIndustryName,
        isEnable: template.isEnable,
        createTime: template.createTime,
        updateTime: template.updateTime
      }, resumeName || undefined)
      message.success('创建成功')
      setUseModalVisible(false)
      // 使简历列表缓存失效，等待完成后再跳转
      await queryClient.invalidateQueries(['user-resumes', user.userId])
      navigate('/resume/my-resumes')
    } catch (error: any) {
      const errorMsg = error?.response?.data?.message || error?.message || '创建简历失败'
      message.error(errorMsg)
    } finally {
      setCreating(false)
    }
  }

  const handleExportPDF = async () => {
    const previewElement = document.querySelector('.live-preview-wrapper')
    if (!previewElement) {
      message.warning('预览区域未加载')
      return
    }

    setExporting(true)
    try {
      const canvas = await html2canvas(previewElement as HTMLElement, {
        scale: 2,
        useCORS: true,
        allowTaint: true
      })
      
      const imgData = canvas.toDataURL('image/png')
      const pdf = new jsPDF('p', 'mm', 'a4')
      const pdfWidth = pdf.internal.pageSize.getWidth()
      const pdfHeight = (canvas.height * pdfWidth) / canvas.width
      
      pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight)
      pdf.save(`${template?.resumeTemplateName || '简历模板'}.pdf`)
      message.success('导出PDF成功')
    } catch (error) {
      message.error('导出失败')
    } finally {
      setExporting(false)
    }
  }

  const handleExportPNG = async () => {
    const previewElement = document.querySelector('.live-preview-wrapper')
    if (!previewElement) {
      message.warning('预览区域未加载')
      return
    }

    setExporting(true)
    try {
      const canvas = await html2canvas(previewElement as HTMLElement, {
        scale: 2,
        useCORS: true,
        allowTaint: true
      })
      
      const link = document.createElement('a')
      link.download = `${template?.resumeTemplateName || '简历模板'}.png`
      link.href = canvas.toDataURL('image/png')
      link.click()
      message.success('导出PNG成功')
    } catch (error) {
      message.error('导出失败')
    } finally {
      setExporting(false)
    }
  }

  const handleExportWord = async () => {
    const previewElement = document.querySelector('.live-preview-wrapper')
    if (!previewElement) {
      message.warning('预览区域未加载')
      return
    }

    setExporting(true)
    try {
      const htmlContent = previewElement.innerHTML
      const blob = new Blob([`
        <html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:w='urn:schemas-microsoft-com:office:word'>
        <head><meta charset='utf-8'><title>${template?.resumeTemplateName || '简历模板'}</title></head>
        <body>${htmlContent}</body>
        </html>
      `], { type: 'application/msword' })
      
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = `${template?.resumeTemplateName || '简历模板'}.doc`
      link.click()
      URL.revokeObjectURL(link.href)
      message.success('导出Word成功')
    } catch (error) {
      message.error('导出失败')
    } finally {
      setExporting(false)
    }
  }

  const toggleFullscreen = () => {
    setIsFullscreen(!isFullscreen)
  }

  const formatDate = (date: string | Date) => {
    if (!date) return '-'
    return new Date(date).toLocaleDateString('zh-CN')
  }

  if (isLoading) {
    return (
      <div className="template-detail-page loading">
        <Spin size="large" tip="加载中..." />
      </div>
    )
  }

  if (error || !template) {
    return (
      <div className="template-detail-page error">
        <Empty description="模板不存在或加载失败">
          <Button type="primary" onClick={handleBack}>返回列表</Button>
        </Empty>
      </div>
    )
  }

  return (
    <div className={`template-detail-page ${isFullscreen ? 'fullscreen' : ''}`}>
      {/* 头部操作栏 */}
      <div className="page-header">
        <Button icon={<ArrowLeftOutlined />} onClick={handleBack}>
          返回
        </Button>
        <div className="header-actions">
          <Button
            icon={isCollected ? <HeartFilled /> : <HeartOutlined />}
            onClick={handleCollect}
          >
            {isCollected ? '取消收藏' : '收藏'}
          </Button>
          <Button
            icon={<DownloadOutlined />}
            onClick={handleExportPNG}
            loading={exporting}
          >
            导出PNG
          </Button>
          <Button
            icon={<DownloadOutlined />}
            onClick={handleExportPDF}
            loading={exporting}
          >
            导出PDF
          </Button>
          <Button
            icon={<FileWordOutlined />}
            onClick={handleExportWord}
            loading={exporting}
          >
            导出Word
          </Button>
          <Button
            onClick={handleUseTemplate}
          >
            使用此模板
          </Button>
        </div>
      </div>

      {/* 主体内容 */}
      <div className="page-content">
        {/* 预览区域 */}
        <Card 
          className="preview-card"
          title={
            <div className="preview-card-header">
              <span>模板预览</span>
              <Button
                type="text"
                icon={isFullscreen ? <FullscreenExitOutlined /> : <FullscreenOutlined />}
                onClick={toggleFullscreen}
              />
            </div>
          }
        >
          <div className="preview-container">
            <ReactCodePreview code={template.resumeTemplateReactCode} />
          </div>
        </Card>

        {/* 信息区域 */}
        {!isFullscreen && (
          <Card className="info-card" title="模板信息">
            <Descriptions column={1} bordered size="small">
              <Descriptions.Item label="模板名称">
                {template.resumeTemplateName}
              </Descriptions.Item>
              <Descriptions.Item label="所属行业">
                <Tag color="blue">{template.industryMapIndustryName || '-'}</Tag>
              </Descriptions.Item>
              <Descriptions.Item label="创建时间">
                {formatDate(template.createTime)}
              </Descriptions.Item>
              <Descriptions.Item label="更新时间">
                {formatDate(template.updateTime)}
              </Descriptions.Item>
            </Descriptions>
          </Card>
        )}
      </div>

      {/* 使用模板弹窗 */}
      <Modal
        title="保存简历"
        open={useModalVisible}
        onCancel={() => setUseModalVisible(false)}
        onOk={handleConfirmUse}
        confirmLoading={creating}
        okText="确定"
        cancelText="取消"
      >
        <div style={{ marginBottom: 16 }}>
          <p>请输入简历名称（可选，不填则自动命名）：</p>
          <Input
            placeholder="请输入简历名称"
            value={resumeName}
            onChange={(e) => setResumeName(e.target.value)}
            maxLength={50}
          />
        </div>
      </Modal>
    </div>
  )
}

export default TemplateDetail
