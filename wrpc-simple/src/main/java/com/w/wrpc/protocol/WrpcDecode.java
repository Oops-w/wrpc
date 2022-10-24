package com.w.wrpc.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w.wrpc.constants.WrpcConstants;
import com.w.wrpc.exception.MagicIllegalException;
import com.w.wrpc.serializa.SerializationEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

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
public class WrpcDecode extends ByteToMessageDecoder {
    /**
     * 对数据进行解码
     *
     * @param ctx     channel容器
     * @param byteBuf 数据
     * @param out     解码后的对象 传到下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        try {
            // read magic
            byte[] magic = new byte[WrpcConstants.MAGIC.length];
            byteBuf.readBytes(magic);
            // 校验magic是否合法
            if (!Arrays.equals(magic, WrpcConstants.MAGIC)) {
                throw new MagicIllegalException("magic illegal");
            }

            byte version = byteBuf.readByte();

            // 获得Req/Res  2 Way Event Version Serialization的byte
            byte b = byteBuf.readByte();
            //type 为true时序列化成 WrpcRequest 为false时序列化成 WrpcResponse
            boolean type = (b & WrpcConstants.REQUEST_TYPE) ==  WrpcConstants.REQUEST_TYPE;
            if (type) {
                boolean needReturn = (b & WrpcConstants.TWO_WAY) == WrpcConstants.TWO_WAY;
                // TODO 对需要返回值的和不需要返回值的做不同处理 考虑后期对不需要返回值的做rpc调用后直接执行后续逻辑
            }
            // 判断是否时心跳事件
            if ((b & WrpcConstants.EVENT) == WrpcConstants.EVENT) {
                log.info("accept origin {} heartbeat info", ctx.channel().remoteAddress());
            }
            // 得到序列化方式
            byte serialization = (byte) (b & WrpcConstants.SERIALIZATION);

            long requestID = byteBuf.readLong();
            int dataLength = byteBuf.readInt();
            //读取预留字段
            byteBuf.readBytes(new byte[WrpcConstants.RESERVE_LENGTH]);
            // 根据type得到序列化后的对象
            byte[] bytes = new byte[dataLength];
            byteBuf.readBytes(bytes);
            // TODO 暂时只有JSON
            if (SerializationEnum.JSON.getCode().equals(serialization)) {
                ObjectMapper objectMapper = new ObjectMapper();
                Object obj = objectMapper.readValue(new String(bytes), Object.class);
                out.add(obj);
            }
        } catch (MagicIllegalException e) {
            log.error("decode exception", e);
            throw new RuntimeException("decode exception ", e);
        }
    }
}