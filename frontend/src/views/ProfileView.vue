<script setup>
import { ref, computed, reactive } from 'vue'
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

const isEditing = ref(false)
const editForm = reactive({
  realName: '',
  email: '',
  avatar: ''
})

const startEdit = () => {
  editForm.realName = user.value.realName || ''
  editForm.email = user.value.email || ''
  editForm.avatar = user.value.avatar || ''
  isEditing.value = true
}

const saveProfile = async () => {
  try {
    await updateUser(user.value.id, editForm)
    const updated = { ...user.value, ...editForm }
    localStorage.setItem('user', JSON.stringify(updated))
    ElMessage.success('保存成功')
    isEditing.value = false
  } catch (e) {
    console.error(e)
  }
}

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
        <el-card>
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center">
              <span>个人信息</span>
              <el-button v-if="!isEditing" type="primary" size="small" @click="startEdit">编辑</el-button>
              <div v-else>
                <el-button size="small" @click="isEditing = false">取消</el-button>
                <el-button type="primary" size="small" @click="saveProfile">保存</el-button>
              </div>
            </div>
          </template>

          <div v-if="!isEditing">
            <div style="text-align: center; margin-bottom: 20px">
              <el-avatar :size="80" :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            </div>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
              <el-descriptions-item label="真实姓名">{{ user.realName || '-' }}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{ user.email || '-' }}</el-descriptions-item>
              <el-descriptions-item label="角色">{{ roleMap[user.role] }}</el-descriptions-item>
              <el-descriptions-item label="状态">{{ statusMap[user.status] }}</el-descriptions-item>
            </el-descriptions>
          </div>

          <el-form v-else label-width="100px">
            <el-form-item label="头像URL">
              <el-input v-model="editForm.avatar" placeholder="请输入头像图片URL" />
            </el-form-item>
            <el-form-item label="真实姓名">
              <el-input v-model="editForm.realName" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="editForm.email" />
            </el-form-item>
          </el-form>
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
