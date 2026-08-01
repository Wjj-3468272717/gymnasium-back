package com.v1;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableDubbo
@NacosPropertySource(dataId = "gym-common.yaml", autoRefreshed = true)
public class GYMServiceWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(GYMServiceWebApplication.class,args);
    }
}
