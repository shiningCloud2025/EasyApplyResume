<template>
  <div class="feedback-manage">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">管理端反馈管理</h1>
        <p class="page-description">处理管理员提交的反馈信息</p>
      </div>
      <div class="header-actions">
        <el-button @click="refreshData" :icon="Refresh" type="default">
          刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="反馈标题">
          <el-input
            v-model="searchForm.adminFeedbackTitle"
            placeholder="请输入反馈标题"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="反馈内容">
          <el-input
            v-model="searchForm.adminFeedbackContent"
            placeholder="请输入反馈内容"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><RefreshRight /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 反馈列表 -->
    <el-card class="table-card">
      <div class="table-header">
        <span class="table-title">反馈列表</span>
      </div>

      <el-table 
        v-loading="loading"
        :data="tableData" 
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column prop="adminFeedbackId" label="反馈ID" width="80" />
        <el-table-column prop="adminFeedbackTitle" label="反馈标题" min-width="200" />
        <el-table-column label="反馈内容" min-width="150">
          <template #default="{ row }">
            <el-button v-if="canViewFeedbackDetail" type="primary" size="small" @click="viewFeedbackContent(row)">
              详情
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="提交时间" width="120">
          <template #default="{ row }">
            {{ row.adminFeedbackTime ? formatDate(row.adminFeedbackTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="最近处理时间" width="120">
          <template #default="{ row }">
            {{ row.adminFeedbackRecentTime ? formatDate(row.adminFeedbackRecentTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="adminFeedbackCurStep" label="当前状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.adminFeedbackCurStep)">
              {{ row.adminFeedbackCurStep }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="adminFeedbackAdminId" label="提交人ID" width="100" />
        <el-table-column prop="adminFeedbackAdminName" label="提交人姓名" width="120" />
        <el-table-column v-if="showActionColumn" label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="canViewFeedbackDetail"
              type="info"
              size="default"
              @click="handleDetail(row)"
            >
              查看
            </el-button>
            <!-- 待接受状态：显示接受和忽视按钮 -->
            <template v-if="canUpdateFeedbackStep && (row.adminFeedbackCurStep === '待接受' || row.adminFeedbackCurStep === '待接收')">
              <el-button
                type="success"
                size="default"
                @click="handleProcess(row, 0, '接受')"
              >
                接受
              </el-button>
              <el-button
                type="warning"
                size="default"
                @click="handleProcess(row, 1, '忽视')"
              >
                忽视
              </el-button>
            </template>
            <!-- 待回复状态：显示回复和拒回复按钮 -->
            <template v-else-if="canUpdateFeedbackStep && row.adminFeedbackCurStep === '待回复'">
              <el-button
                type="primary"
                size="default"
                @click="handleProcess(row, 2, '回复')"
              >
                回复
              </el-button>
              <el-button
                type="danger"
                size="default"
                @click="handleProcess(row, 3, '拒回复')"
              >
                拒回复
              </el-button>
            </template>
            <!-- 已回复、忽视、拒回复状态：不显示处理按钮 -->
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog
      v-model="showDetailDialog"
      title="反馈详情"
      width="700px"
    >
      <div class="feedback-detail" v-if="currentDetail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="管理员反馈ID">
            {{ currentDetail.adminFeedbackId }}
          </el-descriptions-item>
          <el-descriptions-item label="反馈标题">
            {{ currentDetail.adminFeedbackTitle }}
          </el-descriptions-item>
          <el-descriptions-item label="反馈提交时间">
            {{ formatDate(currentDetail.adminFeedbackTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="最近一次处理时间">
            {{ formatDate(currentDetail.adminFeedbackRecentTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="反馈现阶段">
            <el-tag :type="getStatusType(currentDetail.adminFeedbackCurStep)">
              {{ currentDetail.adminFeedbackCurStep }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="提交反馈的管理员ID">
            {{ currentDetail.adminFeedbackAdminId }}
          </el-descriptions-item>
          <el-descriptions-item label="提交反馈的管理员姓名">
            {{ currentDetail.adminFeedbackAdminName }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 查看反馈内容对话框 -->
    <el-dialog
      v-model="contentDialogVisible"
      title="反馈内容"
      width="800px"
      :before-close="() => { contentDialogVisible = false }"
    >
      <template #header>
        <div class="dialog-header">
          <span class="el-dialog__title">反馈内容</span>
          <button class="el-dialog__headerbtn" @click="contentDialogVisible = false">
            <i class="el-dialog__close el-icon el-icon-close"></i>
          </button>
        </div>
      </template>
      <div class="feedback-content">
        <div class="content-display">{{ currentContent?.adminFeedbackContent }}</div>
      </div>
      
      <template #footer>
        <el-button @click="contentDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 处理反馈对话框 -->
    <el-dialog
      v-model="showProcessDialog"
      :title="`${processAction} - ${currentFeedback?.adminFeedbackTitle || ''}`"
      width="600px"
      @close="resetProcessForm"
    >
      <el-form
        ref="processFormRef"
        :model="processForm"
        :rules="processRules"
        label-width="100px"
      >
        <el-form-item 
          label="回复标题" 
          prop="title"
        >
          <el-input 
            v-model="processForm.title" 
            placeholder="请输入回复标题"
          />
        </el-form-item>
        
        <el-form-item 
          label="回复内容" 
          prop="content"
        >
          <div style="border: 1px solid #dcdfe6; border-radius: 4px;">
            <Toolbar
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              mode="default"
              style="border-bottom: 1px solid #dcdfe6"
            />
            <Editor
              v-model="processForm.content"
              :defaultConfig="editorConfig"
              mode="default"
              style="height: 300px;"
              @onCreated="handleEditorCreated"
            />
          </div>
          <div style="font-size: 12px; color: #909399; margin-top: 4px;">
            支持富文本格式，内容将以HTML格式发送
          </div>
        </el-form-item>

        <el-alert
          type="info"
          :closable="false"
          show-icon
        >
          <template #title>
            <span>{{ getOperationDesc(processForm.operationCode) }}</span>
          </template>
        </el-alert>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showProcessDialog = false">取消</el-button>
          <el-button type="primary" @click="handleProcessSubmit" :loading="submitting">
            确定{{ processAction }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 反馈详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="反馈详情"
      width="600px"
      :before-close="() => { detailDialogVisible = false }"
    >
      <template #header>
        <div class="dialog-header">
          <span class="el-dialog__title">反馈详情</span>
          <button class="el-dialog__headerbtn" @click="detailDialogVisible = false">
            <i class="el-dialog__close el-icon el-icon-close"></i>
          </button>
        </div>
      </template>
      <div class="feedback-detail" v-if="currentFeedback">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="反馈标题">
            {{ currentFeedback.adminFeedbackTitle }}
          </el-descriptions-item>
          <el-descriptions-item label="反馈内容">
            <div class="detail-content">
              {{ currentFeedback.adminFeedbackContent }}
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="反馈时间">
            {{ formatDateTime(currentFeedback.adminFeedbackTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="最近更新时间">
            {{ formatDateTime(currentFeedback.adminFeedbackRecentTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="当前状态">
            <el-tag :type="getStatusType(currentFeedback.adminFeedbackCurStep)">
              {{ currentFeedback.adminFeedbackCurStep }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onBeforeUnmount, shallowRef, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search, RefreshRight } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils'
import { feedbackApi } from '@/api/admin'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { IEditorConfig, IToolbarConfig } from '@wangeditor/editor'
import '@wangeditor/editor/dist/css/style.css'

// 格式化日期（只显示日期，不显示时间）
const formatDate = (dateStr: string | Date) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}
import type {
  AdminFeedbackPageVO,
  AdminFeedbackQuery,
  AdminFeedbackInfoVO,
  AdminUpdateFeedbackForm
} from '@/types/admin'
import type { FormInstance } from 'element-plus'
import { useAuthStore } from '@/store/auth'

const authStore = useAuthStore()
const canViewFeedbackDetail = computed(() => authStore.hasPermission('/admin/feedback/findFeedbackById'))
const canUpdateFeedbackStep = computed(() => authStore.hasPermission('/admin/feedback/updateFeedbackStep'))
const showActionColumn = computed(() => canViewFeedbackDetail.value || canUpdateFeedbackStep.value)

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showDetailDialog = ref(false)
const showProcessDialog = ref(false)
const detailDialogVisible = ref(false)
const contentDialogVisible = ref(false)
const currentDetail = ref<AdminFeedbackInfoVO | null>(null)
const currentFeedback = ref<AdminFeedbackPageVO | null>(null)
const currentContent = ref<AdminFeedbackPageVO | null>(null)
const processAction = ref('')

// 搜索表单
const searchForm = reactive<AdminFeedbackQuery>({
  adminFeedbackTitle: '',
  adminFeedbackContent: ''
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<AdminFeedbackPageVO[]>([])

// 处理表单
const processFormRef = ref<FormInstance>()
const processForm = reactive<AdminUpdateFeedbackForm>({
  operationCode: 0,
  title: '',
  content: ''
})

// 表单校验规则（动态计算）
const processRules = reactive({
  title: [
    { required: false, message: '请输入处理后的标题', trigger: 'blur' }
  ],
  content: [
    { required: false, message: '请输入处理内容', trigger: 'blur' }
  ]
})

// 富文本编辑器配置
const editorRef = shallowRef()
const editorConfig: Partial<IEditorConfig> = {
  placeholder: '请输入回复内容，支持富文本格式...',
  MENU_CONF: {}
}
const toolbarConfig: Partial<IToolbarConfig> = {
  toolbarKeys: [
    'headerSelect',
    'bold',
    'italic',
    'underline',
    'color',
    'bgColor',
    '|',
    'fontSize',
    'fontFamily',
    '|',
    'bulletedList',
    'numberedList',
    '|',
    'justifyLeft',
    'justifyCenter',
    'justifyRight',
    '|',
    'emotion',
    'insertLink',
    'insertImage',
    '|',
    'undo',
    'redo'
  ]
}

// 富文本编辑器创建回调
const handleEditorCreated = (editor: any) => {
  editorRef.value = editor
  console.log('📝 富文本编辑器创建成功')
}

// 富文本编辑器内容变化（自动绑定到v-model）

// 获取反馈列表
const getFeedbackList = async () => {
  loading.value = true
  try {
    const response = await feedbackApi.getFeedbackPage(
      pagination.current,
      pagination.size,
      searchForm
    )
    
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取反馈列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getFeedbackList()
}

// 重置搜索
const handleReset = () => {
  searchForm.adminFeedbackTitle = ''
  searchForm.adminFeedbackContent = ''
  pagination.current = 1
  getFeedbackList()
}

// 刷新数据
const refreshData = () => {
  getFeedbackList()
}

// 分页变更
const handleSizeChange = (size: number) => {
  pagination.size = size
  getFeedbackList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getFeedbackList()
}

// 查看详情
const handleDetail = async (row: AdminFeedbackPageVO) => {
  if (!canViewFeedbackDetail.value) {
    ElMessage.error('暂无查看权限')
    return
  }

  try {
    const response = await feedbackApi.getFeedbackDetail(row.adminFeedbackId)
    currentDetail.value = response.data
    showDetailDialog.value = true
  } catch (error) {
    console.error('获取反馈详情失败:', error)
    ElMessage.error('获取详情失败')
  }
}

// 查看反馈内容
const viewFeedbackContent = (row: AdminFeedbackPageVO) => {
  if (!canViewFeedbackDetail.value) {
    ElMessage.error('暂无查看权限')
    return
  }

  currentContent.value = row
  contentDialogVisible.value = true
}

// 处理反馈
const handleProcess = async (row: AdminFeedbackPageVO, operationCode: number, actionName: string) => {
  if (!canUpdateFeedbackStep.value) {
    ElMessage.error('暂无处理权限')
    return
  }

  try {
    // 操作码2（回复）需要弹出对话框填写内容
    if (operationCode === 2) {
      const response = await feedbackApi.getFeedbackDetail(row.adminFeedbackId)
      currentFeedback.value = row
      processAction.value = actionName
      processForm.operationCode = operationCode
      processForm.title = response.data.adminFeedbackTitle
      processForm.content = ''
      
      // 设置为必填
      processRules.title[0].required = true
      processRules.content[0].required = true
      
      showProcessDialog.value = true
      
      // 等待对话框打开后，清空富文本编辑器
      setTimeout(() => {
        if (editorRef.value) {
          editorRef.value.clear()
        }
      }, 100)
    } else {
      // 其他操作（接受0、忽视1、拒回复3）直接确认后执行
      await ElMessageBox.confirm(
        `确定要${actionName}这条反馈吗？`,
        '确认操作',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
      
      // 获取当前登录管理员ID
      const currentUser = authStore.user
      if (!currentUser?.adminId && !currentUser?.userId) {
        ElMessage.error('未获取到当前用户信息，请重新登录')
        return
      }
      
      const operationPersonId = currentUser.adminId || currentUser.userId
      
      console.log('📤 [反馈处理] 直接提交参数:', {
        feedbackId: row.adminFeedbackId,
        operationCode: operationCode,
        operationName: actionName,
        title: '',
        content: '',
        operationPersonId
      })
      
      // 直接调用API
      console.log('🚀 [反馈处理] 准备调用 updateFeedbackStep 接口...')
      const result = await feedbackApi.updateFeedbackStep(
        row.adminFeedbackId,
        operationCode,
        '',  // title为空
        '',  // content为空
        operationPersonId
      )
      console.log('✅ [反馈处理] 接口调用成功，返回结果:', result)
      
      ElMessage.success(`${actionName}成功`)
      console.log('🔄 [反馈处理] 刷新列表...')
      getFeedbackList()
    }
  } catch (error: any) {
    if (error === 'cancel') {
      // 用户取消操作
      return
    }
    console.error('❌ [反馈处理] 处理失败:', error)
    ElMessage.error(error?.message || '操作失败，请重试')
  }
}

// 获取操作说明
const getOperationDesc = (code: number) => {
  const descMap: Record<number, string> = {
    0: '接受反馈后，状态将变为"待回复"，并会发送短信通知',
    1: '忽视反馈后，状态将变为"忽视"',
    2: '回复反馈后，状态将变为"已回复"，并会发送短信通知',
    3: '拒绝回复后，状态将变为"拒回复"'
  }
  return descMap[code] || ''
}

// 获取状态类型
const getStatusType = (status: string) => {
  const typeMap: Record<string, string> = {
    '待接收': 'warning',
    '待接受': 'warning',  // 兼容两种写法
    '待回复': 'primary',
    '已回复': 'success',
    '忽视': 'info',
    '拒回复': 'danger'
  }
  return typeMap[status] || 'info'
}

// 提交处理
const handleProcessSubmit = async () => {
  if (!canUpdateFeedbackStep.value) {
    ElMessage.error('暂无处理权限')
    return
  }

  if (!processFormRef.value || !currentFeedback.value) return
  
  try {
    // 只有操作码2（回复）需要验证
    if (processForm.operationCode === 2) {
    await processFormRef.value.validate()
    }
    
    // 获取当前登录管理员ID
    const currentUser = authStore.user
    if (!currentUser?.adminId && !currentUser?.userId) {
      ElMessage.error('未获取到当前用户信息，请重新登录')
      return
    }
    
    const operationPersonId = currentUser.adminId || currentUser.userId
    
    submitting.value = true
    
    console.log('📤 [反馈处理] 提交参数:', {
      feedbackId: currentFeedback.value.adminFeedbackId,
      operationCode: processForm.operationCode,
      operationName: processAction.value,
      title: processForm.title || '',
      content: processForm.content || '',
      operationPersonId
    })
    
    await feedbackApi.updateFeedbackStep(
      currentFeedback.value.adminFeedbackId,
      processForm.operationCode,
      processForm.title || '',
      processForm.content || '',
      operationPersonId
    )
    
    ElMessage.success(`${processAction.value}成功`)
    showProcessDialog.value = false
    getFeedbackList()
  } catch (error: any) {
    console.error('❌ [反馈处理] 处理失败:', error)
    if (error?.errors) {
      // 表单验证失败
      return
    }
    ElMessage.error(error?.message || '处理失败，请检查网络或重试')
  } finally {
    submitting.value = false
  }
}

// 重置处理表单
const resetProcessForm = () => {
  if (processFormRef.value) {
    processFormRef.value.resetFields()
  }
  
  // 清空富文本编辑器内容
  if (editorRef.value) {
    editorRef.value.clear()
  }
  
  currentFeedback.value = null
  processAction.value = ''
  Object.assign(processForm, {
    operationCode: 0,
    title: '',
    content: ''
  })
}



// 组件挂载
onMounted(() => {
  getFeedbackList()
})

// 组件卸载时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor) {
    editor.destroy()
  }
})
</script>

<style scoped lang="scss">
.feedback-manage {
  font-size: 16px;

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 24px;
  }

  .header-content {
    .page-title {
      font-size: 24px;
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

  .header-actions {
    display: flex;
    gap: 12px;
  }

  .search-card {
    margin-bottom: 24px;
  }

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }

  .table-card {
    .el-table {
      font-size: 16px;
    }
    
    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
    }

    .table-title {
      font-size: 16px;
      font-weight: 600;
      color: #1f2937;
    }
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 24px;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
  }

  .feedback-detail {
    .detail-content {
      line-height: 1.6;
      color: #374151;
      white-space: pre-wrap;
    }
  }

  .feedback-content {
    .content-display {
      line-height: 1.6;
      color: #374151;
      white-space: pre-wrap;
      max-height: 400px;
      overflow-y: auto;
      padding: 12px;
      background-color: #f8f9fa;
      border-radius: 4px;
    }
  }

  .content-title {
    margin: 0 0 16px 0;
    font-size: 18px;
    font-weight: 600;
    color: #333;
    text-align: center;
  }

  .content-display {
    max-height: 300px;
    overflow-y: auto;
    padding: 12px;
    background: #f9fafb;
    border-radius: 6px;
    line-height: 1.6;
    white-space: pre-wrap;
    word-break: break-word;
  }

  .dialog-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .el-dialog__headerbtn {
    background: transparent;
    border: none;
    outline: none;
    cursor: pointer;
    padding: 0;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    transition: background-color 0.2s;
  }

  .el-dialog__headerbtn:hover {
    background-color: #f0f0f0;
  }

  .el-dialog__close {
    font-size: 16px;
    color: #909399;
  }

  .el-dialog__close:hover {
    color: #409eff;
  }

  .dialog-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .el-dialog__headerbtn {
    background: transparent;
    border: none;
    outline: none;
    cursor: pointer;
    padding: 0;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 50%;
    transition: background-color 0.2s;
  }

  .el-dialog__headerbtn:hover {
    background-color: #f0f0f0;
  }

  .el-dialog__close {
    font-size: 16px;
    color: #909399;
  }

  .el-dialog__close:hover {
    color: #409eff;
  }

  .content-ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: help;
  }
}

// 富文本编辑器样式
:deep(.w-e-text-container) {
  background-color: #fff;
}

:deep(.w-e-toolbar) {
  background-color: #f8f9fa !important;
  border-radius: 4px 4px 0 0;
}

:deep(.w-e-text-placeholder) {
  font-style: normal;
  color: #c0c4cc;
}

// 响应式设计
@media (max-width: 768px) {
  .feedback-manage {
    .page-header {
      flex-direction: column;
      gap: 16px;
    }

    .header-actions {
      width: 100%;
      justify-content: flex-start;
    }

    .search-form {
      .el-form-item {
        width: 100%;
        margin-bottom: 16px;
      }
    }
  }
}
</style>