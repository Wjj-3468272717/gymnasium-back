# 通用服务运行时镜像：构建时通过 ARG JAR_FILE 指定各服务的 jar 路径
# 基础镜像用 eclipse-temurin:8-jre（官方 openjdk:8 镜像已废弃，部分镜像源返回 403）
FROM eclipse-temurin:8-jre

ARG JAR_FILE
ENV TZ=Asia/Shanghai

# 安装 tzdata（保证 /usr/share/zoneinfo/Asia/Shanghai 存在）+ curl（排查问题用）
RUN apt-get update && apt-get install -y --no-install-recommends tzdata curl \
    && rm -rf /var/lib/apt/lists/* \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone

WORKDIR /app
COPY ${JAR_FILE} /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
