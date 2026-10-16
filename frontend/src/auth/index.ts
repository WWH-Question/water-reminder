import { ref, computed } from 'vue'

const TOKEN_KEY = 'water_reminder_token'
const USER_KEY = 'water_reminder_user'

const token = ref<string | null>(localStorage.getItem(TOKEN_KEY))
const username = ref<string | null>(localStorage.getItem(USER_KEY))

const isAuthenticated = computed(() => !!token.value)

function setAuth(t: string, u: string) {
  token.value = t
  username.value = u
  localStorage.setItem(TOKEN_KEY, t)
  localStorage.setItem(USER_KEY, u)
}

function clearAuth() {
  token.value = null
  username.value = null
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}

export function useAuth() {
  return { token, username, isAuthenticated, setAuth, clearAuth }
}
