package com.w.wrpc.provider;

/**
 * @author wsy
 * @date 2021/9/23 9:18 上午
 * @Description
 */
public interface ServiceProvider {
    /**
     * 将服务添加到容器中
     * @param rpcServiceName rpc service name
     * @param serviceObject 注册的服务
     */
    void addService(String rpcServiceName, Object serviceObject);

    /**
     * 获得service
     * @param rpcServiceName rpc service name
     * @return service object
     */
    Object getService(String rpcServiceName);

    /**
     * 将服务进行创建和注册
     * @param rpcServiceName rpc service name
     * @param serviceObject 注册的服务
     */
    void publishService(String rpcServiceName, Object serviceObject);
}
