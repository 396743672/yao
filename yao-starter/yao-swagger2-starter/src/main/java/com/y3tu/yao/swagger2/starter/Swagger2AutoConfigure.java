package com.y3tu.yao.swagger2.starter;

import com.y3tu.tool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

/**
 * Swagger2配置
 *
 * @author y3tu
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(Swagger2Properties.class)
public class Swagger2AutoConfigure {

    @Autowired
    private Swagger2Properties swagger2Properties;

    /**
     * swagger相关配置
     */
    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger2Properties.getBasePackage()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo(swagger2Properties))
                .securitySchemes(Collections.singletonList(securityScheme(swagger2Properties)))
                .securityContexts(Collections.singletonList(securityContext(swagger2Properties)));
    }

    private ApiInfo apiInfo(Swagger2Properties swagger) {
        return new ApiInfo(
                swagger.getTitle(),
                swagger.getDescription(),
                swagger.getVersion(),
                null,
                new Contact(swagger.getAuthor(), swagger.getUrl(), swagger.getEmail()),
                swagger.getLicense(), swagger.getLicenseUrl(), Collections.emptyList());
    }

    private SecurityScheme securityScheme(Swagger2Properties swagger) {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant(swagger.getGrantUrl());

        return new OAuthBuilder()
                .name(swagger.getName())
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes(swagger2Properties)))
                .build();
    }

    private SecurityContext securityContext(Swagger2Properties swagger) {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference(swagger.getName(), scopes(swagger))))
                .forPaths(PathSelectors.any())
                .build();
    }

    private AuthorizationScope[] scopes(Swagger2Properties swagger) {
        return new AuthorizationScope[]{
                new AuthorizationScope(swagger.getScope(), StrUtil.EMPTY)
        };
    }
}
