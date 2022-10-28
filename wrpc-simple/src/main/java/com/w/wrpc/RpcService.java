package com.w.wrpc;

import com.w.wrpc.serializa.SerializationEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wsy
 * @date 2022/10/28 8:50 PM
 * @Description
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {
    /**
     * 序列化方式
     */
    SerializationEnum serialization();

    /**
     * 版本
     */
    byte version();
}
