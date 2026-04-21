<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { updateUser } from '../api/user.js'

const user = computed(() => {
  try {
    return JSON.parse(localStorage.getItem('user')) || {}
  } catch {
    return {}
  }
})

const roleMap = { 0: '管理员', 1: '科研人员', 2: '学生', 3: '公众' }
const statusMap = { 0: '待审核', 1: '正常', 2: '禁用' }

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const handleChangePassword = () => {
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  ElMessage.success('密码修改成功（演示）')
}
</script>

<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card title="个人信息">
          <template #header><span>个人信息</span></template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
            <el-descriptions-item label="真实姓名">{{ user.realName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ user.email || '-' }}</el-descriptions-item>
            <el-descriptions-item label="角色">{{ roleMap[user.role] }}</el-descriptions-item>
            <el-descriptions-item label="状态">{{ statusMap[user.status] }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><span>修改密码</span></template>
          <el-form label-width="100px">
            <el-form-item label="原密码">
              <el-input v-model="passwordForm.oldPassword" type="password" />
            </el-form-item>
            <el-form-item label="新密码">
              <el-input v-model="passwordForm.newPassword" type="password" />
            </el-form-item>
            <el-form-item label="确认密码">
              <el-input v-model="passwordForm.confirmPassword" type="password" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleChangePassword">修改密码</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
