<template>
  <div class="industry-map-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">行业管理</h1>
        <p class="page-description">管理系统行业分类配置</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openCreateDialog">
          <i class="el-icon-plus"></i>
          新增行业
        </el-button>
        <el-button @click="refreshData">
          <i class="el-icon-refresh"></i>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="行业代码">
          <el-input-number
            v-model="searchForm.industryMapIndustryCode"
            placeholder="请输入行业代码"
            :controls="false"
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="行业名称">
          <el-input
            v-model="searchForm.industryMapIndustryName"
            placeholder="请输入行业名称"
            clearable
            style="width: 240px"
          />
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

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column prop="industryMapIndustryCode" label="行业代码" width="120" align="center" />
        <el-table-column prop="industryMapIndustryName" label="行业名称" min-width="200" />
        <el-table-column prop="createdTime" label="创建时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="updatedTime" label="修改时间" width="120">
          <template #default="{ row }">
            {{ formatDate(row.updatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="info" size="default" @click="handleView(row)">
              查看
            </el-button>
            <el-button type="primary" size="default" @click="handleEdit(row)">
              编辑
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

    <!-- 创建/编辑行业对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingIndustry ? '编辑行业' : '新增行业'"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="industryFormRef"
        :model="industryForm"
        :rules="industryRules"
        label-width="100px"
      >
        <el-form-item label="行业代码" v-if="editingIndustry">
          <el-input 
            v-model="industryForm.industryMapIndustryCode" 
            disabled
            placeholder="系统自动生成"
          />
          <div style="font-size: 12px; color: #909399; margin-top: 4px;">
            行业代码不可修改
          </div>
        </el-form-item>
        
        <el-form-item label="行业名称" prop="industryMapIndustryName">
          <el-input 
            v-model="industryForm.industryMapIndustryName" 
            placeholder="请输入行业名称，如：互联网、金融、教育等" 
            maxlength="50"
            show-word-limit
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

    <!-- 查看行业详情对话框 -->
    <el-dialog
      v-model="showViewDialog"
      title="行业详情"
      width="600px"
    >
      <div class="industry-detail" v-if="currentViewIndustry">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="行业代码">
            <el-tag type="primary">{{ currentViewIndustry.industryMapIndustryCode }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="行业名称">
            {{ currentViewIndustry.industryMapIndustryName }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(currentViewIndustry.createdTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="修改时间">
            {{ formatDate(currentViewIndustry.updatedTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showViewDialog = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { formatDate } from '@/utils'
import { industryMapApi } from '@/api/admin'
import type {
  IndustryMapPageVO,
  IndustryMapForm,
  IndustryMapQuery,
  IndustryMapInfoVO
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const showViewDialog = ref(false)
const editingIndustry = ref<IndustryMapPageVO | null>(null)
const currentViewIndustry = ref<IndustryMapInfoVO | null>(null)

// 搜索表单
const searchForm = reactive<IndustryMapQuery>({
  industryMapIndustryCode: undefined,
  industryMapIndustryName: ''
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<IndustryMapPageVO[]>([])

// 行业表单
const industryFormRef = ref<FormInstance>()
const industryForm = reactive<IndustryMapForm>({
  industryMapIndustryCode: undefined,
  industryMapIndustryName: ''
})

// 表单校验规则
const industryRules = {
  industryMapIndustryName: [
    { required: true, message: '请输入行业名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ]
}

// 获取行业列表
const getIndustryList = async () => {
  loading.value = true
  try {
    const response = await industryMapApi.getIndustryMapPage(
      pagination.current,
      pagination.size,
      searchForm
    )
    
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取行业列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 新增行业
const openCreateDialog = () => {
  editingIndustry.value = null
  resetForm()
  showCreateDialog.value = true
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getIndustryList()
}

// 重置搜索
const handleReset = () => {
  searchForm.industryMapIndustryCode = undefined
  searchForm.industryMapIndustryName = ''
  pagination.current = 1
  getIndustryList()
}

// 刷新数据
const refreshData = () => {
  getIndustryList()
}

// 分页变更
const handleSizeChange = (size: number) => {
  pagination.size = size
  getIndustryList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getIndustryList()
}

// 查看行业详情
const handleView = async (row: IndustryMapPageVO) => {
  try {
    const response = await industryMapApi.getIndustryMapInfo(row.industryMapIndustryCode)
    currentViewIndustry.value = response.data
    showViewDialog.value = true
  } catch (error) {
    console.error('获取行业详情失败:', error)
    ElMessage.error('获取行业详情失败')
  }
}

// 编辑行业
const handleEdit = async (row: IndustryMapPageVO) => {
  try {
    const response = await industryMapApi.getIndustryMapInfo(row.industryMapIndustryCode)
    const detail = response.data
    
    editingIndustry.value = row
    Object.assign(industryForm, {
      industryMapIndustryCode: detail.industryMapIndustryCode,
      industryMapIndustryName: detail.industryMapIndustryName
    })
    showCreateDialog.value = true
  } catch (error) {
    console.error('获取行业详情失败:', error)
    ElMessage.error('获取行业详情失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!industryFormRef.value) return
  
  try {
    await industryFormRef.value.validate()
    submitting.value = true
    
    if (editingIndustry.value) {
      await industryMapApi.updateIndustryMap(industryForm)
      ElMessage.success('更新成功')
    } else {
      await industryMapApi.addIndustryMap(industryForm)
      ElMessage.success('创建成功')
    }
    
    showCreateDialog.value = false
    getIndustryList()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (industryFormRef.value) {
    industryFormRef.value.resetFields()
  }
  
  editingIndustry.value = null
  Object.assign(industryForm, {
    industryMapIndustryCode: undefined,
    industryMapIndustryName: ''
  })
}

// 组件挂载
onMounted(() => {
  getIndustryList()
})
</script>

<style scoped lang="scss">
.industry-map-management {
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

  .industry-detail {
    padding: 8px 0;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .industry-map-management {
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
        .el-select,
        .el-input-number {
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
