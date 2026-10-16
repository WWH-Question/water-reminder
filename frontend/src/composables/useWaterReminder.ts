import { ref, onMounted, onUnmounted } from 'vue'
import type { Settings, DrinkRecord, Stats } from '@/types'
import * as api from '@/api'

export function useWaterReminder() {
  const settings = ref<Settings>({
    height: 170,
    weight: 65,
    coefficient: 35,
    workStart: '09:00',
    workEnd: '18:00',
    sleepTime: '23:00',
  })

  const records = ref<DrinkRecord[]>([])
  const stats = ref<Stats>({
    drank: 0,
    target: 2000,
    remaining: 2000,
    progress: 0,
    suggestedAmount: 200,
    mode: 'work',
    countdownSeconds: 1800,
  })

  const countdown = ref(1800)
  const showReminder = ref(false)
  const loading = ref(false)
  let timerInterval: number | null = null

  // 加载所有数据（只更新 settings/records/stats，不动 countdown）
  async function loadAll() {
    const [s, r, st] = await Promise.all([
      api.fetchSettings(),
      api.fetchRecords(),
      api.fetchStats(),
    ])
    settings.value = s
    records.value = r
    stats.value = st
  }

  // 保存设置
  async function saveSettings() {
    if (loading.value) return
    loading.value = true
    try {
      const result = await api.updateSettings(settings.value)
      settings.value = result
      await loadAll()
      resetTimer()
    } catch (e) {
      console.error('Failed to save settings:', e)
    } finally {
      loading.value = false
    }
  }

  // 喝水记录
  async function drinkWater(amount: number) {
    if (loading.value) return
    loading.value = true
    try {
      await api.addRecord(amount)
      await loadAll()
      resetTimer()
    } catch (e) {
      console.error('Failed to add record:', e)
    } finally {
      loading.value = false
    }
  }

  // 删除记录
  async function removeRecord(id: number) {
    if (loading.value) return
    loading.value = true
    try {
      await api.deleteRecord(id)
      await loadAll()
      resetTimer()
    } catch (e) {
      console.error('Failed to delete record:', e)
    } finally {
      loading.value = false
    }
  }

  // 倒计时——本地管理，不受后端定时刷新干扰
  function resetTimer() {
    stopTimer()
    countdown.value = stats.value.countdownSeconds
    if (countdown.value <= 0) return
    startCountdown()
  }

  function startCountdown() {
    if (timerInterval !== null) return
    timerInterval = window.setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        stopTimer()
        triggerReminder()
      }
    }, 1000)
  }

  function stopTimer() {
    if (timerInterval !== null) {
      clearInterval(timerInterval)
      timerInterval = null
    }
  }

  function triggerReminder() {
    showReminder.value = true

    // 浏览器通知
    if ('Notification' in window && Notification.permission === 'granted') {
      new Notification('💧 该喝水啦！', {
        body: `建议喝 ${stats.value.suggestedAmount} ml`,
      })
    }

    // 提示音
    playBeep()
  }

  function dismissReminder() {
    showReminder.value = false
    // 刷新后端数据并重新计时
    loadAll().finally(() => resetTimer())
  }

  function confirmDrink(amount: number) {
    showReminder.value = false
    drinkWater(amount)
  }

  function playBeep() {
    try {
      const ctx = new (window.AudioContext || (window as any).webkitAudioContext)()
      const osc = ctx.createOscillator()
      const gain = ctx.createGain()
      osc.connect(gain)
      gain.connect(ctx.destination)
      osc.type = 'sine'
      const now = ctx.currentTime
      osc.frequency.setValueAtTime(800, now)
      osc.frequency.setValueAtTime(1000, now + 0.1)
      gain.gain.setValueAtTime(0.3, now)
      gain.gain.exponentialRampToValueAtTime(0.01, now + 0.3)
      osc.start(now)
      osc.stop(now + 0.3)
    } catch { /* ignore */ }
  }

  // 格式化倒计时
  function formatCountdown(seconds: number): string {
    if (seconds <= 0) return '即将提醒...'
    const m = Math.floor(seconds / 60)
    const s = seconds % 60
    return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
  }

  onMounted(() => {
    loadAll().finally(() => resetTimer())

    // 每分钟只刷新数据（settings/records/stats），不碰 countdown
    const refreshInterval = window.setInterval(() => {
      loadAll()
    }, 60000)

    onUnmounted(() => {
      clearInterval(refreshInterval)
      stopTimer()
    })

    // 请求通知权限
    if ('Notification' in window && Notification.permission === 'default') {
      setTimeout(() => Notification.requestPermission(), 2000)
    }
  })

  return {
    settings,
    records,
    stats,
    countdown,
    showReminder,
    loading,
    saveSettings,
    drinkWater,
    removeRecord,
    dismissReminder,
    confirmDrink,
    formatCountdown,
  }
}
