package com.y3tu.cloud.auth.authorization.annotation;

import com.y3tu.cloud.auth.authorization.store.ResJwtTokenStore;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在启动类上添加该注解来----开启 JWT 令牌存储（资源服务器-非对称加密）
 *
 * @author y3tu
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ResJwtTokenStore.class)
public @interface EnableResJwtTokenStore {
}
