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
                Channel serverChannel = ctx.channel();
                log.info("write idle happen [{}]", serverChannel.remoteAddress());
                serverChannel.writeAndFlush(buildHeartbeatMessage());
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("current channel closed...");
    }

    private WrpcMessage<String> buildHeartbeatMessage() {
        WrpcMessage<String> wrpcMessage = new WrpcMessage<>();

        wrpcMessage.setRequestID(Snowflake.getInstance().nextId());
        wrpcMessage.setHeartbeat(true);
        wrpcMessage.setRequestType(true);
        wrpcMessage.setVersion(new Byte("1"));
        wrpcMessage.setSerialization(SerializationEnum.JSON.getCode());
        wrpcMessage.setData("heartbeat");

        return wrpcMessage;
    }
}
