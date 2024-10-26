package com.hmall.api.client.fallback;

import com.hmall.api.client.ItemClient;
import com.hmall.api.dto.ItemDTO;
import com.hmall.api.dto.OrderDetailDTO;
import com.hmall.common.utils.CollUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collection;
import java.util.List;

@Slf4j
public class ItemClientFallbackFactory implements FallbackFactory<ItemClient> {

    // create 方法最终返回 ItemClient 对象
    // 当远程调用出问题or被限流时，会捕获到一个异常
    @Override
    public ItemClient create(Throwable cause) {

        //记录异常
        log.error("商品查询失败，原因是："+cause.getMessage(), cause);
        return new ItemClient() {
            @Override
            public List<ItemDTO> queryItemByIds(Collection<Long> ids) {
                log.error("商品查询失败。",cause);
                // 返回一个空集合
                return CollUtils.emptyList();
            }

            @Override
            public void deductStock(List<OrderDetailDTO> items) {
                log.error("扣减商品库存失败。",cause);
                throw new RuntimeException(cause);
            }
        };
    }
}
