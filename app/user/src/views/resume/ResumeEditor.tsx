import React, { useState, useEffect } from 'react'
import { 
  Card, 
  Button, 
  Input, 
  Select, 
  Divider, 
  Space, 
  message,
  Modal,
  Tooltip
} from 'antd'
import { 
  SaveOutlined, 
  EyeOutlined, 
  UndoOutlined, 
  RedoOutlined,
  DownloadOutlined,
  FullscreenOutlined,
  SettingOutlined
} from '@ant-design/icons'
import { useParams, useNavigate } from 'react-router-dom'

const { Option } = Select
const { TextArea } = Input

interface ResumeEditorState {
  content: string
  saved: boolean
  lastSaved: string
  section: string
}

const ResumeEditor: React.FC = () => {
  const { sortedNum } = useParams<{ sortedNum: string }>()
  const navigate = useNavigate()
  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)
  const [fullscreen, setFullscreen] = useState(false)
  const [state, setState] = useState<ResumeEditorState>({
    content: '',
    saved: false,
    lastSaved: '',
    section: 'basic'
  })

  // 模拟简历数据
  const resumeData = {
    name: '前端工程师简历',
    sections: {
      basic: {
        name: '基本信息',
        fields: ['姓名', '年龄', '电话', '邮箱', '地址']
      },
      education: {
        name: '教育背景',
        fields: ['学校名称', '专业', '学历', '毕业时间']
      },
      experience: {
        name: '工作经历',
        fields: ['公司名称', '职位', '工作时间', '主要职责']
      },
      skills: {
        name: '技能特长',
        fields: ['专业技能', '语言能力', '证书']
      },
      projects: {
        name: '项目经验',
        fields: ['项目名称', '描述', '职责', '成果']
      }
    }
  }

  useEffect(() => {
    // 自动保存
    const autoSaveTimer = setInterval(() => {
      if (state.content) {
        handleAutoSave()
      }
    }, 30000) // 30秒自动保存

    return () => clearInterval(autoSaveTimer)
  }, [state.content])

  const handleSectionChange = (section: string) => {
    setState({ ...state, section })
  }

  const handleContentChange = (value: string) => {
    setState({ ...state, content: value, saved: false })
  }

  const handleSave = async () => {
    setSaving(true)
    try {
      // 模拟保存API
      await new Promise(resolve => setTimeout(resolve, 1000))
      setState({
        ...state,
        saved: true,
        lastSaved: new Date().toLocaleTimeString('zh-CN')
      })
      message.success('保存成功')
    } catch (error) {
      message.error('保存失败')
    } finally {
      setSaving(false)
    }
  }

  const handleAutoSave = async () => {
    try {
      // 模拟自动保存API
      await new Promise(resolve => setTimeout(resolve, 500))
      setState({
        ...state,
        saved: true,
        lastSaved: new Date().toLocaleTimeString('zh-CN')
      })
      console.log('自动保存成功')
    } catch (error) {
      console.log('自动保存失败')
    }
  }

  const handlePreview = () => {
    Modal.info({
      title: '简历预览',
      width: 800,
      content: (
        <div style={{ padding: '20px' }}>
          <div style={{ 
            border: '1px solid #d9d9d9', 
            padding: '40px',
            minHeight: '600px',
            background: '#fff'
          }}>
            <h2>{resumeData.name}</h2>
            <div style={{ marginTop: '30px' }}>
              <h3>基本信息</h3>
              <p>姓名: 张三</p>
              <p>年龄: 25</p>
              <p>电话: 13800138000</p>
              <p>邮箱: zhangsan@example.com</p>
            </div>
            <Divider />
            <div style={{ marginTop: '30px' }}>
              <h3>教育背景</h3>
              <p>学校: XX大学</p>
              <p>专业: 计算机科学与技术</p>
              <p>学历: 本科</p>
              <p>毕业时间: 2020-06</p>
            </div>
            <Divider />
            <div style={{ marginTop: '30px' }}>
              <h3>工作经历</h3>
              <p>公司: XX科技有限公司</p>
              <p>职位: 前端工程师</p>
              <p>时间: 2020-07至今</p>
              <p>职责: 负责公司Web前端开发工作</p>
            </div>
          </div>
        </div>
      )
    })
  }

  const handleDownload = () => {
    // 模拟下载
    message.success('简历下载中...')
    setTimeout(() => {
      message.success('下载完成')
    }, 2000)
  }

  const toggleFullscreen = () => {
    setFullscreen(!fullscreen)
  }

  const renderSectionContent = () => {
    const section = resumeData.sections[state.section as keyof typeof resumeData.sections]
    
    if (!section) return null

    return (
      <div className="editor-section">
        <h3>{section.name}</h3>
        {state.section === 'basic' && (
          <div className="form-fields">
            <div className="field-group">
              <label>姓名</label>
              <Input placeholder="请输入姓名" />
            </div>
            <div className="field-group">
              <label>年龄</label>
              <Input placeholder="请输入年龄" />
            </div>
            <div className="field-group">
              <label>电话</label>
              <Input placeholder="请输入电话" />
            </div>
            <div className="field-group">
              <label>邮箱</label>
              <Input placeholder="请输入邮箱" />
            </div>
            <div className="field-group">
              <label>地址</label>
              <Input placeholder="请输入地址" />
            </div>
          </div>
        )}
        
        {state.section === 'education' && (
          <div className="form-fields">
            <div className="field-group">
              <label>学校名称</label>
              <Input placeholder="请输入学校名称" />
            </div>
            <div className="field-group">
              <label>专业</label>
              <Input placeholder="请输入专业" />
            </div>
            <div className="field-group">
              <label>学历</label>
              <Select placeholder="请选择学历" style={{ width: '100%' }}>
                <Option value="bachelor">本科</Option>
                <Option value="master">硕士</Option>
                <Option value="doctor">博士</Option>
              </Select>
            </div>
            <div className="field-group">
              <label>毕业时间</label>
              <Input placeholder="请输入毕业时间" />
            </div>
          </div>
        )}
        
        {(state.section === 'experience' || state.section === 'skills' || state.section === 'projects') && (
          <div className="editor-textarea">
            <TextArea
              rows={20}
              placeholder={`请输入${section.name}...`}
              value={state.content}
              onChange={(e) => handleContentChange(e.target.value)}
            />
          </div>
        )}
      </div>
    )
  }

  return (
    <div className={`resume-editor ${fullscreen ? 'fullscreen' : ''}`}>
      <div className="editor-header">
        <div className="header-left">
          <Button 
            onClick={() => navigate('/resume/my-resumes')}
          >
            返回
          </Button>
          <Divider type="vertical" />
          <h2>{resumeData.name} - 简历编辑</h2>
          {!state.saved && (
            <span className="unsaved-indicator">未保存</span>
          )}
          {state.saved && state.lastSaved && (
            <span className="saved-indicator">已于 {state.lastSaved} 保存</span>
          )}
        </div>
        
        <div className="header-actions">
          <Space>
            <Tooltip title="撤销">
              <Button icon={<UndoOutlined />} />
            </Tooltip>
            <Tooltip title="重做">
              <Button icon={<RedoOutlined />} />
            </Tooltip>
            <Tooltip title="预览">
              <Button icon={<EyeOutlined />} onClick={handlePreview} />
            </Tooltip>
            <Tooltip title="下载">
              <Button icon={<DownloadOutlined />} onClick={handleDownload} />
            </Tooltip>
            <Tooltip title={fullscreen ? '退出全屏' : '全屏'}>
              <Button 
                icon={<FullscreenOutlined />} 
                onClick={toggleFullscreen}
              />
            </Tooltip>
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

      <div className="editor-body">
        <div className="editor-sidebar">
          <div className="sidebar-header">
            <span>简历内容</span>
            <Button 
              type="text" 
              size="small" 
              icon={<SettingOutlined />}
            />
          </div>
          
          <div className="sidebar-menu">
            {Object.entries(resumeData.sections).map(([key, section]) => (
              <div
                key={key}
                className={`menu-item ${state.section === key ? 'active' : ''}`}
                onClick={() => handleSectionChange(key)}
              >
                {section.name}
              </div>
            ))}
          </div>
        </div>

        <div className="editor-content">
          {renderSectionContent()}
        </div>
      </div>
    </div>
  )
}

export default ResumeEditor