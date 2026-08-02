# Docker 本地部署（host 网络模式）

> 将 `gym-parent-project`（Spring Boot + Dubbo + Nacos 微服务）部署到 Linux 虚拟机，
> 使用 Docker Compose + `network_mode: host`。**前提：虚拟机上已装 Docker，并配置了国内镜像加速。**

## 部署文件说明

这套部署在 `gym-parent-project/` 下新增了 4 个文件：

| 文件 | 作用 |
|------|------|
| `Dockerfile` | 通用服务运行时镜像。构建时用 `ARG JAR_FILE` 指定各服务的 jar 路径，6 个服务共用 |
| `Dockerfile.nacos-init` | 一次性容器：等待 Nacos 就绪后上传共享配置，成功后退出 |
| `docker-compose.yml` | 编排 MySQL / Nacos / MinIO + 6 个 Java 服务，**全部 `network_mode: host`** |
| `scripts/nacos-init.sh` | 把 `gym-common.properties`（数据源 / MyBatis-Plus / MinIO 配置）上传到 Nacos，所有服务启动时从这里拉取 |

### 为什么 host 网络下不用改任何配置

项目所有服务的配置都把 Nacos / MySQL / MinIO / Dubbo 的地址写死为 `127.0.0.1`。
host 网络模式下，**所有容器共享宿主机的网络栈**——容器里的 `127.0.0.1` 就是虚拟机自己，
所以这些写死的地址原样可用，Java 代码一行都不用改。Dubbo 服务也以虚拟机 IP（如 `192.168.240.128`）注册到 Nacos，同机容器互相可达。

---

## Step 0：前置检查（在 VM 上）

```bash
# 确认工具
git --version && docker --version && docker compose version

# 确认端口没被占用（若 VM 自带 MySQL 或已有监控栈，需先停掉）
# 注：Nacos 2.x 会额外监听 9848（gRPC 端口）
ss -tlnp 2>/dev/null | grep -E "3306|8848|9848|9000|9001|808[1-5]|9999|2088[0-5]" || echo "端口全部空闲"
```

端口冲突时用 `docker ps` 找出占用者，`docker stop <容器名>` 释放后再继续。

---

## Step 1：克隆代码

```bash
git clone https://github.com/Wjj-3468272717/gymnasium-back.git
cd gymnasium-back/gym-parent-project
```

---

## Step 2：预拉取镜像（国内镜像源绕行）

> 国内部分镜像源（nju / daocloud）对部分仓库返回 **403**，提前逐条拉取验证，fail fast。
> **如果你能直连 Docker Hub，本步骤可全部跳过**（`docker compose build/up` 会自动拉取）。

```bash
# 运行时基础镜像（Dockerfile 用）
#   原 openjdk:8-jre-slim 镜像已废弃，部分镜像源 403，故 Dockerfile 改用 eclipse-temurin
docker pull eclipse-temurin:8-jre

# 中间件
docker pull mysql:8.0
docker pull minio/minio

# Nacos：nju / daocloud 对 nacos/nacos-server 官方仓库返回 403，改用阿里云官方仓库
docker pull nacos-registry.cn-hangzhou.cr.aliyuncs.com/nacos/nacos-server:v2.2.3

# 把 Nacos 打成 compose 需要的名字（compose 里引用的是 nacos/nacos-server:v1.4.2）
#   实际跑的是 2.2.3：Nacos 2.x 向下兼容 1.x 客户端协议
#   （Dubbo 2.7.8 + nacos-client 1.4.1 走 HTTP/8848），所以能正常工作
docker tag nacos-registry.cn-hangzhou.cr.aliyuncs.com/nacos/nacos-server:v2.2.3 nacos/nacos-server:v1.4.2

# 确认
docker images | grep -E "eclipse-temurin|nacos|mysql|minio"
```

---

## Step 3：Docker 内编译 jar（一次性，约 10–15 分钟）

VM 没有 Maven/JDK，用 dockerized Maven 编译整个项目：

```bash
docker run --rm \
  -v "$(pwd)":/app \
  -v maven-repo:/root/.m2 \
  -w /app \
  maven:3.6.3-openjdk-8 \
  mvn clean install -DskipTests
```

> `maven-repo` 是命名卷，缓存依赖，下次重新编译只需几分钟。

**编译成功**的标志是最后的 `BUILD SUCCESS`。验证产物：

```bash
ls -lh gym-service-*/target/*-exec.jar gym-service-web/target/gym-service-web-1.0-SNAPSHOT.jar
```

> 注意：5 个 Provider 产出 `*-exec.jar`，web 产出标准 jar（web 没有 exec 版本）。

---

## Step 4：构建镜像

```bash
docker compose build
```

7 个镜像：5 个 Provider + web + nacos-init，全部显示 `Built` 即成功。

---

## Step 5：启动全部服务

```bash
docker compose up -d
```

启动顺序自动编排：MySQL → Nacos → nacos-init（上传配置）→ 5 个 Provider + Web。等待 1 分钟左右。

---

## Step 6：查看状态

```bash
docker compose ps
```

预期结果：
- `mysql`、`nacos`、`minio`、`gym-service-user/member/course/goods/home/web` → **Up**
- `nacos-init` → **Exited (0)**（一次性任务，正常）

---

## Step 7：验证

### 7.1 配置中心加载成功

```bash
docker compose logs gym-service-user | grep NacosConfig
```

预期：`[NacosConfig] Loaded 8 properties from gym-common.properties`

![NacosConfig](Docker部署.assets/image-20260803005612236.png)

### 7.2 验证码接口（web 服务是否响应）

```bash
curl -s http://127.0.0.1:9999/api/login/image -X POST
```

返回含 base64 图片的 JSON 即正常，同时 web 日志会打印验证码：

```bash
docker compose logs gym-service-web | grep "图片验证码"
```

![图片验证码](Docker部署.assets/image-20260803005905654.png)

### 7.3 Dubbo 服务注册到 Nacos

```bash
curl -s "http://127.0.0.1:8848/nacos/v1/ns/service/list?pageNo=1&pageSize=50"
```

或浏览器打开 `http://<VM_IP>:8848/nacos`（**用户名/密码都是 `nacos`**）。

> **注意**：Nacos 里是**每个 RPC 接口注册一个服务**，不是按应用。
> 能看到 20+ 个 `providers:` 开头的服务（gym-service-user/member/course/goods/home/web 相关），
> 以及 consumers 服务，即表示 Dubbo 注册正常（项目共 22 个 @DubboService Provider）。

![Nacos 服务列表](Docker部署.assets/image-20260803005823240.png)

![Nacos 控制台](Docker部署.assets/image-20260803010127980.png)

### 7.4 登录功能（全链路验证）

```bash
# 取验证码（从响应头 X-Captcha 提取，保存会话 cookie）→ 自动登录，一条龙
CODE=$(curl -s -c /tmp/cookies.txt -D - -o /dev/null http://127.0.0.1:9999/api/login/image -X POST \
        | grep -i x-captcha \
        | tr -d '\r' \
        | cut -d' ' -f2)
echo "本次验证码: $CODE"

curl -s -b /tmp/cookies.txt -X POST http://127.0.0.1:9999/api/login/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"123456\",\"code\":\"$CODE\",\"userType\":\"2\"}"
```

> `userType`：`"1"` 会员，`"2"` 员工。admin 是员工，用 `"2"`。
> **必须用 `-c`/`-b` 共享 cookie**——验证码存在会话里，登录不带同一会话会报"验证码过期"。

登录成功返回：

```json
{"code":200,"msg":"登录成功","data":{"token":"eyJ0eXAi...","userId":6,"username":"admin1","userType":"2"}}
```

![登录成功](Docker部署.assets/image-20260803011621608.png)

这次成功的登录验证了整条链路：

```
前端请求 → gym-service-web(:9999)
         → Dubbo RPC（经 Nacos 发现服务）
         → gym-service-user(:20881)
         → MySQL（查到 admin 用户）
         → BCrypt 校验密码
         → 签发 JWT token ✅
```

---

## 系统访问信息

| 项目 | 地址 |
|------|------|
| 后端 API | `http://<VM_IP>:9999` |
| Swagger 文档 | `http://<VM_IP>:9999/swagger-ui.html` |
| Nacos 控制台 | `http://<VM_IP>:8848/nacos`（nacos / nacos） |
| MinIO 控制台 | `http://<VM_IP>:9001`（minioadmin / minioadmin） |
| 测试账号 | 员工 `admin`/`123456`，会员 `2022001`/`123456` |

---

## MinIO 地址注意事项（重要）

Nacos 配置里 `minio.endpoint=http://localhost:9000`。
- **VM 本机访问**：没问题。
- **其他电脑的浏览器访问**：上传图片后返回/存储的 URL 是 `localhost:9000/...`，浏览器打不开。

解决：把 `scripts/nacos-init.sh` 里的 `minio.endpoint` 改成 `http://<VM_IP>:9000`，然后：

```bash
docker compose build nacos-init && docker compose up -d
```

（`docker compose down` 后重新 `up` 会重新执行 nacos-init。）

---

## 排错指南

| 现象 | 原因 | 处理 |
|------|------|------|
| `docker pull` 返回 403 | 镜像源对该仓库有限制（openjdk、nacos 常见） | 按 Step 2 用 eclipse-temurin / 阿里云仓库，或 `docker pull docker.m.daocloud.io/<镜像>` 换源 |
| 端口冲突（`port is already allocated`） | 主机端口被其他容器/服务占用 | `docker ps` 找占用者，`docker stop <容器>` 释放 |
| `nacos-init` 一直 Exited 非 0 | Nacos 没起来或配置上传失败 | `docker compose logs nacos-init` 看原因，确认 nacos 已 healthy |
| Java 服务反复重启 | Nacos 配置未加载（数据源缺失）或 MySQL 未就绪 | `docker compose logs gym-service-user` 看报错；MySQL 首次初始化需 30-40s 才 healthy |
| 登录报"验证码过期" | 登录请求和取验证码的会话不是同一个（cookie 没共享） | 用 7.4 的自动化命令（同一会话取码+登录） |
| Dubbo 调用超时 | 注册的 IP 不可达 | 确认 Provider 在 Nacos 注册的 IP 是本机可达 IP（host 网络下一般是 VM_IP） |

---

## 重新部署与清理

```bash
# 重新部署（代码更新后）
cd gymnasium-back/gym-parent-project
git pull
docker run --rm -v "$(pwd)":/app -v maven-repo:/root/.m2 -w /app \
  maven:3.6.3-openjdk-8 mvn clean install -DskipTests
docker compose build
docker compose up -d

# 停止服务（保留数据卷）
docker compose down

# 停止并清空数据卷（⚠️ 谨慎：会删除 MySQL / Nacos / MinIO 的全部数据）
docker compose down -v
```
