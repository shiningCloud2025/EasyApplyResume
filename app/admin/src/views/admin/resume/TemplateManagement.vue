<template>
  <div class="template-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">简历模板管理</h1>
        <p class="page-description">管理简历模板，为用户提供多样化的简历样式</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openCreateDialog">
          <el-icon><Plus /></el-icon>
          新增模板
        </el-button>
        <el-button @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="模板名称">
          <el-input
            v-model="searchForm.resumeTemplateName"
            placeholder="请输入模板名称"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="行业">
          <el-select 
            v-model="searchForm.resumeTemplateIndustry" 
            placeholder="请选择行业" 
            clearable 
            style="width: 180px"
            filterable
          >
            <el-option
              v-for="industry in industries"
              :key="industry.industryMapIndustryCode"
              :label="industry.industryMapIndustryName"
              :value="industry.industryMapIndustryCode"
            />
          </el-select>
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

    <!-- 模板列表 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column prop="resumeTemplateId" label="ID" width="70" />
        <el-table-column prop="resumeTemplateName" label="模板名称" min-width="200" />
        <el-table-column prop="industryMapIndustryName" label="所属行业" width="120" />
        <el-table-column label="模板代码" width="100" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" link @click="handleViewCode(row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="是否启用" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.resumeTemplateIsActive === 1 ? 'success' : 'danger'">
              {{ row.resumeTemplateIsActive === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="resumeTemplateCreatedTime" label="创建时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.resumeTemplateCreatedTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="resumeTemplateUpdatedTime" label="更新时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.resumeTemplateUpdatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="info" size="default" @click="handleView(row)">
              查看
            </el-button>
            <el-button type="primary" size="default" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" size="default" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        class="pagination"
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 创建/编辑模板对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingTemplate ? '编辑模板' : '新增模板'"
      width="900px"
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
            <el-form-item label="模板名称" prop="resumeTemplateName">
              <el-input v-model="templateForm.resumeTemplateName" placeholder="请输入模板名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属行业" prop="resumeTemplateIndustry">
              <el-select 
                v-model="templateForm.resumeTemplateIndustry" 
                placeholder="请选择行业"
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="industry in industries"
                  :key="industry.industryMapIndustryCode"
                  :label="industry.industryMapIndustryName"
                  :value="industry.industryMapIndustryCode"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="是否启用" prop="resumeTemplateIsActive">
          <el-radio-group v-model="templateForm.resumeTemplateIsActive">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="React代码" prop="resumeTemplateReactCode">
          <el-input
            v-model="templateForm.resumeTemplateReactCode"
            type="textarea"
            :rows="18"
            placeholder="请输入React模板代码"
            style="font-family: 'Consolas', 'Monaco', 'Courier New', monospace;"
          />
        </el-form-item>
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

    <!-- 查看模板详情对话框 -->
    <el-dialog
      v-model="showViewDialog"
      title="模板详情"
      width="700px"
    >
      <div class="template-detail" v-if="currentViewTemplate">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="模板ID">
            {{ currentViewTemplate.resumeTemplateId }}
          </el-descriptions-item>
          <el-descriptions-item label="模板名称">
            {{ currentViewTemplate.resumeTemplateName }}
          </el-descriptions-item>
          <el-descriptions-item label="所属行业">
            {{ currentViewTemplate.industryMapIndustryName }}
          </el-descriptions-item>
          <el-descriptions-item label="是否启用">
            <el-tag :type="currentViewTemplate.isEnable === 1 ? 'success' : 'danger'">
              {{ currentViewTemplate.isEnable === 1 ? '启用' : '禁用' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(currentViewTemplate.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDate(currentViewTemplate.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showViewDialog = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看代码详情对话框 -->
    <el-dialog
      v-model="showCodeDialog"
      title="模板代码详情"
      width="900px"
    >
      <div class="code-detail" v-if="currentCodeTemplate">
        <div class="code-header">
          <span class="code-title">{{ currentCodeTemplate.resumeTemplateName }}</span>
          <el-tag size="small" type="info">{{ currentCodeTemplate.industryMapIndustryName }}</el-tag>
        </div>
        <div class="code-preview">
          <pre><code>{{ currentCodeTemplate.resumeTemplateReactCode || '暂无代码' }}</code></pre>
        </div>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showCodeDialog = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search, RefreshRight } from '@element-plus/icons-vue'
import { resumeTemplateApi, industryMapApi } from '@/api/admin'
import dayjs from 'dayjs'
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
const showViewDialog = ref(false)
const showCodeDialog = ref(false)
const editingTemplate = ref<ResumeTemplatePageVO | null>(null)
const currentViewTemplate = ref<ResumeTemplateInfoVO | null>(null)
const currentCodeTemplate = ref<ResumeTemplateInfoVO | null>(null)

// 搜索表单
const searchForm = reactive<ResumeTemplateQuery>({
  resumeTemplateName: '',
  resumeTemplateIndustry: undefined
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<ResumeTemplatePageVO[]>([])

// 行业数据
const industries = ref<any[]>([])

// 模板表单
const templateFormRef = ref<FormInstance>()
const templateForm = reactive<ResumeTemplateForm>({
  resumeTemplateId: undefined,
  resumeTemplateName: '',
  resumeTemplateReactCode: '',
  resumeTemplateIndustry: undefined,
  resumeTemplateIsActive: 1
})

// 表单校验规则
const templateRules = {
  resumeTemplateName: [
    { required: true, message: '请输入模板名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  resumeTemplateIndustry: [
    { required: true, message: '请选择所属行业', trigger: 'change' }
  ],
  resumeTemplateReactCode: [
    { required: true, message: '请输入React模板代码', trigger: 'blur' }
  ]
}

// 格式化日期（只显示日期，不显示时分秒）
const formatDate = (dateStr: string | undefined) => {
  if (!dateStr) return '-'
  return dayjs(dateStr).format('YYYY-MM-DD')
}

// 格式化日期时间（显示日期和时间）
const formatDateTime = (dateStr: string | undefined) => {
  if (!dateStr) return '-'
  return dateStr.replace('T', ' ').substring(0, 16)
}

// 加载行业数据
const loadIndustries = async () => {
  try {
    const response = await industryMapApi.findAllIndustryMap()
    industries.value = response.data
  } catch (error) {
    console.error('加载行业数据失败:', error)
  }
}

// 获取模板列表
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
    console.error('获取模板列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 新增模板
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
  searchForm.resumeTemplateIndustry = undefined
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

// 查看模板详情（不含代码）
const handleView = async (row: ResumeTemplatePageVO) => {
  try {
    const response = await resumeTemplateApi.getResumeTemplateInfo(row.resumeTemplateId)
    currentViewTemplate.value = response.data
    showViewDialog.value = true
  } catch (error) {
    console.error('获取模板详情失败:', error)
    ElMessage.error('获取模板详情失败')
  }
}

// 查看代码详情
const handleViewCode = async (row: ResumeTemplatePageVO) => {
  try {
    const response = await resumeTemplateApi.getResumeTemplateInfo(row.resumeTemplateId)
    currentCodeTemplate.value = response.data
    showCodeDialog.value = true
  } catch (error) {
    console.error('获取模板代码失败:', error)
    ElMessage.error('获取模板代码失败')
  }
}

// 编辑模板
const handleEdit = async (row: ResumeTemplatePageVO) => {
  try {
    const response = await resumeTemplateApi.getResumeTemplateInfo(row.resumeTemplateId)
    const detail = response.data
    
    editingTemplate.value = row
    
    // 根据行业名称反查行业ID
    const industry = industries.value.find(ind => ind.industryMapIndustryName === detail.industryMapIndustryName)
    
    Object.assign(templateForm, {
      resumeTemplateId: detail.resumeTemplateId,
      resumeTemplateName: detail.resumeTemplateName,
      resumeTemplateReactCode: detail.resumeTemplateReactCode,
      resumeTemplateIndustry: industry ? industry.industryMapIndustryCode : undefined,
      resumeTemplateIsActive: detail.isEnable
    })
    showCreateDialog.value = true
  } catch (error) {
    console.error('获取模板详情失败:', error)
    ElMessage.error('获取模板详情失败')
  }
}

// 删除模板
const handleDelete = (row: ResumeTemplatePageVO) => {
  ElMessageBox.confirm(`确定要删除模板"${row.resumeTemplateName}"吗？`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await resumeTemplateApi.deleteResumeTemplate(row.resumeTemplateId)
      ElMessage.success('删除成功')
      getTemplateList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
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
  Object.assign(templateForm, {
    resumeTemplateId: undefined,
    resumeTemplateName: '',
    resumeTemplateReactCode: '',
    resumeTemplateIndustry: undefined,
    resumeTemplateIsActive: 1
  })
}

// 组件挂载
onMounted(async () => {
  await loadIndustries()
  getTemplateList()
})
</script>

<style scoped lang="scss">
.template-management {
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
  }

  .code-preview-cell {
    font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
    font-size: 13px;
    color: #555;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: help;
    background: #f5f5f5;
    padding: 4px 8px;
    border-radius: 4px;
  }

  .pagination {
    display: flex;
    justify-content: flex-end;
    margin-top: 24px;
    padding-top: 16px;
    border-top: 1px solid #f3f4f6;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
  }

  .template-detail {
    // 模板详情样式
  }

  .code-detail {
    .code-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      padding-bottom: 12px;
      border-bottom: 1px solid #e5e7eb;
      
      .code-title {
        font-size: 16px;
        font-weight: 600;
        color: #1f2937;
      }
    }
    
    .code-preview {
      max-height: 500px;
      overflow-y: auto;
      background: #1e1e1e;
      border-radius: 8px;
      padding: 20px;
      
      pre {
        margin: 0;
        white-space: pre-wrap;
        word-wrap: break-word;
        
        code {
          font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
          font-size: 13px;
          line-height: 1.6;
          color: #d4d4d4;
        }
      }
    }
  }

  .danger {
    color: #ef4444;
    
    &:hover {
      color: #dc2626;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .template-management {
    .page-header {
      flex-direction: column;
      gap: 16px;
    }
    
    .header-actions {
      width: 100%;
      justify-content: flex-start;
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
    
    .el-table {
      font-size: 14px;
    }
  }
}
</style>
