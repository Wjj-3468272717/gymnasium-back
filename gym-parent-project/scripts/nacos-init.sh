#!/bin/sh
set -e

NACOS_URL="http://127.0.0.1:8848"
HEALTH_URL="${NACOS_URL}/nacos/v1/console/health/readiness"
CONFIG_URL="${NACOS_URL}/nacos/v1/cs/configs"

echo "[nacos-init] Waiting for Nacos health endpoint..."
for i in $(seq 1 40); do
    STATUS=$(curl -s -o /dev/null -w "%{http_code}" "$HEALTH_URL" 2>/dev/null || echo "000")
    if [ "$STATUS" = "200" ]; then
        echo "[nacos-init] Nacos is ready (attempt $i)."
        break
    fi
    if [ "$i" -eq 40 ]; then
        echo "[nacos-init] ERROR: Nacos did not become ready after 40 attempts."
        exit 1
    fi
    sleep 3
done

echo "[nacos-init] Uploading gym-common.properties..."

CONTENT=$(printf '%s\n' \
    'spring.datasource.type=com.alibaba.druid.pool.DruidDataSource' \
    'spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver' \
    'spring.datasource.url=jdbc:mysql://localhost:3306/gymnasium?serverTimezone=Asia/Shanghai&characterEncoding=utf-8' \
    'spring.datasource.username=root' \
    'spring.datasource.password=123456' \
    'mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl' \
    'minio.endpoint=http://localhost:9000' \
    'minio.accessKey=minioadmin' \
    'minio.secretKey=minioadmin')

HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$CONFIG_URL" \
    --data-urlencode "dataId=gym-common.properties" \
    --data-urlencode "group=DEFAULT_GROUP" \
    --data-urlencode "type=properties" \
    --data-urlencode "content=$CONTENT")

if [ "$HTTP_CODE" = "200" ]; then
    echo "[nacos-init] Config uploaded successfully (HTTP $HTTP_CODE)."
else
    echo "[nacos-init] ERROR: Config upload returned HTTP $HTTP_CODE"
    exit 1
fi

VERIFY=$(curl -s "${CONFIG_URL}?dataId=gym-common.properties&group=DEFAULT_GROUP")
if [ -n "$VERIFY" ]; then
    echo "[nacos-init] Verified: config is present in Nacos."
else
    echo "[nacos-init] WARNING: Could not verify config retrieval."
fi

echo "[nacos-init] Done."
