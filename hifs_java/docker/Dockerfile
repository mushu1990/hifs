# ====================== 全局变量 ======================
ARG MAIN_MODULE="main-module"
ARG JAR_NAME="application.jar"

# ====================== 阶段1：Maven构建 ======================
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# 重新声明 ARG 变量（每个阶段都需要声明才能使用）
ARG MAIN_MODULE

WORKDIR /build
COPY pom.xml .
COPY */pom.xml ./
# 离线下载所有依赖（利用Docker缓存加速）
RUN mvn dependency:go-offline -B

# 复制所有源码
COPY . .

# 构建所有模块
RUN mvn clean package -DskipTests

# ====================== 阶段2：JAR文件整理 ======================
FROM alpine:3.18 AS jar-exporter

# 重新声明 ARG 变量
ARG MAIN_MODULE
ARG JAR_NAME

WORKDIR /output

# 复制所有构建的 JAR 文件
COPY --from=builder /build/**/target/*.jar ./app/

# 仅保留主模块的 JAR 文件
RUN MAIN_JAR=$(find ./app -name "$MAIN_MODULE*.jar" | head -n 1) && \
    if [ -z "$MAIN_JAR" ]; then \
        echo "错误：未找到主模块 JAR 文件，目录内容如下："; \
        ls -l ./app; \
        exit 1; \
    else \
        # 去除版本号，保留主模块名
        MAIN_JAR_NAME=$(echo "$MAIN_JAR" | sed -E "s/-[0-9]+\.[0-9]+\.[0-9]+\.jar$/.jar/"); \
        echo "找到 JAR: $MAIN_JAR，重命名为 $JAR_NAME"; \
        mv "$MAIN_JAR" "./app/$JAR_NAME"; \
    fi

