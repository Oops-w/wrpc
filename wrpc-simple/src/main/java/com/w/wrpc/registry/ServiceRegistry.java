package com.w.wrpc.registry;


import com.w.wrpc.support.ServiceSupport;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collection;

/**
 * @author wsy
 * @date 2021/9/21 9:47 下午
 * @Description 服务注册器
 */
public interface ServiceRegistry {
    /**
     * 注册服务到注册中心
     * @param serviceName 注册的服务名
     * @param uri {@link URI} 注册的uri
     */
    void registerService(String serviceName, URI uri);

    /**
     * 服务发现
     * @param serviceName 服务名
     * @return 返回需要的服务对应的uri
     */
    URI lockupService(String serviceName);

    /**
     * 连接注册中心
     * @param serviceRegistryUri 对应服务的注册中心
     */
    void connect(URI serviceRegistryUri);

    /**
     * 获取所支持的scheme
     * @return
     */
    Collection<String> supportedSchemes();
}
