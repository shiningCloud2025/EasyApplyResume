<template>
  <div class="job-advice-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">求职攻略文章管理</h1>
        <p class="page-description">管理求职攻略文章内容，提供求职指导信息</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openCreateDialog">
          <i class="el-icon-plus"></i>
          新增文章
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
        <el-form-item label="文章标题">
          <el-input
            v-model="searchForm.jobAdviceArticleTitle"
            placeholder="请输入文章标题"
            clearable
            style="width: 250px"
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.jobAdviceArticleCategory" placeholder="请选择" clearable style="width: 150px">
            <el-option label="简历技巧" value="resume" />
            <el-option label="面试指南" value="interview" />
            <el-option label="职业规划" value="career" />
            <el-option label="行业分析" value="industry" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.jobAdviceArticleState" placeholder="请选择" clearable style="width: 120px">
            <el-option label="已发布" :value="1" />
            <el-option label="草稿" :value="0" />
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

    <!-- 文章列表 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column prop="jobAdviceArticleId" label="ID" width="80" />
        <el-table-column prop="jobAdviceArticleTitle" label="文章标题" min-width="250">
          <template #default="{ row }">
            <div class="article-title">
              <h4>{{ row.jobAdviceArticleTitle }}</h4>
              <p class="article-summary">{{ row.jobAdviceArticleContent?.substring(0, 100) }}...</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="jobAdviceArticleCategory" label="分类" width="120">
          <template #default="{ row }">
            <el-tag :type="getCategoryTagType(row.jobAdviceArticleCategory)">
              {{ getCategoryLabel(row.jobAdviceArticleCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="jobAdviceArticleTag" label="标签" width="150">
          <template #default="{ row }">
            <el-tag
              v-for="tag in row.jobAdviceArticleTag?.split(',')"
              :key="tag"
              size="small"
              style="margin-right: 4px; margin-bottom: 4px;"
            >
              {{ tag }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="jobAdviceArticleState" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.jobAdviceArticleState === 1 ? 'success' : 'info'">
              {{ row.jobAdviceArticleState === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="jobAdviceArticleCreatedTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.jobAdviceArticleCreatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="handlePreview(row)">
              <i class="el-icon-view"></i>
              预览
            </el-button>
            <el-button type="text" size="small" @click="handleEdit(row)">
              <i class="el-icon-edit"></i>
              编辑
            </el-button>
            <el-button type="text" size="small" class="danger" @click="handleDelete(row)">
              <i class="el-icon-delete"></i>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        class="pagination"
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 创建/编辑文章对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingArticle ? '编辑文章' : '新增文章'"
      width="900px"
      @close="resetForm"
    >
      <el-form
        ref="articleFormRef"
        :model="articleForm"
        :rules="articleRules"
        label-width="100px"
      >
        <el-form-item label="文章标题" prop="jobAdviceArticleTitle">
          <el-input v-model="articleForm.jobAdviceArticleTitle" placeholder="请输入文章标题" />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="文章分类" prop="jobAdviceArticleCategory">
              <el-select v-model="articleForm.jobAdviceArticleCategory" placeholder="请选择分类">
                <el-option label="简历技巧" value="resume" />
                <el-option label="面试指南" value="interview" />
                <el-option label="职业规划" value="career" />
                <el-option label="行业分析" value="industry" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="文章标签" prop="jobAdviceArticleTag">
              <el-input v-model="articleForm.jobAdviceArticleTag" placeholder="多个标签用逗号分隔" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="文章状态" prop="jobAdviceArticleState">
          <el-radio-group v-model="articleForm.jobAdviceArticleState">
            <el-radio :label="1">发布</el-radio>
            <el-radio :label="0">草稿</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="文章内容" prop="jobAdviceArticleContent">
          <el-input
            v-model="articleForm.jobAdviceArticleContent"
            type="textarea"
            :rows="15"
            placeholder="请输入文章内容"
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

    <!-- 预览对话框 -->
    <el-dialog
      v-model="showPreviewDialog"
      title="文章预览"
      width="800px"
    >
      <div class="preview-container" v-if="currentPreviewArticle">
        <div class="preview-header">
          <h2>{{ currentPreviewArticle.jobAdviceArticleTitle }}</h2>
          <div class="preview-meta">
            <el-tag :type="getCategoryTagType(currentPreviewArticle.jobAdviceArticleCategory)">
              {{ getCategoryLabel(currentPreviewArticle.jobAdviceArticleCategory) }}
            </el-tag>
            <span class="create-time">{{ formatDateTime(currentPreviewArticle.jobAdviceArticleCreatedTime) }}</span>
          </div>
        </div>
        <div class="preview-content">
          <div v-html="formatContent(currentPreviewArticle.jobAdviceArticleContent)" class="article-content"></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDateTime } from '@/utils'
import { jobAdviceArticleApi } from '@/api/admin'
import type {
  JobAdviceArticleForm,
  JobAdviceArticleQuery
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

// 声明文章类型
interface JobAdviceArticleVO {
  jobAdviceArticleId: number
  jobAdviceArticleTitle: string
  jobAdviceArticleContent: string
  jobAdviceArticleCategory: string
  jobAdviceArticleTag: string
  jobAdviceArticleState: number
  jobAdviceArticleCreatedTime: string
}

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const showPreviewDialog = ref(false)
const editingArticle = ref<JobAdviceArticleVO | null>(null)
const currentPreviewArticle = ref<JobAdviceArticleVO | null>(null)

// 搜索表单
const searchForm = reactive<JobAdviceArticleQuery>({
  jobAdviceArticleTitle: '',
  jobAdviceArticleCategory: '',
  jobAdviceArticleState: undefined
})

// 分页
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<JobAdviceArticleVO[]>([])

// 文章表单
const articleFormRef = ref<FormInstance>()
const articleForm = reactive<JobAdviceArticleForm>({
  jobAdviceArticleId: undefined,
  jobAdviceArticleTitle: '',
  jobAdviceArticleContent: '',
  jobAdviceArticleCategory: '',
  jobAdviceArticleTag: '',
  jobAdviceArticleState: 1
})

// 表单校验规则
const articleRules = {
  jobAdviceArticleTitle: [
    { required: true, message: '请输入文章标题', trigger: 'blur' },
    { min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  jobAdviceArticleCategory: [
    { required: true, message: '请选择文章分类', trigger: 'change' }
  ],
  jobAdviceArticleContent: [
    { required: true, message: '请输入文章内容', trigger: 'blur' },
    { min: 50, message: '文章内容至少50个字符', trigger: 'blur' }
  ]
}

// 获取文章列表
const getArticleList = async () => {
  loading.value = true
  try {
    const response = await jobAdviceArticleApi.getJobAdviceArticlePage(
      pagination.size,
      pagination.page,
      searchForm
    )
    
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取文章列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 新增文章
const openCreateDialog = () => {
  editingArticle.value = null
  resetForm()
  showCreateDialog.value = true
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  getArticleList()
}

// 重置搜索
const handleReset = () => {
  searchForm.jobAdviceArticleTitle = ''
  searchForm.jobAdviceArticleCategory = ''
  searchForm.jobAdviceArticleState = undefined
  pagination.page = 1
  getArticleList()
}

// 刷新数据
const refreshData = () => {
  getArticleList()
}

// 分页变更
const handleSizeChange = (size: number) => {
  pagination.size = size
  getArticleList()
}

const handleCurrentChange = (page: number) => {
  pagination.page = page
  getArticleList()
}

// 编辑文章
const handleEdit = (row: JobAdviceArticleVO) => {
  editingArticle.value = row
  Object.assign(articleForm, {
    jobAdviceArticleId: row.jobAdviceArticleId,
    jobAdviceArticleTitle: row.jobAdviceArticleTitle,
    jobAdviceArticleContent: row.jobAdviceArticleContent,
    jobAdviceArticleCategory: row.jobAdviceArticleCategory,
    jobAdviceArticleTag: row.jobAdviceArticleTag,
    jobAdviceArticleState: row.jobAdviceArticleState
  })
  showCreateDialog.value = true
}

// 预览文章
const handlePreview = (row: JobAdviceArticleVO) => {
  currentPreviewArticle.value = row
  showPreviewDialog.value = true
}

// 删除文章
const handleDelete = (row: JobAdviceArticleVO) => {
  ElMessageBox.confirm(`确定要删除文章"${row.jobAdviceArticleTitle}"吗？`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await jobAdviceArticleApi.deleteJobAdviceArticle(row.jobAdviceArticleId)
      ElMessage.success('删除成功')
      getArticleList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 获取分类标签类型
const getCategoryTagType = (category: string) => {
  const typeMap: Record<string, string> = {
    'resume': 'primary',
    'interview': 'success',
    'career': 'warning',
    'industry': 'info'
  }
  return typeMap[category] || 'info'
}

// 获取分类标签文本
const getCategoryLabel = (category: string) => {
  const labelMap: Record<string, string> = {
    'resume': '简历技巧',
    'interview': '面试指南',
    'career': '职业规划',
    'industry': '行业分析'
  }
  return labelMap[category] || category
}

// 格式化内容
const formatContent = (content: string) => {
  return content.replace(/\n/g, '<br>')
}

// 提交表单
const handleSubmit = async () => {
  if (!articleFormRef.value) return
  
  try {
    await articleFormRef.value.validate()
    submitting.value = true
    
    if (editingArticle.value) {
      await jobAdviceArticleApi.updateJobAdviceArticle(articleForm)
    } else {
      await jobAdviceArticleApi.addJobAdviceArticle(articleForm)
    }
    
    ElMessage.success(editingArticle.value ? '更新成功' : '创建成功')
    showCreateDialog.value = false
    getArticleList()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (articleFormRef.value) {
    articleFormRef.value.resetFields()
  }
  
  editingArticle.value = null
  Object.assign(articleForm, {
    jobAdviceArticleId: undefined,
    jobAdviceArticleTitle: '',
    jobAdviceArticleContent: '',
    jobAdviceArticleCategory: '',
    jobAdviceArticleTag: '',
    jobAdviceArticleState: 1
  })
}

// 组件挂载
onMounted(() => {
  getArticleList()
})
</script>

<style scoped lang="scss">
.job-advice-management {
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

.table-card {
  margin-bottom: 24px;
}

.article-title {
  h4 {
    margin: 0 0 4px 0;
    font-size: 14px;
    font-weight: 600;
    color: #1f2937;
  }
  
  .article-summary {
    margin: 0;
    color: #6b7280;
    font-size: 12px;
    line-height: 1.4;
  }
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

.preview-container {
  .preview-header {
    margin-bottom: 20px;
    padding-bottom: 16px;
    border-bottom: 1px solid #e5e7eb;
    
    h2 {
      margin: 0 0 8px 0;
      color: #1f2937;
    }
    
    .preview-meta {
      display: flex;
      align-items: center;
      gap: 12px;
      
      .create-time {
        color: #6b7280;
        font-size: 14px;
      }
    }
  }
  
  .preview-content {
    max-height: 500px;
    overflow-y: auto;
    padding: 20px;
    background: white;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    
    .article-content {
      line-height: 1.6;
      color: #374151;
    }
  }
}

.danger {
  color: #ef4444;
  
  &:hover {
    color: #dc2626;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .job-advice-management {
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

@media (max-width: 480px) {
  .page-title {
    font-size: 24px;
  }
  
  .page-description {
    font-size: 14px;
  }
  
  .table-card {
    margin: 0 -16px 24px -16px;
    border-radius: 0;
  }
}
</style>