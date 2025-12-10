<template>
  <div class="industry-map-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">行业Map管理</h1>
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
      <el-form :model="searchForm" inline>
        <el-form-item label="行业名称">
          <el-input
            v-model="searchForm.industryMapName"
            placeholder="请输入行业名称"
            clearable
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="行业等级">
          <el-select v-model="searchForm.industryMapLevel" placeholder="请选择" clearable style="width: 180px">
            <el-option label="一级" :value="1" />
            <el-option label="二级" :value="2" />
            <el-option label="三级" :value="3" />
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

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无数据"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        row-key="industryMapId"
      >
        <el-table-column prop="industryMapId" label="ID" width="70" />
        <el-table-column prop="industryMapName" label="行业名称" min-width="200" />
        <el-table-column label="描述" width="100" align="center">
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
        <el-table-column prop="industryMapParentId" label="父级ID" width="100" />
        <el-table-column prop="industryMapLevel" label="等级" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="getLevelTagType(row.industryMapLevel)">
              {{ getLevelText(row.industryMapLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="industryMapCreatedTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.industryMapCreatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="320" fixed="right">
          <template #default="{ row }">
            <el-button type="success" size="default" @click="handleAddChild(row)">
              添加子行业
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

    <!-- 创建/编辑行业对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="industryFormRef"
        :model="industryForm"
        :rules="industryRules"
        label-width="100px"
      >
        <el-form-item label="行业名称" prop="industryMapName">
          <el-input v-model="industryForm.industryMapName" placeholder="请输入行业名称" />
        </el-form-item>
        
        <el-form-item label="行业描述" prop="industryMapIntroduce">
          <el-input v-model="industryForm.industryMapIntroduce" type="textarea" :rows="3" placeholder="请输入行业描述" />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="父级行业" prop="industryMapParentId">
              <el-select 
                v-model="industryForm.industryMapParentId" 
                placeholder="请选择父级行业"
                clearable
                filterable
              >
                <el-option label="无父级行业" :value="0" />
                <el-option
                  v-for="parent in parentIndustries"
                  :key="parent.industryMapId"
                  :label="parent.industryMapName"
                  :value="parent.industryMapId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="行业等级" prop="industryMapLevel">
              <el-input-number
                v-model="industryForm.industryMapLevel"
                :min="1"
                :max="3"
                placeholder="请选择等级"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
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

    <!-- 描述详情弹窗 -->
    <el-dialog
      v-model="introduceDialogVisible"
      title="行业描述"
      width="600px"
    >
      <el-card v-if="currentIntroduceIndustry">
        <template #header>
          <div style="font-weight: 600; font-size: 16px;">{{ currentIntroduceIndustry.industryMapName }}</div>
        </template>
        <div style="padding: 16px; min-height: 100px; white-space: pre-wrap; word-break: break-all; line-height: 1.8;">
          {{ currentIntroduceIndustry.industryMapIntroduce || '该行业暂无描述' }}
        </div>
      </el-card>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="introduceDialogVisible = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDateTime } from '@/utils'
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
const editingIndustry = ref<IndustryMapPageVO | null>(null)
const parentIndustries = ref<IndustryMapPageVO[]>([])

// 搜索表单
const searchForm = reactive<IndustryMapQuery>({
  industryMapName: '',
  industryMapParentId: undefined,
  industryMapLevel: undefined
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
  industryMapId: undefined,
  industryMapName: '',
  industryMapIntroduce: '',
  industryMapParentId: 0,
  industryMapLevel: 1
})

// 表单校验规则
const industryRules = {
  industryMapName: [
    { required: true, message: '请输入行业名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  industryMapIntroduce: [
    { required: true, message: '请输入行业描述', trigger: 'blur' },
    { min: 5, max: 200, message: '长度在 5 到 200 个字符', trigger: 'blur' }
  ],
  industryMapLevel: [
    { required: true, message: '请输入行业等级', trigger: 'change' }
  ]
}

// 弹窗标题
const dialogTitle = computed(() => {
  if (isAddingChild.value) return '添加子行业'
  return editingIndustry.value ? '编辑行业' : '新增行业'
})

// 是否添加子行业
const isAddingChild = ref(false)
const currentParent = ref<IndustryMapPageVO | null>(null)

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

// 获取所有父级行业
const getParentIndustries = async () => {
  try {
    const response = await industryMapApi.findAllIndustryMap()
    parentIndustries.value = response.data
  } catch (error) {
    console.error('获取父级行业列表失败:', error)
  }
}

// 新增行业
const openCreateDialog = () => {
  editingIndustry.value = null
  isAddingChild.value = false
  currentParent.value = null
  resetForm()
  showCreateDialog.value = true
}

// 添加子行业
const handleAddChild = (row: IndustryMapPageVO) => {
  editingIndustry.value = null
  isAddingChild.value = true
  currentParent.value = row
  resetForm()
  industryForm.industryMapParentId = row.industryMapId
  industryForm.industryMapLevel = row.industryMapLevel + 1
  showCreateDialog.value = true
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getIndustryList()
}

// 重置搜索
const handleReset = () => {
  searchForm.industryMapName = ''
  searchForm.industryMapParentId = undefined
  searchForm.industryMapLevel = undefined
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

// 查看描述详情
const introduceDialogVisible = ref(false)
const currentIntroduceIndustry = ref<IndustryMapPageVO | null>(null)

const handleViewIntroduce = (row: IndustryMapPageVO) => {
  currentIntroduceIndustry.value = row
  introduceDialogVisible.value = true
}

// 编辑行业
const handleEdit = async (row: IndustryMapPageVO) => {
  try {
    const response = await industryMapApi.getIndustryMapInfo(row.industryMapId)
    const detail = response.data
    
    editingIndustry.value = row
    isAddingChild.value = false
    currentParent.value = null
    Object.assign(industryForm, {
      industryMapId: detail.industryMapId,
      industryMapName: detail.industryMapName,
      industryMapIntroduce: detail.industryMapIntroduce,
      industryMapParentId: detail.industryMapParentId,
      industryMapLevel: detail.industryMapLevel
    })
    showCreateDialog.value = true
  } catch (error) {
    console.error('获取行业详情失败:', error)
    ElMessage.error('获取行业详情失败')
  }
}

// 删除行业
const handleDelete = (row: IndustryMapPageVO) => {
  ElMessageBox.confirm(`确定要删除行业"${row.industryMapName}"吗？这可能会影响相关的子行业。`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await industryMapApi.updateIndustryMap({
        industryMapId: row.industryMapId,
        industryMapName: row.industryMapName,
        industryMapIntroduce: row.industryMapIntroduce,
        industryMapParentId: row.industryMapParentId,
        industryMapLevel: row.industryMapLevel
      })
      ElMessage.success('删除成功')
      getIndustryList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 获取等级标签类型
const getLevelTagType = (level: number) => {
  const typeMap: Record<number, string> = {
    1: 'primary',
    2: 'success',
    3: 'info'
  }
  return typeMap[level] || 'info'
}

// 获取等级文本
const getLevelText = (level: number) => {
  const levelMap: Record<number, string> = {
    1: '一级',
    2: '二级',
    3: '三级'
  }
  return levelMap[level] || '未知'
}

// 提交表单
const handleSubmit = async () => {
  if (!industryFormRef.value) return
  
  try {
    await industryFormRef.value.validate()
    submitting.value = true
    
    if (isAddingChild.value || editingIndustry.value) {
      await industryMapApi.updateIndustryMap(industryForm)
      ElMessage.success(isAddingChild.value ? '添加子行业成功' : '更新成功')
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
  isAddingChild.value = false
  currentParent.value = null
  Object.assign(industryForm, {
    industryMapId: undefined,
    industryMapName: '',
    industryMapIntroduce: '',
    industryMapParentId: 0,
    industryMapLevel: 1
  })
}

// 组件挂载
onMounted(() => {
  getIndustryList()
  getParentIndustries()
})
</script>

<style scoped lang="scss">
.industry-map-management {
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

.table-card {
  margin-bottom: 24px;
  
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

.danger {
  color: #ef4444;
  
  &:hover {
    color: #dc2626;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .industry-map-management {
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