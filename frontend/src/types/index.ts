export interface Settings {
  height: number
  weight: number
  coefficient: number
  workStart: string
  workEnd: string
  sleepTime: string
}

export interface DrinkRecord {
  id: number
  amount: number
  mode: 'work' | 'daily'
  recordTime: string
  recordDate: string
}

export interface Stats {
  drank: number
  target: number
  remaining: number
  progress: number
  suggestedAmount: number
  mode: 'work' | 'daily'
  countdownSeconds: number
}

export interface AuthResponse {
  token: string
  username: string
}

export interface AdminUser {
  id: number
  username: string
  height: number
  weight: number
  coefficient: number
  workStart: string
  workEnd: string
  sleepTime: string
}
