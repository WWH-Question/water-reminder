<script setup lang="ts">
import type { DrinkRecord } from '@/types'

defineProps<{ records: DrinkRecord[] }>()
const emit = defineEmits<{ delete: [id: number] }>()
</script>

<template>
  <div>
    <div class="records-header">
      <h3>📋 今日记录</h3>
      <span class="record-summary">
        共 {{ records.length }} 次，{{ records.reduce((s, r) => s + r.amount, 0) }} ml
      </span>
    </div>
    <ul v-if="records.length > 0" class="record-list">
      <li v-for="r in records" :key="r.id" class="record-item">
        <span class="rec-time">🕐 {{ r.recordTime }} {{ r.mode === 'daily' ? '· 日常' : '' }}</span>
        <span class="rec-amount">{{ r.amount }} ml</span>
        <button class="rec-delete" @click="emit('delete', r.id)" title="删除">✕</button>
      </li>
    </ul>
    <div v-else class="no-records">今天还没喝水哦，快喝一杯吧 💪</div>
  </div>
</template>

<style scoped>
.records-header {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 12px;
}
.records-header h3 { font-size: 16px; margin: 0; }
.record-summary { font-size: 13px; color: #607D8B; }
.record-list { list-style: none; display: flex; flex-direction: column; gap: 8px; max-height: 200px; overflow-y: auto; padding: 0; }
.record-item {
  display: flex; justify-content: space-between; align-items: center;
  padding: 10px 14px; background: #F5F5F5; border-radius: 10px;
  font-size: 14px;
}
.rec-time { color: #607D8B; }
.rec-amount { font-weight: 600; }
.rec-delete {
  background: none; border: none; color: #BDBDBD;
  cursor: pointer; font-size: 16px; padding: 0 4px;
}
.rec-delete:hover { color: #f44336; }
.no-records { text-align: center; color: #BDBDBD; padding: 20px; font-size: 14px; }
</style>
