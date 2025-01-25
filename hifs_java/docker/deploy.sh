#!/bin/bash

ENV=$1
if [ "$ENV" != "dev" ] && [ "$ENV" != "prod" ]; then
    echo "Usage: ./deploy.sh [dev|prod]"
    exit 1
fi

# 获取脚本所在目录
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "${SCRIPT_DIR}"

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

# 转换文件格式为 Unix
dos2unix rocketmq/broker/conf/broker.conf.template 2>/dev/null || true

# 调试信息
echo "Current environment variables:"
env | grep BROKER_IP1

echo "Content of broker.conf.template:"
cat rocketmq/broker/conf/broker.conf.template

# 生成配置文件
envsubst < rocketmq/broker/conf/broker.conf.template > rocketmq/broker/conf/broker.conf
envsubst < redis/redis.conf.template > redis/redis.conf



# 检查生成的配置文件
echo "Generated broker.conf:"
cat rocketmq/broker/conf/broker.conf

# 启动服务
if [ "$ENV" = "dev" ]; then
    docker compose --profile dev up -d
else
    docker compose --profile prod up -d
fi

# 等待服务启动
echo "Waiting for services to start..."
sleep 10

# 检查服务状态
docker compose ps 