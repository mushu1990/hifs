#!/bin/bash

ENV=$1
if [ "$ENV" != "dev" ] && [ "$ENV" != "prod" ]; then
    echo "Usage: ./deploy.sh [dev|prod]"
    exit 1
fi



# 获取脚本所在目录（同时支持 sh 和 bash）
if [ -n "$BASH_SOURCE" ]; then
    SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
else
    SCRIPT_DIR="$( cd "$( dirname "$0" )" && pwd )"
fi

echo "当前执行目录: $(pwd)"
echo "脚本目录: ${SCRIPT_DIR}"

# 创建必要的目录
mkdir -p data/{mysql,redis,nginx/logs,rocketmq/{namesrv/logs,broker/{logs,store}}} \
         mysql/init \
         redis \
         rocketmq/{namesrv,broker}/{logs,store} \
         nginx/{conf.d,ssl,logs}

# 设置权限
chmod -R 777 data/rocketmq
chmod -R 777 rocketmq

# 加载环境变量
if [ "$ENV" = "dev" ]; then
    set -a
    source .env.dev
    set +a
else
    set -a
    source .env.prod
    set +a
fi


# 生成配置文件
envsubst < rocketmq/broker/conf/broker.conf.template > rocketmq/broker/conf/broker.conf
envsubst < redis/redis.conf.template > redis/redis.conf


# 检查容器是否成功启动
check_container_status() {
    local container_name=$1
    local max_attempts=30  # 最大等待时间（秒）
    local attempt=1

    echo "正在检查 $container_name 服务状态..."
    
    while [ $attempt -le $max_attempts ]; do
        if docker ps --format '{{.Names}}' | grep -q "^${container_name}$"; then
            # 检查容器是否真正运行（不是 restarting 或其他状态）
            local status=$(docker inspect -f '{{.State.Status}}' "${container_name}")
            if [ "$status" = "running" ]; then
                echo "✅ ${container_name} 启动成功"
                return 0
            fi
        fi
        echo "等待 ${container_name} 启动... (${attempt}/${max_attempts})"
        sleep 1
        attempt=$((attempt + 1))
    done
    
    echo "❌ ${container_name} 启动失败"
    return 1
}

# 关闭所有服务
shutdown_all() {
    echo "检测到服务启动失败，正在关闭所有服务..."
    docker-compose down
    exit 1
}

# 启动服务
if [ "$ENV" = "dev" ]; then
    docker compose --profile dev up -d
else
    docker compose --profile prod up -d
fi

# 定义需要检查的服务列表（根据你的 docker-compose.yml 修改）
services=(
    "mysql"
    "redis"
    "rocketmq-namesrv"
    "rocketmq-broker"
    "rocketmq-dashboard"    
    "nginx"
)

# 仅在 prod 环境下添加 app 服务检查
if [ "$ENV" = "prod" ]; then
    services+=("app")
    echo "生产环境：将检查 app 服务"
else
    echo "开发环境：跳过 app 服务检查"
fi

# 检查所有服务
for service in "${services[@]}"; do
    if ! check_container_status "$service"; then
        echo "服务 $service 启动失败，执行清理..."
        shutdown_all
    fi
done

echo "✨ 所有服务启动成功！" 