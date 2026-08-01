package com.v1.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.StringReader;
import java.util.Properties;

/**
 * 在 Bean 创建之前从 Nacos 加载共享配置（Properties 格式），避免与 Dubbo 注解后处理器冲突
 */
public class NacosConfigLoader implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String serverAddr = environment.getProperty("nacos.config.server-addr", "127.0.0.1:8848");
        String dataId = "gym-common.properties";
        String group = "DEFAULT_GROUP";

        try {
            Properties props = new Properties();
            props.setProperty("serverAddr", serverAddr);
            ConfigService configService = NacosFactory.createConfigService(props);
            String content = configService.getConfig(dataId, group, 5000);

            if (content != null && !content.isEmpty()) {
                Properties nacosProps = new Properties();
                nacosProps.load(new StringReader(content));
                environment.getPropertySources().addFirst(
                    new PropertiesPropertySource("nacosConfig", nacosProps)
                );
                System.out.println("[NacosConfig] Loaded " + nacosProps.size() + " properties from " + dataId);
            } else {
                System.out.println("[NacosConfig] WARN: " + dataId + " is empty, using local config only");
            }
        } catch (NacosException e) {
            System.err.println("[NacosConfig] ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[NacosConfig] ERROR: " + e.getMessage());
        }
    }
}
