package com.w.wrpc;

import com.w.wrpc.netty.client.NettyClient;

/**
 * @author wsy
 * @date 2022/10/29 6:49 PM
 * @Description
 */
public class WrpcClientInstance implements WrpcInstance{
    private final NettyClient nettyClient;

    public WrpcClientInstance() {
        this.nettyClient = new NettyClient();
    }
}
