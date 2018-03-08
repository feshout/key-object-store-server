package com.codecool.connection;

import java.io.Serializable;

public class WrapperModel implements Serializable {

    private String key;
    private byte[] value;

     public WrapperModel(String key, byte[] value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public byte[] getValue() {
        return value;
    }
}
