package com.w.wrpc.config;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author wsy
 * @date 2021/9/21 9:05 下午
 * @Description 读取配置文件中的数据
 */
@Slf4j
public class WrpcConfig {
    private static final String PROPERTIES_PATH = "server.properties";
    private static final Properties PROPERTIES = new Properties();
    private static final String SERVER_PORT_STRING = "server.port";
    private static final String REGISTRY_ADDRESS_STRING = "registry.address";
    private static final String RPC_SERVER_ADDRESS_STRING = "rpc.server.address";
    private static final String RPC_CLIENT_HEARTBEAT_TIME_STRING = "rpc.client.heartbeat.time";

    /**
     * rpc client default alive 15s
     */
    private static final long RPC_CLIENT_HEARTBEAT_TIME = 15 * 1000;

    static {
        InputStream is;
        try {
            //初始化properties
            is = WrpcConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH);
            PROPERTIES.load(is);
        } catch (FileNotFoundException e) {
            log.error("load file not exist : ", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("load file io exception : ", e);
            e.printStackTrace();
        }
    }

    /**
     * 获得端口号
     *
     * @return server.port
     */
    public static Integer getServerPort() {
        return Integer.valueOf((String) PROPERTIES.get(SERVER_PORT_STRING));
    }

    /**
     * 获得注册中心地址
     *
     * @return registry.address
     */
    public static String getRegistryAddress() {
        return (String) PROPERTIES.get(REGISTRY_ADDRESS_STRING);
    }

    /**
     * 获得rpc的服务地址
     *
     * @return rpc.server.address
     */
    public static String getRpcServerAddress() {
        return (String) PROPERTIES.get(RPC_SERVER_ADDRESS_STRING);
    }

    /**
     * 获得rpc的存活时间（心跳获取时间）
     */
    public static Long getRpcClientHeartbeatTime() {
        try {
            return Long.valueOf(PROPERTIES.getProperty(RPC_CLIENT_HEARTBEAT_TIME_STRING));
        } catch (Exception e) {
            return RPC_CLIENT_HEARTBEAT_TIME;
        }
    }
}
