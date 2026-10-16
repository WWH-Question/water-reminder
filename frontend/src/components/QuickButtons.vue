<script setup lang="ts">
import { ref } from 'vue'

const emit = defineEmits<{ drink: [amount: number] }>()

const presets = [100, 200, 300, 500]
const customAmount = ref<number | null>(null)

function handleCustom() {
  if (customAmount.value && customAmount.value > 0) {
    emit('drink', customAmount.value)
    customAmount.value = null
  }
}
</script>

<template>
  <div class="quick-btns">
    <button
      v-for="amount in presets"
      :key="amount"
      class="quick-btn"
      @click="emit('drink', amount)"
    >
      💧 {{ amount }} ml
    </button>
    <div class="quick-btn custom">
      ✏️
      <input
        v-model.number="customAmount"
        type="number"
        placeholder="自定义 ml"
        min="10"
        max="2000"
        @keydown.enter="handleCustom"
      />
    </div>
  </div>
</template>

<style scoped>
.quick-btns { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
.quick-btn {
  padding: 14px;
  border: 2px solid #E0E0E0;
  border-radius: 12px;
  background: #fff;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: .2s;
  color: #263238;
  font-family: inherit;
}
.quick-btn:hover { border-color: #2196F3; background: #E3F2FD; }
.quick-btn:active { transform: scale(0.97); }
.quick-btn.custom {
  grid-column: 1 / -1;
  display: flex; align-items: center; gap: 8px;
  cursor: default;
}
.quick-btn.custom input {
  flex: 1; border: none; outline: none; font-size: 16px;
  background: transparent; text-align: center; color: #263238;
  font-family: inherit;
}
.quick-btn.custom input::placeholder { color: #BDBDBD; }
</style>
