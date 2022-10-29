package com.w.wrpc.dto;

/**
 * @author wsy
 * @date 2022/10/29 11:31 PM
 * @Description
 */
public class WrpcResult implements Result {
    private Object value;
    private Throwable throwable;

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Throwable getThrowable() {
        return throwable;
    }

    @Override
    public void setThrowable(Throwable t) {
        this.throwable = t;
    }

    @Override
    public boolean hasException() {
        return false;
    }

    @Override
    public Object recreate() throws Throwable {
        return value;
    }
}
