package com.w.wrpc.dto;

import java.io.Serializable;

/**
 * @author wsy
 * @date 2021/9/21 10:33 上午
 * @Description
 */
public class WrpcMessage <T> implements Serializable {
    /**
     * 请求ID
     */
    private Long requestID;

    /**
     * 请求类型 request = true response = false
     */
    private Boolean requestType;

    /**
     * 是否是心跳检测
     */
    private Boolean heartbeat = false;

    /**
     * 是否需要返回值
     */
    private Boolean needReturn = false;

    /**
     * 序列化方式
     */
    private Byte serialization;

    /**
     * 版本号
     */
    private Byte version;

    /**
     * 数据
     */
    private T data;

    public Long getRequestID() {
        return requestID;
    }

    public void setRequestID(Long requestID) {
        this.requestID = requestID;
    }

    public Boolean getRequestType() {
        return requestType;
    }

    public void setRequestType(Boolean requestType) {
        this.requestType = requestType;
    }

    public Boolean getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Boolean heartbeat) {
        this.heartbeat = heartbeat;
    }

    public Boolean getNeedReturn() {
        return needReturn;
    }

    public void setNeedReturn(Boolean needReturn) {
        this.needReturn = needReturn;
    }

    public Byte getSerialization() {
        return serialization;
    }

    public void setSerialization(Byte serialization) {
        this.serialization = serialization;
    }

    public Byte getVersion() {
        return version;
    }

    public void setVersion(Byte version) {
        this.version = version;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WrpcMessage{" +
                "requestID=" + requestID +
                ", requestType=" + requestType +
                ", heartbeat=" + heartbeat +
                ", needReturn=" + needReturn +
                ", serialization=" + serialization +
                ", version=" + version +
                ", data=" + data +
                '}';
    }
}
