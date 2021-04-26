package com.nsu.yygh.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// 包的扫描规则
@ComponentScan("com.nsu")
public class CmnServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmnServiceApplication.class, args);
    }
}
