package com.w.wrpc.netty.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wsy
 * @date 2021/9/23 10:37 上午
 * @Description
 */
@Slf4j
public class ChannelProvider {
    private final Map<String, Channel> ipChannelMap = new ConcurrentHashMap<>();

    /**
     * 绑定ip地址和channel的关系
     *
     * @param inetSocketAddress
     * @param channel
     */
    public void set(InetSocketAddress inetSocketAddress, Channel channel) {
        String key = inetSocketAddress.toString();
        if (ipChannelMap.containsKey(key)) {
            return;
        }
        ipChannelMap.put(key, channel);
        log.info("inetSocketAddress {} channel {} bind success", inetSocketAddress, channel);
    }

    /**
     * 根据address 获得 channel
     *
     * @param inetSocketAddress
     * @return 存在映射关系的话返回 {@link Channel} 不存在的话则返回 null
     */
    public Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        Channel channel = ipChannelMap.get(key);
        if (channel != null && channel.isActive()) {
            return channel;
        } else {
            // address 对应的channel 不存在 可能是被关闭了
            ipChannelMap.remove(key);
        }
        return null;
    }

    /**
     * 根据address删除映射关系
     *
     * @param inetSocketAddress
     */
    public void remove(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        ipChannelMap.remove(key);
        log.info("Channel map size :[{}]", ipChannelMap.size());
    }

}
