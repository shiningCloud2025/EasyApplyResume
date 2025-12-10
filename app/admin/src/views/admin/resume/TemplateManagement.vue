<template>
  <div class="template-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">简历模版管理</h1>
        <p class="page-description">管理简历模版，为用户提供多样化的简历样式</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openCreateDialog">
          <i class="el-icon-plus"></i>
          新增模版
        </el-button>
        <el-button @click="refreshData">
          <i class="el-icon-refresh"></i>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="模版名称">
          <el-input
            v-model="searchForm.resumeTemplateName"
            placeholder="请输入模版名称"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.resumeTemplateState" placeholder="请选择" clearable style="width: 180px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <i class="el-icon-refresh"></i>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 模版网格展示 -->
    <el-card class="templates-card">
      <div class="templates-grid">
        <div 
          v-for="template in tableData" 
          :key="template.resumeTemplateId"
          class="template-card"
        >
          <div class="template-preview">
            <img :src="template.thumbnail || '/default-template.jpg'" :alt="template.resumeTemplateName" />
            <div class="template-overlay">
              <div class="overlay-actions">
                <el-button type="text" size="small" @click="handlePreview(template)">
                  <i class="el-icon-view"></i>
                  预览
                </el-button>
                <el-button type="text" size="small" @click="handleEdit(template)">
                  <i class="el-icon-edit"></i>
                  编辑
                </el-button>
              </div>
            </div>
          </div>
          <div class="template-info">
            <h4 class="template-name">{{ template.resumeTemplateName }}</h4>
            <div class="template-meta">
              <el-tag :type="template.resumeTemplateState === 1 ? 'success' : 'danger'" size="small">
                {{ template.resumeTemplateState === 1 ? '启用' : '禁用' }}
              </el-tag>
            </div>
            <div class="template-stats">
              <span class="price">￥{{ template.resumeTemplatePrice }}</span>
              <span class="create-time">{{ formatDate(template.resumeTemplateCreatedTime) }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <el-pagination
        class="pagination"
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[12, 24, 48, 96]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 创建/编辑模版对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingTemplate ? '编辑模版' : '新增模版'"
      width="800px"
      @close="resetForm"
    >
      <el-form
        ref="templateFormRef"
        :model="templateForm"
        :rules="templateRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模版名称" prop="resumeTemplateName">
              <el-input v-model="templateForm.resumeTemplateName" placeholder="请输入模版名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模版价格" prop="resumeTemplatePrice">
              <el-input-number 
                v-model="templateForm.resumeTemplatePrice" 
                :min="0" 
                :precision="2"
                placeholder="请输入价格"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="模版描述" prop="resumeTemplateDescribe">
          <el-input
            v-model="templateForm.resumeTemplateDescribe"
            type="textarea"
            :rows="3"
            placeholder="请输入模版描述"
          />
        </el-form-item>
        
        <el-form-item label="状态" prop="resumeTemplateState">
          <el-radio-group v-model="templateForm.resumeTemplateState">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-tabs v-model="activeTab" class="template-tabs">
          <el-tab-pane label="HTML模版" name="html">
            <el-form-item label="HTML代码">
              <el-input
                v-model="templateForm.resumeTemplateHtml"
                type="textarea"
                :rows="12"
                placeholder="请输入HTML模版代码"
              />
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="CSS样式" name="css">
            <el-form-item label="CSS代码">
              <el-input
                v-model="templateForm.resumeTemplateCss"
                type="textarea"
                :rows="12"
                placeholder="请输入CSS样式代码"
              />
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showCreateDialog = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 预览对话框 -->
    <el-dialog
      v-model="showPreviewDialog"
      title="模版预览"
      width="900px"
    >
      <div class="preview-container" v-if="currentPreviewTemplate">
        <div class="preview-header">
          <h3>{{ currentPreviewTemplate.resumeTemplateName }}</h3>
          <p>{{ currentPreviewTemplate.resumeTemplateDescribe }}</p>
        </div>
        <div class="preview-content">
          <div v-html="currentPreviewTemplate.resumeTemplateHtml" class="preview-html"></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { resumeTemplateApi } from '@/api/admin'
import type {
  ResumeTemplatePageVO,
  ResumeTemplateForm,
  ResumeTemplateQuery,
  ResumeTemplateInfoVO
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const showPreviewDialog = ref(false)
const editingTemplate = ref<ResumeTemplatePageVO | null>(null)
const currentPreviewTemplate = ref<ResumeTemplatePageVO | null>(null)
const activeTab = ref('html')

// 搜索表单
const searchForm = reactive<ResumeTemplateQuery>({
  resumeTemplateName: '',
  resumeTemplateState: undefined
})

// 分页
const pagination = reactive({
  current: 1,
  size: 12,
  total: 0
})

// 表格数据
const tableData = ref<ResumeTemplatePageVO[]>([])

// 模版表单
const templateFormRef = ref<FormInstance>()
const templateForm = reactive<ResumeTemplateForm>({
  resumeTemplateId: undefined,
  resumeTemplateName: '',
  resumeTemplateDescribe: '',
  resumeTemplateHtml: '',
  resumeTemplateCss: '',
  resumeTemplatePrice: 0,
  resumeTemplateState: 1
})

// 表单校验规则
const templateRules = {
  resumeTemplateName: [
    { required: true, message: '请输入模版名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  resumeTemplateDescribe: [
    { required: true, message: '请输入模版描述', trigger: 'blur' },
    { min: 10, max: 200, message: '长度在 10 到 200 个字符', trigger: 'blur' }
  ],
  resumeTemplateHtml: [
    { required: true, message: '请输入HTML模版代码', trigger: 'blur' }
  ],
  resumeTemplateCss: [
    { required: true, message: '请输入CSS样式代码', trigger: 'blur' }
  ]
}

// 获取模版列表
const getTemplateList = async () => {
  loading.value = true
  try {
    const response = await resumeTemplateApi.getResumeTemplatePage(
      pagination.current,
      pagination.size,
      searchForm
    )
    
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取模版列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 新增模版
const openCreateDialog = () => {
  editingTemplate.value = null
  resetForm()
  showCreateDialog.value = true
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getTemplateList()
}

// 重置搜索
const handleReset = () => {
  searchForm.resumeTemplateName = ''
  searchForm.resumeTemplateState = undefined
  pagination.current = 1
  getTemplateList()
}

// 刷新数据
const refreshData = () => {
  getTemplateList()
}

// 分页变更
const handleSizeChange = (size: number) => {
  pagination.size = size
  getTemplateList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getTemplateList()
}

// 编辑模版
const handleEdit = (row: ResumeTemplatePageVO) => {
  editingTemplate.value = row
  Object.assign(templateForm, {
    resumeTemplateId: row.resumeTemplateId,
    resumeTemplateName: row.resumeTemplateName,
    resumeTemplateDescribe: row.resumeTemplateDescribe,
    resumeTemplateHtml: '', // HTML和CSS需要单独加载
    resumeTemplateCss: '',
    resumeTemplatePrice: row.resumeTemplatePrice,
    resumeTemplateState: row.resumeTemplateState
  })
  
  // 加载详细的模版信息
  loadTemplateDetail(row.resumeTemplateId)
  showCreateDialog.value = true
}

// 加载模版详情
const loadTemplateDetail = async (templateId: number) => {
  try {
    const response = await resumeTemplateApi.getResumeTemplateInfo(templateId)
    const detail = response.data
    templateForm.resumeTemplateHtml = detail.resumeTemplateHtml
    templateForm.resumeTemplateCss = detail.resumeTemplateCss
  } catch (error) {
    console.error('加载模版详情失败:', error)
  }
}

// 预览模版
const handlePreview = async (row: ResumeTemplatePageVO) => {
  currentPreviewTemplate.value = row
  
  // 如果没有详细信息，则加载
  if (!row.resumeTemplateHtml) {
    try {
      const response = await resumeTemplateApi.getResumeTemplateInfo(row.resumeTemplateId)
      currentPreviewTemplate.value = { ...row, ...response.data }
    } catch (error) {
      console.error('加载模版详情失败:', error)
      ElMessage.error('加载模版详情失败')
      return
    }
  }
  
  showPreviewDialog.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!templateFormRef.value) return
  
  try {
    await templateFormRef.value.validate()
    submitting.value = true
    
    if (editingTemplate.value) {
      await resumeTemplateApi.updateResumeTemplate(templateForm)
    } else {
      await resumeTemplateApi.addResumeTemplate(templateForm)
    }
    
    ElMessage.success(editingTemplate.value ? '更新成功' : '创建成功')
    showCreateDialog.value = false
    getTemplateList()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (templateFormRef.value) {
    templateFormRef.value.resetFields()
  }
  
  editingTemplate.value = null
  activeTab.value = 'html'
  Object.assign(templateForm, {
    resumeTemplateId: undefined,
    resumeTemplateName: '',
    resumeTemplateDescribe: '',
    resumeTemplateHtml: '',
    resumeTemplateCss: '',
    resumeTemplatePrice: 0,
    resumeTemplateState: 1
  })
}

// 格式化日期
const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString()
}

// 组件挂载
onMounted(() => {
  getTemplateList()
})
</script>

<style scoped lang="scss">
.template-management {
  font-size: 16px;
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #e5e7eb;
}

.header-content {
  flex: 1;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
  margin-bottom: 8px;
}

.page-description {
  font-size: 16px;
  color: #6b7280;
  margin: 0;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.search-card {
  margin-bottom: 24px;
}

.templates-card {
  .templates-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
    margin-bottom: 24px;
  }
  
  .template-card {
    border: 1px solid #e5e7eb;
    border-radius: 12px;
    overflow: hidden;
    transition: all 0.3s ease;
    
    &:hover {
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
      transform: translateY(-2px);
    }
  }
  
  .template-preview {
    position: relative;
    height: 200px;
    background: #f9fafb;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
    
    .template-overlay {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.5);
      display: flex;
      align-items: center;
      justify-content: center;
      opacity: 0;
      transition: opacity 0.3s ease;
      
      &:hover {
        opacity: 1;
      }
      
      .overlay-actions {
        display: flex;
        gap: 12px;
        
        .el-button {
          color: white;
          background: rgba(255, 255, 255, 0.2);
          border: 1px solid rgba(255, 255, 255, 0.3);
          
          &:hover {
            background: rgba(255, 255, 255, 0.3);
          }
        }
      }
    }
  }
  
  .template-info {
    padding: 16px;
    
    .template-name {
      font-size: 16px;
      font-weight: 600;
      color: #1f2937;
      margin: 0 0 8px 0;
    }
    
    .template-meta {
      margin-bottom: 12px;
      
      .el-tag {
        margin-right: 8px;
      }
    }
    
    .template-stats {
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: #6b7280;
      font-size: 14px;
      
      .price {
        color: #ef4444;
        font-weight: 600;
      }
      
      .create-time {
        font-size: 12px;
      }
    }
  }
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #f3f4f6;
}

.template-tabs {
  margin-top: 16px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.preview-container {
  .preview-header {
    margin-bottom: 20px;
    padding-bottom: 16px;
    border-bottom: 1px solid #e5e7eb;
    
    h3 {
      margin: 0 0 8px 0;
      color: #1f2937;
    }
    
    p {
      margin: 0;
      color: #6b7280;
    }
  }
  
  .preview-content {
    max-height: 500px;
    overflow-y: auto;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    padding: 20px;
    background: white;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .template-management {
    padding: 16px;
  }
  
  .page-header {
    flex-direction: column;
    gap: 16px;
  }
  
  .header-actions {
    width: 100%;
    justify-content: flex-start;
  }
  
  .templates-card .templates-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .search-card .el-form {
    .el-form-item {
      display: block;
      margin-bottom: 16px;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .el-input,
      .el-select {
        width: 100%;
      }
    }
  }
}

@media (max-width: 480px) {
  .page-title {
    font-size: 24px;
  }
  
  .page-description {
    font-size: 14px;
  }
  
  .templates-card {
    margin: 0 -16px 24px -16px;
    border-radius: 0;
  }
}
</style>