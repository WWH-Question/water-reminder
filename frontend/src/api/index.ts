import axios from 'axios'
import type { Settings, DrinkRecord, Stats, AdminUser } from '@/types'

const api = axios.create({
  baseURL: '/api',
  timeout: 5000,
})

// Request interceptor — inject JWT
api.interceptors.request.use(config => {
  const token = localStorage.getItem('water_reminder_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Response interceptor — handle 401
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('water_reminder_token')
      localStorage.removeItem('water_reminder_user')
      if (!window.location.pathname.startsWith('/login') && !window.location.pathname.startsWith('/register')) {
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

// Auth
export interface LoginRequest { username: string; password: string }
export interface RegisterRequest { username: string; password: string }
export interface AuthResponse { token: string; username: string }

export async function login(req: LoginRequest): Promise<AuthResponse> {
  const { data } = await api.post('/auth/login', req)
  return data
}

export async function register(req: RegisterRequest): Promise<AuthResponse> {
  const { data } = await api.post('/auth/register', req)
  return data
}

// Settings
export async function fetchSettings(): Promise<Settings> {
  const { data } = await api.get('/settings')
  return data
}

export async function updateSettings(settings: Settings): Promise<Settings> {
  const { data } = await api.put('/settings', settings)
  return data
}

// Records
export async function fetchRecords(): Promise<DrinkRecord[]> {
  const { data } = await api.get('/records')
  return data
}

export async function addRecord(amount: number): Promise<DrinkRecord> {
  const { data } = await api.post('/records', { amount })
  return data
}

export async function deleteRecord(id: number): Promise<void> {
  await api.delete(`/records/${id}`)
}

// Stats
export async function fetchStats(): Promise<Stats> {
  const { data } = await api.get('/stats')
  return data
}

// Admin
export async function fetchAllUsers(): Promise<AdminUser[]> {
  const { data } = await api.get('/admin/users')
  return data
}

export async function updateAdminUser(id: number, user: Partial<AdminUser>): Promise<AdminUser> {
  const { data } = await api.put(`/admin/users/${id}`, user)
  return data
}
