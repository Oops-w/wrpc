package com.w.wrpc.protocol;


import io.netty.channel.ChannelHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wsy
 * @date 2021/9/21 10:22 上午
 * @Description
 *
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
 *
 */
@Slf4j
@ChannelHandler.Sharable
public class WrpcCodec {

}
