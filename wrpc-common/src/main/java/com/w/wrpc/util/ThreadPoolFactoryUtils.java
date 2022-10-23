package com.w.wrpc.util;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author wsy
 * @date 2021/9/21 5:30 下午
 * @Description
 */
public class ThreadPoolFactoryUtils {
    /**
     *
     * @param threadNamePrefix 线程池内 线程的名称前缀
     * @param domain 是否是保护线程
     * @return {@link ThreadFactory} 返回自定义线程前缀的线程工厂
     */
    public static ThreadFactory createThreadFactory(String threadNamePrefix, boolean domain) {
        if(threadNamePrefix != null) {
            if(domain) {
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .setDaemon(true).build();
            } else {
                return new ThreadFactoryBuilder()
                        .setNameFormat(threadNamePrefix + "-%d")
                        .build();
            }
        }
        return Executors.defaultThreadFactory();
    }
}
