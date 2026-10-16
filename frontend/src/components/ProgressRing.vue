<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  progress: number
  drank: number
  target: number
}>()

const circumference = 2 * Math.PI * 70 // ~439.82
const dashOffset = computed(() => circumference - (circumference * props.progress / 100))
const isOver = computed(() => props.progress >= 100)
</script>

<template>
  <div class="progress-section">
    <div class="progress-circle-wrap">
      <svg viewBox="0 0 160 160">
        <circle class="bg-circle" cx="80" cy="80" r="70" />
        <circle
          class="fg-circle"
          :class="{ over: isOver }"
          cx="80" cy="80" r="70"
          stroke-dasharray="439.82"
          :stroke-dashoffset="dashOffset"
        />
      </svg>
      <div class="progress-center">
        <span class="pct">{{ progress }}%</span>
        <span class="label">达成率</span>
      </div>
    </div>
    <div class="progress-stats">
      <span>已喝 <strong>{{ drank }} ml</strong></span>
      <span>目标 <strong>{{ target }} ml</strong></span>
    </div>
  </div>
</template>

<style scoped>
.progress-section { text-align: center; }
.progress-circle-wrap {
  position: relative;
  width: 160px; height: 160px;
  margin: 0 auto 12px;
}
.progress-circle-wrap svg { transform: rotate(-90deg); width: 160px; height: 160px; }
.bg-circle {
  fill: none; stroke: #E0E0E0; stroke-width: 10;
}
.fg-circle {
  fill: none; stroke: #2196F3; stroke-width: 10;
  stroke-linecap: round;
  transition: stroke-dashoffset 0.8s ease, stroke 0.5s;
}
.fg-circle.over { stroke: #4CAF50; }
.progress-center {
  position: absolute; inset: 0;
  display: flex; flex-direction: column;
  align-items: center; justify-content: center;
}
.progress-center .pct { font-size: 32px; font-weight: 700; }
.progress-center .label { font-size: 12px; color: #607D8B; }
.progress-stats {
  display: flex; justify-content: center; gap: 24px;
  font-size: 14px; color: #607D8B;
}
.progress-stats strong { color: #263238; }

@media (max-width: 400px) {
  .progress-circle-wrap { width: 130px; height: 130px; }
  .progress-circle-wrap svg { width: 130px; height: 130px; }
  .progress-center .pct { font-size: 26px; }
}
</style>
