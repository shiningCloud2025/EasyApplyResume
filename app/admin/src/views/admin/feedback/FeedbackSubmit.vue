<template>
  <div class="feedback-submit">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">意见反馈</h1>
        <p class="page-description">您的反馈对我们非常重要，帮助我们改进产品和服务</p>
      </div>
    </div>

    <div class="content-wrapper">
      <el-card class="submit-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <el-icon><Edit /></el-icon>
            <span>提交反馈</span>
          </div>
        </template>

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

        <el-form
          ref="feedbackFormRef"
          :model="feedbackForm"
          :rules="feedbackRules"
          label-width="100px"
          size="large"
          class="feedback-form"
        >
          <el-form-item label="反馈标题" prop="adminFeedbackTitle">
            <el-input
              v-model="feedbackForm.adminFeedbackTitle"
              placeholder="请简要描述您的问题或建议"
              maxlength="35"
              show-word-limit
              clearable
            />
          </el-form-item>

          <el-form-item label="反馈内容" prop="adminFeedbackContent">
            <el-input
              v-model="feedbackForm.adminFeedbackContent"
              type="textarea"
              :rows="12"
              placeholder="请详细描述您遇到的问题或建议，我们会认真阅读并处理"
              show-word-limit
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" size="large" @click="handleSubmit" :loading="submitting">
              <el-icon><Position /></el-icon>
              提交反馈
            </el-button>
            <el-button size="large" @click="handleReset">
              <el-icon><RefreshLeft /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <div class="stats-section">
        <el-row :gutter="16">
          <el-col :span="8">
            <el-card class="stat-card" shadow="hover">
              <div class="stat-content">
                <el-icon class="stat-icon" :size="32" color="#409eff">
                  <ChatDotRound />
                </el-icon>
                <div class="stat-info">
                  <div class="stat-value">快速响应</div>
                  <div class="stat-label">1-3个工作日</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="stat-card" shadow="hover">
              <div class="stat-content">
                <el-icon class="stat-icon" :size="32" color="#67c23a">
                  <CircleCheck />
                </el-icon>
                <div class="stat-info">
                  <div class="stat-value">认真对待</div>
                  <div class="stat-label">每条反馈必回复</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="stat-card" shadow="hover">
              <div class="stat-content">
                <el-icon class="stat-icon" :size="32" color="#e6a23c">
                  <TrendCharts />
                </el-icon>
                <div class="stat-info">
                  <div class="stat-value">持续改进</div>
                  <div class="stat-label">不断优化体验</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import {
  Edit,
  Position,
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
      ElMessage.error('未获取到用户信息')
      return
    }

    submitting.value = true

    // 设置管理员ID
    feedbackForm.adminFeedbackAdminId = user.userId

    console.log('📤 [反馈提交] 提交反馈:', feedbackForm)
    await feedbackApi.addFeedback(feedbackForm)

    ElMessage.success('反馈提交成功！我们会尽快处理')

    // 重置表单
    handleReset()

    // 询问是否跳转到反馈记录页面
    setTimeout(() => {
      ElMessage({
        message: '可在"管理端反馈记录"中查看处理进度',
        type: 'info',
        duration: 3000
      })
    }, 1000)
  } catch (error) {
    console.error('❌ [反馈提交] 提交失败:', error)
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
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 32px;

  .header-content {
    .page-title {
      font-size: 28px;
      font-weight: 700;
      color: #1f2937;
      margin-bottom: 8px;
    }

    .page-description {
      font-size: 14px;
      color: #6b7280;
      margin: 0;
    }
  }
}

.content-wrapper {
  max-width: 900px;
  margin: 0 auto;
}

.submit-card {
  border-radius: 16px;
  margin-bottom: 24px;
  
  .card-header {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 18px;
    font-weight: 600;
    color: #1f2937;
  }

  .tip-alert {
    margin-bottom: 24px;
    border-radius: 8px;

    :deep(.el-alert__title) {
      font-size: 14px;
      line-height: 1.6;
    }
  }

  .feedback-form {
    :deep(.el-form-item__label) {
      font-weight: 500;
    }

    :deep(.el-input__wrapper) {
      box-shadow: 0 0 0 1px var(--el-input-border-color,var(--el-border-color)) inset;
    }

    :deep(.el-textarea__inner) {
      box-shadow: 0 0 0 1px var(--el-input-border-color,var(--el-border-color)) inset;
    }
  }
}

.stats-section {
  .stat-card {
    border-radius: 12px;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
    }

    .stat-content {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 8px;

      .stat-icon {
        flex-shrink: 0;
      }

      .stat-info {
        flex: 1;

        .stat-value {
          font-size: 16px;
          font-weight: 600;
          color: #1f2937;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 13px;
          color: #6b7280;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .feedback-submit {
    padding: 16px;
  }

  .stats-section {
    .el-col {
      margin-bottom: 12px;
    }
  }
}
</style>
