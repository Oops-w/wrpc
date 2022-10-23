package com.w.wrpc.factory;

/**
 * @author wsy
 * @date 2021/9/22 11:31 上午
 * @Description
 */
public interface BeanFactory {

    /**
     * 根据beanName 获得 bean
     *
     * @param beanName
     * @param clazz
     * @param <T>      需要获得的对象
     * @return
     */
    <T> T getBean(String beanName, Class<T> clazz);

    /**
     *
     * @param beanName
     * @return
     */
    Object getBean(String beanName);
}
