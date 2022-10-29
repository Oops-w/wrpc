package com.w.wrpc.factory;

import com.w.wrpc.proxy.WrpcServiceInvocationHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * @author wsy
 * @date 2022/10/29 6:53 PM
 * @Description
 */
public class JDKProxyFacotry {
    public static <T> T getJdkProxy(Class<T> clazz) throws Exception {
        Object instance = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new WrpcServiceInvocationHandler(clazz.getConstructor().newInstance()));
        return (T) instance;
    }
}
