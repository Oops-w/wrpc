package com.w.wrpc.provider.impl;

import com.w.wrpc.config.WrpcConfig;
import com.w.wrpc.exception.WrpcException;
import com.w.wrpc.exception.WrpcExceptionMessage;
import com.w.wrpc.factory.SingletonBeanFactory;
import com.w.wrpc.provider.ServiceProvider;
import com.w.wrpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wsy
 * @date 2021/9/23 9:21 上午
 * @Description
 */
@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {
    /**
     * key: rpc service name(interface name + version + group)
     * value: service object
     */
    private Map<String, Object> serviceMap ;
    private Set<String> registeredService ;
    private ServiceRegistry serviceRegistry ;


    /**
     * 获得bean
     */ {
        SingletonBeanFactory singletonBeanFactory = SingletonBeanFactory.getInstance();
        serviceRegistry = singletonBeanFactory.getBean("zkServiceRegistry", ServiceRegistry.class);
        serviceMap = new ConcurrentHashMap<>();
        registeredService = new HashSet<>();
    }


    @Override
    public void addService(String rpcServiceName, Object serviceObject) {
        if (registeredService.contains(rpcServiceName)) {
            return;
        }
        serviceMap.put(rpcServiceName, serviceObject);
        registeredService.add(rpcServiceName);
        log.info("add service {} and interfaces {}", rpcServiceName, serviceObject.getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (service == null) {
            throw new WrpcException(WrpcExceptionMessage.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(String rpcServiceName, Object serviceObject) {
        try {
            // 注册当前提供者的接口和提供者的地址到注册中心
            serviceRegistry.registry(rpcServiceName, new InetSocketAddress(InetAddress.getLocalHost().getHostName(), WrpcConfig.getServerPort()));
            this.addService(rpcServiceName, serviceObject);
        } catch (UnknownHostException e) {
            log.error("unknown host exception", e);
        }
    }
}
