<template>
  <div class="map-management-page">
    <div class="page-header">
      <div>
        <h1 class="page-title">大学Map管理</h1>
        <p class="page-description">仅支持查询与分页查询</p>
      </div>
    </div>

    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="大学名称">
          <el-input v-model="searchForm.universityMapName" placeholder="请输入大学名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table v-loading="loading" :data="tableData" border>
        <el-table-column prop="universityMapId" label="ID" width="120" />
        <el-table-column prop="universityMapName" label="大学名称" min-width="240" />
        <el-table-column prop="universityMapAddress" label="地址" min-width="260" />
        <el-table-column prop="universityMapLat" label="经度" width="140" />
        <el-table-column prop="universityMapLng" label="纬度" width="140" />
        <el-table-column label="状态" width="110">
          <template #default="{ row }">
            <el-tag :type="row.universityMapStatus === 0 ? 'success' : 'danger'">
              {{ formatUniversityStatus(row.universityMapStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleView(row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pagination"
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[10, 20, 50]"
        @size-change="getList"
        @current-change="getList"
      />
    </el-card>

    <el-dialog v-model="viewVisible" title="大学详情" width="520px">
      <el-descriptions v-if="currentRow" :column="1" border>
        <el-descriptions-item label="ID">{{ currentRow.universityMapId }}</el-descriptions-item>
        <el-descriptions-item label="大学名称">{{ currentRow.universityMapName }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ currentRow.universityMapAddress || '-' }}</el-descriptions-item>
        <el-descriptions-item label="经度">{{ currentRow.universityMapLat || '-' }}</el-descriptions-item>
        <el-descriptions-item label="纬度">{{ currentRow.universityMapLng || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentRow.universityMapStatus === 0 ? 'success' : 'danger'">
            {{ formatUniversityStatus(currentRow.universityMapStatus) }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { universityMapApi } from '@/api/admin'
import type { UniversityMap } from '@/types/admin'

const loading = ref(false)
const viewVisible = ref(false)
const currentRow = ref<UniversityMap | null>(null)
const tableData = ref<UniversityMap[]>([])
const searchForm = reactive({ universityMapName: '' })
const pagination = reactive({ current: 1, size: 10, total: 0 })

const getList = async () => {
  loading.value = true
  try {
    const res = await universityMapApi.getUniversityMapPage(pagination.current, pagination.size, searchForm)
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (e) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  getList()
}

const handleReset = () => {
  searchForm.universityMapName = ''
  pagination.current = 1
  getList()
}

const formatUniversityStatus = (status?: number) => {
  if (status === 0) return '启用中'
  if (status === 1) return '禁用中'
  return '未知'
}

const handleView = async (row: UniversityMap) => {
  try {
    const res = await universityMapApi.getUniversityMapInfo(row.universityMapId)
    currentRow.value = res.data
    viewVisible.value = true
  } catch {
    ElMessage.error('获取详情失败')
  }
}

onMounted(getList)
</script>

<style scoped lang="scss">
.map-management-page { display: flex; flex-direction: column; gap: 16px; }
.page-header { display: flex; justify-content: space-between; align-items: center; }
.page-title { margin: 0; }
.page-description { margin: 8px 0 0; color: #6b7280; }
.search-card { margin-bottom: 0; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
