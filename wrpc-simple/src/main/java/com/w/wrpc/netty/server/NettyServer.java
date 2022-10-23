package com.w.wrpc.netty.server;


import com.w.wrpc.config.WrpcConfig;
import com.w.wrpc.factory.SingletonBeanFactory;
import com.w.wrpc.provider.ServiceProvider;
import com.w.wrpc.provider.impl.ZkServiceProviderImpl;
import com.w.wrpc.util.ThreadPoolFactoryUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wsy
 * @date 2021/9/21 9:19 下午
 * @Description
 */
@Slf4j
public class NettyServer {
    private ServiceProvider serviceProvider ;

    public NettyServer() {
        serviceProvider = SingletonBeanFactory.getInstance().getBean("zkServiceProvider", ZkServiceProviderImpl.class);
        start();
    }

    /**
     * 注册服务
     *
     * @param serviceName 服务的全限定类名
     */
    public void registryService(String serviceName, Object serviceObject) {
        serviceProvider.publishService(serviceName, serviceObject);
    }

    /**
     * 开启netty服务器 只执行一次
     */
    private void start() {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        NioEventLoopGroup handlerThread = new NioEventLoopGroup(
                2 * NettyRuntime.availableProcessors(),
                ThreadPoolFactoryUtils.createThreadFactory("handler-group", false));
        final Integer serverPort = WrpcConfig.getServerPort();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            Channel channel = bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    // tcp连接的全连接队列的大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 保持心跳
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // TODO 30s 没收到消息就关闭channel
                            pipeline.addLast(new IdleStateHandler(0, 0, 30));
                            pipeline.addLast(new NettyServerHandler());
                            log.info("netty server start success listen port :" + serverPort);
                        }
                    }).bind(serverPort).sync().channel();
            log.info("netty server start success port {}", WrpcConfig.getServerPort());
            //利用closeFuture 阻塞
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("netty server start fail", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            handlerThread.shutdownGracefully();
        }
    }
}
