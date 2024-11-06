# 商城微服务项目

> 作者：济楚 Vizzini
> 
> https://github.com/vizzinii

基于 Java SpringBoot + SpringCloud 实现的使用微服务架构的商城项目，整合了常用技术栈，实现了主流业务。



## 技术栈

### 用户层
- Web

### CDN内容分发网络
- HTML
- CSS/JS
- Json

### 负载均衡层
- Nginx
- LVS

### 网关层
- Spring Cloud Gateway

### 服务层
- 注册、登录、个人中心、文章、行为数据、任务调度、搜索、自媒体管理、平台管理等中间件服务
- Spring Boot 2.7.12
- Spring Cloud
- Spring MVC 框架
- Seata 分布式框架
- Alibaba Nacos 配置与服务共享平台
- RabbitMQ
- MyBatis + MyBatis Plus 数据访问
- Spring 事务注解
- Swagger + Knife4j 接口文档

### 数据层
- MySQL
- Redis

### DevOps
- Git
- maven
- docker

### 工具类
- Hutool 工具库
- Apache Commons Lang3 工具类
- Lombok 注解

### 业务特性

- Spring Session Redis 分布式登录
- 全局请求响应拦截器（记录日志）
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- Swagger + Knife4j 接口文档
- 自定义权限注解 + 全局校验
- 全局跨域处理
- 多环境配置


## 业务功能

### 用户
#### 收货地址管理接口
- 根据ID查询地址
- 查询当前用户地址列表

#### 用户相关接口
- 用户登录
- 扣减余额

### 商品
#### 商品管理相关接口
- 新增商品
- 根据ID查询商品
- 根据ID批量查询商品
- 分页查询商品
- 更新商品
- 更新商品状态
- 根据ID删除商品
- 批量扣减库存
#### 搜索商品相关接口
- 搜索商品

### 购物车
#### 购物车相关接口
- 添加商品到购物车
- 更新购物车数据
- 查询购物车列表
- 删除购物车中商品
- 批量删除购物车中商品

### 订单
#### 订单管理接口
- 创建订单
- 根据id查询订单
- 标记订单已支付

### 支付
#### 支付相关接口
- 生成支付单
- 查询支付单
- 尝试基于用户余额支付

## 单元测试

- JUnit5 单元测试
- 示例单元测试类
