package com.w.wrpc.protocol;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author wsy
 * @date 2021/9/21 1:09 下午
 * @Description
 */
@ChannelHandler.Sharable
public class WrpcLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {
    public WrpcLengthFieldBasedFrameDecoder() {
        // magic 4 byte version 1 byte struct 1 byte requestID 8byte dataLength 4 byte reserve 14 byte
        super(1024, 14, 4, 14, 0);
    }

    /**
     *
     * @param maxFrameLength 桢最大长度
     * @param lengthFieldOffset 偏移量
     * @param lengthFieldLength 头有多长
     * @param lengthAdjustment 读取到
     * @param initialBytesToStrip
     */
    private WrpcLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
