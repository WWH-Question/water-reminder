<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps<{
  show: boolean
  suggestedAmount: number
}>()

const emit = defineEmits<{
  confirm: [amount: number]
  dismiss: []
}>()

const amount = ref(props.suggestedAmount > 0 ? props.suggestedAmount : 200)
</script>

<template>
  <div v-if="show" class="modal-overlay" @click="emit('dismiss')">
    <div class="modal" @click.stop>
      <div class="modal-icon">💧</div>
      <h2>该喝水啦！</h2>
      <p class="modal-sub">
        {{ suggestedAmount > 0 ? '建议喝 ' + suggestedAmount + ' ml' : '自由饮用，保持水分 💪' }}
      </p>
      <div class="modal-input-row">
        <input v-model.number="amount" type="number" min="10" max="2000" @keydown.enter="emit('confirm', amount)" />
        <span>ml</span>
      </div>
      <div class="modal-btns">
        <button class="btn-skip" @click="emit('dismiss')">跳过</button>
        <button class="btn-confirm" @click="emit('confirm', amount)">✅ 记录喝水</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed; inset: 0;
  background: rgba(0,0,0,0.5); z-index: 100;
  display: flex; align-items: center; justify-content: center;
}
.modal {
  background: #fff; border-radius: 16px;
  padding: 28px 24px; text-align: center;
  max-width: 360px; width: 90%;
  box-shadow: 0 8px 32px rgba(0,0,0,0.2);
  animation: modalIn .3s ease;
}
@keyframes modalIn { from { transform: scale(.9); opacity:0; } to { transform: scale(1); opacity:1; } }
.modal-icon { font-size: 48px; margin-bottom: 8px; }
.modal h2 { font-size: 20px; margin: 0 0 4px; }
.modal-sub { font-size: 14px; color: #607D8B; margin-bottom: 16px; }
.modal-input-row {
  display: flex; gap: 8px; justify-content: center; margin-bottom: 16px;
}
.modal-input-row input {
  width: 100px; padding: 10px; border: 2px solid #E0E0E0;
  border-radius: 10px; font-size: 20px; text-align: center;
  font-family: inherit;
}
.modal-input-row input:focus { outline: none; border-color: #2196F3; }
.modal-input-row span { align-self: center; font-size: 16px; color: #607D8B; }
.modal-btns { display: flex; gap: 10px; }
.modal-btns button {
  flex: 1; padding: 12px; border: none; border-radius: 10px;
  font-size: 15px; font-weight: 600; cursor: pointer; font-family: inherit;
}
.btn-confirm { background: #2196F3; color: #fff; }
.btn-confirm:hover { background: #1976D2; }
.btn-skip { background: #F5F5F5; color: #607D8B; }
.btn-skip:hover { background: #E0E0E0; }
</style>
