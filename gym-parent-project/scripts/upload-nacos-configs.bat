@echo off
REM Upload shared gym-common.properties to Nacos Config
set NACOS_URL=http://127.0.0.1:8848/nacos/v1/cs/configs

echo Uploading gym-common.properties (datasource + mybatis + minio)...

curl -s -X POST "%NACOS_URL%" ^
  -d "dataId=gym-common.properties" ^
  -d "group=DEFAULT_GROUP" ^
  -d "type=properties" ^
  -d "content=spring.datasource.type=com.alibaba.druid.pool.DruidDataSource%%0Aspring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver%%0Aspring.datasource.url=jdbc:mysql://localhost:3306/gymnasium?serverTimezone=Asia/Shanghai%%26characterEncoding=utf-8%%0Aspring.datasource.username=root%%0Aspring.datasource.password=123456%%0Amybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl%%0Aminio.endpoint=http://localhost:9000%%0Aminio.accessKey=minioadmin%%0Aminio.secretKey=minioadmin"

echo.
echo === Done ===
echo.
echo Nacos: http://127.0.0.1:8848/nacos/
echo Config: gym-common.properties / DEFAULT_GROUP / properties
echo.
echo Content:
echo   spring.datasource.type / driver-class-name / url / username / password
echo   mybatis-plus.configuration.log-impl
echo   minio.endpoint / accessKey / secretKey
echo.
pause
