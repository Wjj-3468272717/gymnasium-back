@echo off
REM Upload all service configs to Nacos Config
set NACOS_URL=http://127.0.0.1:8848/nacos/v1/cs/configs

echo Uploading gym-service-user.yaml...
curl -s -X POST "%NACOS_URL%" -d "dataId=gym-service-user.yaml&group=DEFAULT_GROUP&type=yaml&content=spring:%%0A  datasource:%%0A    type: com.alibaba.druid.pool.DruidDataSource%%0A    driver-class-name: com.mysql.cj.jdbc.Driver%%0A    url: jdbc:mysql://localhost:3306/gymnasium?serverTimezone=Asia/Shanghai%%26characterEncoding=utf-8%%0A    username: root%%0A    password: 123456%%0Amybatis-plus:%%0A  configuration:%%0A    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl%%0Adubbo:%%0A  registry:%%0A    address: nacos://127.0.0.1:8848%%0A  protocol:%%0A    host: 127.0.0.1%%0A    port: 20881%%0A  application:%%0A    name: gym-service-user%%0A  scan:%%0A    base-packages: com.v1.service.user.provider%%0Aserver:%%0A  port: 8081"
echo.

echo Uploading gym-service-member.yaml...
curl -s -X POST "%NACOS_URL%" -d "dataId=gym-service-member.yaml&group=DEFAULT_GROUP&type=yaml&content=spring:%%0A  datasource:%%0A    type: com.alibaba.druid.pool.DruidDataSource%%0A    driver-class-name: com.mysql.cj.jdbc.Driver%%0A    url: jdbc:mysql://localhost:3306/gymnasium?serverTimezone=Asia/Shanghai%%26characterEncoding=utf-8%%0A    username: root%%0A    password: 123456%%0Amybatis-plus:%%0A  configuration:%%0A    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl%%0Adubbo:%%0A  registry:%%0A    address: nacos://127.0.0.1:8848%%0A  protocol:%%0A    host: 127.0.0.1%%0A    port: 20882%%0A  application:%%0A    name: gym-service-member%%0A  scan:%%0A    base-packages: com.v1.service.member.provider%%0Aserver:%%0A  port: 8082"
echo.

echo Uploading gym-service-course.yaml...
curl -s -X POST "%NACOS_URL%" -d "dataId=gym-service-course.yaml&group=DEFAULT_GROUP&type=yaml&content=spring:%%0A  datasource:%%0A    type: com.alibaba.druid.pool.DruidDataSource%%0A    driver-class-name: com.mysql.cj.jdbc.Driver%%0A    url: jdbc:mysql://localhost:3306/gymnasium?serverTimezone=Asia/Shanghai%%26characterEncoding=utf-8%%0A    username: root%%0A    password: 123456%%0Amybatis-plus:%%0A  configuration:%%0A    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl%%0Adubbo:%%0A  registry:%%0A    address: nacos://127.0.0.1:8848%%0A  protocol:%%0A    host: 127.0.0.1%%0A    port: 20883%%0A  application:%%0A    name: gym-service-course%%0A  scan:%%0A    base-packages: com.v1.service.course.provider%%0Aserver:%%0A  port: 8083"
echo.

echo Uploading gym-service-goods.yaml...
curl -s -X POST "%NACOS_URL%" -d "dataId=gym-service-goods.yaml&group=DEFAULT_GROUP&type=yaml&content=spring:%%0A  datasource:%%0A    type: com.alibaba.druid.pool.DruidDataSource%%0A    driver-class-name: com.mysql.cj.jdbc.Driver%%0A    url: jdbc:mysql://localhost:3306/gymnasium?serverTimezone=Asia/Shanghai%%26characterEncoding=utf-8%%0A    username: root%%0A    password: 123456%%0Amybatis-plus:%%0A  configuration:%%0A    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl%%0Adubbo:%%0A  registry:%%0A    address: nacos://127.0.0.1:8848%%0A  protocol:%%0A    host: 127.0.0.1%%0A    port: 20884%%0A  application:%%0A    name: gym-service-goods%%0A  scan:%%0A    base-packages: com.v1.service.goods.provider%%0Aserver:%%0A  port: 8084"
echo.

echo Uploading gym-service-home.yaml (with minio)...
curl -s -X POST "%NACOS_URL%" -d "dataId=gym-service-home.yaml&group=DEFAULT_GROUP&type=yaml&content=spring:%%0A  datasource:%%0A    type: com.alibaba.druid.pool.DruidDataSource%%0A    driver-class-name: com.mysql.cj.jdbc.Driver%%0A    url: jdbc:mysql://localhost:3306/gymnasium?serverTimezone=Asia/Shanghai%%26characterEncoding=utf-8%%0A    username: root%%0A    password: 123456%%0Amybatis-plus:%%0A  configuration:%%0A    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl%%0Adubbo:%%0A  registry:%%0A    address: nacos://127.0.0.1:8848%%0A  protocol:%%0A    host: 127.0.0.1%%0A    port: 20885%%0A  application:%%0A    name: gym-service-home%%0A  scan:%%0A    base-packages: com.v1.service.home.provider%%0Aserver:%%0A  port: 8085%%0Aminio:%%0A  endpoint: http://localhost:9000%%0A  accessKey: minioadmin%%0A  secretKey: minioadmin"
echo.

echo Uploading gym-service-web.yaml...
curl -s -X POST "%NACOS_URL%" -d "dataId=gym-service-web.yaml&group=DEFAULT_GROUP&type=yaml&content=spring:%%0A  datasource:%%0A    type: com.alibaba.druid.pool.DruidDataSource%%0A    driver-class-name: com.mysql.cj.jdbc.Driver%%0A    url: jdbc:mysql://localhost:3306/gymnasium?serverTimezone=Asia/Shanghai%%26characterEncoding=utf-8%%0A    username: root%%0A    password: 123456%%0Amybatis-plus:%%0A  configuration:%%0A    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl%%0Adubbo:%%0A  registry:%%0A    address: nacos://127.0.0.1:8848%%0A  protocol:%%0A    host: 127.0.0.1%%0A    port: 20880%%0A    name: dubbo%%0A  application:%%0A    name: gym-service-web%%0A  consumer:%%0A    check: false%%0A    timeout: 10000%%0Aserver:%%0A  port: 9999%%0Aminio:%%0A  endpoint: http://localhost:9000%%0A  accessKey: minioadmin%%0A  secretkey: minioadmin%%0Ajwt:%%0A  issuer: gaobie%%0A  secret: com.v1%%0A  expiration: 30%%0Aignore:%%0A  url: /api/login/login,/api/login/image"
echo.

echo === All configs uploaded ===
echo Verify at: http://127.0.0.1:8848/nacos/ (Config Management - Config List)
pause
