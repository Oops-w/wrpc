package com.w.wrpc.exception;

/**
 * @author wsy
 * @date 2021/9/21 5:47 下午
 * @Description
 */
public class WrpcDecodeException extends RuntimeException {
    public WrpcDecodeException(String message) {
        super(message);
    }
}
