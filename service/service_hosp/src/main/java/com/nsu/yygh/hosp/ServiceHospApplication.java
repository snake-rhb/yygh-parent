package com.nsu.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// 指定包的扫描规则，在引入util工具包后，包的路径不一样，我们要从com.nsu包路径下开始扫描
@ComponentScan(basePackages = "com.nsu")
// 将服务注册到nacos注册中心中
@EnableDiscoveryClient
// 远程调用字典接口
@EnableFeignClients("com.nsu")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class);
    }
}
