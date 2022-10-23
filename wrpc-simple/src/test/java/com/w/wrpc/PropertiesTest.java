package com.w.wrpc;

import com.w.wrpc.config.WrpcConfig;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * @author wsy
 * @date 2021/9/21 9:00 下午
 * @Description
 */
public class PropertiesTest {
    @Test
    public void test() throws IOException {
        Properties properties = new Properties();
        InputStream is = WrpcConfig.class.getClassLoader().getResourceAsStream("application.properties");
        properties.load(is);
        System.out.println(properties);
    }
}
