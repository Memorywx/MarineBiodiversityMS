<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getObservationList, createObservation, updateObservation, deleteObservation } from '../api/observation.js'
import { getAllEcosystems } from '../api/ecosystem.js'

const tableData = ref([])
const loading = ref(false)
const page = reactive({ current: 1, size: 10, total: 0 })
const search = reactive({ locationName: '', startTime: '', endTime: '' })
const ecosystemList = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const form = reactive({
  id: null,
  observationTime: '',
  locationName: '',
  latitude: null,
  longitude: null,
  ecosystemId: null,
  observer: '',
  waterTemperature: null,
  salinity: null,
  phValue: null,
  depth: null,
  remarks: ''
})

const rules = {
  observationTime: [{ required: true, message: '请选择观测时间', trigger: 'change' }],
  latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }],
  longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getObservationList({
      page: page.current,
      size: page.size,
      locationName: search.locationName,
      startTime: search.startTime,
      endTime: search.endTime
    })
    tableData.value = res.data.records || []
    page.total = res.data.total || 0
  } catch (e) { console.error(e) }
  loading.value = false
}

const loadEcosystems = async () => {
  try {
    const res = await getAllEcosystems()
    ecosystemList.value = res.data || []
  } catch (e) { console.error(e) }
}

const handleSearch = () => { page.current = 1; loadData() }
const handleReset = () => {
  search.locationName = ''
  search.startTime = ''
  search.endTime = ''
  handleSearch()
}

const handleAdd = () => {
  isEdit.value = false
  Object.keys(form).forEach(k => form[k] = k === 'id' ? null : (k === 'ecosystemId' ? null : ''))
  form.latitude = null
  form.longitude = null
  form.waterTemperature = null
  form.salinity = null
  form.phValue = null
  form.depth = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该观测记录吗？', '提示', { type: 'warning' })
  await deleteObservation(row.id)
  ElMessage.success('删除成功')
  loadData()
}

const handleSubmit = async () => {
  await formRef.value.validate()
  if (isEdit.value) {
    await updateObservation(form.id, form)
    ElMessage.success('更新成功')
  } else {
    await createObservation(form)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  loadData()
}

const handlePageChange = (val) => { page.current = val; loadData() }

onMounted(() => { loadData(); loadEcosystems() })
</script>

<template>
  <div>
    <el-card style="margin-bottom: 16px">
      <el-form :inline="true" :model="search">
        <el-form-item label="地点">
          <el-input v-model="search.locationName" placeholder="地点名称" clearable />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker v-model="search.startTime" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker v-model="search.endTime" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <div style="margin-bottom: 16px">
        <el-button type="primary" @click="handleAdd">新增观测记录</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="observationTime" label="观测时间" width="160" />
        <el-table-column prop="locationName" label="地点" width="160" />
        <el-table-column prop="latitude" label="纬度" width="100" />
        <el-table-column prop="longitude" label="经度" width="100" />
        <el-table-column prop="ecosystemName" label="生态系统" width="120" />
        <el-table-column prop="observer" label="观测人员" width="120" />
        <el-table-column prop="speciesCount" label="物种数" width="80" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
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

    <el-dialog :title="isEdit ? '编辑观测记录' : '新增观测记录'" v-model="dialogVisible" width="650px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="观测时间" prop="observationTime">
              <el-date-picker v-model="form.observationTime" type="datetime" placeholder="选择时间" value-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地点名称">
              <el-input v-model="form.locationName" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="纬度" prop="latitude">
              <el-input-number v-model="form.latitude" :precision="6" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度" prop="longitude">
              <el-input-number v-model="form.longitude" :precision="6" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="生态系统">
              <el-select v-model="form.ecosystemId" placeholder="请选择" clearable style="width: 100%">
                <el-option v-for="item in ecosystemList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="观测人员">
              <el-input v-model="form.observer" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="8"><el-form-item label="水温"><el-input-number v-model="form.waterTemperature" :precision="2" style="width: 100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="盐度"><el-input-number v-model="form.salinity" :precision="2" style="width: 100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="深度(m)"><el-input-number v-model="form.depth" :precision="2" style="width: 100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="form.remarks" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
