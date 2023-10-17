package com.gill.mache.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Server
 *
 * @author gill
 * @version 2023/09/21
 **/
@Getter
@Setter
@ToString
public class HostPort {

    private String host;

    private int port;

    public HostPort(String hostPort) {
        String[] split = hostPort.split(":");
        this.host = split[0];
        this.port = Integer.parseInt(split[1]);
    }
}
