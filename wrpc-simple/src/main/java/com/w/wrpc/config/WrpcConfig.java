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
    private static final String SERVER_PORT = "server.port";
    private static final String REGISTRY_ADDRESS = "registry.address";
    private static final String RPC_SERVER_ADDRESS = "rpc.server.address";

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
        return Integer.valueOf((String) PROPERTIES.get(SERVER_PORT));
    }

    /**
     * 获得注册中心地址
     *
     * @return registry.address
     */
    public static String getRegistryAddress() {
        return (String) PROPERTIES.get(REGISTRY_ADDRESS);
    }

    /**
     * 获得rpc的服务地址
     *
     * @return rpc.server.address
     */
    public static String getRpcServerAddress() {
        return (String) PROPERTIES.get(RPC_SERVER_ADDRESS);
    }

}
