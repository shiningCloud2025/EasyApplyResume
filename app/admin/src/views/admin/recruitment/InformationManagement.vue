<template>
  <div class="information-management">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">招聘信息管理</h1>
        <p class="page-description">管理企业招聘信息发布</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openCreateDialog">
          <i class="el-icon-plus"></i>
          新增招聘信息
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
        <el-form-item label="公司名称">
          <el-input
            v-model="searchForm.employmentInformationCompanyName"
            placeholder="请输入公司名称"
            clearable
            style="width: 160px"
          />
        </el-form-item>
        <el-form-item label="行业大类">
          <el-select 
            v-model="searchForm.employmentInformationIndustryCategories" 
            placeholder="请选择行业" 
            clearable 
            style="width: 130px"
          >
            <el-option
              v-for="industry in industryList"
              :key="industry.industryMapIndustryCode"
              :label="industry.industryMapIndustryName"
              :value="industry.industryMapIndustryCode"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="企业性质">
          <el-select 
            v-model="searchForm.employmentInformationCompanyType"
            placeholder="请选择" 
            clearable 
            style="width: 120px"
          >
            <el-option label="央企" :value="1" />
            <el-option label="国企" :value="2" />
            <el-option label="国企控股" :value="3" />
            <el-option label="私企" :value="4" />
            <el-option label="外企" :value="5" />
            <el-option label="合资" :value="6" />
            <el-option label="公务员" :value="7" />
            <el-option label="事业编" :value="8" />
          </el-select>
        </el-form-item>
        <el-form-item label="招聘批次">
          <el-select 
            v-model="searchForm.employmentInformationBatch"
            placeholder="请选择" 
            clearable 
            style="width: 130px"
          >
            <el-option label="春招" :value="1" />
            <el-option label="暑期实习" :value="2" />
            <el-option label="秋招" :value="3" />
            <el-option label="寒假实习" :value="4" />
            <el-option label="日常实习" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="招聘岗位">
          <el-select 
            v-model="searchForm.employmentInformationRecruitPosition" 
            placeholder="请选择岗位" 
            clearable 
            style="width: 130px"
          >
            <el-option
              v-for="position in positionList"
              :key="position.recruitPositionId"
              :label="position.recruitPositionName"
              :value="position.recruitPositionId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="招聘对象">
          <el-select 
            v-model="searchForm.employmentInformationRecruitObject" 
            placeholder="请选择" 
            clearable 
            style="width: 110px"
          >
            <el-option label="应届生" :value="1" />
            <el-option label="社会招聘" :value="2" />
            <el-option label="实习生" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="网申状态">
          <el-select
            v-model="searchForm.employmentInformationOnlineApplicationStatus"
            placeholder="请选择状态"
            clearable
            style="width: 110px"
          >
            <el-option label="进行中" value="进行中" />
            <el-option label="已结束" value="已结束" />
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

    <!-- 数据列表 - 完整字段 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column prop="employmentInformationId" label="ID" width="70" />
        <el-table-column label="公司名称" min-width="160">
          <template #default="{ row }">
            {{ row.employmentInformationCompanyName || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="行业" width="100">
          <template #default="{ row }">
            {{ row.employmentInformationIndustryCategoriesName || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="企业性质" width="100" align="center">
          <template #default="{ row }">
            {{ getCompanyTypeName(row.employmentInformationCompanyType) }}
          </template>
        </el-table-column>
        <el-table-column label="招聘批次" width="100" align="center">
          <template #default="{ row }">
            {{ getBatchName(row.employmentInformationBatch) }}
          </template>
        </el-table-column>
        <el-table-column label="招聘岗位" width="120">
          <template #default="{ row }">
            {{ getPositionName(row.employmentInformationRecruitPosition) }}
          </template>
        </el-table-column>
        <el-table-column label="招聘对象" width="100" align="center">
          <template #default="{ row }">
            {{ getRecruitObjectName(row.employmentInformationRecruitObject) }}
          </template>
        </el-table-column>
        <el-table-column label="招聘省份" width="150">
          <template #default="{ row }">
            <el-tag
              v-for="(province, index) in row.employmentInformationRecruitLocationFirstName?.slice(0, 2)" 
              :key="index"
              size="small"
              style="margin-right: 4px; margin-bottom: 4px"
            >
              {{ province }}
            </el-tag>
            <span v-if="row.employmentInformationRecruitLocationFirstName && row.employmentInformationRecruitLocationFirstName.length > 2">
              +{{ row.employmentInformationRecruitLocationFirstName.length - 2 }}
            </span>
            <span v-if="!row.employmentInformationRecruitLocationFirstName || row.employmentInformationRecruitLocationFirstName.length === 0">-</span>
          </template>
        </el-table-column>
        <el-table-column label="招聘城市" width="180">
          <template #default="{ row }">
            <el-tag
              v-for="(city, index) in row.employmentInformationRecruitLocationSecondName?.slice(0, 2)" 
              :key="index"
              size="small"
              style="margin-right: 4px; margin-bottom: 4px"
            >
              {{ city }}
            </el-tag>
            <span v-if="row.employmentInformationRecruitLocationSecondName && row.employmentInformationRecruitLocationSecondName.length > 2">
              +{{ row.employmentInformationRecruitLocationSecondName.length - 2 }}
            </span>
            <span v-if="!row.employmentInformationRecruitLocationSecondName || row.employmentInformationRecruitLocationSecondName.length === 0">-</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="120">
          <template #default="{ row }">
            {{ row.employmentInformationStartTime ? formatDate(row.employmentInformationStartTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="截止时间" width="120">
          <template #default="{ row }">
            {{ row.employmentInformationStopTime ? formatDate(row.employmentInformationStopTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="更新时间" width="120">
          <template #default="{ row }">
            {{ row.employmentInformationUpdatedTime ? formatDate(row.employmentInformationUpdatedTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="网申状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.employmentInformationOnlineApplicationStatus === '进行中' ? 'success' : 'info'">
              {{ row.employmentInformationOnlineApplicationStatus || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="投递方式" min-width="200">
          <template #default="{ row }">
            <el-tooltip :content="row.employmentInformationSubmissionWay || '暂无'" placement="top">
              <div class="text-ellipsis">
                {{ row.employmentInformationSubmissionWay || '-' }}
              </div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="内推码" width="120" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.employmentInformationEmployeeReferralCode" type="success" size="small">
              {{ row.employmentInformationEmployeeReferralCode }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="官方公告" width="120" align="center">
          <template #default="{ row }">
            <el-button 
              v-if="row.employmentInformationOfficialAnnouncement" 
              type="primary" 
              size="small" 
              link
              @click="showAnnouncement(row)"
            >
              查看公告
            </el-button>
            <span v-else>-</span>
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

    <!-- 查看详情对话框 - 完整字段 -->
    <el-dialog
      v-model="showViewDialog"
      title="招聘信息详情"
      width="900px"
    >
      <div class="info-detail" v-if="currentViewInfo">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="招聘信息ID">
            {{ currentViewInfo.employmentInformationId }}
          </el-descriptions-item>
          <el-descriptions-item label="招聘编号" v-if="currentViewInfo.employmentInformationCode">
            {{ currentViewInfo.employmentInformationCode }}
          </el-descriptions-item>
          <el-descriptions-item label="公司名称" :span="2">
            {{ currentViewInfo.employmentInformationCompanyName }}
          </el-descriptions-item>
          <el-descriptions-item label="行业大类">
            {{ currentViewInfo.employmentInformationIndustryCategoriesName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="企业性质">
            {{ getCompanyTypeName(currentViewInfo.employmentInformationCompanyType) }}
          </el-descriptions-item>
          <el-descriptions-item label="招聘批次">
            {{ getBatchName(currentViewInfo.employmentInformationBatch) }}
          </el-descriptions-item>
          <el-descriptions-item label="招聘对象">
            {{ getRecruitObjectName(currentViewInfo.employmentInformationRecruitObject) }}
          </el-descriptions-item>
          <el-descriptions-item label="招聘省份" :span="2">
            <el-tag 
              v-for="(province, index) in currentViewInfo.employmentInformationRecruitLocationFirstName" 
              :key="index"
              style="margin-right: 8px"
            >
              {{ province }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="招聘城市" :span="2">
            <el-tag 
              v-for="(city, index) in currentViewInfo.employmentInformationRecruitLocationSecondName" 
              :key="index"
              style="margin-right: 8px"
            >
              {{ city }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ currentViewInfo.employmentInformationStartTime ? formatDate(currentViewInfo.employmentInformationStartTime) : '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="截止时间">
            {{ formatDate(currentViewInfo.employmentInformationStopTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间" :span="2">
            {{ currentViewInfo.employmentInformationUpdatedTime ? formatDate(currentViewInfo.employmentInformationUpdatedTime) : '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="网申状态">
            <el-tag :type="currentViewInfo.employmentInformationOnlineApplicationStatus === '进行中' ? 'success' : 'info'">
              {{ currentViewInfo.employmentInformationOnlineApplicationStatus }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="投递方式">
            {{ currentViewInfo.employmentInformationSubmissionWay }}
          </el-descriptions-item>
          <el-descriptions-item label="内推码" :span="2" v-if="currentViewInfo.employmentInformationEmployeeReferralCode">
            <el-tag type="success">{{ currentViewInfo.employmentInformationEmployeeReferralCode }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="官方公告" :span="2" v-if="currentViewInfo.employmentInformationOfficialAnnouncement">
            <div class="detail-content">{{ currentViewInfo.employmentInformationOfficialAnnouncement }}</div>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showViewDialog = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 新增/编辑对话框 - 完整字段 -->
    <el-dialog
      v-model="showEditDialog"
      :title="editingInfo ? '编辑招聘信息' : '新增招聘信息'"
      width="900px"
      @close="resetForm"
    >
      <el-form
        ref="infoFormRef"
        :model="infoForm"
        :rules="infoRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
        <el-form-item label="公司名称" prop="employmentInformationCompanyName">
              <el-input 
                v-model="infoForm.employmentInformationCompanyName" 
                placeholder="请输入公司名称"
                maxlength="30"
                show-word-limit
              />
        </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="招聘批次" prop="employmentInformationBatch">
              <el-select 
                v-model="infoForm.employmentInformationBatch" 
                placeholder="请选择招聘批次" 
                style="width: 100%"
              >
                <el-option label="春招" :value="1" />
                <el-option label="暑期实习" :value="2" />
                <el-option label="秋招" :value="3" />
                <el-option label="寒假实习" :value="4" />
                <el-option label="日常实习" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="行业大类" prop="employmentInformationIndustryCategories">
              <el-select 
                v-model="infoForm.employmentInformationIndustryCategories" 
                placeholder="请选择行业" 
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="industry in industryList"
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
                v-model="infoForm.employmentInformationCompanyType" 
                placeholder="请选择企业性质"
                style="width: 100%"
              >
                <el-option label="央企" :value="1" />
                <el-option label="国企" :value="2" />
                <el-option label="国企控股" :value="3" />
                <el-option label="私企" :value="4" />
                <el-option label="外企" :value="5" />
                <el-option label="合资" :value="6" />
                <el-option label="公务员" :value="7" />
                <el-option label="事业编" :value="8" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="招聘岗位" prop="employmentInformationRecruitPosition">
              <el-select 
                v-model="infoForm.employmentInformationRecruitPosition" 
                placeholder="请选择岗位"
                style="width: 100%"
                filterable
              >
                <el-option
                  v-for="position in positionList"
                  :key="position.recruitPositionId"
                  :label="position.recruitPositionName"
                  :value="position.recruitPositionId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="招聘对象" prop="employmentInformationRecruitObject">
              <el-select 
                v-model="infoForm.employmentInformationRecruitObject" 
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
            <el-form-item label="招聘省份" prop="employmentInformationRecruitLocationFirstList">
              <el-select 
                v-model="infoForm.employmentInformationRecruitLocationFirstList" 
                placeholder="请选择省份（可多选）" 
                multiple
                collapse-tags
                style="width: 100%"
                @focus="handleProvinceFocus"
              >
                <el-option
                  v-for="province in provinceList"
                  :key="province.provinceMapPid"
                  :label="province.provinceMapPname"
                  :value="province.provinceMapPid"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="招聘城市" prop="employmentInformationRecruitLocationSecondList">
              <el-select 
                v-model="infoForm.employmentInformationRecruitLocationSecondList" 
                placeholder="请先选择省份" 
                multiple
                collapse-tags
                style="width: 100%"
                :disabled="!infoForm.employmentInformationRecruitLocationFirstList || infoForm.employmentInformationRecruitLocationFirstList.length === 0"
                @focus="handleCityFocus"
              >
                <el-option
                  v-for="city in cityList"
                  :key="city.cityMapCid"
                  :label="city.cityMapCname"
                  :value="city.cityMapCid"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
        <el-form-item label="截止时间" prop="employmentInformationStopTime">
          <el-date-picker
                v-model="infoForm.employmentInformationStopTime"
                type="date"
                placeholder="选择截止日期"
            style="width: 100%"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
          />
        </el-form-item>
          </el-col>
          <el-col :span="12">
        <el-form-item label="网申状态" prop="employmentInformationOnlineApplicationStatus">
              <el-select 
                v-model="infoForm.employmentInformationOnlineApplicationStatus" 
                placeholder="请选择网申状态" 
                style="width: 100%"
              >
                <el-option label="进行中" value="进行中" />
                <el-option label="已结束" value="已结束" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="投递方式" prop="employmentInformationSubmissionWay">
          <el-input
            v-model="infoForm.employmentInformationSubmissionWay" 
            placeholder="提供招聘网址"
            maxlength="1024"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="内推码" prop="employmentInformationEmployeeReferralCode">
          <el-input
            v-model="infoForm.employmentInformationEmployeeReferralCode" 
            placeholder="请输入内推码（选填）"
            maxlength="255"
          />
        </el-form-item>
        
        <el-form-item label="官方公告" prop="employmentInformationOfficialAnnouncement">
          <el-input
            v-model="infoForm.employmentInformationOfficialAnnouncement"
            type="textarea"
            :rows="4"
            placeholder="请输入官方公告（选填）"
            maxlength="1024"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showEditDialog = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, watch, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDate } from '@/utils'
import { employmentInformationApi, industryMapApi, recruitPositionApi, provinceMapApi } from '@/api/admin'
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
const showViewDialog = ref(false)
const showEditDialog = ref(false)
const editingInfo = ref<EmploymentInformationPageVO | null>(null)
const currentViewInfo = ref<EmploymentInformationInfoVO | null>(null)

// 下拉选择数据
const industryList = ref<any[]>([])
const positionList = ref<any[]>([])
const provinceList = ref<any[]>([])
const cityList = ref<any[]>([])

// 搜索表单
const searchForm = reactive<EmploymentInformationQuery>({
  employmentInformationCompanyName: '',
  employmentInformationIndustryCategories: undefined,
  employmentInformationCompanyType: undefined,
  employmentInformationBatch: undefined,
  employmentInformationRecruitPosition: undefined,
  employmentInformationRecruitObject: undefined,
  employmentInformationOnlineApplicationStatus: undefined
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 表格数据
const tableData = ref<EmploymentInformationPageVO[]>([])
const allTableData = ref<EmploymentInformationPageVO[]>([])

// 表单 - 完整字段
const infoFormRef = ref<FormInstance>()
const infoForm = reactive<EmploymentInformationForm>({
  employmentInformationId: undefined,
  employmentInformationCode: undefined,
  employmentInformationCompanyName: '',
  employmentInformationIndustryCategories: undefined as any,
  employmentInformationCompanyType: undefined as any,
  employmentInformationBatch: undefined as any,
  employmentInformationRecruitPosition: undefined as any,
  employmentInformationRecruitObject: undefined as any,
  employmentInformationRecruitLocationFirstList: [],
  employmentInformationRecruitLocationSecondList: [],
  employmentInformationRecruitLocationDetail: '',
  employmentInformationStopTime: '',
  employmentInformationOnlineApplicationStatus: '',
  employmentInformationOfficialAnnouncement: '',
  employmentInformationSubmissionWay: '',
  employmentInformationEmployeeReferralCode: ''
})

// 表单校验规则 - 完整必填字段
const infoRules = {
  employmentInformationCompanyName: [
    { required: true, message: '请输入公司名称', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  employmentInformationIndustryCategories: [
    { required: true, message: '请选择行业大类', trigger: 'change' }
  ],
  employmentInformationCompanyType: [
    { required: true, message: '请选择企业性质', trigger: 'change' }
  ],
  employmentInformationBatch: [
    { required: true, message: '请选择招聘批次', trigger: 'change' }
  ],
  employmentInformationRecruitPosition: [
    { required: true, message: '请选择招聘岗位', trigger: 'change' }
  ],
  employmentInformationRecruitObject: [
    { required: true, message: '请选择招聘对象', trigger: 'change' }
  ],
  employmentInformationRecruitLocationFirstList: [
    { required: true, message: '请选择招聘省份', trigger: 'change' }
  ],
  employmentInformationRecruitLocationSecondList: [
    { required: true, message: '请选择招聘城市', trigger: 'change' }
  ],
  employmentInformationStopTime: [
    { required: true, message: '请选择截止时间', trigger: 'change' }
  ],
  employmentInformationOnlineApplicationStatus: [
    { required: true, message: '请选择网申状态', trigger: 'change' }
  ],
  employmentInformationSubmissionWay: [
    { required: true, message: '请输入投递方式', trigger: 'blur' },
    { max: 1024, message: '最多1024个字符', trigger: 'blur' }
  ]
}

// 获取企业性质名称
const getCompanyTypeName = (type: number) => {
  const typeMap: Record<number, string> = {
    1: '央企',
    2: '国企',
    3: '国企控股',
    4: '私企',
    5: '外企',
    6: '合资',
    7: '公务员',
    8: '事业编'
  }
  return typeMap[type] || '-'
}

// 获取招聘批次名称
const getBatchName = (batch: number) => {
  const batchMap: Record<number, string> = {
    1: '春招',
    2: '暑期实习',
    3: '秋招',
    4: '寒假实习',
    5: '日常实习'
  }
  return batchMap[batch] || '-'
}

// 获取招聘对象名称
const getRecruitObjectName = (obj: number) => {
  const objMap: Record<number, string> = {
    1: '应届生',
    2: '社会招聘',
    3: '实习生'
  }
  return objMap[obj] || '-'
}

// 获取岗位名称
const getPositionName = (positionId: number) => {
  const position = positionList.value.find(p => p.recruitPositionId === positionId)
  return position?.recruitPositionName || '-'
}

// 显示官方公告
const showAnnouncement = (row: EmploymentInformationPageVO) => {
  ElMessageBox.alert(row.employmentInformationOfficialAnnouncement, '官方公告', {
    confirmButtonText: '关闭',
    dangerouslyUseHTMLString: false
  })
}

// 获取行业列表
const getIndustryList = async () => {
  console.log('🏢 [行业] 开始获取行业列表...')
  try {
    const response = await industryMapApi.findAllIndustryMap()
    console.log('🏢 [行业] API响应:', response)
    console.log('🏢 [行业] 数据:', response.data)
    industryList.value = response.data || []
    console.log('✅ [行业] 加载成功，共', industryList.value.length, '个行业')
  } catch (error) {
    console.error('❌ [行业] 获取失败:', error)
    ElMessage.error('获取行业列表失败')
  }
}

// 获取岗位列表
const getPositionList = async () => {
  console.log('💼 [岗位] 开始获取岗位列表...')
  try {
    const response = await recruitPositionApi.getAllRecruitPositions()
    console.log('💼 [岗位] API响应:', response)
    console.log('💼 [岗位] 数据:', response.data)
    positionList.value = response.data || []
    console.log('✅ [岗位] 加载成功，共', positionList.value.length, '个岗位')
  } catch (error) {
    console.error('❌ [岗位] 获取失败:', error)
    ElMessage.error('获取岗位列表失败')
  }
}

// 获取省份列表
const getProvinceList = async () => {
  // 如果已经加载过，不重复加载
  if (provinceList.value.length > 0) {
    console.log('✅ 省份列表已缓存，共', provinceList.value.length, '个省份')
    return
  }
  
  console.log('🌍 [省份] 开始获取省份列表...')
  try {
    const response = await provinceMapApi.getAllProvince()
    console.log('🌍 [省份] API响应:', response)
    console.log('🌍 [省份] 数据类型:', typeof response.data, Array.isArray(response.data))
    console.log('🌍 [省份] 数据内容:', response.data)
    
    if (response && response.data) {
      provinceList.value = response.data
      console.log('✅ [省份] 赋值成功！provinceList.value =', provinceList.value)
      console.log('✅ [省份] 共', provinceList.value.length, '个省份')
      console.log('✅ [省份] 第一个省份:', provinceList.value[0])
    } else {
      console.error('❌ [省份] 响应数据为空')
      provinceList.value = []
    }
  } catch (error) {
    console.error('❌ [省份] 获取失败:', error)
    ElMessage.error('获取省份列表失败')
  }
}

// 省份选择框获得焦点时加载数据
const handleProvinceFocus = () => {
  console.log('👆 [省份] 用户点击了省份选择框')
  getProvinceList()
}

// 记录最后一个被选择的省份ID
const lastSelectedProvinceId = ref<number | null>(null)

// 监听省份选择，记录最后选择的省份
watch(() => infoForm.employmentInformationRecruitLocationFirstList, (newProvinces, oldProvinces) => {
  console.log('👀 [省份变化] 新选择的省份:', newProvinces)
  console.log('👀 [省份变化] 旧的省份:', oldProvinces)
  
  if (newProvinces && newProvinces.length > 0) {
    // 找出新增的省份（最后一个选择的）
    const newProvince = newProvinces.find(id => !oldProvinces?.includes(id))
    if (newProvince) {
      lastSelectedProvinceId.value = newProvince
      console.log('📍 [省份变化] 最后选择的省份ID:', newProvince)
      // 清空城市下拉列表，等待用户点击时重新加载
      cityList.value = []
    }
  } else {
    console.log('🗑️ [省份变化] 清空所有省份')
    lastSelectedProvinceId.value = null
    cityList.value = []
    infoForm.employmentInformationRecruitLocationSecondList = []
  }
}, { deep: true })

// 城市选择框获得焦点时，加载最后选择的省份对应的城市
const handleCityFocus = async () => {
  console.log('👆 [城市] 用户点击了城市选择框')
  console.log('👆 [城市] 已选省份:', infoForm.employmentInformationRecruitLocationFirstList)
  console.log('👆 [城市] 最后选择的省份ID:', lastSelectedProvinceId.value)
  
  // 如果没有选择省份，提示用户
  if (!infoForm.employmentInformationRecruitLocationFirstList || infoForm.employmentInformationRecruitLocationFirstList.length === 0) {
    ElMessage.warning('请先选择省份')
    return
  }
  
  // 如果有最后选择的省份，加载该省份的城市
  if (lastSelectedProvinceId.value) {
    try {
      console.log(`🏙️ [城市加载] 正在加载省份ID ${lastSelectedProvinceId.value} 的城市...`)
      const response = await provinceMapApi.getCityByProvinceId(lastSelectedProvinceId.value)
      cityList.value = response.data || []
      console.log(`✅ [城市加载] 加载了 ${cityList.value.length} 个城市`)
    } catch (error) {
      console.error('❌ [城市加载] 获取城市列表失败:', error)
      ElMessage.error('获取城市列表失败')
    }
  } else if (infoForm.employmentInformationRecruitLocationFirstList.length > 0) {
    // 如果没有记录最后选择的省份，但是有省份被选中，使用第一个省份
    const firstProvinceId = infoForm.employmentInformationRecruitLocationFirstList[0]
    lastSelectedProvinceId.value = firstProvinceId
    console.log(`🏙️ [城市加载] 使用第一个省份ID ${firstProvinceId}`)
    try {
      const response = await provinceMapApi.getCityByProvinceId(firstProvinceId)
      cityList.value = response.data || []
      console.log(`✅ [城市加载] 加载了 ${cityList.value.length} 个城市`)
    } catch (error) {
      console.error('❌ [城市加载] 获取城市列表失败:', error)
      ElMessage.error('获取城市列表失败')
    }
  }
}

// 获取列表(一次性获取所有数据)
const getInfoList = async () => {
  loading.value = true
  try {
    const response = await employmentInformationApi.getEmploymentInformationPage(
      1,
      10000,
      searchForm
    )
    
    console.log('📊 招聘信息分页数据:', response.data)
    console.log('📋 记录列表:', response.data.records)
    if (response.data.records && response.data.records.length > 0) {
      console.log('🔍 第一条数据详情:', response.data.records[0])
    }
    
    allTableData.value = response.data.records
    pagination.total = allTableData.value.length
    pagination.current = 1
    updateTableData()
  } catch (error) {
    console.error('获取列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 前端分页:更新当前页数据
const updateTableData = () => {
  const start = (pagination.current - 1) * pagination.size
  const end = start + pagination.size
  tableData.value = allTableData.value.slice(start, end)
}

// 新增
const openCreateDialog = () => {
  editingInfo.value = null
  resetForm()
  showEditDialog.value = true
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getInfoList()
}

// 重置搜索
const handleReset = () => {
  searchForm.employmentInformationCompanyName = ''
  searchForm.employmentInformationIndustryCategories = undefined
  searchForm.employmentInformationCompanyType = undefined
  searchForm.employmentInformationBatch = undefined
  searchForm.employmentInformationRecruitPosition = undefined
  searchForm.employmentInformationRecruitObject = undefined
  searchForm.employmentInformationOnlineApplicationStatus = undefined
  pagination.current = 1
  getInfoList()
}

// 刷新数据
const refreshData = () => {
  getInfoList()
}

// 分页变更
const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  updateTableData()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  updateTableData()
}

// 查看详情
const handleView = async (row: EmploymentInformationPageVO) => {
  try {
    const response = await employmentInformationApi.getEmploymentInformationInfo(row.employmentInformationId)
    console.log('🔍 招聘信息详情数据:', response.data)
    currentViewInfo.value = response.data
    showViewDialog.value = true
  } catch (error) {
    console.error('获取详情失败:', error)
    ElMessage.error('获取详情失败')
  }
}

// 编辑
const handleEdit = async (row: EmploymentInformationPageVO) => {
  try {
    const response = await employmentInformationApi.getEmploymentInformationInfo(row.employmentInformationId)
    const detail = response.data
    
    console.log('✏️ 编辑数据:', detail)
    
    editingInfo.value = row
    
    // 注意：详细地址如果是数组，需要转换为字符串
    const detailAddress = Array.isArray(detail.employmentInformationRecruitLocationDetail) 
      ? detail.employmentInformationRecruitLocationDetail.join(', ') 
      : (detail.employmentInformationRecruitLocationDetail || '')
    
    // 确保行业列表已加载
    if (industryList.value.length === 0) {
      await getIndustryList()
    }
    
    // 根据行业名称反查行业 ID
    let industryId: number | undefined = undefined
    if (detail.employmentInformationIndustryCategoriesName) {
      const foundIndustry = industryList.value.find(
        (ind: any) => ind.industryMapIndustryName === detail.employmentInformationIndustryCategoriesName
      )
      if (foundIndustry) {
        industryId = foundIndustry.industryMapIndustryCode
      }
    }
    
    // 加载省份列表
    if (provinceList.value.length === 0) {
      await getProvinceList()
    }
    
    // 根据省份名称反查省份 ID
    let provinceIds: number[] = []
    if (detail.employmentInformationRecruitLocationFirstName && Array.isArray(detail.employmentInformationRecruitLocationFirstName)) {
      for (const provinceName of detail.employmentInformationRecruitLocationFirstName) {
        const foundProvince = provinceList.value.find(
          (p: any) => p.provinceMapPname === provinceName
        )
        if (foundProvince) {
          provinceIds.push(foundProvince.provinceMapPid)
        }
      }
    }
    
    // 加载城市列表并反查城市 ID
    let cityIds: number[] = []
    if (provinceIds.length > 0 && detail.employmentInformationRecruitLocationSecondName && Array.isArray(detail.employmentInformationRecruitLocationSecondName)) {
      // 加载第一个省份的城市
      lastSelectedProvinceId.value = provinceIds[0]
      const cityResponse = await provinceMapApi.getCityByProvinceId(provinceIds[0])
      cityList.value = cityResponse.data || []
      
      for (const cityName of detail.employmentInformationRecruitLocationSecondName) {
        const foundCity = cityList.value.find(
          (c: any) => c.cityMapCname === cityName
        )
        if (foundCity) {
          cityIds.push(foundCity.cityMapCid)
        }
      }
    }
    
    Object.assign(infoForm, {
      employmentInformationId: detail.employmentInformationId,
      employmentInformationCode: detail.employmentInformationCode,
      employmentInformationCompanyName: detail.employmentInformationCompanyName,
      employmentInformationIndustryCategories: industryId,
      employmentInformationCompanyType: detail.employmentInformationCompanyType,
      employmentInformationBatch: detail.employmentInformationBatch,
      employmentInformationRecruitPosition: detail.employmentInformationRecruitPosition,
      employmentInformationRecruitObject: detail.employmentInformationRecruitObject,
      employmentInformationRecruitLocationFirstList: provinceIds,
      employmentInformationRecruitLocationSecondList: cityIds,
      employmentInformationRecruitLocationDetail: detailAddress,
      employmentInformationStopTime: detail.employmentInformationStopTime,
      employmentInformationOnlineApplicationStatus: detail.employmentInformationOnlineApplicationStatus,
      employmentInformationOfficialAnnouncement: detail.employmentInformationOfficialAnnouncement || '',
      employmentInformationSubmissionWay: detail.employmentInformationSubmissionWay,
      employmentInformationEmployeeReferralCode: detail.employmentInformationEmployeeReferralCode || ''
    })
    showEditDialog.value = true
  } catch (error) {
    console.error('获取详情失败:', error)
    ElMessage.error('获取详情失败')
  }
}

// 删除
const handleDelete = (row: EmploymentInformationPageVO) => {
  ElMessageBox.confirm(`确定要删除"${row.employmentInformationCompanyName}"的招聘信息吗？`, '确认删除', {
    type: 'warning'
  }).then(async () => {
    try {
      await employmentInformationApi.deleteEmploymentInformation(row.employmentInformationId)
      ElMessage.success('删除成功')
      getInfoList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const handleSubmit = async () => {
  if (!infoFormRef.value) return
  
  try {
    await infoFormRef.value.validate()
    submitting.value = true
    
    console.log('📤 提交的表单数据:', infoForm)
    
    if (editingInfo.value) {
      await employmentInformationApi.updateEmploymentInformation(infoForm)
      ElMessage.success('更新成功')
    } else {
      await employmentInformationApi.addEmploymentInformation(infoForm)
      ElMessage.success('创建成功')
    }
    
    showEditDialog.value = false
    getInfoList()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (infoFormRef.value) {
    infoFormRef.value.resetFields()
  }
  
  editingInfo.value = null
  Object.assign(infoForm, {
    employmentInformationId: undefined,
    employmentInformationCode: undefined,
    employmentInformationCompanyName: '',
    employmentInformationIndustryCategories: undefined,
    employmentInformationCompanyType: undefined,
    employmentInformationBatch: undefined,
    employmentInformationRecruitPosition: undefined,
    employmentInformationRecruitObject: undefined,
    employmentInformationRecruitLocationFirstList: [],
    employmentInformationRecruitLocationSecondList: [],
    employmentInformationRecruitLocationDetail: '',
    employmentInformationStopTime: '',
    employmentInformationOnlineApplicationStatus: '',
    employmentInformationOfficialAnnouncement: '',
    employmentInformationSubmissionWay: '',
    employmentInformationEmployeeReferralCode: ''
  })
}

// 组件挂载
onMounted(() => {
  getIndustryList()
  getPositionList()
  // 省份列表在点击时才加载，提高性能
  getInfoList()
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

  .info-detail {
    .detail-content {
      line-height: 1.6;
      color: #374151;
      white-space: pre-wrap;
    }
  }

  .text-ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 200px;
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
