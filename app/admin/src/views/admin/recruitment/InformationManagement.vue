<template>
  <div class="information-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">招聘信息管理</h1>
        <p class="page-description">管理招聘信息内容，维护企业招聘资讯</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openCreateDialog">
          <i class="el-icon-plus"></i>
          新增信息
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
        <el-form-item label="信息标题">
          <el-input
            v-model="searchForm.employmentInformationTitle"
            placeholder="请输入信息标题"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="公司名称">
          <el-input
            v-model="searchForm.employmentInformationCompany"
            placeholder="请输入公司名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="城市">
          <el-input
            v-model="searchForm.employmentInformationCity"
            placeholder="请输入城市"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.employmentInformationState" placeholder="请选择" clearable style="width: 120px">
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

    <!-- 信息列表 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column prop="employmentInformationId" label="ID" width="80" />
        <el-table-column prop="employmentInformationTitle" label="信息标题" min-width="200" />
        <el-table-column prop="employmentInformationCompany" label="公司名称" min-width="150" />
        <el-table-column prop="employmentInformationCity" label="城市" width="100" />
        <el-table-column prop="employmentInformationSalary" label="薪资" width="120">
          <template #default="{ row }">
            <el-tag type="warning">{{ row.employmentInformationSalary }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="employmentInformationExperience" label="经验要求" width="100" />
        <el-table-column prop="employmentInformationEducation" label="学历要求" width="100" />
        <el-table-column prop="employmentInformationState" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.employmentInformationState === 1 ? 'success' : 'info'">
              {{ row.employmentInformationState === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="employmentInformationCreatedTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.employmentInformationCreatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="text" size="small" @click="handleView(row)">
              <i class="el-icon-view"></i>
              查看
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

    <!-- 创建/编辑信息对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingInformation ? '编辑信息' : '新增信息'"
      width="900px"
      @close="resetForm"
    >
      <el-form
        ref="informationFormRef"
        :model="informationForm"
        :rules="informationRules"
        label-width="100px"
      >
        <el-form-item label="信息标题" prop="employmentInformationTitle">
          <el-input v-model="informationForm.employmentInformationTitle" placeholder="请输入信息标题" />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公司名称" prop="employmentInformationCompany">
              <el-input v-model="informationForm.employmentInformationCompany" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资待遇" prop="employmentInformationSalary">
              <el-input v-model="informationForm.employmentInformationSalary" placeholder="如：15-25K/月" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="工作城市" prop="employmentInformationCity">
              <el-input v-model="informationForm.employmentInformationCity" placeholder="请输入工作城市" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="经验要求" prop="employmentInformationExperience">
              <el-select v-model="informationForm.employmentInformationExperience" placeholder="请选择">
                <el-option label="不限" value="不限" />
                <el-option label="1-3年" value="1-3年" />
                <el-option label="3-5年" value="3-5年" />
                <el-option label="5-10年" value="5-10年" />
                <el-option label="10年以上" value="10年以上" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="学历要求" prop="employmentInformationEducation">
              <el-select v-model="informationForm.employmentInformationEducation" placeholder="请选择">
                <el-option label="不限" value="不限" />
                <el-option label="大专" value="大专" />
                <el-option label="本科" value="本科" />
                <el-option label="硕士" value="硕士" />
                <el-option label="博士" value="博士" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="技能要求" prop="employmentInformationSkill">
          <el-input v-model="informationForm.employmentInformationSkill" placeholder="请输入技能要求，多个技能用逗号分隔" />
        </el-form-item>
        
        <el-form-item label="职位介绍" prop="employmentInformationIntroduce">
          <el-input
            v-model="informationForm.employmentInformationIntroduce"
            type="textarea"
            :rows="4"
            placeholder="请输入职位介绍"
          />
        </el-form-item>
        
        <el-form-item label="福利待遇" prop="employmentInformationWelfare">
          <el-input
            v-model="informationForm.employmentInformationWelfare"
            type="textarea"
            :rows="3"
            placeholder="请输入福利待遇"
          />
        </el-form-item>
        
        <el-form-item label="发布状态" prop="employmentInformationState">
          <el-radio-group v-model="informationForm.employmentInformationState">
            <el-radio :label="1">发布</el-radio>
            <el-radio :label="0">草稿</el-radio>
          </el-radio-group>
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

    <!-- 查看信息详情对话框 -->
    <el-dialog
      v-model="showViewDialog"
      title="信息详情"
      width="800px"
    >
      <div class="information-detail" v-if="currentViewInformation">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="信息标题" span="2">
            {{ currentViewInformation.employmentInformationTitle }}
          </el-descriptions-item>
          <el-descriptions-item label="公司名称">
            {{ currentViewInformation.employmentInformationCompany }}
          </el-descriptions-item>
          <el-descriptions-item label="薪资待遇">
            <el-tag type="warning">{{ currentViewInformation.employmentInformationSalary }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="工作城市">
            {{ currentViewInformation.employmentInformationCity }}
          </el-descriptions-item>
          <el-descriptions-item label="经验要求">
            {{ currentViewInformation.employmentInformationExperience }}
          </el-descriptions-item>
          <el-descriptions-item label="学历要求">
            {{ currentViewInformation.employmentInformationEducation }}
          </el-descriptions-item>
          <el-descriptions-item label="技能要求" span="2">
            {{ currentViewInformation.employmentInformationSkill }}
          </el-descriptions-item>
          <el-descriptions-item label="职位介绍" span="2">
            <div class="detail-content">{{ currentViewInformation.employmentInformationIntroduce }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="福利待遇" span="2">
            <div class="detail-content">{{ currentViewInformation.employmentInformationWelfare }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="发布状态">
            <el-tag :type="currentViewInformation.employmentInformationState === 1 ? 'success' : 'info'">
              {{ currentViewInformation.employmentInformationState === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(currentViewInformation.employmentInformationCreatedTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDateTime } from '@/utils'
import { employmentInformationApi } from '@/api/admin'
import type {
  EmploymentInformationPageVO,
  EmploymentInformationForm,
  EmploymentInformationQuery,
  EmploymentInformationInfoVO
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const showViewDialog = ref(false)
const editingInformation = ref<EmploymentInformationPageVO | null>(null)
const currentViewInformation = ref<EmploymentInformationInfoVO | null>(null)

// 搜索表单
const searchForm = reactive<EmploymentInformationQuery>({
  employmentInformationTitle: '',
  employmentInformationCompany: '',
  employmentInformationCity: '',
  employmentInformationState: undefined
})

// 分页
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<EmploymentInformationPageVO[]>([])

// 信息表单
const informationFormRef = ref<FormInstance>()
const informationForm = reactive<EmploymentInformationForm>({
  employmentInformationId: undefined,
  employmentInformationTitle: '',
  employmentInformationCompany: '',
  employmentInformationSalary: '',
  employmentInformationCity: '',
  employmentInformationExperience: '',
  employmentInformationEducation: '',
  employmentInformationSkill: '',
  employmentInformationIntroduce: '',
  employmentInformationWelfare: '',
  employmentInformationState: 1
})

// 表单校验规则
const informationRules = {
  employmentInformationTitle: [
    { required: true, message: '请输入信息标题', trigger: 'blur' },
    { min: 5, max: 100, message: '长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  employmentInformationCompany: [
    { required: true, message: '请输入公司名称', trigger: 'blur' }
  ],
  employmentInformationSalary: [
    { required: true, message: '请输入薪资待遇', trigger: 'blur' }
  ],
  employmentInformationCity: [
    { required: true, message: '请输入工作城市', trigger: 'blur' }
  ],
  employmentInformationIntroduce: [
    { required: true, message: '请输入职位介绍', trigger: 'blur' },
    { min: 20, message: '职位介绍至少20个字符', trigger: 'blur' }
  ]
}

// 获取信息列表
const getInformationList = async () => {
  loading.value = true
  try {
    const response = await employmentInformationApi.getEmploymentInformationPage(
      pagination.page,
      pagination.size,
      searchForm
    )
    
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取信息列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 新增信息
const openCreateDialog = () => {
  editingInformation.value = null
  resetForm()
  showCreateDialog.value = true
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  getInformationList()
}

// 重置搜索
const handleReset = () => {
  searchForm.employmentInformationTitle = ''
  searchForm.employmentInformationCompany = ''
  searchForm.employmentInformationCity = ''
  searchForm.employmentInformationState = undefined
  pagination.page = 1
  getInformationList()
}

// 刷新数据
const refreshData = () => {
  getInformationList()
}

// 分页变更
const handleSizeChange = (size: number) => {
  pagination.size = size
  getInformationList()
}

const handleCurrentChange = (page: number) => {
  pagination.page = page
  getInformationList()
}

// 查看信息详情
const handleView = async (row: EmploymentInformationPageVO) => {
  try {
    const response = await employmentInformationApi.getEmploymentInformationInfo(row.employmentInformationId)
    currentViewInformation.value = response.data
    showViewDialog.value = true
  } catch (error) {
    console.error('获取信息详情失败:', error)
    ElMessage.error('获取信息详情失败')
  }
}

// 编辑信息
const handleEdit = async (row: EmploymentInformationPageVO) => {
  try {
    const response = await employmentInformationApi.getEmploymentInformationInfo(row.employmentInformationId)
    const detail = response.data
    
    editingInformation.value = row
    Object.assign(informationForm, {
      employmentInformationId: detail.employmentInformationId,
      employmentInformationTitle: detail.employmentInformationTitle,
      employmentInformationCompany: detail.employmentInformationCompany,
      employmentInformationSalary: detail.employmentInformationSalary,
      employmentInformationCity: detail.employmentInformationCity,
      employmentInformationExperience: detail.employmentInformationExperience,
      employmentInformationEducation: detail.employmentInformationEducation,
      employmentInformationSkill: detail.employmentInformationSkill,
      employmentInformationIntroduce: detail.employmentInformationIntroduce,
      employmentInformationWelfare: detail.employmentInformationWelfare,
      employmentInformationState: detail.employmentInformationState
    })
    showCreateDialog.value = true
  } catch (error) {
    console.error('获取信息详情失败:', error)
    ElMessage.error('获取信息详情失败')
  }
}

// 删除信息
const handleDelete = (row: EmploymentInformationPageVO) => {
  ElMessageBox.confirm(`确定要删除信息"${row.employmentInformationTitle}"吗？`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await employmentInformationApi.deleteEmploymentInformation({
        employmentInformationId: row.employmentInformationId
      })
      ElMessage.success('删除成功')
      getInformationList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!informationFormRef.value) return
  
  try {
    await informationFormRef.value.validate()
    submitting.value = true
    
    if (editingInformation.value) {
      await employmentInformationApi.updateEmploymentInformation(informationForm)
    } else {
      await employmentInformationApi.addEmploymentInformation(informationForm)
    }
    
    ElMessage.success(editingInformation.value ? '更新成功' : '创建成功')
    showCreateDialog.value = false
    getInformationList()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (informationFormRef.value) {
    informationFormRef.value.resetFields()
  }
  
  editingInformation.value = null
  Object.assign(informationForm, {
    employmentInformationId: undefined,
    employmentInformationTitle: '',
    employmentInformationCompany: '',
    employmentInformationSalary: '',
    employmentInformationCity: '',
    employmentInformationExperience: '',
    employmentInformationEducation: '',
    employmentInformationSkill: '',
    employmentInformationIntroduce: '',
    employmentInformationWelfare: '',
    employmentInformationState: 1
  })
}

// 组件挂载
onMounted(() => {
  getInformationList()
})
</script>

<style scoped lang="scss">
.information-management {
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

.information-detail {
  .detail-content {
    line-height: 1.6;
    color: #374151;
    white-space: pre-wrap;
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
  .information-management {
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