package com.jllsq.util;

import org.junit.Assert;

import static org.junit.Assert.*;

public class IntegerUtilTest {

    @org.junit.Test
    public void unsignedIntegerToBytes() {

        Assert.assertArrayEquals(new byte[]{'1','0'},IntegerUtil.unsignedIntegerToBytes(10));

    }
}