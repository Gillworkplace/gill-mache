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

    private String hostPort;

    public HostPort() {
    }

    public HostPort(String hostPort) {
        this.hostPort = hostPort;
    }
}
