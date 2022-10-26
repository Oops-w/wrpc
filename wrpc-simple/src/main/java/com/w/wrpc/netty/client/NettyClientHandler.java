package com.w.wrpc.netty.client;

import com.w.wrpc.config.WrpcConfig;
import com.w.wrpc.enums.RequestTypeEnum;
import com.w.wrpc.serializa.SerializationEnum;
import com.w.wrpc.dto.WrpcMessage;
import com.w.wrpc.factory.SingletonBeanFactory;
import com.w.wrpc.util.ResettableTimer;
import com.w.wrpc.util.Snowflake;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * @author wsy
 * @date 2021/9/23 10:54 上午
 * @Description
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);

    private ChannelProvider channelProvider;

    private ResettableTimer heartbeatTimer;
    {
        channelProvider = SingletonBeanFactory.getInstance().getBean("channelProvider", ChannelProvider.class);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        heartbeatTimer = generateResettableTimer(ctx);
        heartbeatTimer.schedule();
        super.channelActive(ctx);
    }

    /**
     * Read the message transmitted by the server
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            log.info("client receive msg: [{}]", msg);
            if (msg instanceof String
                        && StringUtils.equals("heartbeat", (String) msg)) {
                // reset close time
                heartbeatTimer.reset();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.WRITER_IDLE)) {
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
        wrpcMessage.setNeedReturn(false);
        wrpcMessage.setHeartbeat(true);
        wrpcMessage.setRequestType(RequestTypeEnum.REQUEST.getValue());
        wrpcMessage.setVersion(new Byte("1"));
        wrpcMessage.setSerialization(SerializationEnum.JSON.getCode());
        wrpcMessage.setData("heartbeat");

        return wrpcMessage;
    }

    private ResettableTimer generateResettableTimer(ChannelHandlerContext ctx) {
        return new ResettableTimer(() -> new TimerTask() {
            @Override
            public void run() {
                logger.info("current client will close cause by server [{}] not return heartbeat", ctx.channel().remoteAddress());
                ctx.close();
            }
        }, WrpcConfig.getRpcClientHeartbeatTime(), TimeUnit.MILLISECONDS);
    }
}
