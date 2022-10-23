package com.w.wrpc.registry;


import java.net.InetSocketAddress;

/**
 * @author wsy
 * @date 2021/9/21 9:47 下午
 * @Description
 */
public interface ServiceRegistry {
    /**
     * 注册服务到注册中心
     * @param rpcServiceName 注册的服务名
     * @param address {@link InetSocketAddress} 注册的地址
     */
    void registry(String rpcServiceName, InetSocketAddress address);
}
