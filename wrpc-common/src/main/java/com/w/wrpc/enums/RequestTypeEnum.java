package com.w.wrpc.enums;

/**
 * @author wsy
 * @date 2022/10/26 11:33 PM
 * @Description
 */
public enum RequestTypeEnum {
    REQUEST(Boolean.TRUE),
    RESPONSE(Boolean.FALSE),
    ;
    private final Boolean value;

    RequestTypeEnum(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
