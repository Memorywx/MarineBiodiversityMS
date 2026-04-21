<script setup>
import { ref, onMounted, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserList, createUser, updateUser, deleteUser, approveUser, updateUserRoleStatus } from '../api/user.js'

const tableData = ref([])
const loading = ref(false)
const page = reactive({ current: 1, size: 10, total: 0 })
const search = reactive({ keyword: '', role: '' })
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const form = reactive({
  id: null,
  username: '',
  password: '',
  realName: '',
  email: '',
  role: 1,
  status: 1
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const roleMap = { 0: '管理员', 1: '科研人员', 2: '学生', 3: '公众' }
const statusMap = { 0: '待审核', 1: '正常', 2: '禁用' }
const statusType = { 0: 'warning', 1: 'success', 2: 'danger' }

const loadData = async () => {
  loading.value = true
  try {
    const res = await getUserList({
      page: page.current,
      size: page.size,
      keyword: search.keyword,
      role: search.role
    })
    tableData.value = res.data.records || []
    page.total = res.data.total || 0
  } catch (e) { console.error(e) }
  loading.value = false
}

const handleSearch = () => { page.current = 1; loadData() }
const handleReset = () => { search.keyword = ''; search.role = ''; handleSearch() }

const handleAdd = () => {
  isEdit.value = false
  Object.keys(form).forEach(k => form[k] = k === 'id' ? null : (k === 'role' ? 1 : (k === 'status' ? 1 : '')))
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  form.password = ''
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm('确定删除该用户吗？', '提示', { type: 'warning' })
  await deleteUser(row.id)
  ElMessage.success('删除成功')
  loadData()
}

const handleApprove = async (row) => {
  await approveUser(row.id)
  ElMessage.success('审核通过')
  loadData()
}

const handleSubmit = async () => {
  await formRef.value.validate()
  const data = { ...form }
  if (isEdit.value && !data.password) delete data.password
  if (isEdit.value) {
    await updateUser(form.id, data)
    ElMessage.success('更新成功')
  } else {
    await createUser(data)
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
        <el-form-item label="关键词">
          <el-input v-model="search.keyword" placeholder="用户名/真实姓名" clearable />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="search.role" placeholder="请选择" clearable style="width: 150px">
            <el-option label="管理员" :value="0" />
            <el-option label="科研人员" :value="1" />
            <el-option label="学生" :value="2" />
            <el-option label="公众" :value="3" />
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
        <el-button type="primary" @click="handleAdd">新增用户</el-button>
      </div>
      <el-table :data="tableData" v-loading="loading" border>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="realName" label="真实姓名" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column label="角色" width="100">
          <template #default="{ row }">
            {{ roleMap[row.role] }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType[row.status]">{{ statusMap[row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status === 0" type="success" size="small" @click="handleApprove(row)">审核</el-button>
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

    <el-dialog :title="isEdit ? '编辑用户' : '新增用户'" v-model="dialogVisible" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="form.realName" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" style="width: 100%">
            <el-option label="管理员" :value="0" />
            <el-option label="科研人员" :value="1" />
            <el-option label="学生" :value="2" />
            <el-option label="公众" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="待审核" :value="0" />
            <el-option label="正常" :value="1" />
            <el-option label="禁用" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
