package com.w.wrpc.exception;

/**
 * @author wsy
 * @date 2021/9/21 11:17 上午
 * @Description
 */
public class MagicIllegalException extends RuntimeException {

    public MagicIllegalException(String message) {
        super(message);
    }

    public MagicIllegalException(String message, Throwable cause) {
        super(message, cause);
    }
}
