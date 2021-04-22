package com.nsu.yygh.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 配置Swagger的接口配置类
 */

@Configuration
@EnableSwagger2
public class MySwaggerConfig {
    /**
     * 微服务下的接口测试
     * @return
     */
    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 分组
                .groupName("webAPI")
                .apiInfo(webApiInfo())
                .select()
                // 扫描的路径
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }

    /**
     * 微服务的文档描述
     * @return
     */
    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("网站API文档")
                .description("描述网站微服务接口定义")
                .version("1.0")
                .build();
    }


    /**
     * admin接口下的测试
     * @return
     */
    @Bean
    public Docket adminApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 分组
                .groupName("adminAPI")
                .apiInfo(adminApiInfo())
                .select()
                // 扫描的路径
                .paths(PathSelectors.ant("/admin/**"))
                .build();
    }

    /**
     * 微服务的文档描述
     * @return
     */
    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("后台管理系统API文档")
                .description("描述后台管理系统接口定义")
                .version("1.0")
                .build();

    }
}
