<script setup lang="ts">
defineProps<{
  countdownDisplay: string
  suggestedAmount: number
  isUrgent: boolean
  isComplete: boolean
}>()
</script>

<template>
  <div class="timer-section">
    <div class="timer-left">
      <span class="timer-icon">⏱</span>
      <div class="timer-text">
        <div class="next-label">下次提醒</div>
        <div v-if="isComplete" class="countdown done">🎉 已完成</div>
        <div v-else :class="['countdown', { urgent: isUrgent }]">{{ countdownDisplay }}</div>
      </div>
    </div>
    <div class="suggest-amount">
      <div class="suggest-label">建议每次</div>
      <div class="suggest-value">
        {{ suggestedAmount > 0 ? suggestedAmount + ' ml' : '✅ 已达标' }}
      </div>
    </div>
  </div>
</template>

<style scoped>
.timer-section {
  display: flex; align-items: center; justify-content: space-between;
  flex-wrap: wrap; gap: 8px;
}
.timer-left { display: flex; align-items: center; gap: 8px; }
.timer-icon { font-size: 28px; }
.timer-text .next-label { font-size: 12px; color: #607D8B; }
.timer-text .countdown { font-size: 22px; font-weight: 700; color: #2196F3; }
.timer-text .countdown.urgent { color: #f44336; animation: pulse 1s infinite; }
.timer-text .countdown.done { color: #4CAF50; }
@keyframes pulse { 0%,100% { opacity:1; } 50% { opacity:.5; } }
.suggest-amount { text-align: right; }
.suggest-amount .suggest-label { font-size: 12px; color: #607D8B; }
.suggest-amount .suggest-value { font-size: 22px; font-weight: 700; color: #00BCD4; }
</style>
