package com.jllsq.util;

public class IntegerUtil {

    public static byte[] unsignedIntegerToBytes(int data) {
        if (data < 0){
            data = -data;
        }
        int temp = data;
        int len = 1;
        while (temp / 10 != 0) {
            len ++;
            temp = temp/10;
        }
        byte[] result = new byte[len];
        for (int i = len-1;i >= 0; i --) {
            result[i] = (byte) (data%10+'0');
            data = data / 10;
        }
        return result;
    }

}
