package com.w.wrpc;

import com.w.wrpc.factory.SingletonBeanFactory;
import com.w.wrpc.provider.impl.ZkServiceProviderImpl;
import org.junit.Test;

/**
 * @author wsy
 * @date 2021/9/23 9:39 上午
 * @Description
 */
public class ServiceRegisterTest {
    @Test
    public void zkServiceRegisterTest() {
        SingletonBeanFactory singletonBeanFactory = SingletonBeanFactory.getInstance();
        ZkServiceProviderImpl zkServiceProvider = singletonBeanFactory.getBean("zkServiceProvider", ZkServiceProviderImpl.class);
        zkServiceProvider.publishService("singletonBeanFactory", singletonBeanFactory);
    }
}
