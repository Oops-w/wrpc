package com.w.wrpc.constants;

/**
 * @author wsy
 * @date 2021/9/21 5:44 下午
 * @Description
 */
public interface WrpcConstants {
    /**
     * magic
     */
    byte[] MAGIC = new byte[]{'o', 'o', 'p', 's'};

    byte REQUEST_TYPE = (byte) 0x80;

    byte TWO_WAY = 0x40;

    byte EVENT = 0x20;

    byte SERIALIZATION = 0x1f;

    int RESERVE_LENGTH = 14;

    /**
     * TODO 可以考虑配置文件配置跟路径
     */
    String ZK_REGISTRY_SERVICE_ROOT_PATH = "/rpc";
}
