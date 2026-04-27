<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSpeciesList, createSpecies, updateSpecies, deleteSpecies } from '../api/species.js'
import { exportCSV } from '../utils/export.js'

const tableData = ref([])
const loading = ref(false)
const page = reactive({ current: 1, size: 10, total: 0 })
const search = reactive({ keyword: '', phylum: '', protectionLevel: '' })
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const form = reactive({
  id: null,
  chineseName: '',
  scientificName: '',
  phylum: '',
  className: '',
  orderName: '',
  family: '',
  genus: '',
  species: '',
  morphologicalFeatures: '',
  livingHabits: '',
  distribution: '',
  distributionLat: null,
  distributionLng: null,
  protectionLevel: '',
  iucnStatus: '',
  isPublic: 1
})

const rules = {
  chineseName: [{ required: true, message: '请输入中文名', trigger: 'blur' }],
  scientificName: [{ required: true, message: '请输入学名', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getSpeciesList({
      page: page.current,
      size: page.size,
      keyword: search.keyword,
      phylum: search.phylum,
      protectionLevel: search.protectionLevel
    })
    tableData.value = res.data.records || []
    page.total = res.data.total || 0
  } catch (e) { console.error(e) }
  loading.value = false
}

const handleSearch = () => {
  page.current = 1
  loadData()
}

const handleReset = () => {
  search.keyword = ''
  search.phylum = ''
  search.protectionLevel = ''
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  Object.keys(form).forEach(k => form[k] = k === 'isPublic' ? 1 : (k === 'id' ? null : ''))
  form.distributionLat = null
  form.distributionLng = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该物种吗？', '提示', { type: 'warning' })
  await deleteSpecies(row.id)
  ElMessage.success('删除成功')
  loadData()
}

const handleSubmit = async () => {
  await formRef.value.validate()
  if (isEdit.value) {
    await updateSpecies(form.id, form)
    ElMessage.success('更新成功')
  } else {
    await createSpecies(form)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  loadData()
}

const handlePageChange = (val) => {
  page.current = val
  loadData()
}

onMounted(loadData)
</script>

<template>
  <div>
    <el-card style="margin-bottom: 16px">
      <el-form :inline="true" :model="search">
        <el-form-item label="关键词">
          <el-input v-model="search.keyword" placeholder="中文名/学名" clearable />
        </el-form-item>
        <el-form-item label="门">
          <el-input v-model="search.phylum" placeholder="门" clearable />
        </el-form-item>
        <el-form-item label="保护等级">
          <el-select v-model="search.protectionLevel" placeholder="请选择" clearable style="width: 150px">
            <el-option label="国家一级" value="国家一级" />
            <el-option label="国家二级" value="国家二级" />
            <el-option label="无" value="无" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <div style="margin-bottom: 16px">
        <el-button type="primary" @click="handleAdd">新增物种</el-button>
        <el-button @click="exportCSV('物种信息.csv', [
          { label: 'ID', prop: 'id' },
          { label: '中文名', prop: 'chineseName' },
          { label: '学名', prop: 'scientificName' },
          { label: '门', prop: 'phylum' },
          { label: '纲', prop: 'className' },
          { label: '保护等级', prop: 'protectionLevel' },
          { label: 'IUCN', prop: 'iucnStatus' },
          { label: '创建时间', prop: 'createTime' }
        ], tableData)">导出CSV</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="chineseName" label="中文名" width="140" />
        <el-table-column prop="scientificName" label="学名" width="180" />
        <el-table-column prop="phylum" label="门" width="120" />
        <el-table-column prop="className" label="纲" width="100" />
        <el-table-column prop="protectionLevel" label="保护等级" width="100" />
        <el-table-column prop="iucnStatus" label="IUCN" width="80" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="$router.push('/species/' + row.id)">详情</el-button>
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

    <el-dialog :title="isEdit ? '编辑物种' : '新增物种'" v-model="dialogVisible" width="700px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="中文名" prop="chineseName">
              <el-input v-model="form.chineseName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学名" prop="scientificName">
              <el-input v-model="form.scientificName" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="8"><el-form-item label="门"><el-input v-model="form.phylum" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="纲"><el-input v-model="form.className" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="目"><el-input v-model="form.orderName" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="8"><el-form-item label="科"><el-input v-model="form.family" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="属"><el-input v-model="form.genus" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="种"><el-input v-model="form.species" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="保护等级">
              <el-select v-model="form.protectionLevel" style="width: 100%">
                <el-option label="国家一级" value="国家一级" />
                <el-option label="国家二级" value="国家二级" />
                <el-option label="无" value="无" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="IUCN">
              <el-select v-model="form.iucnStatus" style="width: 100%">
                <el-option label="CR" value="CR" />
                <el-option label="EN" value="EN" />
                <el-option label="VU" value="VU" />
                <el-option label="NT" value="NT" />
                <el-option label="LC" value="LC" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="形态特征">
          <el-input v-model="form.morphologicalFeatures" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="生活习性">
          <el-input v-model="form.livingHabits" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="分布区域">
          <el-input v-model="form.distribution" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
