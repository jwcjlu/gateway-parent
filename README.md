# gateway-parent
     gateway是一款高性能的api网关，基于netty实现，对后端服务器实现池化处理，现在提供了限流，并发控制，负载均衡功能
，这些功能都是插入式的。在控制台可以添加或取消某个插件，通过etcd进行监听并立即生效，单纯负载均衡测试的性能和nginx差不多
