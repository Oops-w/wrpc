package com.w.wrpc;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wsy
 * @date 2022/10/30 2:27 PM
 * @Description
 */
public class WrpcProtocol {
    private static final Integer DEFALUT_PORT = 9999;

    /**
     * <server-url, channel>
     */
    private static final Map<String, Object> CHANNEL_MAP = Maps.newConcurrentMap();


    public List<Channel> getChannels(String serverUrl) {
        Object clientChannel = CHANNEL_MAP.get(serverUrl);
        List<Channel> channels = null;

        if (clientChannel instanceof List) {
            channels = (List<Channel>) clientChannel;
        }


        if (Objects.isNull(clientChannel) || channels.size() == 0) {
            // create rpc instance

        }

        return channels;
    }
}
