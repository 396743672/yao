package com.y3tu.yao.upms;

import com.y3tu.tool.web.annotation.EnableDefaultExceptionAdvice;
import com.y3tu.tool.web.annotation.EnableToolWeb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 *
 * @author y3tu
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDefaultExceptionAdvice
@EnableToolWeb
@ComponentScan(basePackages="com.y3tu.yao")
public class UpmsServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UpmsServerApplication.class, args);
    }
}
