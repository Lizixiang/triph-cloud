# triph-cloud
springcloud微服务脚手架

|服务名 | 描述|
|:--------:| :-------------|
|eureka-client | 注册中心客户端案例系统|
|eureka-server | 注册中心服务端系统（负责服务的注册和订阅）|
|triph-auth | 权限管理系统（负责网关鉴权，权限拦截等）|
|triph-common | 系统公共模块|
|triph-gateway | 网关（负责用户认证，鉴权，日志监控，限流，缓存等）|


unfinished issues：
1. 系统增加性能监控平台（APM）
2. 通过canal监控mysql的binlog日志加入到rocketMq消息并同步到es
3. 增加Config配置中心
4. 增加日志检索功能
5. 增加分布式锁注解
6. 增加读写分离功能
