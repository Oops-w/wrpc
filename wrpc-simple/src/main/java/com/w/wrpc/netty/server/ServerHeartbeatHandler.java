package com.w.wrpc.netty.server;

import com.w.wrpc.dto.WrpcMessage;
import com.w.wrpc.enums.RequestTypeEnum;
import com.w.wrpc.serializa.SerializationEnum;
import com.w.wrpc.util.Snowflake;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author wsy
 * @date 2022/10/26 11:24 PM
 * @Description
 */
@Slf4j
public class ServerHeartbeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof String
                && StringUtils.equals("heartbeat", (String) msg)) {
            ctx.writeAndFlush(generateHeartbeatRep());
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private WrpcMessage<String> generateHeartbeatRep() {
        WrpcMessage<String> heartbeatMessage = new WrpcMessage<>();

        heartbeatMessage.setData("heartbeat");
        heartbeatMessage.setVersion(Byte.valueOf("1"));
        heartbeatMessage.setRequestID(Snowflake.getInstance().nextId());
        heartbeatMessage.setRequestType(RequestTypeEnum.RESPONSE.getValue());
        heartbeatMessage.setSerialization(SerializationEnum.JSON.getCode());
        heartbeatMessage.setHeartbeat(true);
        heartbeatMessage.setNeedReturn(false);

        return heartbeatMessage;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(ExceptionUtils.getStackTrace(cause), cause);
        ctx.close();
    }
}
