package com.y3tu.cloud.gateway.config;

import com.alibaba.fastjson.JSONArray;
import com.y3tu.cloud.common.constants.CommonConstants;
import com.y3tu.cloud.common.vo.ZuulRouteVO;
import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liuht
 * @date 2018/5/15
 * 动态路由实现
 */
@Slf4j
public class DynamicRouteLocator extends DiscoveryClientRouteLocator {

    private ZuulProperties properties;

    private RedisTemplate redisTemplate;

    public DynamicRouteLocator(String servletPath, DiscoveryClient discovery, ZuulProperties properties,
                               ServiceInstance localServiceInstance, RedisTemplate redisTemplate) {
        super(servletPath, discovery, properties, localServiceInstance);
        this.properties = properties;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 重写路由配置
     *
     * @return 路由表
     */
    @Override
    protected LinkedHashMap<String, ZuulProperties.ZuulRoute> locateRoutes() {
        //读取properties配置、eureka默认配置
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>(super.locateRoutes());
        log.debug("初始默认的路由配置完成");
        routesMap.putAll(locateRoutesFromCache());
        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StrUtil.isNotBlank(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }

    /**
     * 从Redis中读取缓存的路由信息，没有从rbac拉取，避免启动链路依赖问题（取舍），网关依赖业务模块的问题
     *
     * @return 缓存中的路由表
     */
    private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromCache() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();

        String vals = (String) redisTemplate.opsForValue().get(CommonConstants.ROUTE_KEY);
        if (vals == null) {
            return routes;
        }

        List<ZuulRouteVO> results = JSONArray.parseArray(vals, ZuulRouteVO.class);
        for (ZuulRouteVO result : results) {
            if (StrUtil.isBlank(result.getPath()) && StrUtil.isBlank(result.getUrl())) {
                continue;
            }

            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
            try {
                zuulRoute.setId(result.getServiceId());
                zuulRoute.setPath(result.getPath());
                zuulRoute.setServiceId(result.getServiceId());
                zuulRoute.setRetryable(StrUtil.equals(result.getRetryable(), "0") ? Boolean.FALSE : Boolean.TRUE);
                zuulRoute.setStripPrefix(StrUtil.equals(result.getStripPrefix(), "0") ? Boolean.FALSE : Boolean.TRUE);
                zuulRoute.setUrl(result.getUrl());
                String[] strings = StrUtil.split(result.getSensitiveheadersList(), ",");
                if (strings != null) {
                    List<String> sensitiveHeadersList = CollectionUtil.toList(StrUtil.split(result.getSensitiveheadersList(), ","));
                    if (sensitiveHeadersList != null) {
                        Set<String> sensitiveHeaderSet = CollectionUtil.newHashSet();
                        sensitiveHeaderSet.addAll(sensitiveHeadersList);
                        zuulRoute.setSensitiveHeaders(sensitiveHeaderSet);
                        zuulRoute.setCustomSensitiveHeaders(true);
                    }
                }

            } catch (Exception e) {
                log.error("从数据库加载路由配置异常", e);
            }
            log.debug("添加数据库自定义的路由配置,path：{}，serviceId:{}", zuulRoute.getPath(), zuulRoute.getServiceId());
            routes.put(zuulRoute.getPath(), zuulRoute);
        }
        return routes;
    }
}
