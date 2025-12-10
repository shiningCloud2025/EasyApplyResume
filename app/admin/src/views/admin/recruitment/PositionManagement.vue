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
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="薪资范围">
          <el-input
            v-model="searchForm.recruitPositionSalary"
            placeholder="请输入薪资范围"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.recruitPositionState" placeholder="请选择" clearable style="width: 180px">
            <el-option label="招聘中" :value="1" />
            <el-option label="已结束" :value="0" />
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
        <el-table-column prop="recruitPositionSalary" label="薪资范围" width="120">
          <template #default="{ row }">
            <el-tag type="warning">{{ row.recruitPositionSalary }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recruitPositionIntroduce" label="职位描述" width="100" align="center">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="default"
              @click="handleViewIntroduce(row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="recruitPositionState" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.recruitPositionState === 1 ? 'success' : 'info'">
              {{ row.recruitPositionState === 1 ? '招聘中' : '已结束' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recruitPositionCreatedTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.recruitPositionCreatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
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
        label-width="100px"
      >
        <el-form-item label="岗位名称" prop="recruitPositionName">
          <el-input v-model="positionForm.recruitPositionName" placeholder="请输入岗位名称" />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="薪资范围" prop="recruitPositionSalary">
              <el-input v-model="positionForm.recruitPositionSalary" placeholder="如：15-25K/月" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="招聘状态" prop="recruitPositionState">
              <el-radio-group v-model="positionForm.recruitPositionState">
                <el-radio :label="1">招聘中</el-radio>
                <el-radio :label="0">已结束</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="职位描述" prop="recruitPositionIntroduce">
          <el-input
            v-model="positionForm.recruitPositionIntroduce"
            type="textarea"
            :rows="4"
            placeholder="请输入职位描述，包括岗位职责、任职要求等"
          />
        </el-form-item>
        
        <el-form-item label="任职要求" prop="recruitPositionRequirement">
          <el-input
            v-model="positionForm.recruitPositionRequirement"
            type="textarea"
            :rows="4"
            placeholder="请输入任职要求，包括学历、经验、技能等"
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

    </el-dialog>

    <!-- 职位描述详情弹窗 -->
    <el-dialog
      v-model="introduceDialogVisible"
      title="职位描述"
      width="700px"
    >
      <el-card v-if="currentIntroducePosition">
        <template #header>
          <div style="font-weight: 600; font-size: 16px;">{{ currentIntroducePosition.recruitPositionName }}</div>
        </template>
        <div style="padding: 16px; min-height: 150px; white-space: pre-wrap; word-break: break-all; line-height: 1.8; max-height: 400px; overflow-y: auto;">
          {{ currentIntroducePosition.recruitPositionIntroduce || '暂无职位描述' }}
        </div>
      </el-card>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="introduceDialogVisible = false">关闭</el-button>
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
        <el-descriptions :column="1" border>
          <el-descriptions-item label="岗位名称">
            {{ currentViewPosition.recruitPositionName }}
          </el-descriptions-item>
          <el-descriptions-item label="薪资范围">
            <el-tag type="warning">{{ currentViewPosition.recruitPositionSalary }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="招聘状态">
            <el-tag :type="currentViewPosition.recruitPositionState === 1 ? 'success' : 'info'">
              {{ currentViewPosition.recruitPositionState === 1 ? '招聘中' : '已结束' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(currentViewPosition.recruitPositionCreatedTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDateTime(currentViewPosition.recruitPositionUpdatedTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="职位描述">
            <div class="detail-content">{{ currentViewPosition.recruitPositionIntroduce }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="任职要求">
            <div class="detail-content">{{ currentViewPosition.recruitPositionRequirement }}</div>
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
import { recruitPositionApi } from '@/api/admin'
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

// 搜索表单
const searchForm = reactive<RecruitPositionQuery>({
  recruitPositionName: '',
  recruitPositionSalary: '',
  recruitPositionState: undefined
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
  recruitPositionSalary: '',
  recruitPositionIntroduce: '',
  recruitPositionRequirement: '',
  recruitPositionState: 1
})

// 表单校验规则
const positionRules = {
  recruitPositionName: [
    { required: true, message: '请输入岗位名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  recruitPositionSalary: [
    { required: true, message: '请输入薪资范围', trigger: 'blur' }
  ],
  recruitPositionIntroduce: [
    { required: true, message: '请输入职位描述', trigger: 'blur' },
    { min: 10, message: '职位描述至少10个字符', trigger: 'blur' }
  ],
  recruitPositionRequirement: [
    { required: true, message: '请输入任职要求', trigger: 'blur' },
    { min: 10, message: '任职要求至少10个字符', trigger: 'blur' }
  ]
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
  searchForm.recruitPositionSalary = ''
  searchForm.recruitPositionState = undefined
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

// 查看职位描述详情
const introduceDialogVisible = ref(false)
const currentIntroducePosition = ref<RecruitPositionPageVO | null>(null)

const handleViewIntroduce = (row: RecruitPositionPageVO) => {
  currentIntroducePosition.value = row
  introduceDialogVisible.value = true
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
      recruitPositionSalary: detail.recruitPositionSalary,
      recruitPositionIntroduce: detail.recruitPositionIntroduce,
      recruitPositionRequirement: detail.recruitPositionRequirement,
      recruitPositionState: detail.recruitPositionState
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
    recruitPositionSalary: '',
    recruitPositionIntroduce: '',
    recruitPositionRequirement: '',
    recruitPositionState: 1
  })
}

// 组件挂载
onMounted(() => {
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

  .position-intro {
    color: #6b7280;
    line-height: 1.4;
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

  .danger {
    color: #ef4444;
    
    &:hover {
      color: #dc2626;
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