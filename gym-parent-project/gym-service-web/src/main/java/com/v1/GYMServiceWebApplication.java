package com.v1;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = "com.v1",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com\\.v1\\.service\\..*"))
@EnableSwagger2
@EnableDubbo
public class GYMServiceWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(GYMServiceWebApplication.class,args);
    }
}
