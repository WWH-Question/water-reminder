# 💧 Water Reminder 喝水提醒

一款桌面喝水提醒应用：根据身高、体重自动计算每日饮水量目标，结合上下班时间智能拆分饮水节奏，定时提醒你按时喝水。

## ✨ 功能特性

- 🔐 **用户注册 / 登录**：JWT 认证，账号数据存本地 H2
- 🎯 **智能饮水目标**：每日需水量 = 体重 × 系数（可自定义系数）
- 🕐 **双模式提醒**：根据上下班时间自动切换「工作模式」（每 30 分钟）与「日常模式」（30 / 60 分钟）
- 📊 **进度环**：实时展示今日已喝 / 目标 / 剩余
- 💧 **快捷记录**：一键记录喝水，自动推荐每次饮用量
- 📋 **喝水记录**：按天查看历史记录
- ⏰ **倒计时提醒**：距下次喝水倒计时 + 弹窗提醒

## 🛠 技术栈

| 层 | 技术 |
|----|------|
| 桌面壳 | Electron 33 |
| 前端 | Vue 3 + TypeScript + Vite + vue-router + axios |
| 后端 | Spring Boot 3.2.1 + Spring Security + JWT (jjwt 0.12) |
| 数据库 | H2（本地文件数据库，用户信息 + 喝水记录，自包含）|
| 构建 | Maven + electron-builder（NSIS 安装包）|

## 📁 项目结构

```
waterdrink/
├── backend/          # Spring Boot 后端（Java 17）
│   └── src/main/java/com/waterreminder/
│       ├── config/       # 双数据源配置（均指向本地 H2）
│       ├── controller/   # Auth / Record / Settings / Stats / Admin
│       ├── entity/       # UserInfo（用户）/ DrinkRecord（喝水记录）
│       ├── security/     # JWT 认证
│       └── service/      # 饮水量计算 / 提醒逻辑
├── frontend/         # Vue 3 前端
│   └── src/
│       ├── views/        # 登录 / 注册 / 主页
│       ├── components/   # 进度环 / 快捷按钮 / 记录列表 / 提醒弹窗
│       └── api/          # axios 封装
├── electron/         # Electron 主进程 / preload
├── scripts/          # 构建 JRE 脚本
└── package.json      # 打包脚本（electron-builder）
```

## 🚀 本地开发

### 前置要求

- JDK 17、Maven
- Node.js 18+（含 npm）

### 后端

```bash
cd backend
mvn spring-boot:run
# 默认运行在 http://localhost:8080
```

### 前端

```bash
cd frontend
npm install
npm run dev
# 默认运行在 http://localhost:5173
```

### Electron（桌面壳）

```bash
# 先打包后端 JAR
cd backend && mvn package -DskipTests

# 再以开发模式启动 Electron（会加载 localhost:5173）
npm run dev:electron
```

## 📦 构建安装包

```bash
npm run build:all   # 依次构建前端 + 后端 + 精简 JRE
npm run package     # 生成 NSIS 安装包到 release/
```

打包后 Electron 会**内嵌 JRE + 后端 JAR**，双击安装即可离线运行，无需额外安装 Java 或数据库服务（后端启动时自动运行在 `localhost:8080`）。

## 🔑 环境变量

`application.yml` 中的敏感信息已改为环境变量占位符，`JWT_SECRET` 已有可用默认值，本地开发可直接运行；如需自定义：

| 变量 | 说明 |
|------|------|
| `JWT_SECRET` | JWT 签名密钥（base64 编码，**至少 32 字节**）|

Windows 示例（PowerShell）：

```powershell
$env:JWT_SECRET="base64编码的密钥"
```

## 🗄 数据库说明

| 数据 | 存储 | 位置 |
|------|------|------|
| 用户信息（用户名 / 密码哈希 / 身高 / 体重 / 上下班时间 / 睡觉时间）| H2 文件数据库 | `./data/water_reminder_user` |
| 喝水记录 | H2 文件数据库 | `./data/water_reminder_h2` |
