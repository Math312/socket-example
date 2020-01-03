package com.jllsq.common.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AtomicMessage {

    private int total;

    private byte[] bytes;

    private String message;


}
