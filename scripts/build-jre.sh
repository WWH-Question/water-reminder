#!/bin/bash
# 使用 jlink 生成最小 JRE 用于 Electron 打包
# 需要 JDK 17+

JDK_HOME="D:/jdkjdk"
JRE_OUTPUT="$(dirname "$0")/../jre"

echo "=== 生成最小 JRE ==="
echo "JDK: $JDK_HOME"
echo "输出: $JRE_OUTPUT"

# 清理旧的 JRE
rm -rf "$JRE_OUTPUT"

# jlink 需要的模块 (Spring Boot + H2 + JPA)
"$JDK_HOME/bin/jlink" \
  --compress=2 \
  --no-header-files \
  --no-man-pages \
  --add-modules java.base,java.sql,java.naming,java.management,java.instrument,java.xml,java.desktop,java.security.jgss \
  --output "$JRE_OUTPUT"

if [ -d "$JRE_OUTPUT" ]; then
  JRE_SIZE=$(du -sh "$JRE_OUTPUT" 2>/dev/null | cut -f1)
  echo "=== JRE 生成成功: $JRE_OUTPUT ($JRE_SIZE) ==="
else
  echo "=== JRE 生成失败 ==="
  exit 1
fi
