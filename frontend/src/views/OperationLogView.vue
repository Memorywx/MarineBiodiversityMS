<script setup>
import { ref, onMounted, reactive } from 'vue'
import { getOperationLogList } from '../api/operationLog.js'

const tableData = ref([])
const loading = ref(false)
const page = reactive({ current: 1, size: 20, total: 0 })
const search = reactive({ username: '', operation: '' })

const statusMap = { 0: '失败', 1: '成功' }
const statusType = { 0: 'danger', 1: 'success' }

const loadData = async () => {
  loading.value = true
  try {
    const res = await getOperationLogList({
      page: page.current,
      size: page.size,
      username: search.username,
      operation: search.operation
    })
    tableData.value = res.data.records || []
    page.total = res.data.total || 0
  } catch (e) { console.error(e) }
  loading.value = false
}

const handleSearch = () => { page.current = 1; loadData() }
const handleReset = () => { search.username = ''; search.operation = ''; handleSearch() }
const handlePageChange = (val) => { page.current = val; loadData() }

onMounted(loadData)
</script>

<template>
  <div>
    <el-card style="margin-bottom: 16px">
      <el-form :inline="true" :model="search">
        <el-form-item label="用户名">
          <el-input v-model="search.username" placeholder="用户名" clearable />
        </el-form-item>
        <el-form-item label="操作">
          <el-input v-model="search.operation" placeholder="操作描述" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="operation" label="操作描述" width="180" />
        <el-table-column prop="method" label="方法" width="80" />
        <el-table-column prop="requestUrl" label="请求URL" show-overflow-tooltip />
        <el-table-column prop="ipAddress" label="IP地址" width="140" />
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="statusType[row.status]">{{ statusMap[row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="160" />
      </el-table>
      <el-pagination
        style="margin-top: 16px; justify-content: flex-end"
        layout="total, prev, pager, next"
        :total="page.total"
        :page-size="page.size"
        :current-page="page.current"
        @current-change="handlePageChange"
      />
    </el-card>
  </div>
</template>
