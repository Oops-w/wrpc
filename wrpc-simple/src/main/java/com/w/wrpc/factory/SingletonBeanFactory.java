package com.w.wrpc.factory;


import com.w.wrpc.netty.client.ChannelProvider;
import com.w.wrpc.netty.client.NettyClient;
import com.w.wrpc.provider.impl.ZkServiceProviderImpl;
import com.w.wrpc.registry.zookeeper.ZkServiceDiscoveryImpl;
import com.w.wrpc.registry.zookeeper.ZkServiceRegistryImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wsy
 * @date 2021/9/22 11:31 上午
 * @Description 模拟ioc容器
 */
public class SingletonBeanFactory implements BeanFactory {

    private static SingletonBeanFactory INSTANCE;
    private static final Map<String, Object> container = new ConcurrentHashMap<>();
    private static final Object obj = new Object();

    /**
     * 通过静态代码块加载Bean
     */
    static {
        container.put("zkServiceRegistry", new ZkServiceRegistryImpl());
        container.put("zkServiceDiscovery", new ZkServiceDiscoveryImpl());
        container.put("zkServiceProvider", new ZkServiceProviderImpl());
        container.put("channelProvider", new ChannelProvider());
    }

    private SingletonBeanFactory() {

    }

    public static SingletonBeanFactory getInstance() {
        if(INSTANCE == null) {
            synchronized (obj) {
                if(INSTANCE == null) {
                    INSTANCE = new SingletonBeanFactory();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public <T> T getBean(String beanName, Class<T> clazz) {
        Object obj = container.get(beanName);
        if (obj == null) {
            throw new RuntimeException("bean name " + beanName + "not exist");
        }
        return (T) obj;
    }

    @Override
    public Object getBean(String beanName) {
        return getBean(beanName, Object.class);
    }

}
