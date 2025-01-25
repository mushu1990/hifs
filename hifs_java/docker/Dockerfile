# 构建阶段：使用 Maven 4.x + JDK 17
FROM maven:4.0.0-alpha-13-openjdk-17 AS builder

WORKDIR /build
# 先复制 pom.xml 并下载依赖（利用 Docker 缓存）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源码并构建
COPY src/ ./src/
RUN mvn clean package -DskipTests

# ----------------------------

# 运行阶段：使用轻量级 JRE 17
FROM openjdk:17-jdk-slim

# 设置时区、非 root 用户
RUN apt-get update && apt-get install -y --no-install-recommends tzdata \
    && rm -rf /var/lib/apt/lists/* \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && useradd -m appuser
WORKDIR /app
USER appuser

# 从构建阶段复制产物
COPY --from=builder --chown=appuser:appuser /build/target/*.jar ./app.jar

# 暴露端口与健康检查
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]