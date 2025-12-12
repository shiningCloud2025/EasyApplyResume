<template>
  <div class="job-advice-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">求职攻略文章管理</h1>
        <p class="page-description">管理求职攻略文章内容，提供求职指导信息</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openCreateDialog">
          <el-icon><Plus /></el-icon>
          新增文章
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
        <el-form-item label="文章标题">
          <el-input
            v-model="searchForm.jobAdviceArticleTitle"
            placeholder="请输入文章标题"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-input
            v-model="searchForm.jobAdviceArticleCategory"
            placeholder="请输入文章分类"
            clearable
            style="width: 160px"
          />
        </el-form-item>
        <el-form-item label="标签">
          <el-input
            v-model="searchForm.jobAdviceArticleTags"
            placeholder="请输入文章标签"
            clearable
            style="width: 160px"
          />
        </el-form-item>
        <el-form-item label="作者">
          <el-input
            v-model="searchForm.jobAdviceArticleAuthorName"
            placeholder="请输入作者名称"
            clearable
            style="width: 160px"
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

    <!-- 文章列表 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无数据"
        :default-sort="{ prop: 'jobAdviceArticleUpdatedTime', order: 'descending' }"
      >
        <el-table-column prop="jobAdviceArticleId" label="ID" width="60" />
        <el-table-column prop="jobAdviceArticleTitle" label="文章标题" width="245" show-overflow-tooltip />
        <el-table-column prop="jobAdviceArticleAuthorName" label="作者" width="180" />
        <el-table-column label="正文" width="100" align="center">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleViewContent(row)">
              详情
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="jobAdviceArticleCategory" label="分类" width="100" show-overflow-tooltip />
        <el-table-column prop="jobAdviceArticleTags" label="标签" width="120" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tag
              v-for="tag in row.jobAdviceArticleTags?.split(',')"
              :key="tag"
              size="small"
              style="margin-right: 4px;"
            >
              {{ tag }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag size="small" :type="row.jobAdviceArticlePublishedStatus === 1 ? 'success' : 'info'">
              {{ row.jobAdviceArticlePublishedStatus === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发布时间" width="140">
          <template #default="{ row }">
            {{ formatDateTime(row.jobAdviceArticlePublishedTime, 'MM-DD HH:mm') }}
          </template>
        </el-table-column>
        <el-table-column label="更新时间" width="140">
          <template #default="{ row }">
            {{ formatDateTime(row.jobAdviceArticleUpdatedTime, 'MM-DD HH:mm') }}
          </template>
        </el-table-column>
        <el-table-column width="200" fixed="right">
          <template #header>
            <div style="text-align: right; padding-right: 65px;">操作</div>
          </template>
          <template #default="{ row }">
            <div style="display: flex; gap: 8px; justify-content: flex-end; padding-right: 10px;">
              <el-button type="info" size="small" @click="handleView(row)">
                查看
              </el-button>
              <el-button type="primary" size="small" @click="handleEdit(row)">
                编辑
              </el-button>
              <el-button type="danger" size="small" @click="handleDelete(row)">
                删除
              </el-button>
            </div>
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
              <el-input v-model="articleForm.jobAdviceArticleCategory" placeholder="请输入文章分类" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="文章标签" prop="jobAdviceArticleTags">
              <el-input v-model="articleForm.jobAdviceArticleTags" placeholder="多个标签用逗号分隔" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="作者">
          <el-input v-model="articleForm.jobAdviceArticleAuthorName" placeholder="请输入作者名称" />
        </el-form-item>
        
        <el-form-item label="发布状态" prop="jobAdviceArticlePublishedStatus">
          <el-radio-group v-model="articleForm.jobAdviceArticlePublishedStatus">
            <el-radio :label="1">已发布</el-radio>
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

    <!-- 查看正文详情弹窗 -->
    <el-dialog
      v-model="contentDialogVisible"
      title="文章正文"
      width="800px"
    >
      <div v-if="currentContentArticle" class="content-container">
        <div class="content-body">
          <div style="white-space: pre-wrap; word-break: break-all; line-height: 1.8;">
            {{ currentContentArticle.jobAdviceArticleContent || '暂无内容' }}
          </div>
        </div>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="contentDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看文章信息对话框（不包含正文） -->
    <el-dialog
      v-model="viewDialogVisible"
      title="文章详情"
      width="700px"
    >
      <el-descriptions v-if="currentViewArticle" :column="2" border>
        <el-descriptions-item label="文章ID">
          {{ currentViewArticle.jobAdviceArticleId }}
        </el-descriptions-item>
        <el-descriptions-item label="文章标题">
          {{ currentViewArticle.jobAdviceArticleTitle }}
        </el-descriptions-item>
        <el-descriptions-item label="作者">
          {{ currentViewArticle.jobAdviceArticleAuthorName }}
        </el-descriptions-item>
        <el-descriptions-item label="分类">
          {{ currentViewArticle.jobAdviceArticleCategory }}
        </el-descriptions-item>
        <el-descriptions-item label="标签" :span="2">
          <el-tag
            v-for="tag in currentViewArticle.jobAdviceArticleTags?.split(',')"
            :key="tag"
            size="small"
            style="margin-right: 4px; margin-bottom: 4px;"
          >
            {{ tag }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="发布状态">
          <el-tag :type="currentViewArticle.jobAdviceArticlePublishedStatus === 1 ? 'success' : 'info'">
            {{ currentViewArticle.jobAdviceArticlePublishedStatus === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="发布时间">
          {{ formatDateTime(currentViewArticle.jobAdviceArticlePublishedTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间" :span="2">
          {{ formatDateTime(currentViewArticle.jobAdviceArticleUpdatedTime) }}
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="viewDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search, RefreshRight } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils'
import { jobAdviceArticleApi } from '@/api/admin'
import type {
  JobAdviceArticleForm,
  JobAdviceArticleQuery,
  JobAdviceArticlePageVO
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

// 文章详情VO（与分页VO相同）
type JobAdviceArticleInfoVO = JobAdviceArticlePageVO

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const contentDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const editingArticle = ref<JobAdviceArticlePageVO | null>(null)
const currentContentArticle = ref<JobAdviceArticleInfoVO | null>(null)
const currentViewArticle = ref<JobAdviceArticleInfoVO | null>(null)

// 搜索表单
const searchForm = reactive<JobAdviceArticleQuery>({
  jobAdviceArticleTitle: '',
  jobAdviceArticleContent: '',
  jobAdviceArticleCategory: '',
  jobAdviceArticleTags: '',
  jobAdviceArticleAuthorName: ''
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<JobAdviceArticlePageVO[]>([])

// 文章表单
const articleFormRef = ref<FormInstance>()
const articleForm = reactive<JobAdviceArticleForm>({
  jobAdviceArticleId: undefined,
  jobAdviceArticleTitle: '',
  jobAdviceArticleContent: '',
  jobAdviceArticleCategory: '',
  jobAdviceArticleTags: '',
  jobAdviceArticleAuthorName: '',
  jobAdviceArticlePublishedStatus: 1
})

// 表单校验规则
const articleRules = {
  jobAdviceArticleTitle: [
    { required: true, message: '请输入文章标题', trigger: 'blur' }
  ],
  jobAdviceArticleCategory: [
    { required: true, message: '请输入文章分类', trigger: 'blur' }
  ],
  jobAdviceArticleContent: [
    { required: true, message: '请输入文章内容', trigger: 'blur' }
  ]
}

// 获取文章列表
const getArticleList = async () => {
  loading.value = true
  try {
    const response = await jobAdviceArticleApi.getJobAdviceArticlePage(
      pagination.current,
      pagination.size,
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
  pagination.current = 1
  getArticleList()
}

// 重置搜索
const handleReset = () => {
  searchForm.jobAdviceArticleTitle = ''
  searchForm.jobAdviceArticleContent = ''
  searchForm.jobAdviceArticleCategory = ''
  searchForm.jobAdviceArticleTags = ''
  searchForm.jobAdviceArticleAuthorName = ''
  pagination.current = 1
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

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getArticleList()
}


// 查看正文详情（直接使用分页返回的数据）
const handleViewContent = (row: JobAdviceArticlePageVO) => {
  currentContentArticle.value = row
  contentDialogVisible.value = true
}

// 查看文章信息（不包含正文，直接使用分页返回的数据）
const handleView = (row: JobAdviceArticlePageVO) => {
  currentViewArticle.value = row
  viewDialogVisible.value = true
}

// 编辑文章
const handleEdit = (row: JobAdviceArticlePageVO) => {
  editingArticle.value = row
  Object.assign(articleForm, {
    jobAdviceArticleId: row.jobAdviceArticleId,
    jobAdviceArticleTitle: row.jobAdviceArticleTitle,
    jobAdviceArticleContent: row.jobAdviceArticleContent,
    jobAdviceArticleCategory: row.jobAdviceArticleCategory,
    jobAdviceArticleTags: row.jobAdviceArticleTags,
    jobAdviceArticleAuthorName: row.jobAdviceArticleAuthorName,
    jobAdviceArticlePublishedStatus: row.jobAdviceArticlePublishedStatus
  })
  showCreateDialog.value = true
}

// 删除文章
const handleDelete = (row: JobAdviceArticlePageVO) => {
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
    jobAdviceArticleTags: '',
    jobAdviceArticleAuthorName: '',
    jobAdviceArticlePublishedStatus: 1
  })
}

// 组件挂载
onMounted(() => {
  getArticleList()
})
</script>

<style scoped lang="scss">
.job-advice-management {
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

  .content-container {
    .content-header {
      margin-bottom: 20px;
      padding-bottom: 16px;
      border-bottom: 1px solid #e5e7eb;
      
      h3 {
        margin: 0;
        color: #1f2937;
        font-size: 20px;
      }
    }
    
    .content-body {
      max-height: 500px;
      overflow-y: auto;
      padding: 20px;
      background: #f9fafb;
      border: 1px solid #e5e7eb;
      border-radius: 8px;
      line-height: 1.8;
      color: #374151;
    }
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
        
        .author {
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
}

// 响应式设计
@media (max-width: 768px) {
  .job-advice-management {
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