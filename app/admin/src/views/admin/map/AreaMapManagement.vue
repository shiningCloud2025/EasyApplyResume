<template>
  <div class="map-management-page">
    <div class="page-header"><div><h1 class="page-title">区县Map管理</h1><p class="page-description">仅支持查询与分页查询</p></div></div>
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true">
        <el-form-item label="区县名称">
          <el-input v-model="searchForm.areaMapAname" placeholder="请输入区县名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <el-table v-loading="loading" :data="tableData" border>
        <el-table-column prop="areaMapId" label="ID" width="120" />
        <el-table-column prop="areaMapAname" label="区县名称" min-width="240" />
        <el-table-column prop="areaMapCid" label="城市ID" width="120" />
        <el-table-column label="操作" width="120"><template #default="{ row }"><el-button link type="primary" @click="handleView(row)">查看</el-button></template></el-table-column>
      </el-table>
      <el-pagination class="pagination" v-model:current-page="pagination.current" v-model:page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next, jumper" :page-sizes="[10, 20, 50]" @size-change="getList" @current-change="getList" />
    </el-card>
    <el-dialog v-model="viewVisible" title="区县详情" width="420px"><el-descriptions v-if="currentRow" :column="1" border><el-descriptions-item label="ID">{{ currentRow.areaMapId }}</el-descriptions-item><el-descriptions-item label="区县名称">{{ currentRow.areaMapAname }}</el-descriptions-item><el-descriptions-item label="城市ID">{{ currentRow.areaMapCid }}</el-descriptions-item></el-descriptions></el-dialog>
  </div>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { areaMapApi } from '@/api/admin'
import type { AreaMap, AreaMapPageVO } from '@/types/admin'
const loading = ref(false), viewVisible = ref(false)
const currentRow = ref<AreaMap | null>(null)
const tableData = ref<AreaMapPageVO[]>([])
const searchForm = reactive({ areaMapAname: '' })
const pagination = reactive({ current: 1, size: 10, total: 0 })
const getList = async () => { loading.value = true; try { const res = await areaMapApi.getAreaMapPage(pagination.current, pagination.size, searchForm); tableData.value = res.data.records; pagination.total = res.data.total } catch { ElMessage.error('加载失败') } finally { loading.value = false } }
const handleSearch = () => { pagination.current = 1; getList() }
const handleReset = () => { searchForm.areaMapAname = ''; pagination.current = 1; getList() }
const handleView = async (row: AreaMapPageVO) => { const res = await areaMapApi.getAreaMapInfo(row.areaMapId); currentRow.value = res.data; viewVisible.value = true }
onMounted(getList)
</script>
<style scoped lang="scss">.map-management-page{display:flex;flex-direction:column;gap:16px}.page-title{margin:0}.page-description{margin:8px 0 0;color:#6b7280}.pagination{margin-top:16px;display:flex;justify-content:flex-end}</style>
