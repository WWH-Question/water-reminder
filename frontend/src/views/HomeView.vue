<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '@/auth'
import { useWaterReminder } from '@/composables/useWaterReminder'
import Header from '@/components/Header.vue'
import ProgressRing from '@/components/ProgressRing.vue'
import TimerSection from '@/components/TimerSection.vue'
import QuickButtons from '@/components/QuickButtons.vue'
import RecordList from '@/components/RecordList.vue'
import SettingsPanel from '@/components/SettingsPanel.vue'
import ReminderModal from '@/components/ReminderModal.vue'

const router = useRouter()
const { username, clearAuth } = useAuth()

const {
  settings,
  records,
  stats,
  countdown,
  showReminder,
  saveSettings,
  drinkWater,
  removeRecord,
  dismissReminder,
  confirmDrink,
  formatCountdown,
} = useWaterReminder()

const countdownDisplay = computed(() => formatCountdown(countdown.value))
const isUrgent = computed(() => countdown.value <= 60 && countdown.value > 0)
const isComplete = computed(() => stats.value.remaining <= 0 && stats.value.drank > 0)

async function handleToggleMode() {
  const now = new Date()
  const time = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`
  const afterWork = `${String(now.getHours() + 1).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`

  if (stats.value.mode === 'work') {
    settings.value.workEnd = time
  } else {
    settings.value.workEnd = afterWork
  }
  await saveSettings()
}

function handleLogout() {
  clearAuth()
  router.push('/login')
}
</script>

<template>
  <div class="container">
    <div class="card">
      <div class="user-bar">
        <span class="user-name">👤 {{ username }}</span>
        <button class="logout-btn" @click="handleLogout">退出登录</button>
      </div>
    </div>

    <div class="card">
      <Header :mode="stats.mode" @toggle-mode="handleToggleMode" />
    </div>

    <div class="card">
      <ProgressRing :progress="stats.progress" :drank="stats.drank" :target="stats.target" />
    </div>

    <div class="card">
      <TimerSection
        :countdown-display="countdownDisplay"
        :suggested-amount="stats.suggestedAmount"
        :is-urgent="isUrgent"
        :is-complete="isComplete"
      />
    </div>

    <div class="card">
      <QuickButtons @drink="drinkWater" />
    </div>

    <div class="card">
      <RecordList :records="records" @delete="removeRecord" />
    </div>

    <div class="card">
      <SettingsPanel :settings="settings" @save="saveSettings" />
    </div>

    <ReminderModal
      :show="showReminder"
      :suggested-amount="stats.suggestedAmount"
      @confirm="confirmDrink"
      @dismiss="dismissReminder"
    />
  </div>
</template>

<style scoped>
.user-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.user-name {
  font-size: 15px;
  font-weight: 600;
  color: var(--text);
}
.logout-btn {
  padding: 6px 16px;
  border: 2px solid var(--border);
  border-radius: 8px;
  background: #fff;
  font-size: 13px;
  font-weight: 600;
  color: var(--text-secondary);
  cursor: pointer;
  transition: .2s;
  font-family: inherit;
}
.admin-link {
  font-size: 13px;
  font-weight: 600;
  color: var(--primary);
  text-decoration: none;
  padding: 6px 12px;
  border-radius: 6px;
  transition: .2s;
}
.admin-link:hover {
  background: rgba(var(--primary-rgb, 74,144,226), 0.1);
}
.logout-btn:hover {
  border-color: var(--danger);
  color: var(--danger);
}
</style>
