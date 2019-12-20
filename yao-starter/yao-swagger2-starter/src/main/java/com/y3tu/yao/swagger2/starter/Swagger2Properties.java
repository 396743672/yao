package com.y3tu.yao.swagger2.starter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author y3tu
 */
@Data
@ConfigurationProperties(prefix = "yao.swagger2")
@Component
public class Swagger2Properties {
    private String basePackage;
    private String title;
    private String description;
    private String version;
    private String author;
    private String url;
    private String email;
    private String license;
    private String licenseUrl;
    private String grantUrl;
    private String name;
    private String scope;
}
