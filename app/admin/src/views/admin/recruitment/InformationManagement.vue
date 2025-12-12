<template>
  <div class="information-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">招聘信息管理</h1>
        <p class="page-description">管理招聘信息内容，维护企业招聘资讯</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openCreateDialog">
          <el-icon><Plus /></el-icon>
          新增信息
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
        <el-form-item label="公司名称">
          <el-input
            v-model="searchForm.employmentInformationCompanyName"
            placeholder="请输入公司名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="招聘批次">
          <el-input-number
            v-model="searchForm.employmentInformationBatch"
            :controls="false"
            placeholder="年份"
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item label="网申状态">
          <el-input
            v-model="searchForm.employmentInformationOnlineApplicationStatus"
            placeholder="请输入网申状态"
            clearable
            style="width: 150px"
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

    <!-- 信息列表 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column prop="employmentInformationId" label="ID" width="70" />
        <el-table-column prop="employmentInformationCompanyName" label="公司名称" min-width="150" />
        <el-table-column prop="employmentInformationIndustryCategoriesName" label="行业" width="120" />
        <el-table-column prop="employmentInformationBatch" label="招聘批次" width="100" />
        <el-table-column label="招聘地点" min-width="150">
          <template #default="{ row }">
            <div v-if="row.employmentInformationRecruitLocationFirstName && row.employmentInformationRecruitLocationFirstName.length > 0">
              <el-tag
                v-for="(city, index) in row.employmentInformationRecruitLocationFirstName"
                :key="index"
                size="small"
                style="margin-right: 4px; margin-bottom: 4px;"
              >
                {{ city }}
              </el-tag>
            </div>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="employmentInformationOnlineApplicationStatus" label="网申状态" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.employmentInformationOnlineApplicationStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="employmentInformationStopTime" label="截止时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.employmentInformationStopTime) }}
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

    <!-- 创建/编辑信息对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingInformation ? '编辑招聘信息' : '新增招聘信息'"
      width="900px"
      @close="resetForm"
    >
      <el-form
        ref="informationFormRef"
        :model="informationForm"
        :rules="informationRules"
        label-width="120px"
      >
        <el-form-item label="公司名称" prop="employmentInformationCompanyName">
          <el-input v-model="informationForm.employmentInformationCompanyName" placeholder="请输入公司名称" />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="行业大类" prop="employmentInformationIndustryCategories">
              <el-select 
                v-model="informationForm.employmentInformationIndustryCategories" 
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
          <el-col :span="12">
            <el-form-item label="企业性质" prop="employmentInformationCompanyType">
              <el-select 
                v-model="informationForm.employmentInformationCompanyType" 
                placeholder="请选择企业性质"
                style="width: 100%"
              >
                <el-option label="国有企业" :value="1" />
                <el-option label="民营企业" :value="2" />
                <el-option label="外资企业" :value="3" />
                <el-option label="合资企业" :value="4" />
                <el-option label="其他" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="招聘批次" prop="employmentInformationBatch">
              <el-input-number 
                v-model="informationForm.employmentInformationBatch" 
                :min="2000" 
                :max="2100" 
                :controls="false"
                placeholder="年份"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="招聘岗位" prop="employmentInformationRecruitPosition">
              <el-select 
                v-model="informationForm.employmentInformationRecruitPosition" 
                placeholder="请选择岗位"
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="position in positions"
                  :key="position.recruitPositionId"
                  :label="position.recruitPositionName"
                  :value="position.recruitPositionId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="招聘对象" prop="employmentInformationRecruitObject">
              <el-select 
                v-model="informationForm.employmentInformationRecruitObject" 
                placeholder="请选择招聘对象"
                style="width: 100%"
              >
                <el-option label="应届生" :value="1" />
                <el-option label="社会招聘" :value="2" />
                <el-option label="实习生" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="招聘省份" prop="employmentInformationRecruitLocationFirst">
              <el-select 
                v-model="informationForm.employmentInformationRecruitLocationFirst" 
                multiple
                placeholder="请选择省份"
                style="width: 100%"
                @change="handleProvinceChange"
              >
                <el-option
                  v-for="province in provinces"
                  :key="province.provinceMapId"
                  :label="province.provinceMapName"
                  :value="province.provinceMapId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="招聘城市" prop="employmentInformationRecruitLocationSecond">
              <el-select 
                v-model="informationForm.employmentInformationRecruitLocationSecond" 
                multiple
                placeholder="请先选择省份"
                style="width: 100%"
                :disabled="!informationForm.employmentInformationRecruitLocationFirst || informationForm.employmentInformationRecruitLocationFirst.length === 0"
              >
                <el-option
                  v-for="city in availableCities"
                  :key="city.cityMapId"
                  :label="city.cityMapName"
                  :value="city.cityMapId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="详细地址" prop="employmentInformationRecruitLocationDetail">
          <el-input
            v-model="informationForm.employmentInformationRecruitLocationDetail"
            placeholder="请输入详细地址（可选）"
          />
        </el-form-item>
        
        <el-form-item label="截止时间" prop="employmentInformationStopTime">
          <el-date-picker
            v-model="informationForm.employmentInformationStopTime"
            type="datetime"
            placeholder="选择截止时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="网申状态" prop="employmentInformationOnlineApplicationStatus">
          <el-input
            v-model="informationForm.employmentInformationOnlineApplicationStatus"
            placeholder="如：开放中、已结束"
          />
        </el-form-item>
        
        <el-form-item label="投递方式" prop="employmentInformationSubmissionWay">
          <el-input
            v-model="informationForm.employmentInformationSubmissionWay"
            type="textarea"
            :rows="3"
            placeholder="请输入投递方式"
          />
        </el-form-item>
        
        <el-form-item label="官方公告" prop="employmentInformationOfficialAnnouncement">
          <el-input
            v-model="informationForm.employmentInformationOfficialAnnouncement"
            type="textarea"
            :rows="4"
            placeholder="请输入官方公告（可选）"
          />
        </el-form-item>
        
        <el-form-item label="内推码" prop="employmentInformationEmployeeReferralCode">
          <el-input
            v-model="informationForm.employmentInformationEmployeeReferralCode"
            placeholder="请输入内推码（可选）"
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

    <!-- 查看信息详情对话框 -->
    <el-dialog
      v-model="showViewDialog"
      title="招聘信息详情"
      width="800px"
    >
      <div class="information-detail" v-if="currentViewInformation">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="招聘编号">
            {{ currentViewInformation.employmentInformationCode }}
          </el-descriptions-item>
          <el-descriptions-item label="公司名称">
            {{ currentViewInformation.employmentInformationCompanyName }}
          </el-descriptions-item>
          <el-descriptions-item label="行业大类">
            {{ currentViewInformation.employmentInformationIndustryCategoriesName }}
          </el-descriptions-item>
          <el-descriptions-item label="企业性质">
            {{ getCompanyTypeName(currentViewInformation.employmentInformationCompanyType) }}
          </el-descriptions-item>
          <el-descriptions-item label="招聘批次">
            {{ currentViewInformation.employmentInformationBatch }}
          </el-descriptions-item>
          <el-descriptions-item label="招聘对象">
            {{ getRecruitObjectName(currentViewInformation.employmentInformationRecruitObject) }}
          </el-descriptions-item>
          <el-descriptions-item label="招聘省份" :span="2">
            <el-tag
              v-for="(name, index) in currentViewInformation.employmentInformationRecruitLocationFirstName"
              :key="index"
              style="margin-right: 6px;"
            >
              {{ name }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="招聘城市" :span="2">
            <el-tag
              v-for="(name, index) in currentViewInformation.employmentInformationRecruitLocationSecondName"
              :key="index"
              type="success"
              style="margin-right: 6px;"
            >
              {{ name }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="详细地址" :span="2">
            <div v-if="currentViewInformation.employmentInformationRecruitLocationDetail && currentViewInformation.employmentInformationRecruitLocationDetail.length > 0">
              {{ currentViewInformation.employmentInformationRecruitLocationDetail.join('、') }}
            </div>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="网申状态">
            <el-tag>{{ currentViewInformation.employmentInformationOnlineApplicationStatus }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="截止时间">
            {{ formatDateTime(currentViewInformation.employmentInformationStopTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="投递方式" :span="2">
            <div class="detail-content">{{ currentViewInformation.employmentInformationSubmissionWay }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="官方公告" :span="2">
            <div class="detail-content">{{ currentViewInformation.employmentInformationOfficialAnnouncement || '-' }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="内推码" :span="2">
            {{ currentViewInformation.employmentInformationEmployeeReferralCode || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(currentViewInformation.employmentInformationStartTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDateTime(currentViewInformation.employmentInformationUpdatedTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search, RefreshRight } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils'
import { employmentInformationApi, industryMapApi, provinceMapApi, recruitPositionApi } from '@/api/admin'
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
  employmentInformationCompanyName: '',
  employmentInformationBatch: undefined,
  employmentInformationOnlineApplicationStatus: ''
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<EmploymentInformationPageVO[]>([])

// 下拉选项数据
const industries = ref<any[]>([])
const provinces = ref<any[]>([])
const allCities = ref<any[]>([])
const positions = ref<any[]>([])

// 信息表单
const informationFormRef = ref<FormInstance>()
const informationForm = reactive<EmploymentInformationForm>({
  employmentInformationId: undefined,
  employmentInformationCompanyName: '',
  employmentInformationIndustryCategories: 0,
  employmentInformationCompanyType: 1,
  employmentInformationBatch: new Date().getFullYear(),
  employmentInformationRecruitPosition: 0,
  employmentInformationRecruitObject: 1,
  employmentInformationRecruitLocationFirst: [],
  employmentInformationRecruitLocationSecond: [],
  employmentInformationRecruitLocationDetail: '',
  employmentInformationStopTime: '',
  employmentInformationOnlineApplicationStatus: '',
  employmentInformationOfficialAnnouncement: '',
  employmentInformationSubmissionWay: '',
  employmentInformationEmployeeReferralCode: ''
})

// 表单校验规则
const informationRules = {
  employmentInformationCompanyName: [
    { required: true, message: '请输入公司名称', trigger: 'blur' }
  ],
  employmentInformationIndustryCategories: [
    { required: true, message: '请选择行业大类', trigger: 'change' }
  ],
  employmentInformationCompanyType: [
    { required: true, message: '请选择企业性质', trigger: 'change' }
  ],
  employmentInformationBatch: [
    { required: true, message: '请输入招聘批次', trigger: 'blur' }
  ],
  employmentInformationRecruitPosition: [
    { required: true, message: '请选择招聘岗位', trigger: 'change' }
  ],
  employmentInformationRecruitObject: [
    { required: true, message: '请选择招聘对象', trigger: 'change' }
  ],
  employmentInformationRecruitLocationFirst: [
    { required: true, message: '请选择招聘省份', trigger: 'change' }
  ],
  employmentInformationRecruitLocationSecond: [
    { required: true, message: '请选择招聘城市', trigger: 'change' }
  ],
  employmentInformationStopTime: [
    { required: true, message: '请选择截止时间', trigger: 'change' }
  ],
  employmentInformationOnlineApplicationStatus: [
    { required: true, message: '请输入网申状态', trigger: 'blur' }
  ],
  employmentInformationSubmissionWay: [
    { required: true, message: '请输入投递方式', trigger: 'blur' }
  ]
}

// 根据选中的省份动态计算可用的城市
const availableCities = computed(() => {
  if (!informationForm.employmentInformationRecruitLocationFirst || informationForm.employmentInformationRecruitLocationFirst.length === 0) {
    return []
  }
  return allCities.value.filter(city => 
    informationForm.employmentInformationRecruitLocationFirst.includes(city.cityMapProvinceId)
  )
})

// 企业性质名称映射
const getCompanyTypeName = (type: number) => {
  const map: Record<number, string> = {
    1: '国有企业',
    2: '民营企业',
    3: '外资企业',
    4: '合资企业',
    5: '其他'
  }
  return map[type] || '-'
}

// 招聘对象名称映射
const getRecruitObjectName = (type: number) => {
  const map: Record<number, string> = {
    1: '应届生',
    2: '社会招聘',
    3: '实习生'
  }
  return map[type] || '-'
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

// 加载省份数据
const loadProvinces = async () => {
  try {
    const response = await provinceMapApi.getAllProvince()
    provinces.value = response.data
  } catch (error) {
    console.error('加载省份数据失败:', error)
  }
}

// 加载所有城市数据
const loadAllCities = async () => {
  try {
    // 加载所有省份的城市
    for (const province of provinces.value) {
      const response = await provinceMapApi.getCityByProvinceId(province.provinceMapId)
      allCities.value.push(...response.data)
    }
  } catch (error) {
    console.error('加载城市数据失败:', error)
  }
}

// 加载岗位数据
const loadPositions = async () => {
  try {
    const response = await recruitPositionApi.getAllRecruitPositions()
    positions.value = response.data
  } catch (error) {
    console.error('加载岗位数据失败:', error)
  }
}

// 省份变更时清空城市选择
const handleProvinceChange = () => {
  // 清空已选城市中不在当前省份的城市
  informationForm.employmentInformationRecruitLocationSecond = informationForm.employmentInformationRecruitLocationSecond.filter(cityId => {
    const city = allCities.value.find(c => c.cityMapId === cityId)
    return city && informationForm.employmentInformationRecruitLocationFirst.includes(city.cityMapProvinceId)
  })
}

// 获取信息列表
const getInformationList = async () => {
  loading.value = true
  try {
    const response = await employmentInformationApi.getEmploymentInformationPage(
      pagination.current,
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
  pagination.current = 1
  getInformationList()
}

// 重置搜索
const handleReset = () => {
  searchForm.employmentInformationCompanyName = ''
  searchForm.employmentInformationBatch = undefined
  searchForm.employmentInformationOnlineApplicationStatus = ''
  pagination.current = 1
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

const handleCurrentChange = (current: number) => {
  pagination.current = current
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
    
    // 从详情数据中提取省份和城市的ID（需要从名称反查）
    const provinceIds: number[] = []
    const cityIds: number[] = []
    
    // 根据省份名称找到省份ID
    if (detail.employmentInformationRecruitLocationFirstName) {
      detail.employmentInformationRecruitLocationFirstName.forEach(provinceName => {
        const province = provinces.value.find(p => p.provinceMapName === provinceName)
        if (province) {
          provinceIds.push(province.provinceMapId)
        }
      })
    }
    
    // 根据城市名称找到城市ID
    if (detail.employmentInformationRecruitLocationSecondName) {
      detail.employmentInformationRecruitLocationSecondName.forEach(cityName => {
        const city = allCities.value.find(c => c.cityMapName === cityName)
        if (city) {
          cityIds.push(city.cityMapId)
        }
      })
    }
    
    Object.assign(informationForm, {
      employmentInformationId: detail.employmentInformationId,
      employmentInformationCompanyName: detail.employmentInformationCompanyName,
      employmentInformationIndustryCategories: 101, // 需要从名称反查，这里先用默认值
      employmentInformationCompanyType: detail.employmentInformationCompanyType,
      employmentInformationBatch: detail.employmentInformationBatch,
      employmentInformationRecruitPosition: detail.employmentInformationRecruitPosition,
      employmentInformationRecruitObject: detail.employmentInformationRecruitObject,
      employmentInformationRecruitLocationFirst: provinceIds,
      employmentInformationRecruitLocationSecond: cityIds,
      employmentInformationRecruitLocationDetail: detail.employmentInformationRecruitLocationDetail ? detail.employmentInformationRecruitLocationDetail.join(',') : '',
      employmentInformationStopTime: detail.employmentInformationStopTime,
      employmentInformationOnlineApplicationStatus: detail.employmentInformationOnlineApplicationStatus,
      employmentInformationOfficialAnnouncement: detail.employmentInformationOfficialAnnouncement,
      employmentInformationSubmissionWay: detail.employmentInformationSubmissionWay,
      employmentInformationEmployeeReferralCode: detail.employmentInformationEmployeeReferralCode
    })
    showCreateDialog.value = true
  } catch (error) {
    console.error('获取信息详情失败:', error)
    ElMessage.error('获取信息详情失败')
  }
}

// 删除信息
const handleDelete = (row: EmploymentInformationPageVO) => {
  ElMessageBox.confirm(`确定要删除公司"${row.employmentInformationCompanyName}"的招聘信息吗？`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await employmentInformationApi.deleteEmploymentInformation({
        employmentInformationId: row.employmentInformationId
      } as any)
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
    employmentInformationCompanyName: '',
    employmentInformationIndustryCategories: 0,
    employmentInformationCompanyType: 1,
    employmentInformationBatch: new Date().getFullYear(),
    employmentInformationRecruitPosition: 0,
    employmentInformationRecruitObject: 1,
    employmentInformationRecruitLocationFirst: [],
    employmentInformationRecruitLocationSecond: [],
    employmentInformationRecruitLocationDetail: '',
    employmentInformationStopTime: '',
    employmentInformationOnlineApplicationStatus: '',
    employmentInformationOfficialAnnouncement: '',
    employmentInformationSubmissionWay: '',
    employmentInformationEmployeeReferralCode: ''
  })
}

// 组件挂载
onMounted(async () => {
  await loadIndustries()
  await loadProvinces()
  await loadAllCities()
  await loadPositions()
  getInformationList()
})
</script>

<style scoped lang="scss">
.information-management {
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
}

// 响应式设计
@media (max-width: 768px) {
  .information-management {
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
