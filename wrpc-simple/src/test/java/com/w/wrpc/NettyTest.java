package com.w.wrpc;

import com.w.wrpc.netty.client.NettyClient;
import com.w.wrpc.netty.server.NettyServer;
import org.junit.Test;

import java.io.IOException;

/**
 * @author wsy
 * @date 2022/10/24 8:50 PM
 * @Description
 */
public class NettyTest {
    @Test
    public void server() throws IOException {
        NettyServer nettyServer = new NettyServer();

        System.in.read();
    }

}
