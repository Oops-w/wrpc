package com.w.wrpc.protocol;

import com.w.wrpc.constants.WrpcConstants;
import com.w.wrpc.dto.WrpcMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wsy
 * @date 2021/9/21 10:22 上午
 * @Description
 * 0     1     2     3     4        5       6    7    8    9   10   11   12   13   14   15  16   17   18   ...32
 * +-----+-----+-----+-----+--------+------+----+----+----+----+----+----+----+----+----+----+---+----+-------+
 * |       magic           |version | byte |         requestID                     |    dataLength    |reserve|
 * +-----------------------+--------+------+---------------------------------------+------------------+-------+
 * |                                                                                                          |
 * |                                         body                                                             |
 * |                                                                                                          |
 * |                                        ... ...                                                           |
 * +----------------------------------------------------------------------------------------------------------+
 * magic (4 byte)：魔术
 * version (1 byte): 版本信息
 * byte (1 byte)：包含多个信息 0x80 代表请求类型 0x40 代表是否需要返回值 0x20 代表是否是心跳事件 0x1f 代表当前序列化方式
 * requestID (8 byte): 唯一请求序列号
 * dataLength (4 byte): 数据长度
 * reserve (14 byte): 预留字段
 * body: 数据
 * 协议头 32 byte
 */
@Slf4j
public class WrpcEncode extends MessageToByteEncoder<WrpcMessage> {

    /**
     * 进行数据编码
     *
     * @param ctx     channel容器
     * @param message {@link WrpcMessage} rpc传送时的消息对象
     * @param out     bytebuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, WrpcMessage message, ByteBuf out) throws Exception {
        try {
            // 4byte magic
            out.writeBytes(WrpcConstants.MAGIC);
            // version
            out.writeByte(message.getVersion());
            byte b = 0x00;
            if (message.getRequestType()) {
                //request = true
                b |= WrpcConstants.REQUEST_TYPE;
                if (message.getNeedReturn()) {
                    // need = true
                    b |= WrpcConstants.TWO_WAY;
                }
            }
            if (message.getHeartbeat()) {
                // 是心跳事件
                b |= WrpcConstants.EVENT;
            }
            // 添加序列化方式
            b |= WrpcConstants.SERIALIZATION;
            out.writeByte(b);
            out.writeLong(message.getRequestID());
            // TODO 获得序列化后的length
            byte[] bytes = {1, 2, 3};
            int dataLength = bytes.length;
            out.writeInt(dataLength);
            // 填写预留字段
            out.writeBytes(new byte[WrpcConstants.RESERVE_LENGTH]);
            out.writeBytes(bytes);
            //写入channel
            ctx.writeAndFlush(out);
        } catch (Exception e) {
            log.error("encode exception", e);
            throw new RuntimeException("encode exception ", e);
        }
    }
}