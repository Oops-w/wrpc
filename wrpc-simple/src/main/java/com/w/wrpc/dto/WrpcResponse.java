package com.w.wrpc.dto;


import java.io.Serializable;

/**
 * @author wsy
 * @date 2021/9/21 10:30 上午
 * @Description rpc响应数据传输对象
 */
public class WrpcResponse<T> implements Serializable {
    private Long requestID;
    private Integer code;
    private String message;
    private T data;

    public WrpcResponse(Long requestID) {
        this.requestID = requestID;
        this.code = 200;
        this.message = "success";
    }

    public WrpcResponse(Long requestID, Integer code, String message, T data) {
        this.requestID = requestID;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WrpcResponse{" +
                "requestID=" + requestID +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
