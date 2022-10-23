package com.w.wrpc.registry;

import com.w.wrpc.dto.WrpcRequest;

import java.net.InetSocketAddress;

/**
 * @author wsy
 * @date 2021/9/22 9:10 下午
 * @Description
 */
public interface ServiceDiscovery {
    /**
     * lookup service by rpcServiceName
     *
     * @param request rpc service pojo
     * @return service address
     */
    InetSocketAddress lookupService(WrpcRequest request);
}
