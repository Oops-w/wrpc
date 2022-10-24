package com.w.wrpc.util;

/**
 * @author wsy
 * @date 2022/10/25 1:24 AM
 * @Description
 */
public class ByteUtil {
    public static String toBinaryString(byte b) {
        return Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
    }
}
