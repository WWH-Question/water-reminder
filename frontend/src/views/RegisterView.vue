<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '@/auth'
import * as api from '@/api'

const router = useRouter()
const { setAuth } = useAuth()

const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const error = ref('')
const loading = ref(false)

async function handleRegister() {
  error.value = ''
  if (!username.value || !password.value) {
    error.value = '请填写所有字段'
    return
  }
  if (username.value.trim().length < 3) {
    error.value = '用户名至少3个字符'
    return
  }
  if (password.value.length < 6) {
    error.value = '密码至少6位'
    return
  }
  if (password.value !== confirmPassword.value) {
    error.value = '两次密码不一致'
    return
  }
  loading.value = true
  try {
    const res = await api.register({ username: username.value.trim(), password: password.value })
    setAuth(res.token, res.username)
    router.push('/')
  } catch (e: any) {
    error.value = e.response?.data?.error || '注册失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-container">
    <div class="auth-card">
      <h1>💧 喝水提醒</h1>
      <p class="subtitle">创建新账号</p>

      <form @submit.prevent="handleRegister">
        <div class="field">
          <label>用户名</label>
          <input v-model="username" type="text" placeholder="至少3个字符" autocomplete="username" />
        </div>
        <div class="field">
          <label>密码</label>
          <input v-model="password" type="password" placeholder="至少6位" autocomplete="new-password" />
        </div>
        <div class="field">
          <label>确认密码</label>
          <input v-model="confirmPassword" type="password" placeholder="再次输入密码" autocomplete="new-password" />
        </div>

        <p v-if="error" class="error">{{ error }}</p>

        <button type="submit" :disabled="loading" class="btn-primary">
          {{ loading ? '注册中...' : '注 册' }}
        </button>
      </form>

      <p class="switch">
        已有账号？<router-link to="/login">去登录</router-link>
      </p>
    </div>
  </div>
</template>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
}
.auth-card {
  background: var(--card);
  border-radius: var(--radius);
  padding: 32px 28px;
  box-shadow: var(--shadow);
  width: 100%;
  max-width: 400px;
}
.auth-card h1 {
  font-size: 28px;
  text-align: center;
  margin-bottom: 4px;
}
.subtitle {
  text-align: center;
  color: var(--text-secondary);
  font-size: 14px;
  margin-bottom: 24px;
}
.field {
  margin-bottom: 16px;
}
.field label {
  display: block;
  font-size: 14px;
  font-weight: 600;
  color: var(--text);
  margin-bottom: 6px;
}
.field input {
  width: 100%;
  padding: 12px 14px;
  border: 2px solid var(--border);
  border-radius: 10px;
  font-size: 16px;
  font-family: inherit;
  outline: none;
  transition: border-color .2s;
}
.field input:focus {
  border-color: var(--primary);
}
.error {
  color: var(--danger);
  font-size: 14px;
  margin-bottom: 12px;
  text-align: center;
}
.btn-primary {
  width: 100%;
  padding: 14px;
  background: var(--primary);
  color: #fff;
  font-size: 17px;
  font-weight: 700;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: .2s;
  font-family: inherit;
}
.btn-primary:hover { background: var(--primary-dark); }
.btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }
.switch {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: var(--text-secondary);
}
.switch a { color: var(--primary); font-weight: 600; text-decoration: none; }
</style>
