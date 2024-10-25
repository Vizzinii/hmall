package com.hmall.gateway.routers;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

@Component
@Slf4j
@RequiredArgsConstructor
public class DynamicRouteLoader {

    private final NacosConfigManager nacosConfigManager;

    private final RouteDefinitionWriter routeDefinitionWriter;

    private final Set<String> routeIds = new HashSet<>();

    private final String dataId = "gateway-routes.json";

    private final String group = "DEFAULT_GROUP";

    // 初始化路由监听器的方法
    @PostConstruct //这个注解的意思是，一创建DynamicRouteLoader这个Bean就执行下面的方法
    public void initRouteConfigListener() throws NacosException {
        // 1.项目启动时，先拉取一次配置，并且添加配置监听器
        String configInfo = nacosConfigManager.getConfigService()
                .getConfigAndSignListener(dataId, group, 5000, new Listener() {

                    @Override
                    public Executor getExecutor() {
                        return null;
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        // 2.监听到配置变更，需要去更新路由表
                        updateConfigInfo(configInfo);
                    }
                });
        // 3.第一次读取到配置，也需要更新到路由表
        updateConfigInfo(configInfo);
    }
    public void updateConfigInfo(String configInfo) {
        // TODO
        log.info("监听到并修改路由配置信息 : {}", configInfo);
        // 1.解析配置信息，转为RouteDefinition
        List<RouteDefinition> routeDefinitions = JSONUtil.toList(configInfo, RouteDefinition.class);

        // 2.删除旧的路由表
        // 因为删除路由表需要根据id删，但是这里无法直接得到id
        // 所以在上一次保存新的路由表之后，保存新路由表的id
        for (String routeId : routeIds) {
            routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
        }

        // 3.清空旧的路由表
        routeIds.clear();

        // 4.更新路由表
        for (RouteDefinition routeDefinition : routeDefinitions) {
            // save方法要的是Mono对象
            // 一定要调用 subscribe() ，订阅 routeDefinition 的变化。当 routeDefinition 发生变化再调用 save()
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            // 记录路由id，方便下一次更新路由表时删除
            routeIds.add(routeDefinition.getId());
        }
    }
}
