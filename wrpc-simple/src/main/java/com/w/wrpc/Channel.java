package com.w.wrpc;

/**
 * @author wsy
 * @date 2022/10/30 2:33 PM
 * @Description
 */

public interface Channel {
    void send(Object msg);

    Object receive();
}
