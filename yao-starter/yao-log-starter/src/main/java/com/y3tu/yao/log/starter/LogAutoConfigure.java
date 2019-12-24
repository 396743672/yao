package com.y3tu.yao.log.starter;

import com.y3tu.yao.log.starter.aspect.LogAspect;
import com.y3tu.yao.log.starter.configure.LogProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author y3tu
 */
@Configuration
@ConditionalOnClass(LogAspect.class)
@EnableConfigurationProperties(LogProperties.class)
public class LogAutoConfigure {

    @Bean
    @ConditionalOnMissingBean(LogAspect.class)
    public LogAspect logAspect() {
        return new LogAspect();
    }

}
