<script setup lang="ts">
import { ref } from 'vue'
import type { Settings } from '@/types'

const props = defineProps<{ settings: Settings }>()
const emit = defineEmits<{ save: [settings: Settings] }>()

const open = ref(false)
const form = ref<Settings>({ ...props.settings })

function handleSave() {
  emit('save', { ...form.value })
  open.value = false
}
</script>

<template>
  <div>
    <div class="settings-toggle" @click="open = !open">
      ⚙️ 设置 <span>{{ open ? '▼' : '▶' }}</span>
    </div>
    <div v-if="open" class="settings-panel">
      <div class="setting-row">
        <label>身高 (cm)</label>
        <input v-model.number="form.height" type="number" min="100" max="250" />
      </div>
      <div class="setting-row">
        <label>体重 (kg)</label>
        <input v-model.number="form.weight" type="number" min="30" max="200" />
      </div>
      <div class="setting-row">
        <label>饮水系数 (ml/kg)</label>
        <select v-model.number="form.coefficient">
          <option :value="30">30 — 少量活动</option>
          <option :value="35">35 — 正常活动</option>
          <option :value="40">40 — 大量活动</option>
        </select>
      </div>
      <div class="setting-row">
        <label>上班时间</label>
        <input v-model="form.workStart" type="time" />
      </div>
      <div class="setting-row">
        <label>下班时间</label>
        <input v-model="form.workEnd" type="time" />
      </div>
      <div class="setting-row">
        <label>睡觉时间</label>
        <input v-model="form.sleepTime" type="time" />
      </div>
      <button class="save-btn" @click="handleSave">💾 保存设置</button>
    </div>
  </div>
</template>

<style scoped>
.settings-toggle {
  display: flex; align-items: center; gap: 6px;
  cursor: pointer; font-size: 14px; color: #607D8B;
  user-select: none;
}
.settings-toggle:hover { color: #2196F3; }
.settings-panel { margin-top: 12px; }
.setting-row {
  display: flex; justify-content: space-between; align-items: center;
  padding: 8px 0;
}
.setting-row label { font-size: 14px; color: #607D8B; }
.setting-row input, .setting-row select {
  width: 120px; padding: 6px 10px; border: 1px solid #E0E0E0;
  border-radius: 8px; font-size: 14px; text-align: center;
  font-family: inherit;
}
.setting-row input:focus, .setting-row select:focus { outline: none; border-color: #2196F3; }
.save-btn {
  width: 100%; margin-top: 12px; padding: 12px;
  background: #2196F3; color: #fff; border: none;
  border-radius: 10px; font-size: 15px; font-weight: 600;
  cursor: pointer; font-family: inherit;
}
.save-btn:hover { background: #1976D2; }
</style>
