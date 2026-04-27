<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const user = computed(() => {
  try {
    return JSON.parse(localStorage.getItem('user')) || {}
  } catch {
    return {}
  }
})

const isAdmin = computed(() => user.value.role === 0)

const activeMenu = computed(() => route.path)

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

const menuItems = [
  { index: '/dashboard', icon: 'Odometer', title: '仪表盘' },
  { index: '/species', icon: 'Collection', title: '物种管理' },
  { index: '/ecosystem', icon: 'MapLocation', title: '生态系统' },
  { index: '/observation', icon: 'View', title: '观测记录' },
  { index: '/user', icon: 'UserFilled', title: '用户管理', adminOnly: true },
  { index: '/operation-logs', icon: 'List', title: '操作日志', adminOnly: true },
]
</script>

<template>
  <el-container style="height: 100vh">
    <el-aside width="220px" style="background: #1a5276; color: #fff">
      <div style="padding: 20px; font-size: 18px; font-weight: bold; text-align: center; border-bottom: 1px solid rgba(255,255,255,0.1)">
        海洋生物多样性
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#1a5276"
        text-color="#fff"
        active-text-color="#ffd04b"
        style="border: none"
      >
        <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.index" v-show="!item.adminOnly || isAdmin">
          <el-icon>
            <component :is="item.icon" />
          </el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header style="background: #fff; display: flex; align-items: center; justify-content: space-between; box-shadow: 0 1px 4px rgba(0,0,0,0.1)">
        <div style="font-size: 16px; color: #606266">{{ route.meta.title || '海洋生物多样性信息管理系统' }}</div>
        <div style="display: flex; align-items: center; gap: 12px">
          <el-tag v-if="user.role === 0" type="danger">管理员</el-tag>
          <el-tag v-else-if="user.role === 1" type="primary">科研人员</el-tag>
          <el-tag v-else type="info">普通用户</el-tag>
          <span style="color: #606266">{{ user.realName || user.username }}</span>
          <el-dropdown>
            <el-icon size="20" style="cursor: pointer"><Setting /></el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/profile')">个人中心</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main style="background: #f5f7fa; padding: 20px; overflow-y: auto">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
