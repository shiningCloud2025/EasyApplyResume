<template>
  <div class="position-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">招聘岗位管理</h1>
        <p class="page-description">管理招聘岗位信息，维护企业职位库</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openCreateDialog">
          <i class="el-icon-plus"></i>
          新增岗位
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
        <el-form-item label="岗位名称">
          <el-input
            v-model="searchForm.recruitPositionName"
            placeholder="请输入岗位名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="所属行业">
          <el-select 
            v-model="searchForm.recruitPositionIndustryCode" 
            placeholder="请选择行业" 
            clearable 
            style="width: 160px"
          >
            <el-option
              v-for="industry in industryList"
              :key="industry.industryMapIndustryCode"
              :label="industry.industryMapIndustryName"
              :value="industry.industryMapIndustryCode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="最低月薪">
          <el-input-number
            v-model="searchForm.minMonthSalary"
            placeholder="最低薪资"
            :min="0"
            :step="1000"
            :controls="false"
            style="width: 130px"
          />
        </el-form-item>
        <el-form-item label="最高月薪">
          <el-input-number
            v-model="searchForm.maxMonthSalary"
            placeholder="最高薪资"
            :min="0"
            :step="1000"
            :controls="false"
            style="width: 130px"
          />
        </el-form-item>
        <el-form-item label="周工作日">
          <el-select 
            v-model="searchForm.weekWorkDayNum" 
            placeholder="请选择" 
            clearable 
            style="width: 120px"
          >
            <el-option label="1天" :value="1" />
            <el-option label="2天" :value="2" />
            <el-option label="3天" :value="3" />
            <el-option label="4天" :value="4" />
            <el-option label="5天" :value="5" />
            <el-option label="6天" :value="6" />
            <el-option label="7天" :value="7" />
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

    <!-- 岗位列表 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column prop="recruitPositionId" label="ID" width="70" />
        <el-table-column prop="recruitPositionName" label="岗位名称" min-width="200" />
        <el-table-column label="所属行业" width="120">
          <template #default="{ row }">
            {{ row.recruitPositionIndustryName || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="薪资范围" width="150">
          <template #default="{ row }">
            <el-tag type="warning" v-if="row.minMonthSalary && row.maxMonthSalary">
              {{ row.minMonthSalary }}-{{ row.maxMonthSalary }}元/月
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="周工作日" width="100" align="center">
          <template #default="{ row }">
            <span v-if="row.weekWorkDayNum">{{ row.weekWorkDayNum }}天/周</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="福利待遇" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.goodWelfare || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="120">
          <template #default="{ row }">
            {{ row.createdTime ? formatDate(row.createdTime) : '-' }}
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

    <!-- 创建/编辑岗位对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingPosition ? '编辑岗位' : '新增岗位'"
      width="800px"
      @close="resetForm"
    >
      <el-form
        ref="positionFormRef"
        :model="positionForm"
        :rules="positionRules"
        label-width="120px"
      >
        <el-form-item label="岗位名称" prop="recruitPositionName">
          <el-input 
            v-model="positionForm.recruitPositionName" 
            placeholder="请输入岗位名称，如：Java开发工程师" 
            maxlength="30"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="所属行业" prop="recruitPositionIndustryCode">
          <el-select 
            v-model="positionForm.recruitPositionIndustryCode" 
            placeholder="请选择所属行业" 
            style="width: 100%"
          >
            <el-option
              v-for="industry in industryList"
              :key="industry.industryMapIndustryCode"
              :label="industry.industryMapIndustryName"
              :value="industry.industryMapIndustryCode"
            />
          </el-select>
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最低月薪" prop="minMonthSalary">
              <el-input-number 
                v-model="positionForm.minMonthSalary" 
                :min="0" 
                :max="999999"
                :step="1000"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
              <span style="margin-left: 8px; color: #909399;">元/月</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最高月薪" prop="maxMonthSalary">
              <el-input-number 
                v-model="positionForm.maxMonthSalary" 
                :min="0" 
                :max="999999"
                :step="1000"
                :precision="2"
                controls-position="right"
                style="width: 100%"
              />
              <span style="margin-left: 8px; color: #909399;">元/月</span>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="每周工作日数" prop="weekWorkDayNum">
          <el-select 
            v-model="positionForm.weekWorkDayNum" 
            placeholder="请选择每周工作日数" 
            style="width: 100%"
          >
            <el-option label="1天/周" :value="1" />
            <el-option label="2天/周" :value="2" />
            <el-option label="3天/周" :value="3" />
            <el-option label="4天/周" :value="4" />
            <el-option label="5天/周" :value="5" />
            <el-option label="6天/周" :value="6" />
            <el-option label="7天/周" :value="7" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="福利待遇" prop="goodWelfare">
          <el-input
            v-model="positionForm.goodWelfare"
            type="textarea"
            :rows="4"
            placeholder="请输入福利待遇，如：五险一金、带薪年假、年终奖、股票期权等"
            maxlength="200"
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

    <!-- 查看岗位详情对话框 -->
    <el-dialog
      v-model="showViewDialog"
      title="岗位详情"
      width="700px"
    >
      <div class="position-detail" v-if="currentViewPosition">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="岗位ID">
            {{ currentViewPosition.recruitPositionId }}
          </el-descriptions-item>
          <el-descriptions-item label="岗位名称">
            {{ currentViewPosition.recruitPositionName }}
          </el-descriptions-item>
          <el-descriptions-item label="所属行业">
            {{ currentViewPosition.recruitPositionIndustryName || currentViewPosition.recruitPositionIndustryCode }}
          </el-descriptions-item>
          <el-descriptions-item label="薪资范围">
            <el-tag type="warning">{{ currentViewPosition.minMonthSalary }}-{{ currentViewPosition.maxMonthSalary }}元/月</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="每周工作日数">
            {{ currentViewPosition.weekWorkDayNum }}天/周
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(currentViewPosition.createdTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间" v-if="currentViewPosition.updatedTime">
            {{ formatDate(currentViewPosition.updatedTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="福利待遇" :span="2">
            <div class="detail-content">{{ currentViewPosition.goodWelfare || '暂无' }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDate } from '@/utils'
import { recruitPositionApi, industryMapApi } from '@/api/admin'
import type {
  RecruitPositionPageVO,
  RecruitPositionForm,
  RecruitPositionQuery,
  RecruitPositionInfoVO
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const showViewDialog = ref(false)
const editingPosition = ref<RecruitPositionPageVO | null>(null)
const currentViewPosition = ref<RecruitPositionInfoVO | null>(null)
const industryList = ref<any[]>([])

// 搜索表单
const searchForm = reactive<RecruitPositionQuery>({
  recruitPositionName: '',
  recruitPositionIndustryCode: undefined,
  minMonthSalary: undefined,
  maxMonthSalary: undefined,
  weekWorkDayNum: undefined
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<RecruitPositionPageVO[]>([])

// 岗位表单
const positionFormRef = ref<FormInstance>()
const positionForm = reactive<RecruitPositionForm>({
  recruitPositionId: undefined,
  recruitPositionName: '',
  recruitPositionIndustryCode: undefined,
  minMonthSalary: undefined,
  maxMonthSalary: undefined,
  weekWorkDayNum: undefined,
  goodWelfare: ''
})

// 表单校验规则
const positionRules = {
  recruitPositionName: [
    { required: true, message: '请输入岗位名称', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  recruitPositionIndustryCode: [
    { required: true, message: '请选择所属行业', trigger: 'change' }
  ],
  minMonthSalary: [
    { required: true, message: '请输入最低月薪', trigger: 'blur' },
    { 
      validator: (_rule: any, value: number, callback: any) => {
        if (value <= 0) {
          callback(new Error('最低月薪必须大于0'))
        } else if (positionForm.maxMonthSalary > 0 && value > positionForm.maxMonthSalary) {
          callback(new Error('最低月薪不能大于最高月薪'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ],
  maxMonthSalary: [
    { required: true, message: '请输入最高月薪', trigger: 'blur' },
    { 
      validator: (_rule: any, value: number, callback: any) => {
        if (value <= 0) {
          callback(new Error('最高月薪必须大于0'))
        } else if (positionForm.minMonthSalary > 0 && value < positionForm.minMonthSalary) {
          callback(new Error('最高月薪不能小于最低月薪'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ],
  weekWorkDayNum: [
    { required: true, message: '请选择每周工作日数', trigger: 'change' }
  ]
}

// 获取行业列表
const getIndustryList = async () => {
  try {
    const response = await industryMapApi.findAllIndustryMap()
    industryList.value = response.data || []
  } catch (error) {
    console.error('获取行业列表失败:', error)
    ElMessage.error('获取行业列表失败')
  }
}

// 获取岗位列表
const getPositionList = async () => {
  loading.value = true
  try {
    const response = await recruitPositionApi.getRecruitPositionPage(
      pagination.current,
      pagination.size,
      searchForm
    )
    
    console.log('📊 后端返回的分页数据:', response.data)
    console.log('📋 记录列表:', response.data.records)
    if (response.data.records && response.data.records.length > 0) {
      console.log('🔍 第一条数据详情:', response.data.records[0])
    }
    
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取岗位列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 新增岗位
const openCreateDialog = () => {
  editingPosition.value = null
  resetForm()
  showCreateDialog.value = true
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getPositionList()
}

// 重置搜索
const handleReset = () => {
  searchForm.recruitPositionName = ''
  searchForm.recruitPositionIndustryCode = undefined
  searchForm.minMonthSalary = undefined
  searchForm.maxMonthSalary = undefined
  searchForm.weekWorkDayNum = undefined
  pagination.current = 1
  getPositionList()
}

// 刷新数据
const refreshData = () => {
  getPositionList()
}

// 分页变更
const handleSizeChange = (size: number) => {
  pagination.size = size
  getPositionList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getPositionList()
}

// 查看岗位详情
const handleView = async (row: RecruitPositionPageVO) => {
  try {
    const response = await recruitPositionApi.getRecruitPositionInfo(row.recruitPositionId)
    currentViewPosition.value = response.data
    showViewDialog.value = true
  } catch (error) {
    console.error('获取岗位详情失败:', error)
    ElMessage.error('获取岗位详情失败')
  }
}

// 编辑岗位
const handleEdit = async (row: RecruitPositionPageVO) => {
  try {
    const response = await recruitPositionApi.getRecruitPositionInfo(row.recruitPositionId)
    const detail = response.data
    
    editingPosition.value = row
    Object.assign(positionForm, {
      recruitPositionId: detail.recruitPositionId,
      recruitPositionName: detail.recruitPositionName,
      recruitPositionIndustryCode: detail.recruitPositionIndustryCode,
      minMonthSalary: detail.minMonthSalary,
      maxMonthSalary: detail.maxMonthSalary,
      weekWorkDayNum: detail.weekWorkDayNum,
      goodWelfare: detail.goodWelfare || ''
    })
    showCreateDialog.value = true
  } catch (error) {
    console.error('获取岗位详情失败:', error)
    ElMessage.error('获取岗位详情失败')
  }
}

// 删除岗位
const handleDelete = (row: RecruitPositionPageVO) => {
  ElMessageBox.confirm(`确定要删除岗位"${row.recruitPositionName}"吗？`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await recruitPositionApi.deleteRecruitPosition(row.recruitPositionId)
      ElMessage.success('删除成功')
      getPositionList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!positionFormRef.value) return
  
  try {
    await positionFormRef.value.validate()
    submitting.value = true
    
    if (editingPosition.value) {
      await recruitPositionApi.updateRecruitPosition(positionForm)
    } else {
      await recruitPositionApi.addRecruitPosition(positionForm)
    }
    
    ElMessage.success(editingPosition.value ? '更新成功' : '创建成功')
    showCreateDialog.value = false
    getPositionList()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (positionFormRef.value) {
    positionFormRef.value.resetFields()
  }
  
  editingPosition.value = null
  Object.assign(positionForm, {
    recruitPositionId: undefined,
    recruitPositionName: '',
    recruitPositionIndustryCode: undefined,
    minMonthSalary: undefined,
    maxMonthSalary: undefined,
    weekWorkDayNum: undefined,
    goodWelfare: ''
  })
}

// 组件挂载
onMounted(() => {
  getIndustryList()
  getPositionList()
})
</script>

<style scoped lang="scss">
.position-management {
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

  .position-detail {
    .detail-content {
      line-height: 1.6;
      color: #374151;
      white-space: pre-wrap;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .position-management {
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
