package com.w.wrpc.serializa;

/**
 * @author wsy
 * @date 2022/10/25 12:45 AM
 * @Description
 */
public enum SerializationEnum {
    /**
     * 心跳事件写入的序列化方式 不需要序列化(因为body会为空)
     */
    HEARTBEAT(new Byte("1"), "心跳事件"),

    /**
     * JSON序列化方式
     */
    JSON(new Byte("2"), "JSON"),
    ;
    private Byte code;
    private String desc;

    SerializationEnum(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
