package com.hmall.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
// 微服务中读取需要热更新的配置属性。如下，热更新的属性的路径就是 hm.cart.maxItems
@ConfigurationProperties(prefix = "hm.cart") // 代表与购物车相关的属性
public class CartProperties {
    private Integer maxItems;
}
