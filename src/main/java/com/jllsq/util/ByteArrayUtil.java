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

    public static byte[] readLine(byte[] data,int startIndex) {
        if (data.length <= startIndex) {
            return null;
        }
        int end = startIndex;
        for (int i = startIndex;i < data.length - 1;i ++) {
            if (data[i] == '\r' && data[i+1] == '\n') {
                break;
            }
            end ++;
        }
        int len = end - startIndex;
        byte[] result = new byte[len];
        System.arraycopy(data,startIndex,result,0,len);
        return result;
    }

}
