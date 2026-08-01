package com.v1;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.v1.service.course.**.mapper")
@NacosPropertySource(dataId = "gym-common.yaml", autoRefreshed = true)
public class GymServiceCourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(GymServiceCourseApplication.class, args);
    }
}
