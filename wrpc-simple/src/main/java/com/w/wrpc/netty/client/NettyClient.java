package com.w.wrpc.netty.client;

import com.w.wrpc.protocol.WrpcDecode;
import com.w.wrpc.protocol.WrpcEncode;
import com.w.wrpc.protocol.WrpcLengthFieldBasedFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author wsy
 * @date 2021/9/23 9:52 上午
 * @Description
 */
public class NettyClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private Bootstrap bootstrap;
    private NioEventLoopGroup worker;

    public NettyClient() {
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.ERROR);
        worker = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap
                .group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        p.addLast(new WrpcLengthFieldBasedFrameDecoder());

                        // If no data is sent to the server within 15 seconds, a heartbeat request is sent
                        p.addLast(new IdleStateHandler(0, 1, 0, TimeUnit.SECONDS));
                        p.addLast(loggingHandler);
                        p.addLast(new WrpcDecode());
                        p.addLast(new WrpcEncode());

                        p.addLast(new NettyClientHandler());
                    }
                });
        doConnect(new InetSocketAddress("localhost", 9999));
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
            logger.info("connect server {} success", inetSocketAddress.getAddress().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return channel;
    }
}
