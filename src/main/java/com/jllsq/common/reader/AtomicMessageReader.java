package com.jllsq.common.reader;

import com.jllsq.common.entity.AtomicMessage;
import com.jllsq.util.ByteArrayUtil;
import com.jllsq.util.IntegerUtil;

public class AtomicMessageReader {

    public static AtomicMessage readMessage(byte[] data, int startIndex) {
        if (data.length == startIndex) {
            return null;
        }
        if (data[startIndex] != '$') {

        }
        int current = startIndex + 1;
        byte[] lengthNumBytes = ByteArrayUtil.readLine(data, current);
        int lengthNum = IntegerUtil.bytesToInt(lengthNumBytes);
        current += lengthNumBytes.length + 2;
        byte[] messageBytes = new byte[lengthNum];
        System.arraycopy(data, current, messageBytes, 0, lengthNum);
        return AtomicMessage.builder()
                .total(1 + lengthNumBytes.length + 2 + messageBytes.length + 2)
                .bytes(messageBytes)
                .message(new String(messageBytes))
                .build();
    }

}
