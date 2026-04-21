<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getStats, getSpeciesStats, getObservationStats, getSpeciesDistribution, getObservationPoints } from '../api/dashboard.js'

const stats = ref({})
const speciesStats = ref({})
const observationStats = ref({})

const loadData = async () => {
  try {
    const r1 = await getStats()
    stats.value = r1.data
    const r2 = await getSpeciesStats('protection')
    speciesStats.value = r2.data
    const r3 = await getObservationStats('timeline')
    observationStats.value = r3.data
    renderSpeciesChart(r2.data)
    renderObservationChart(r3.data)
  } catch (e) { console.error(e) }
}

const renderSpeciesChart = (data) => {
  const dom = document.getElementById('species-chart')
  if (!dom) return
  const chart = echarts.init(dom)
  chart.setOption({
    title: { text: '物种保护等级分布', left: 'center' },
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: data.map(i => ({ name: i.name, value: i.value }))
    }]
  })
}

const renderObservationChart = (data) => {
  const dom = document.getElementById('observation-chart')
  if (!dom) return
  const chart = echarts.init(dom)
  chart.setOption({
    title: { text: '观测记录月度趋势', left: 'center' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.xAxis || [] },
    yAxis: { type: 'value' },
    series: [{ type: 'line', data: data.series || [], smooth: true, areaStyle: {} }]
  })
}

onMounted(() => {
  loadData()
})
</script>

<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="6">
        <el-card>
          <div style="display: flex; align-items: center; gap: 12px">
            <el-icon size="40" color="#409EFF"><Collection /></el-icon>
            <div>
              <div style="font-size: 24px; font-weight: bold">{{ stats.speciesCount || 0 }}</div>
              <div style="color: #909399; font-size: 14px">物种总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="display: flex; align-items: center; gap: 12px">
            <el-icon size="40" color="#67C23A"><MapLocation /></el-icon>
            <div>
              <div style="font-size: 24px; font-weight: bold">{{ stats.ecosystemCount || 0 }}</div>
              <div style="color: #909399; font-size: 14px">生态系统数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="display: flex; align-items: center; gap: 12px">
            <el-icon size="40" color="#E6A23C"><View /></el-icon>
            <div>
              <div style="font-size: 24px; font-weight: bold">{{ stats.observationCount || 0 }}</div>
              <div style="color: #909399; font-size: 14px">观测记录数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div style="display: flex; align-items: center; gap: 12px">
            <el-icon size="40" color="#F56C6C"><UserFilled /></el-icon>
            <div>
              <div style="font-size: 24px; font-weight: bold">{{ stats.userCount || 0 }}</div>
              <div style="color: #909399; font-size: 14px">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="12">
        <el-card>
          <div id="species-chart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div id="observation-chart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
