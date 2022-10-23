package com.w.wrpc.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author wsy
 * @date 2021/9/23 9:52 上午
 * @Description
 */
public class NettyClient {
    private Bootstrap bootstrap;
    private NioEventLoopGroup worker;

    public NettyClient() {
        worker = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap
                .group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        // If no data is sent to the server within 15 seconds, a heartbeat request is sent
                        p.addLast(new IdleStateHandler(0, 15, 0, TimeUnit.SECONDS));
                        p.addLast(new NettyClientHandler());
                    }
                });
    }

    /**
     * 连接服务端
     *
     * @param inetSocketAddress
     * @return 连接成功返回 {@link Channel} 失败返回null
     */
    private Channel doConnect(InetSocketAddress inetSocketAddress) {
        Channel channel = null;
        try {
            channel = bootstrap.connect(inetSocketAddress).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return channel;
    }

}
