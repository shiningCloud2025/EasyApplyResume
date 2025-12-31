#!/bin/bash
# EasyApply 部署脚本
# 使用方法: ./deploy.sh [start|stop|restart|build|logs]

set -e

COMPOSE_FILE="docker-compose.yml"

case "$1" in
    start)
        echo "🚀 启动服务..."
        docker-compose -f $COMPOSE_FILE up -d
        echo "✅ 服务已启动"
        docker-compose -f $COMPOSE_FILE ps
        ;;
    stop)
        echo "🛑 停止服务..."
        docker-compose -f $COMPOSE_FILE down
        echo "✅ 服务已停止"
        ;;
    restart)
        echo "🔄 重启服务..."
        docker-compose -f $COMPOSE_FILE down
        docker-compose -f $COMPOSE_FILE up -d
        echo "✅ 服务已重启"
        ;;
    build)
        echo "🔨 构建镜像..."
        docker-compose -f $COMPOSE_FILE build --no-cache
        echo "✅ 构建完成"
        ;;
    logs)
        docker-compose -f $COMPOSE_FILE logs -f ${2:-}
        ;;
    status)
        docker-compose -f $COMPOSE_FILE ps
        ;;
    *)
        echo "使用方法: $0 {start|stop|restart|build|logs|status}"
        echo ""
        echo "  start   - 启动所有服务"
        echo "  stop    - 停止所有服务"
        echo "  restart - 重启所有服务"
        echo "  build   - 重新构建镜像"
        echo "  logs    - 查看日志 (可指定服务名)"
        echo "  status  - 查看服务状态"
        exit 1
        ;;
esac
