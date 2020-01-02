package com.jllsq.util;

public class ByteArrayUtil {

    public static byte[] appendBytes(byte[] ... data) {
        int len = 0;
        for (byte[] array: data) {
            len += array.length;
        }
        byte[] result = new byte[len];
        int index = 0;
        for (byte[] array:data) {
            System.arraycopy(array,0,result,index,array.length);
            index += array.length;
        }
        return result;
    }

}
