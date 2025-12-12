import React, { useState } from 'react'
import { 
  Card, 
  Form, 
  Input, 
  Select, 
  Button, 
  message,
  Rate,
  Space
} from 'antd'
import { useMutation, useQueryClient } from 'react-query'
import { feedbackAPI } from '@api/feedback'
import { useUserStore } from '@stores/userStore'
import type { FeedbackFormData } from '@types/index'

const FeedbackSubmit: React.FC = () => {
  const [form] = Form.useForm()
  const { user } = useUserStore()
  const queryClient = useQueryClient()
  
  // 提交反馈mutation
  const submitFeedbackMutation = useMutation(
    (feedbackData: FeedbackFormData) => feedbackAPI.addFeedback(feedbackData),
    {
      onSuccess: () => {
        message.success('反馈提交成功')
        form.resetFields()
        queryClient.invalidateQueries(['feedbacks', user?.userId])
      },
      onError: (error) => {
        message.error('反馈提交失败')
        console.error('提交失败:', error)
      }
    }
  )

  const handleSubmit = (values: any) => {
    if (!user) {
      message.warning('请先登录')
      return
    }

    const feedbackData: FeedbackFormData = {
      feedbackUserId: user.userId,
      feedbackTitle: values.title,
      feedbackContent: values.content,
      feedbackType: values.type,
      feedbackRating: values.rating,
      feedbackStatus: 'pending'
    }

    submitFeedbackMutation.mutate(feedbackData)
  }

  return (
    <div className="feedback-submit">
      <Card title="提交反馈" style={{ maxWidth: 800, margin: '0 auto' }}>
        <Form
          form={form}
          layout="vertical"
          onFinish={handleSubmit}
        >
          <Form.Item
            label="反馈类型"
            name="type"
            rules={[{ required: true, message: '请选择反馈类型' }]}
          >
            <Select placeholder="请选择反馈类型">
              <Select.Option value="bug">问题反馈</Select.Option>
              <Select.Option value="feature">功能建议</Select.Option>
              <Select.Option value="improvement">改进建议</Select.Option>
              <Select.Option value="other">其他</Select.Option>
            </Select>
          </Form.Item>

          <Form.Item
            label="反馈标题"
            name="title"
            rules={[{ required: true, message: '请输入反馈标题' }]}
          >
            <Input placeholder="请输入反馈标题" maxLength={100} showCount />
          </Form.Item>

          <Form.Item
            label="满意度评分"
            name="rating"
            rules={[{ required: true, message: '请评分' }]}
          >
            <Rate allowHalf />
          </Form.Item>

          <Form.Item
            label="详细描述"
            name="content"
            rules={[{ required: true, message: '请详细描述您的反馈' }]}
          >
            <Input.TextArea 
              placeholder="请详细描述您的问题或建议..." 
              rows={6} 
              maxLength={1000} 
              showCount 
            />
          </Form.Item>

          <Form.Item>
            <Space>
              <Button 
                type="primary" 
                htmlType="submit" 
                loading={submitFeedbackMutation.isLoading}
              >
                提交反馈
              </Button>
              <Button onClick={() => form.resetFields()}>
                重置
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Card>
    </div>
  )
}

export default FeedbackSubmit