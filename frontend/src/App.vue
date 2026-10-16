<script setup lang="ts">
</script>

<template>
  <router-view />
</template>

<style>
:root {
  --primary: #2196F3;
  --primary-dark: #1976D2;
  --accent: #00BCD4;
  --bg: #f0f7ff;
  --card: #ffffff;
  --text: #263238;
  --text-secondary: #607D8B;
  --progress-bg: #E0E0E0;
  --success: #4CAF50;
  --warning: #FF9800;
  --danger: #f44336;
  --border: #E0E0E0;
  --shadow: 0 2px 12px rgba(0,0,0,0.08);
  --radius: 16px;
}

* { margin: 0; padding: 0; box-sizing: border-box; }

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: var(--bg);
  color: var(--text);
  min-height: 100vh;
  display: flex;
  justify-content: center;
  padding: 16px;
}

#app {
  width: 100%;
  max-width: 480px;
}

.container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.card {
  background: var(--card);
  border-radius: var(--radius);
  padding: 20px;
  box-shadow: var(--shadow);
}

@media (max-width: 400px) {
  .container { gap: 10px; }
  .card { padding: 14px; }
}
</style>
