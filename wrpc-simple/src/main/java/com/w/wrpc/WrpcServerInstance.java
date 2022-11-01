package com.w.wrpc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.w.wrpc.annotation.RpcService;
import com.w.wrpc.config.WrpcConfig;
import com.w.wrpc.factory.SingletonBeanFactory;
import com.w.wrpc.netty.server.NettyServer;
import com.w.wrpc.provider.ServiceProvider;
import com.w.wrpc.provider.impl.ZkServiceProviderImpl;
import com.w.wrpc.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author wsy
 * @date 2022/10/29 5:59 PM
 * @Description
 */
public class WrpcServerInstance implements WrpcInstance {
    private static final Logger logger = LoggerFactory.getLogger(WrpcServerInstance.class);
    private static final Map<String, WrpcInstance> STARTED_SERVER = Maps.newConcurrentMap();
    private final NettyServer nettyServer;

    private final Supplier<String> instanceMapKeySupplier = () -> {
        try {
            return InetAddress.getLocalHost().getHostAddress() + WrpcConfig.getServerPort();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    };

    private ServiceProvider serviceProvider;

    public WrpcServerInstance() throws UnknownHostException {
        serviceProvider = SingletonBeanFactory.getInstance().getBean("zkServiceProvider", ZkServiceProviderImpl.class);

        try {
            initRegistryService();
        } catch (Exception e) {
            throw new RuntimeException("Service failed to register!");
        }

        this.nettyServer = new NettyServer();
        STARTED_SERVER.put(instanceMapKeySupplier.get(), this);
    }

    public static Collection<WrpcInstance> getStaredServer() throws UnknownHostException {
        Collection<WrpcInstance> values = STARTED_SERVER.values();

        if (values.isEmpty()) {
            values = Lists.newArrayList(new WrpcServerInstance());
        }

        return values;
    }

    @Override
    public void send(Object msg) {
        nettyServer.writeAndFlush(msg);
    }

    @Override
    public Object receive() {
        return null;
    }

    private void initRegistryService() throws Exception {
        List<Class<?>> classes = ClassUtil.getClasses("wrpc");

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(RpcService.class)) {
                Constructor<?> constructor = clazz.getConstructor();
                Object instance = constructor.newInstance();
                registryService(clazz.getName(), instance);
            }
        }
    }

    /**
     *
     * @param className    class name
     * @param instance     server instance
     */
    private void registryService(String className, Object instance) {
        serviceProvider.publishService(className, instance);
    }

}
