<template>
  <div class="question-bank-management">
    <div class="page-header">
      <div class="header-content">
        <h2 class="page-title">题库题目管理</h2>
        <p class="page-description">维护笔试专项题库题目，支持分页查询、分类联动、查看、新增、编辑与删除。</p>
      </div>
      <div class="header-actions">
        <el-button v-if="canAddQuestionBank" type="primary" @click="openCreateDialog">
          <el-icon><Plus /></el-icon>
          新增题目
        </el-button>
        <el-button @click="refreshData">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="题目描述">
          <el-input
            v-model="searchForm.questionBankDescription"
            placeholder="请输入题目描述关键词"
            clearable
            style="width: 260px"
          />
        </el-form-item>
        <el-form-item label="题目类型">
          <el-select
            v-model="searchForm.questionBankType"
            placeholder="请选择题目类型"
            clearable
            style="width: 180px"
          >
            <el-option
              v-for="item in questionTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属大类">
          <el-select
            v-model="searchForm.questionFirstCategoryId"
            placeholder="请选择所属大类"
            clearable
            filterable
            style="width: 200px"
            @change="handleSearchFirstCategoryChange"
          >
            <el-option
              v-for="item in questionFirstCategoryOptions"
              :key="item.questionFirstCategoryId"
              :label="item.questionFirstCategoryName"
              :value="item.questionFirstCategoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属小类">
          <el-select
            v-model="searchForm.questionSecondCategoryId"
            placeholder="请选择所属小类"
            clearable
            filterable
            :loading="searchSecondCategoryLoading"
            :disabled="!searchForm.questionFirstCategoryId"
            style="width: 200px"
          >
            <el-option
              v-for="item in filteredSearchSecondCategoryOptions"
              :key="item.questionSecondCategoryId"
              :label="item.questionSecondCategoryName"
              :value="item.questionSecondCategoryId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="题目难度">
          <el-select
            v-model="searchForm.questionBankDifficulty"
            placeholder="请选择题目难度"
            clearable
            style="width: 180px"
          >
            <el-option
              v-for="item in difficultyOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="题目状态">
          <el-select
            v-model="searchForm.questionBankState"
            placeholder="请选择题目状态"
            clearable
            style="width: 180px"
          >
            <el-option
              v-for="item in stateOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无题库题目数据"
      >
        <el-table-column prop="questionBankId" label="题目ID" width="120" align="center" />
        <el-table-column prop="questionBankDescription" label="题目描述" min-width="280" show-overflow-tooltip />
        <el-table-column label="题目类型" width="110" align="center">
          <template #default="{ row }">
            <el-tag>{{ getQuestionTypeLabel(row.questionBankType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="questionFirstCategoryName" label="题目大类" min-width="140" />
        <el-table-column prop="questionSecondCategoryName" label="题目小类" min-width="140" />
        <el-table-column label="题目难度" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="getDifficultyTagType(row.questionBankDifficulty)">
              {{ getDifficultyLabel(row.questionBankDifficulty) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="题目状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="row.questionBankState === 1 ? 'success' : 'danger'">
              {{ getStateLabel(row.questionBankState) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="questionBankCreateTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.questionBankCreateTime) }}
          </template>
        </el-table-column>
        <el-table-column v-if="showQuestionBankActionColumn" label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-if="canViewQuestionBankInfo" type="info" size="default" @click="handleView(row)">查看</el-button>
            <el-button v-if="canUpdateQuestionBank" type="primary" size="default" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="canDeleteQuestionBank" type="danger" size="default" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

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

    <el-dialog
      v-model="showCreateDialog"
      :title="editingQuestionBank ? '编辑题库题目' : '新增题库题目'"
      width="920px"
      @close="resetForm"
    >
      <el-form
        ref="questionBankFormRef"
        :model="questionBankForm"
        :rules="questionBankRules"
        label-width="110px"
      >
        <el-form-item label="题目ID" v-if="editingQuestionBank">
          <el-input v-model="questionBankForm.questionBankId" disabled />
        </el-form-item>

        <el-form-item label="题目描述" prop="questionBankDescription">
          <el-input
            v-model="questionBankForm.questionBankDescription"
            type="textarea"
            :rows="4"
            placeholder="请输入题目描述"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>

        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="题目类型" prop="questionBankType">
              <el-select
                v-model="questionBankForm.questionBankType"
                placeholder="请选择题目类型"
                style="width: 100%"
                @change="handleFormTypeChange"
              >
                <el-option
                  v-for="item in questionTypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="题目难度" prop="questionBankDifficulty">
              <el-select
                v-model="questionBankForm.questionBankDifficulty"
                placeholder="请选择题目难度"
                style="width: 100%"
              >
                <el-option
                  v-for="item in difficultyOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="所属大类" prop="questionFirstCategoryId">
              <el-select
                v-model="questionBankForm.questionFirstCategoryId"
                placeholder="请选择所属大类"
                filterable
                style="width: 100%"
                @change="handleFormFirstCategoryChange"
              >
                <el-option
                  v-for="item in questionFirstCategoryOptions"
                  :key="item.questionFirstCategoryId"
                  :label="item.questionFirstCategoryName"
                  :value="item.questionFirstCategoryId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属小类" prop="questionSecondCategoryId">
              <el-select
                v-model="questionBankForm.questionSecondCategoryId"
                placeholder="请选择所属小类"
                filterable
                :loading="formSecondCategoryLoading"
                :disabled="!questionBankForm.questionFirstCategoryId"
                style="width: 100%"
              >
                <el-option
                  v-for="item in filteredFormSecondCategoryOptions"
                  :key="item.questionSecondCategoryId"
                  :label="item.questionSecondCategoryName"
                  :value="item.questionSecondCategoryId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16" v-if="showOptionABFields">
          <el-col :span="12">
            <el-form-item label="选项A" prop="questionBankOptionA" :required="showOptionABFields">
              <el-input
                v-model="questionBankForm.questionBankOptionA"
                placeholder="请输入选项A"
                maxlength="255"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选项B" prop="questionBankOptionB" :required="showOptionABFields">
              <el-input
                v-model="questionBankForm.questionBankOptionB"
                placeholder="请输入选项B"
                maxlength="255"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16" v-if="showOptionCDFields">
          <el-col :span="12">
            <el-form-item label="选项C" prop="questionBankOptionC" :required="showOptionCDFields">
              <el-input
                v-model="questionBankForm.questionBankOptionC"
                placeholder="请输入选项C"
                maxlength="255"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="选项D" prop="questionBankOptionD" :required="showOptionCDFields">
              <el-input
                v-model="questionBankForm.questionBankOptionD"
                placeholder="请输入选项D"
                maxlength="255"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item
          label="正确答案"
          prop="questionBankCorrectAnswer"
          :required="showCorrectAnswerField"
          v-if="showCorrectAnswerField"
        >
          <el-input
            v-model="questionBankForm.questionBankCorrectAnswer"
            :placeholder="correctAnswerPlaceholder"
            maxlength="100"
            show-word-limit
          />
          <div class="field-hint">{{ correctAnswerHint }}</div>
        </el-form-item>

        <el-form-item
          label="参考答案"
          prop="questionBankReferenceAnswer"
          :required="showReferenceAnswerField"
          v-if="showReferenceAnswerField"
        >
          <el-input
            v-model="questionBankForm.questionBankReferenceAnswer"
            type="textarea"
            :rows="4"
            placeholder="请输入参考答案"
            maxlength="4000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="题目图片">
          <el-input
            v-model="questionBankForm.questionBankImage"
            placeholder="请输入题目图片地址"
            maxlength="1024"
            show-word-limit
            clearable
          />
        </el-form-item>

        <el-form-item label="代码段">
          <el-input
            v-model="questionBankForm.questionBankCode"
            type="textarea"
            :rows="6"
            placeholder="请输入题目代码段"
          />
        </el-form-item>

        <el-form-item label="题目解析">
          <el-input
            v-model="questionBankForm.questionBankAnalysis"
            type="textarea"
            :rows="4"
            placeholder="请输入题目解析"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="题目状态" prop="questionBankState">
          <el-select
            v-model="questionBankForm.questionBankState"
            placeholder="请选择题目状态"
            style="width: 100%"
          >
            <el-option
              v-for="item in stateOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showCreateDialog = false">取消</el-button>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="showViewDialog" title="题库题目详情" width="920px">
      <div class="question-bank-detail" v-if="currentViewQuestionBank">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="题目ID">
            <el-tag type="primary">{{ currentViewQuestionBank.questionBankId }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="题目类型">
            <el-tag>{{ getQuestionTypeLabel(currentViewQuestionBank.questionBankType) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="所属大类">
            {{ displayText(currentViewQuestionBank.questionFirstCategoryName) }}
          </el-descriptions-item>
          <el-descriptions-item label="所属小类">
            {{ displayText(currentViewQuestionBank.questionSecondCategoryName) }}
          </el-descriptions-item>
          <el-descriptions-item label="题目难度">
            <el-tag :type="getDifficultyTagType(currentViewQuestionBank.questionBankDifficulty)">
              {{ getDifficultyLabel(currentViewQuestionBank.questionBankDifficulty) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="题目状态">
            <el-tag :type="currentViewQuestionBank.questionBankState === 1 ? 'success' : 'danger'">
              {{ getStateLabel(currentViewQuestionBank.questionBankState) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="题目描述" :span="2">
            <div class="multiline-text">{{ displayText(currentViewQuestionBank.questionBankDescription) }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="选项A" v-if="hasText(currentViewQuestionBank.questionBankOptionA)">
            <div class="multiline-text">{{ currentViewQuestionBank.questionBankOptionA }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="选项B" v-if="hasText(currentViewQuestionBank.questionBankOptionB)">
            <div class="multiline-text">{{ currentViewQuestionBank.questionBankOptionB }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="选项C" v-if="hasText(currentViewQuestionBank.questionBankOptionC)">
            <div class="multiline-text">{{ currentViewQuestionBank.questionBankOptionC }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="选项D" v-if="hasText(currentViewQuestionBank.questionBankOptionD)">
            <div class="multiline-text">{{ currentViewQuestionBank.questionBankOptionD }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="正确答案" :span="2" v-if="hasText(currentViewQuestionBank.questionBankCorrectAnswer)">
            <div class="multiline-text">{{ currentViewQuestionBank.questionBankCorrectAnswer }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="参考答案" :span="2" v-if="hasText(currentViewQuestionBank.questionBankReferenceAnswer)">
            <div class="multiline-text">{{ currentViewQuestionBank.questionBankReferenceAnswer }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="题目图片" :span="2" v-if="hasText(currentViewQuestionBank.questionBankImage)">
            <div class="multiline-text">{{ currentViewQuestionBank.questionBankImage }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="代码段" :span="2" v-if="hasText(currentViewQuestionBank.questionBankCode)">
            <div class="code-text">{{ currentViewQuestionBank.questionBankCode }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="题目解析" :span="2" v-if="hasText(currentViewQuestionBank.questionBankAnalysis)">
            <div class="multiline-text">{{ currentViewQuestionBank.questionBankAnalysis }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(currentViewQuestionBank.questionBankCreateTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDateTime(currentViewQuestionBank.questionBankUpdateTime) }}
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
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils'
import { questionBankApi, questionFirstCategoryApi, questionSecondCategoryApi } from '@/api/admin'
import { useAuthStore } from '@/store/auth'
import type {
  AdminQuestionBankForm,
  AdminQuestionBankInfoVO,
  AdminQuestionBankPageVO,
  AdminQuestionBankQuery,
  AdminQuestionFirstCategoryInfoVO,
  AdminQuestionSecondCategoryInfoVO
} from '@/types/admin'
import type { FormInstance } from 'element-plus'

const authStore = useAuthStore()
const canAddQuestionBank = computed(() => authStore.hasPermission('/admin/questionBank/addQuestionBank'))
const canViewQuestionBankInfo = computed(() => authStore.hasPermission('/admin/questionBank/findQuestionBankById'))
const canUpdateQuestionBank = computed(() => authStore.hasPermission('/admin/questionBank/updateQuestionBank'))
const canDeleteQuestionBank = computed(() => authStore.hasPermission('/admin/questionBank/deleteQuestionBank'))
const showQuestionBankActionColumn = computed(() => {
  return canViewQuestionBankInfo.value || canUpdateQuestionBank.value || canDeleteQuestionBank.value
})

const loading = ref(false)
const submitting = ref(false)
const showCreateDialog = ref(false)
const showViewDialog = ref(false)
const editingQuestionBank = ref<AdminQuestionBankPageVO | null>(null)
const currentViewQuestionBank = ref<AdminQuestionBankInfoVO | null>(null)

const questionTypeOptions = [
  { label: '单选题', value: 1 },
  { label: '多选题', value: 2 },
  { label: '判断题', value: 3 },
  { label: '填空题', value: 4 },
  { label: '简答题', value: 5 }
]

const difficultyOptions = [
  { label: '默认', value: 0 },
  { label: '简单', value: 1 },
  { label: '中等', value: 2 },
  { label: '困难', value: 3 }
]

const stateOptions = [
  { label: '禁用', value: 0 },
  { label: '启用', value: 1 }
]

const searchForm = reactive<AdminQuestionBankQuery>({
  questionBankDescription: '',
  questionBankType: undefined,
  questionFirstCategoryId: undefined,
  questionSecondCategoryId: undefined,
  questionBankDifficulty: undefined,
  questionBankState: undefined
})

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const tableData = ref<AdminQuestionBankPageVO[]>([])
const questionFirstCategoryOptions = ref<AdminQuestionFirstCategoryInfoVO[]>([])
const searchQuestionSecondCategoryOptions = ref<AdminQuestionSecondCategoryInfoVO[]>([])
const formQuestionSecondCategoryOptions = ref<AdminQuestionSecondCategoryInfoVO[]>([])
const searchSecondCategoryLoading = ref(false)
const formSecondCategoryLoading = ref(false)

const questionBankFormRef = ref<FormInstance>()
const questionBankForm = reactive<AdminQuestionBankForm>({
  questionBankId: undefined,
  questionBankDescription: '',
  questionBankOptionA: '',
  questionBankOptionB: '',
  questionBankOptionC: '',
  questionBankOptionD: '',
  questionBankCorrectAnswer: '',
  questionBankImage: '',
  questionBankCode: '',
  questionBankType: undefined,
  questionFirstCategoryId: undefined,
  questionFirstCategoryName: '',
  questionSecondCategoryId: undefined,
  questionSecondCategoryName: '',
  questionBankReferenceAnswer: '',
  questionBankAnalysis: '',
  questionBankDifficulty: 0,
  questionBankState: 1
})

const questionBankRules = {
  questionBankDescription: [
    { required: true, message: '请输入题目描述', trigger: 'blur' },
    { max: 2000, message: '题目描述不能超过2000个字符', trigger: 'blur' }
  ],
  questionBankType: [
    { required: true, message: '请选择题目类型', trigger: 'change' }
  ],
  questionFirstCategoryId: [
    { required: true, message: '请选择所属大类', trigger: 'change' }
  ],
  questionSecondCategoryId: [
    { required: true, message: '请选择所属小类', trigger: 'change' }
  ],
  questionBankOptionA: [
    {
      validator: (_rule: unknown, value: string, callback: (error?: Error) => void) => {
        if (!showOptionABFields.value) {
          callback()
          return
        }

        if (!value?.trim()) {
          callback(new Error('请输入选项A'))
          return
        }

        callback()
      },
      trigger: ['blur', 'change']
    }
  ],
  questionBankOptionB: [
    {
      validator: (_rule: unknown, value: string, callback: (error?: Error) => void) => {
        if (!showOptionABFields.value) {
          callback()
          return
        }

        if (!value?.trim()) {
          callback(new Error('请输入选项B'))
          return
        }

        callback()
      },
      trigger: ['blur', 'change']
    }
  ],
  questionBankOptionC: [
    {
      validator: (_rule: unknown, value: string, callback: (error?: Error) => void) => {
        if (!showOptionCDFields.value) {
          callback()
          return
        }

        if (!value?.trim()) {
          callback(new Error('请输入选项C'))
          return
        }

        callback()
      },
      trigger: ['blur', 'change']
    }
  ],
  questionBankOptionD: [
    {
      validator: (_rule: unknown, value: string, callback: (error?: Error) => void) => {
        if (!showOptionCDFields.value) {
          callback()
          return
        }

        if (!value?.trim()) {
          callback(new Error('请输入选项D'))
          return
        }

        callback()
      },
      trigger: ['blur', 'change']
    }
  ],
  questionBankCorrectAnswer: [
    {
      validator: (_rule: unknown, value: string, callback: (error?: Error) => void) => {
        if (!showCorrectAnswerField.value) {
          callback()
          return
        }

        if (!value?.trim()) {
          callback(new Error('请输入正确答案'))
          return
        }

        callback()
      },
      trigger: ['blur', 'change']
    }
  ],
  questionBankReferenceAnswer: [
    {
      validator: (_rule: unknown, value: string, callback: (error?: Error) => void) => {
        if (!showReferenceAnswerField.value) {
          callback()
          return
        }

        if (!value?.trim()) {
          callback(new Error('请输入参考答案'))
          return
        }

        callback()
      },
      trigger: ['blur', 'change']
    }
  ],
  questionBankDifficulty: [
    { required: true, message: '请选择题目难度', trigger: 'change' }
  ],
  questionBankState: [
    { required: true, message: '请选择题目状态', trigger: 'change' }
  ],
  questionBankImage: [
    { max: 1024, message: '题目图片地址不能超过1024个字符', trigger: 'blur' }
  ],
  questionBankAnalysis: [
    { max: 2000, message: '题目解析不能超过2000个字符', trigger: 'blur' }
  ]
}

const filteredSearchSecondCategoryOptions = computed(() => searchQuestionSecondCategoryOptions.value)

const filteredFormSecondCategoryOptions = computed(() => formQuestionSecondCategoryOptions.value)

const showOptionABFields = computed(() => [1, 2, 3].includes(questionBankForm.questionBankType || 0))
const showOptionCDFields = computed(() => [1, 2].includes(questionBankForm.questionBankType || 0))
const showCorrectAnswerField = computed(() => [1, 2, 3, 4].includes(questionBankForm.questionBankType || 0))
const showReferenceAnswerField = computed(() => questionBankForm.questionBankType === 5)

const correctAnswerPlaceholder = computed(() => {
  if (questionBankForm.questionBankType === 1) {
    return '请输入正确答案，例如 A'
  }
  if (questionBankForm.questionBankType === 2) {
    return '请输入正确答案，例如 A，C'
  }
  if (questionBankForm.questionBankType === 3) {
    return '请输入正确答案，例如 A 或 B'
  }
  if (questionBankForm.questionBankType === 4) {
    return '请输入填空题正确答案'
  }
  return '请输入正确答案'
})

const correctAnswerHint = computed(() => {
  if (questionBankForm.questionBankType === 2) {
    return '多选题答案使用中文逗号分隔，例如 A，C。输入英文逗号时会在提交前自动规范。'
  }
  if (questionBankForm.questionBankType === 3) {
    return '判断题只允许填写 A 或 B，对应上方两个判断选项。'
  }
  if (questionBankForm.questionBankType === 1) {
    return '单选题只允许填写 A、B、C、D 中的一个。'
  }
  return ''
})

const getQuestionTypeLabel = (value?: number) => {
  const target = questionTypeOptions.find(item => item.value === value)
  return target?.label || '-'
}

const getDifficultyLabel = (value?: number) => {
  const target = difficultyOptions.find(item => item.value === value)
  return target?.label || '-'
}

const getStateLabel = (value?: number) => {
  const target = stateOptions.find(item => item.value === value)
  return target?.label || '-'
}

const getDifficultyTagType = (value?: number) => {
  if (value === 1) {
    return 'success'
  }
  if (value === 2) {
    return 'warning'
  }
  if (value === 3) {
    return 'danger'
  }
  return 'info'
}

const hasText = (value?: string) => {
  return !!value?.trim()
}

const displayText = (value?: string) => {
  return hasText(value) ? value : '-'
}

const toOptionalNumber = (value?: number | string) => {
  if (typeof value === 'number' && Number.isFinite(value)) {
    return value
  }

  if (typeof value === 'string') {
    const trimmedValue = value.trim()
    if (/^\d+$/.test(trimmedValue)) {
      return Number(trimmedValue)
    }
  }

  return undefined
}

const buildSearchQuery = (): AdminQuestionBankQuery => {
  const questionFirstCategoryId = toOptionalNumber(searchForm.questionFirstCategoryId)

  return {
    questionBankDescription: searchForm.questionBankDescription.trim() || undefined,
    questionBankType: toOptionalNumber(searchForm.questionBankType),
    questionFirstCategoryId,
    questionSecondCategoryId: questionFirstCategoryId
      ? toOptionalNumber(searchForm.questionSecondCategoryId)
      : undefined,
    questionBankDifficulty: toOptionalNumber(searchForm.questionBankDifficulty),
    questionBankState: toOptionalNumber(searchForm.questionBankState)
  }
}

const getQuestionBankList = async () => {
  loading.value = true
  try {
    const response = await questionBankApi.getQuestionBankPage(
      pagination.current,
      pagination.size,
      buildSearchQuery()
    )
    tableData.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    console.error('获取题库题目列表失败:', error)
    ElMessage.error('加载题库题目数据失败')
  } finally {
    loading.value = false
  }
}

const getCategoryOptions = async () => {
  try {
    const firstCategoryResponse = await questionFirstCategoryApi.getAllQuestionFirstCategory()
    questionFirstCategoryOptions.value = firstCategoryResponse.data || []
  } catch (error) {
    console.error('获取题库分类选项失败:', error)
    ElMessage.error('加载题库分类选项失败')
  }
}

const loadQuestionSecondCategoryOptions = async (questionFirstCategoryId: number, scene: 'search' | 'form') => {
  if (scene === 'search') {
    searchSecondCategoryLoading.value = true
  } else {
    formSecondCategoryLoading.value = true
  }

  try {
    const response = await questionSecondCategoryApi.getQuestionSecondCategoryByFirstCategoryId(questionFirstCategoryId)
    const options = response.data || []

    if (scene === 'search') {
      searchQuestionSecondCategoryOptions.value = options
    } else {
      formQuestionSecondCategoryOptions.value = options
    }
  } catch (error) {
    console.error('获取题库小类选项失败:', error)
    ElMessage.error('加载题库小类选项失败')

    if (scene === 'search') {
      searchQuestionSecondCategoryOptions.value = []
    } else {
      formQuestionSecondCategoryOptions.value = []
    }
  } finally {
    if (scene === 'search') {
      searchSecondCategoryLoading.value = false
    } else {
      formSecondCategoryLoading.value = false
    }
  }
}

const openCreateDialog = () => {
  editingQuestionBank.value = null
  resetForm()
  showCreateDialog.value = true
}

const handleSearch = () => {
  pagination.current = 1
  getQuestionBankList()
}

const handleReset = () => {
  searchForm.questionBankDescription = ''
  searchForm.questionBankType = undefined
  searchForm.questionFirstCategoryId = undefined
  searchForm.questionSecondCategoryId = undefined
  searchForm.questionBankDifficulty = undefined
  searchForm.questionBankState = undefined
  searchQuestionSecondCategoryOptions.value = []
  pagination.current = 1
  getQuestionBankList()
}

const refreshData = () => {
  getQuestionBankList()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  getQuestionBankList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getQuestionBankList()
}

const handleSearchFirstCategoryChange = async () => {
  searchForm.questionSecondCategoryId = undefined
  searchQuestionSecondCategoryOptions.value = []

  if (!searchForm.questionFirstCategoryId) {
    return
  }

  await loadQuestionSecondCategoryOptions(searchForm.questionFirstCategoryId, 'search')
}

const handleFormFirstCategoryChange = async () => {
  questionBankForm.questionSecondCategoryId = undefined
  questionBankForm.questionSecondCategoryName = ''
  formQuestionSecondCategoryOptions.value = []

  if (!questionBankForm.questionFirstCategoryId) {
    return
  }

  await loadQuestionSecondCategoryOptions(questionBankForm.questionFirstCategoryId, 'form')
}

const handleFormTypeChange = () => {
  normalizeQuestionBankFormByType()
  questionBankFormRef.value?.clearValidate([
    'questionBankOptionA',
    'questionBankOptionB',
    'questionBankOptionC',
    'questionBankOptionD',
    'questionBankCorrectAnswer',
    'questionBankReferenceAnswer'
  ])
}

const normalizeQuestionBankFormByType = () => {
  if (questionBankForm.questionBankType === 1 || questionBankForm.questionBankType === 2) {
    questionBankForm.questionBankReferenceAnswer = ''
    return
  }

  if (questionBankForm.questionBankType === 3) {
    questionBankForm.questionBankOptionC = ''
    questionBankForm.questionBankOptionD = ''
    questionBankForm.questionBankReferenceAnswer = ''
    return
  }

  if (questionBankForm.questionBankType === 4) {
    questionBankForm.questionBankOptionA = ''
    questionBankForm.questionBankOptionB = ''
    questionBankForm.questionBankOptionC = ''
    questionBankForm.questionBankOptionD = ''
    questionBankForm.questionBankReferenceAnswer = ''
    return
  }

  if (questionBankForm.questionBankType === 5) {
    questionBankForm.questionBankOptionA = ''
    questionBankForm.questionBankOptionB = ''
    questionBankForm.questionBankOptionC = ''
    questionBankForm.questionBankOptionD = ''
    questionBankForm.questionBankCorrectAnswer = ''
  }
}

const validateTypeSpecificFields = () => {
  const optionA = questionBankForm.questionBankOptionA.trim()
  const optionB = questionBankForm.questionBankOptionB.trim()
  const optionC = questionBankForm.questionBankOptionC.trim()
  const optionD = questionBankForm.questionBankOptionD.trim()
  const correctAnswer = questionBankForm.questionBankCorrectAnswer.trim()
  const referenceAnswer = questionBankForm.questionBankReferenceAnswer.trim()

  if (questionBankForm.questionBankType === 1) {
    if (!optionA || !optionB || !optionC || !optionD) {
      ElMessage.error('单选题必须填写选项A、B、C、D')
      return false
    }
    if (!correctAnswer) {
      ElMessage.error('单选题必须填写正确答案')
      return false
    }
  }

  if (questionBankForm.questionBankType === 2) {
    if (!optionA || !optionB || !optionC || !optionD) {
      ElMessage.error('多选题必须填写选项A、B、C、D')
      return false
    }
    if (!correctAnswer) {
      ElMessage.error('多选题必须填写正确答案')
      return false
    }
  }

  if (questionBankForm.questionBankType === 3) {
    if (!optionA || !optionB) {
      ElMessage.error('判断题必须填写选项A、B')
      return false
    }
    if (!correctAnswer) {
      ElMessage.error('判断题必须填写正确答案')
      return false
    }
  }

  if (questionBankForm.questionBankType === 4 && !correctAnswer) {
    ElMessage.error('填空题必须填写正确答案')
    return false
  }

  if (questionBankForm.questionBankType === 5 && !referenceAnswer) {
    ElMessage.error('简答题必须填写参考答案')
    return false
  }

  return true
}

const normalizeMultiChoiceAnswer = (value: string) => {
  return value
    .split(/[，,]/)
    .map(item => item.trim().toUpperCase())
    .filter(Boolean)
    .join('，')
}

const buildPayload = () => {
  const firstCategory = questionFirstCategoryOptions.value.find(
    item => item.questionFirstCategoryId === questionBankForm.questionFirstCategoryId
  )
  const secondCategory = formQuestionSecondCategoryOptions.value.find(
    item => item.questionSecondCategoryId === questionBankForm.questionSecondCategoryId
  )

  if (!firstCategory) {
    ElMessage.error('请选择有效的题目大类')
    return null
  }

  if (!secondCategory || secondCategory.questionFirstCategoryId !== firstCategory.questionFirstCategoryId) {
    ElMessage.error('请选择有效的题目小类')
    return null
  }

  const payload: AdminQuestionBankForm = {
    questionBankId: questionBankForm.questionBankId,
    questionBankDescription: questionBankForm.questionBankDescription.trim(),
    questionBankOptionA: questionBankForm.questionBankOptionA.trim(),
    questionBankOptionB: questionBankForm.questionBankOptionB.trim(),
    questionBankOptionC: questionBankForm.questionBankOptionC.trim(),
    questionBankOptionD: questionBankForm.questionBankOptionD.trim(),
    questionBankCorrectAnswer: questionBankForm.questionBankCorrectAnswer.trim().toUpperCase(),
    questionBankImage: questionBankForm.questionBankImage.trim(),
    questionBankCode: questionBankForm.questionBankCode.trim(),
    questionBankType: questionBankForm.questionBankType,
    questionFirstCategoryId: firstCategory.questionFirstCategoryId,
    questionFirstCategoryName: firstCategory.questionFirstCategoryName,
    questionSecondCategoryId: secondCategory.questionSecondCategoryId,
    questionSecondCategoryName: secondCategory.questionSecondCategoryName,
    questionBankReferenceAnswer: questionBankForm.questionBankReferenceAnswer.trim(),
    questionBankAnalysis: questionBankForm.questionBankAnalysis.trim(),
    questionBankDifficulty: questionBankForm.questionBankDifficulty,
    questionBankState: questionBankForm.questionBankState
  }

  if (payload.questionBankType === 1) {
    payload.questionBankReferenceAnswer = ''
  }

  if (payload.questionBankType === 2) {
    payload.questionBankCorrectAnswer = normalizeMultiChoiceAnswer(payload.questionBankCorrectAnswer)
    payload.questionBankReferenceAnswer = ''
  }

  if (payload.questionBankType === 3) {
    payload.questionBankCorrectAnswer = payload.questionBankCorrectAnswer.toUpperCase()
    payload.questionBankOptionC = ''
    payload.questionBankOptionD = ''
    payload.questionBankReferenceAnswer = ''
  }

  if (payload.questionBankType === 4) {
    payload.questionBankOptionA = ''
    payload.questionBankOptionB = ''
    payload.questionBankOptionC = ''
    payload.questionBankOptionD = ''
    payload.questionBankReferenceAnswer = ''
  }

  if (payload.questionBankType === 5) {
    payload.questionBankOptionA = ''
    payload.questionBankOptionB = ''
    payload.questionBankOptionC = ''
    payload.questionBankOptionD = ''
    payload.questionBankCorrectAnswer = ''
  }

  return payload
}

const handleView = async (row: AdminQuestionBankPageVO) => {
  try {
    const response = await questionBankApi.getQuestionBankInfo(row.questionBankId)
    currentViewQuestionBank.value = response.data
    showViewDialog.value = true
  } catch (error) {
    console.error('获取题库题目详情失败:', error)
    ElMessage.error('获取题库题目详情失败')
  }
}

const handleEdit = async (row: AdminQuestionBankPageVO) => {
  try {
    const response = await questionBankApi.getQuestionBankInfo(row.questionBankId)
    const detail = response.data

    editingQuestionBank.value = row

    if (detail.questionFirstCategoryId) {
      await loadQuestionSecondCategoryOptions(detail.questionFirstCategoryId, 'form')
    } else {
      formQuestionSecondCategoryOptions.value = []
    }

    Object.assign(questionBankForm, {
      questionBankId: detail.questionBankId,
      questionBankDescription: detail.questionBankDescription || '',
      questionBankOptionA: detail.questionBankOptionA || '',
      questionBankOptionB: detail.questionBankOptionB || '',
      questionBankOptionC: detail.questionBankOptionC || '',
      questionBankOptionD: detail.questionBankOptionD || '',
      questionBankCorrectAnswer: detail.questionBankCorrectAnswer || '',
      questionBankImage: detail.questionBankImage || '',
      questionBankCode: detail.questionBankCode || '',
      questionBankType: detail.questionBankType,
      questionFirstCategoryId: detail.questionFirstCategoryId,
      questionFirstCategoryName: detail.questionFirstCategoryName || '',
      questionSecondCategoryId: detail.questionSecondCategoryId,
      questionSecondCategoryName: detail.questionSecondCategoryName || '',
      questionBankReferenceAnswer: detail.questionBankReferenceAnswer || '',
      questionBankAnalysis: detail.questionBankAnalysis || '',
      questionBankDifficulty: detail.questionBankDifficulty,
      questionBankState: detail.questionBankState
    })
    normalizeQuestionBankFormByType()
    showCreateDialog.value = true
  } catch (error) {
    console.error('获取题库题目详情失败:', error)
    ElMessage.error('获取题库题目详情失败')
  }
}

const handleDelete = async (row: AdminQuestionBankPageVO) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除题库题目“${row.questionBankDescription}”吗？删除后该题目将从题库中移除，请谨慎操作。`,
      '删除确认',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await questionBankApi.deleteQuestionBank(row.questionBankId)
    ElMessage.success('删除题库题目成功')

    if (tableData.value.length === 1 && pagination.current > 1) {
      pagination.current -= 1
    }
    getQuestionBankList()
  } catch (error) {
    if (error === 'cancel' || error === 'close') {
      return
    }
    console.error('删除题库题目失败:', error)
  }
}

const handleSubmit = async () => {
  if (!questionBankFormRef.value) return

  try {
    await questionBankFormRef.value.validate()

    if (!validateTypeSpecificFields()) {
      return
    }

    const payload = buildPayload()
    if (!payload) {
      return
    }

    submitting.value = true

    if (editingQuestionBank.value) {
      await questionBankApi.updateQuestionBank(payload)
      ElMessage.success('修改题库题目成功')
    } else {
      await questionBankApi.addQuestionBank(payload)
      ElMessage.success('新增题库题目成功')
    }

    showCreateDialog.value = false
    getQuestionBankList()
  } catch (error) {
    console.error('提交题库题目表单失败:', error)
  } finally {
    submitting.value = false
  }
}

const resetForm = () => {
  formQuestionSecondCategoryOptions.value = []

  Object.assign(questionBankForm, {
    questionBankId: undefined,
    questionBankDescription: '',
    questionBankOptionA: '',
    questionBankOptionB: '',
    questionBankOptionC: '',
    questionBankOptionD: '',
    questionBankCorrectAnswer: '',
    questionBankImage: '',
    questionBankCode: '',
    questionBankType: undefined,
    questionFirstCategoryId: undefined,
    questionFirstCategoryName: '',
    questionSecondCategoryId: undefined,
    questionSecondCategoryName: '',
    questionBankReferenceAnswer: '',
    questionBankAnalysis: '',
    questionBankDifficulty: 0,
    questionBankState: 1
  })
  questionBankFormRef.value?.clearValidate()
}

onMounted(() => {
  getCategoryOptions()
  getQuestionBankList()
})

defineExpose({
  refreshData,
  openCreateDialog
})
</script>

<style scoped lang="scss">
.question-bank-management {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 24px;
    gap: 16px;
  }

  .header-content {
    .page-title {
      margin: 0 0 8px;
      font-size: 24px;
      font-weight: 700;
      color: #111827;
    }

    .page-description {
      margin: 0;
      color: #6b7280;
      font-size: 14px;
      line-height: 1.6;
    }
  }

  .header-actions {
    display: flex;
    gap: 12px;
  }

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    :deep(.el-card__body) {
      display: flex;
      flex-direction: column;
      gap: 20px;
    }
  }

  .pagination {
    justify-content: flex-end;
  }

  .field-hint {
    width: 100%;
    margin-top: 6px;
    font-size: 12px;
    color: #6b7280;
    line-height: 1.5;
  }

  .multiline-text {
    white-space: pre-wrap;
    word-break: break-word;
    line-height: 1.7;
  }

  .code-text {
    white-space: pre-wrap;
    word-break: break-word;
    line-height: 1.7;
    background: #f8fafc;
    padding: 12px;
    border-radius: 8px;
    color: #1f2937;
  }
}

@media (max-width: 768px) {
  .question-bank-management {
    .page-header {
      flex-direction: column;
      justify-content: stretch;
    }

    .header-actions {
      width: 100%;
      flex-direction: column;
    }

    .search-form {
      :deep(.el-form-item) {
        width: 100%;
        margin-right: 0;
      }

      :deep(.el-input),
      :deep(.el-select) {
        width: 100% !important;
      }
    }
  }
}
</style>
