<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getSpeciesDetail } from '../api/species.js'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const species = ref({})

const loadData = async () => {
  loading.value = true
  try {
    const res = await getSpeciesDetail(route.params.id)
    species.value = res.data
  } catch (e) {
    console.error(e)
    ElMessage.error('获取物种详情失败')
  }
  loading.value = false
}

const goBack = () => router.push('/species')

onMounted(loadData)
</script>

<template>
  <div v-loading="loading">
    <el-page-header @back="goBack" title="物种详情" />

    <el-card style="margin-top: 16px">
      <el-row :gutter="24">
        <el-col :span="8">
          <el-image
            :src="species.images?.[0] || 'https://picsum.photos/seed/marine/400/300'"
            style="width: 100%; border-radius: 8px"
            fit="cover"
          />
        </el-col>
        <el-col :span="16">
          <h2 style="margin-bottom: 8px">
            {{ species.chineseName }}
            <span style="font-size: 16px; color: #909399; font-weight: normal; margin-left: 12px">
              {{ species.scientificName }}
            </span>
          </h2>
          <div style="margin-bottom: 16px">
            <el-tag v-if="species.protectionLevel" type="danger" style="margin-right: 8px">{{ species.protectionLevel }}</el-tag>
            <el-tag v-if="species.iucnStatus" type="warning">IUCN: {{ species.iucnStatus }}</el-tag>
          </div>

          <el-descriptions :column="3" border>
            <el-descriptions-item label="门">{{ species.phylum || '-' }}</el-descriptions-item>
            <el-descriptions-item label="纲">{{ species.className || '-' }}</el-descriptions-item>
            <el-descriptions-item label="目">{{ species.orderName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="科">{{ species.family || '-' }}</el-descriptions-item>
            <el-descriptions-item label="属">{{ species.genus || '-' }}</el-descriptions-item>
            <el-descriptions-item label="种">{{ species.species || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-col>
      </el-row>
    </el-card>

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="12">
        <el-card>
          <template #header><span>形态特征</span></template>
          <div style="line-height: 1.8; white-space: pre-wrap">{{ species.morphologicalFeatures || '暂无描述' }}</div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><span>生活习性</span></template>
          <div style="line-height: 1.8; white-space: pre-wrap">{{ species.livingHabits || '暂无描述' }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 16px">
      <template #header><span>分布区域</span></template>
      <div style="line-height: 1.8; white-space: pre-wrap; margin-bottom: 12px">{{ species.distribution || '暂无描述' }}</div>
      <el-descriptions :column="2" border v-if="species.distributionLat || species.distributionLng">
        <el-descriptions-item label="分布中心纬度">{{ species.distributionLat }}</el-descriptions-item>
        <el-descriptions-item label="分布中心经度">{{ species.distributionLng }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card style="margin-top: 16px" v-if="species.references || species.videoUrl">
      <template #header><span>参考资料</span></template>
      <div v-if="species.videoUrl" style="margin-bottom: 12px">
        <strong>视频链接：</strong>
        <el-link :href="species.videoUrl" target="_blank" type="primary">{{ species.videoUrl }}</el-link>
      </div>
      <div v-if="species.references" style="line-height: 1.8; white-space: pre-wrap">{{ species.references }}</div>
    </el-card>
  </div>
</template>
