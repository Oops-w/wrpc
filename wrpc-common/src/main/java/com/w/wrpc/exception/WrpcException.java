package com.w.wrpc.exception;

/**
 * @author wsy
 * @date 2021/9/22 10:37 下午
 * @Description
 */
public class WrpcException extends RuntimeException {
    public WrpcException(String message) {
        super(message);
    }

    public WrpcException(WrpcExceptionMessage message, String details) {
        super(message.getMessage() + ": " + details);
    }

    public WrpcException(WrpcExceptionMessage message) {
        super(message.getMessage());
    }

    public WrpcException(String message, Throwable cause) {
        super(message, cause);
    }

}
