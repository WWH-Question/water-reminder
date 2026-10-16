<script setup lang="ts">
defineProps<{ mode: string }>()
const emit = defineEmits<{ toggleMode: [] }>()
</script>

<template>
  <div class="header">
    <div class="header-left">
      <h1>💧 喝水提醒</h1>
      <span :class="['mode-badge', mode === 'work' ? 'mode-work' : 'mode-daily']">
        {{ mode === 'work' ? '工作模式' : '日常模式' }}
      </span>
    </div>
    <div class="toggle-row" @click="emit('toggleMode')">
      <span>{{ mode === 'work' ? '下班切换' : '切换工作' }}</span>
      <label class="toggle-switch" @click.stop>
        <input type="checkbox" :checked="mode === 'daily'" @change="emit('toggleMode')" />
        <span class="toggle-slider"></span>
      </label>
    </div>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 12px;
}
.header-left { display: flex; align-items: center; gap: 8px; }
.header-left h1 { font-size: 22px; font-weight: 700; margin: 0; }
.mode-badge {
  font-size: 13px;
  font-weight: 600;
  padding: 4px 12px;
  border-radius: 20px;
  color: #fff;
}
.mode-work { background: #2196F3; }
.mode-daily { background: #00BCD4; }
.toggle-row {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  color: #607D8B;
  cursor: pointer;
}
.toggle-switch {
  position: relative;
  width: 56px;
  height: 28px;
  flex-shrink: 0;
}
.toggle-switch input { opacity: 0; width: 0; height: 0; }
.toggle-slider {
  position: absolute;
  inset: 0;
  background: #2196F3;
  border-radius: 28px;
  cursor: pointer;
  transition: .3s;
}
.toggle-slider::before {
  content: '';
  position: absolute;
  width: 22px; height: 22px;
  left: 3px; bottom: 3px;
  background: #fff;
  border-radius: 50%;
  transition: .3s;
}
.toggle-switch input:checked + .toggle-slider { background: #00BCD4; }
.toggle-switch input:checked + .toggle-slider::before { transform: translateX(28px); }
</style>
