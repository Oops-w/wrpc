package com.w.wrpc.exception;

/**
 * @author wsy
 * @date 2021/9/22 10:38 下午
 * @Description
 */
public enum  WrpcExceptionMessage {
    /**
     * 服务在注册中心中未找到
     */
    SERVICE_CAN_NOT_BE_FOUND("服务在注册中心中未找到");
    private String message;

    WrpcExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
