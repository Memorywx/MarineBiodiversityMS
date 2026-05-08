<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import L from 'leaflet'
import { getStats, getSpeciesStats, getObservationStats, getSpeciesDistribution, getObservationPoints } from '../api/dashboard.js'

const stats = ref({})

const loadData = async () => {
  try {
    const r1 = await getStats()
    stats.value = r1.data
    const r2 = await getSpeciesStats('protection')
    renderSpeciesChart(r2.data)
    const r3 = await getObservationStats('timeline')
    renderObservationChart(r3.data)
    const r4 = await getSpeciesDistribution()
    renderSpeciesMap(r4.data)
    const r5 = await getObservationPoints()
    renderObservationMap(r5.data)
    const r6 = await getObservationStats('ecosystem')
    renderEcosystemChart(r6.data)
    const r7 = await getObservationStats('observer')
    renderObserverChart(r7.data)
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

const renderSpeciesMap = (data) => {
  const dom = document.getElementById('species-map')
  if (!dom || !data || data.length === 0) return
  const map = L.map('species-map').setView([21.5, 112], 6)
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
  }).addTo(map)
  data.forEach(p => {
    if (p.lat != null && p.lng != null) {
      L.circleMarker([p.lat, p.lng], { radius: 8, color: '#409EFF', fillColor: '#409EFF', fillOpacity: 0.8 }).addTo(map)
        .bindPopup(`<b>${p.chineseName}</b><br/>${p.scientificName}`)
    }
  })
}

const renderObservationMap = (data) => {
  const dom = document.getElementById('observation-map')
  if (!dom || !data || data.length === 0) return
  const map = L.map('observation-map').setView([21.5, 112], 6)
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
  }).addTo(map)
  data.forEach(p => {
    if (p.lat != null && p.lng != null) {
      L.circleMarker([p.lat, p.lng], { radius: 8, color: '#67C23A', fillColor: '#67C23A', fillOpacity: 0.8 }).addTo(map)
        .bindPopup(`<b>${p.locationName || '观测点'}</b><br/>时间：${p.observationTime}<br/>生态系统：${p.ecosystemName || '-'}<br/>物种数：${p.speciesCount || 0}`)
    }
  })
}

const renderEcosystemChart = (data) => {
  const dom = document.getElementById('ecosystem-chart')
  if (!dom) return
  const chart = echarts.init(dom)
  chart.setOption({
    title: { text: '生态系统观测次数', left: 'center' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.data?.map(i => i.name) || [] },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: data.data?.map(i => i.value) || [] }]
  })
}

const renderObserverChart = (data) => {
  const dom = document.getElementById('observer-chart')
  if (!dom) return
  const chart = echarts.init(dom)
  chart.setOption({
    title: { text: '观测人员统计', left: 'center' },
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: data.data?.map(i => i.name) || [] },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: data.data?.map(i => i.value) || [] }]
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
              <div style="font-size: 24px; font-weight: bold">{{ stats.totalSpecies || 0 }}</div>
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
              <div style="font-size: 24px; font-weight: bold">{{ stats.totalEcosystems || 0 }}</div>
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
              <div style="font-size: 24px; font-weight: bold">{{ stats.totalObservations || 0 }}</div>
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
              <div style="font-size: 24px; font-weight: bold">{{ stats.totalUsers || 0 }}</div>
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

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="12">
        <el-card>
          <template #header><span>物种分布地图</span></template>
          <div id="species-map" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header><span>观测地点地图</span></template>
          <div id="observation-map" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="12">
        <el-card>
          <div id="ecosystem-chart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div id="observer-chart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
