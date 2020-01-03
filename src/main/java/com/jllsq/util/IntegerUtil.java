package com.jllsq.util;

public class IntegerUtil {

    private static final byte SUB = '-';

    public static byte[] unsignedIntegerToBytes(int data) {
        if (data < 0) {
            data = -data;
        }
        int temp = data;
        int len = 1;
        while (temp / 10 != 0) {
            len++;
            temp = temp / 10;
        }
        byte[] result = new byte[len];
        for (int i = len - 1; i >= 0; i--) {
            result[i] = (byte) (data % 10 + '0');
            data = data / 10;
        }
        return result;
    }

    public static int bytesToInt(byte[] data) {
        int startIndex = 0;
        if (data[0] == SUB) {
            startIndex++;
        }
        int sum = 0;
        for (int i = startIndex; i < data.length; i++) {
            if (data[i] <= '9' && data[0] >= '0') {
                sum *= 10;
                sum += (data[i] - '0');
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (startIndex == 1) {
            return -sum;
        }
        return sum;
    }

}
