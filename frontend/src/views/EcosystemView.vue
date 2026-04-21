<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getEcosystemList, createEcosystem, updateEcosystem, deleteEcosystem } from '../api/ecosystem.js'

const tableData = ref([])
const loading = ref(false)
const page = reactive({ current: 1, size: 10, total: 0 })
const search = reactive({ name: '' })
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const form = reactive({
  id: null,
  name: '',
  type: '',
  description: '',
  geoRange: '',
  environmentFeatures: ''
})

const rules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getEcosystemList({
      page: page.current,
      size: page.size,
      name: search.name
    })
    tableData.value = res.data.records || []
    page.total = res.data.total || 0
  } catch (e) { console.error(e) }
  loading.value = false
}

const handleSearch = () => { page.current = 1; loadData() }
const handleReset = () => { search.name = ''; handleSearch() }

const handleAdd = () => {
  isEdit.value = false
  Object.keys(form).forEach(k => form[k] = k === 'id' ? null : '')
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该生态系统吗？', '提示', { type: 'warning' })
  await deleteEcosystem(row.id)
  ElMessage.success('删除成功')
  loadData()
}

const handleSubmit = async () => {
  await formRef.value.validate()
  if (isEdit.value) {
    await updateEcosystem(form.id, form)
    ElMessage.success('更新成功')
  } else {
    await createEcosystem(form)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  loadData()
}

const handlePageChange = (val) => { page.current = val; loadData() }

onMounted(loadData)
</script>

<template>
  <div>
    <el-card style="margin-bottom: 16px">
      <el-form :inline="true" :model="search">
        <el-form-item label="名称">
          <el-input v-model="search.name" placeholder="生态系统名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <div style="margin-bottom: 16px">
        <el-button type="primary" @click="handleAdd">新增生态系统</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="名称" width="140" />
        <el-table-column prop="type" label="类型" width="120" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="geoRange" label="地理范围" show-overflow-tooltip />
        <el-table-column prop="environmentFeatures" label="环境特征" show-overflow-tooltip />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
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

    <el-dialog :title="isEdit ? '编辑生态系统' : '新增生态系统'" v-model="dialogVisible" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="form.type" placeholder="如 coral_reef" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="地理范围">
          <el-input v-model="form.geoRange" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="环境特征">
          <el-input v-model="form.environmentFeatures" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
