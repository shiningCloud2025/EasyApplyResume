<template>
  <div class="feedback-submit">
    <el-card class="feedback-card">
      <!-- 页面标题 -->
      <div class="page-header">
        <div class="header-icon">
          <el-icon :size="40"><ChatLineRound /></el-icon>
        </div>
        <div class="header-text">
          <h1>意见反馈</h1>
          <p>您的反馈对我们非常重要，帮助我们改进产品和服务</p>
        </div>
      </div>

      <!-- 提示信息 -->
      <el-alert
        type="info"
        :closable="false"
        show-icon
        class="tip-alert"
      >
        <template #title>
          <span>温馨提示：请详细描述您遇到的问题或建议，我们会在1-3个工作日内处理您的反馈</span>
        </template>
      </el-alert>

      <!-- 反馈表单 -->
      <el-form
        ref="feedbackFormRef"
        :model="feedbackForm"
        :rules="feedbackRules"
        label-position="top"
        class="feedback-form"
      >
        <el-form-item label="反馈标题" prop="adminFeedbackTitle">
          <el-input
            v-model="feedbackForm.adminFeedbackTitle"
            placeholder="请简要描述您的问题或建议（5-35字符）"
            maxlength="35"
            show-word-limit
            clearable
            size="large"
          />
        </el-form-item>

        <el-form-item label="反馈内容" prop="adminFeedbackContent">
          <el-input
            v-model="feedbackForm.adminFeedbackContent"
            type="textarea"
            :rows="10"
            placeholder="请详细描述您遇到的问题或建议，我们会认真阅读并处理（至少10个字符）"
            show-word-limit
            resize="none"
          />
        </el-form-item>

        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            @click="handleSubmit" 
            :loading="submitting"
            style="width: 160px;"
          >
            <el-icon><Promotion /></el-icon>
            提交反馈
          </el-button>
          <el-button 
            size="large" 
            @click="handleReset"
            style="width: 120px;"
          >
            <el-icon><RefreshLeft /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 底部特色卡片 -->
      <div class="features-section">
        <div class="feature-item">
          <el-icon :size="24" color="#409eff"><ChatDotRound /></el-icon>
          <div class="feature-text">
            <div class="feature-title">快速响应</div>
            <div class="feature-desc">1-3个工作日内处理</div>
          </div>
        </div>
        <div class="feature-item">
          <el-icon :size="24" color="#67c23a"><CircleCheck /></el-icon>
          <div class="feature-text">
            <div class="feature-title">认真对待</div>
            <div class="feature-desc">每条反馈必回复</div>
          </div>
        </div>
        <div class="feature-item">
          <el-icon :size="24" color="#e6a23c"><TrendCharts /></el-icon>
          <div class="feature-text">
            <div class="feature-title">持续改进</div>
            <div class="feature-desc">不断优化体验</div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import {
  ChatLineRound,
  Promotion,
  RefreshLeft,
  ChatDotRound,
  CircleCheck,
  TrendCharts
} from '@element-plus/icons-vue'
import { feedbackApi } from '@/api/admin'
import type { AdminFeedbackForm } from '@/types/admin'
import { useAuthStore } from '@/store/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()
const feedbackFormRef = ref<FormInstance>()
const submitting = ref(false)

// 反馈表单
const feedbackForm = reactive<AdminFeedbackForm>({
  adminFeedbackTitle: '',
  adminFeedbackContent: '',
  adminFeedbackAdminId: 0
})

// 表单验证规则
const feedbackRules = {
  adminFeedbackTitle: [
    { required: true, message: '请输入反馈标题', trigger: 'blur' },
    { min: 5, max: 35, message: '标题长度在 5 到 35 个字符', trigger: 'blur' }
  ],
  adminFeedbackContent: [
    { required: true, message: '请输入反馈内容', trigger: 'blur' },
    { min: 10, message: '内容至少 10 个字符', trigger: 'blur' }
  ]
}

// 提交反馈
const handleSubmit = async () => {
  if (!feedbackFormRef.value) return

  try {
    await feedbackFormRef.value.validate()

    const user = authStore.user
    if (!user?.userId) {
      ElMessage.error('未获取到用户信息，请重新登录')
      return
    }

    submitting.value = true

    // 设置管理员ID
    feedbackForm.adminFeedbackAdminId = user.userId

    console.log('📤 [反馈提交] 提交数据:', feedbackForm)
    
    // 调用后端接口 POST /admin/feedback/addFeedback
    await feedbackApi.addFeedback(feedbackForm)

    ElMessage.success('反馈提交成功！我们会尽快处理')

    // 重置表单
    handleReset()

    // 提示用户
    setTimeout(() => {
      ElMessage({
        message: '可在"管理端反馈记录"中查看处理进度',
        type: 'info',
        duration: 3000
      })
    }, 1000)
  } catch (error: any) {
    console.error('❌ [反馈提交] 提交失败:', error)
    if (error.errors) {
      // 表单验证失败
      return
    }
    ElMessage.error('提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const handleReset = () => {
  if (feedbackFormRef.value) {
    feedbackFormRef.value.resetFields()
  }
  feedbackForm.adminFeedbackTitle = ''
  feedbackForm.adminFeedbackContent = ''
}

onMounted(() => {
  console.log('💬 [反馈提交] 页面加载完成')
})
</script>

<style scoped lang="scss">
.feedback-submit {
  min-height: calc(100vh - 120px);
  padding: 40px 24px;
  background: linear-gradient(to bottom, #f9fafb, #ffffff);
  display: flex;
  justify-content: center;
  align-items: center;
}

.feedback-card {
  width: 100%;
  max-width: 800px;
  border-radius: 20px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
  overflow: visible;
  padding: 40px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 2px solid #f3f4f6;

  .header-icon {
    width: 70px;
    height: 70px;
    border-radius: 16px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    box-shadow: 0 8px 16px rgba(102, 126, 234, 0.3);
  }

  .header-text {
    flex: 1;

    h1 {
      font-size: 28px;
      font-weight: 700;
      color: #1f2937;
      margin: 0 0 8px 0;
    }

    p {
      font-size: 14px;
      color: #6b7280;
      margin: 0;
      line-height: 1.5;
    }
  }
}

.tip-alert {
  margin-bottom: 32px;
  border-radius: 12px;
  background: #f0f9ff;
  border: 1px solid #bae7ff;

  :deep(.el-alert__content) {
    .el-alert__title {
      font-size: 14px;
      line-height: 1.6;
      color: #1890ff;
    }
  }
}

.feedback-form {
  position: relative;
  z-index: 1;

  :deep(.el-form-item) {
    margin-bottom: 28px;
  }

  :deep(.el-form-item__label) {
    font-size: 15px;
    font-weight: 600;
    color: #1f2937;
    margin-bottom: 8px;
  }

  :deep(.el-input) {
    .el-input__wrapper {
      border-radius: 10px;
      padding: 12px 16px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
      transition: all 0.3s;

      &:hover {
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
      }

      &.is-focus {
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        border-color: #667eea;
      }
    }
  }

  :deep(.el-textarea) {
    .el-textarea__inner {
      border-radius: 10px;
      padding: 12px 16px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
      font-family: inherit;
      line-height: 1.6;
      transition: all 0.3s;

      &:hover {
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
      }

      &:focus {
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        border-color: #667eea;
      }
    }
  }

  .el-button {
    border-radius: 10px;
    font-weight: 600;
    transition: all 0.3s;

    &.el-button--primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;
      box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
      }

      &:active {
        transform: translateY(0);
      }
    }
  }
}

.features-section {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-top: 40px;
  padding-top: 32px;
  border-top: 2px solid #f3f4f6;

  .feature-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 16px;
    background: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);
    border-radius: 12px;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08);
      background: linear-gradient(135deg, #ffffff 0%, #f7fafc 100%);
    }

    .el-icon {
      flex-shrink: 0;
    }

    .feature-text {
      .feature-title {
        font-size: 15px;
        font-weight: 600;
        color: #1f2937;
        margin-bottom: 4px;
      }

      .feature-desc {
        font-size: 12px;
        color: #6b7280;
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .feedback-submit {
    padding: 20px 12px;
  }

  .feedback-card {
    padding: 24px 20px;
  }

  .page-header {
    flex-direction: column;
    text-align: center;

    .header-icon {
      width: 60px;
      height: 60px;
    }

    .header-text {
      h1 {
        font-size: 24px;
      }

      p {
        font-size: 13px;
      }
    }
  }

  .features-section {
    grid-template-columns: 1fr;
    gap: 12px;
  }
}
</style>
