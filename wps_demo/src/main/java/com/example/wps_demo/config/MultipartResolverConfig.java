package com.example.wps_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @BelongsProject: leaf-onlyoffice
 * @BelongsPackage: com.ideayp.leaf.config
 * @Author: TongHui
 * @CreateTime: 2022-12-01 17:37
 * @Description: TODO
 * @Version: 1.0
 */
@Configuration
public class MultipartResolverConfig {
    @Bean(name="multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(true);
        resolver.setMaxInMemorySize(102400000);
        resolver.setMaxUploadSize(102400000);
        return resolver;
    }
}
