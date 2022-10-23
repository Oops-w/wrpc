package com.w.wrpc.netty.client;

import com.w.wrpc.dto.WrpcMessage;
import com.w.wrpc.dto.WrpcResponse;
import com.w.wrpc.factory.SingletonBeanFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author wsy
 * @date 2021/9/23 10:54 上午
 * @Description
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private ChannelProvider channelProvider;

    {
        channelProvider = SingletonBeanFactory.getInstance().getBean("channelProvider", ChannelProvider.class);
    }

    /**
     * Read the message transmitted by the server
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            log.info("client receive msg: [{}]", msg);
            if (msg instanceof WrpcMessage) {
                WrpcMessage message = (WrpcMessage) msg;
                // TODO deal with message
                // 如果是心跳就输出日志 如果不是心跳则进行处理
                if(message.getHeartbeat()) {
                    log.info("heartbeat [{}]", message);
                } else {
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.WRITER_IDLE)) {
                // TODO send heartbeat
                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
                Channel channel = channelProvider.get((InetSocketAddress) ctx.channel().remoteAddress());
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
