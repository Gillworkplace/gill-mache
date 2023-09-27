package com.gill.mache.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * MapCommand
 *
 * @author gill
 * @version 2023/09/21
 **/
@Getter
@Setter
@ToString
public class MapCommand {

    private String key;

    private byte[] value;

    public MapCommand() {
    }

    public MapCommand(String key) {
        this.key = key;
    }

    public MapCommand(String key, byte[] value) {
        this.key = key;
        this.value = value;
    }
}
