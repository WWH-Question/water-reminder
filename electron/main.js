const { app, BrowserWindow, dialog } = require('electron')
const { spawn } = require('child_process')
const path = require('path')
const fs = require('fs')
const http = require('http')

// ── 配置 ──────────────────────────────────────────────────
const BACKEND_PORT = 8080
const DEV_MODE = process.env.ELECTRON_DEV === '1'

// ── 确定 Java 和 JAR 路径 ─────────────────────────────────
function getJavaPath() {
  if (!DEV_MODE) {
    const javaBin = process.platform === 'win32' ? 'java.exe' : 'java'
    // 多次尝试不同路径（兼容 portable exe 和 unpacked 目录）
    const candidates = [
      path.join(process.resourcesPath, 'jre', 'bin', javaBin),
      path.join(path.dirname(process.execPath), 'resources', 'jre', 'bin', javaBin),
      path.join(__dirname, '..', 'jre', 'bin', javaBin),
    ]
    for (const jre of candidates) {
      if (fs.existsSync(jre)) {
        console.log('[Electron] 找到内嵌 JRE:', jre)
        return jre
      }
    }
    console.log('[Electron] 未找到内嵌 JRE，尝试的系统 Java...')
  }
  return 'java'
}

function getJarPath() {
  if (!DEV_MODE) {
    // 多次尝试不同路径
    const candidates = [
      path.join(process.resourcesPath, 'water-reminder.jar'),
      path.join(path.dirname(process.execPath), 'resources', 'water-reminder.jar'),
      path.join(__dirname, '..', 'water-reminder.jar'),
    ]
    for (const jar of candidates) {
      if (fs.existsSync(jar)) {
        console.log('[Electron] 找到 JAR:', jar)
        return jar
      }
    }
    throw new Error(
      '找不到后端 JAR 文件！\n' +
      '请确认 water-reminder.jar 在 resources 目录中\n' +
      '已尝试:\n' + candidates.join('\n')
    )
  }
  // 开发模式：JAR 在 backend/target/ 下
  const devJar = path.join(__dirname, '..', 'backend', 'target', 'water-reminder-1.0.0.jar')
  if (!fs.existsSync(devJar)) {
    throw new Error(
      `找不到后端 JAR 文件，请先运行:\n` +
      `  cd backend && mvn package -DskipTests\n` +
      `期望路径: ${devJar}`
    )
  }
  return devJar
}

// ── 数据库路径 ────────────────────────────────────────────
function getDbPath() {
  // 使用用户数据目录，卸载不会丢数据
  const userDataDir = app.getPath('userData')
  return path.join(userDataDir, 'data', 'water_reminder')
}

// ── 启动后端 ──────────────────────────────────────────────
function startBackend() {
  const javaPath = getJavaPath()
  const jarPath = getJarPath()
  const dbPath = getDbPath()

  // 确保数据目录存在
  const dataDir = path.dirname(dbPath)
  fs.mkdirSync(dataDir, { recursive: true })

  console.log(`[Electron] 启动后端: ${javaPath}`)
  console.log(`[Electron] JAR: ${jarPath}`)
  console.log(`[Electron] 数据库: ${dbPath}`)

  const proc = spawn(javaPath, [
    `-DDB_PATH=${dbPath.replace(/\\/g, '/')}`,
    '-jar',
    jarPath,
    `--server.port=${BACKEND_PORT}`,
  ], {
    stdio: ['ignore', 'pipe', 'pipe'],
    env: { ...process.env },
  })

  proc.stdout.on('data', (data) => {
    console.log(`[Backend] ${data.toString().trim()}`)
  })

  proc.stderr.on('data', (data) => {
    console.error(`[Backend Error] ${data.toString().trim()}`)
  })

  proc.on('close', (code) => {
    console.log(`[Electron] 后端已退出 (code: ${code})`)
    if (mainWindow && !mainWindow.isDestroyed()) {
      mainWindow.webContents.send('backend-down')
    }
  })

  return proc
}

// ── 等待后端就绪 ──────────────────────────────────────────
function waitForBackend(maxRetries = 60, interval = 1000) {
  return new Promise((resolve, reject) => {
    let retries = 0
    const check = () => {
      const req = http.get(`http://localhost:${BACKEND_PORT}/api/settings`, (res) => {
        if (res.statusCode < 500) {
          console.log('[Electron] 后端已就绪')
          resolve(true)
        } else {
          retry()
        }
      })
      req.on('error', () => retry())
      req.setTimeout(2000, () => {
        req.destroy()
        retry()
      })
    }

    const retry = () => {
      retries++
      if (retries >= maxRetries) {
        reject(new Error('后端启动超时，请检查 Java 是否已安装'))
        return
      }
      setTimeout(check, interval)
    }

    check()
  })
}

// ── 创建窗口 ──────────────────────────────────────────────
let mainWindow = null

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 480,
    height: 820,
    minWidth: 380,
    minHeight: 600,
    title: '💧 喝水提醒',
    autoHideMenuBar: true,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration: false,
    },
  })

  const url = DEV_MODE
    ? 'http://localhost:5173'
    : `http://localhost:${BACKEND_PORT}`

  mainWindow.loadURL(url)

  mainWindow.on('closed', () => {
    mainWindow = null
  })
}

// ── 应用生命周期 ──────────────────────────────────────────
let backendProcess = null

app.whenReady().then(async () => {
  try {
    backendProcess = startBackend()
    await waitForBackend()
    createWindow()
  } catch (err) {
    dialog.showErrorBox('启动失败', err.message)
    app.quit()
  }
})

app.on('window-all-closed', () => {
  // Windows/Linux: 关闭所有窗口时退出
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('before-quit', () => {
  // 杀掉后端进程
  if (backendProcess && !backendProcess.killed) {
    console.log('[Electron] 正在关闭后端...')
    backendProcess.kill('SIGTERM')
    // 强制杀（Windows 不支持 SIGTERM）
    setTimeout(() => {
      if (backendProcess && !backendProcess.killed) {
        backendProcess.kill('SIGKILL')
      }
    }, 3000)
  }
})

app.on('activate', () => {
  // macOS: 点击 dock 图标重新创建窗口
  if (BrowserWindow.getAllWindows().length === 0) {
    createWindow()
  }
})
