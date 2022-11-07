package com.bs.grpchelloservice.config;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "r2dbc.slave")
public class SlaveDataSourceProperties {
    private String address;
    private int port;
    private String username;
    private String password;
    private String db;

    public SlaveDataSourceProperties(String address, int port, String username, String password, String db) {
        this.address = address;
        this.port = port;
        this.username = username;
        this.password = password;
        this.db = db;
    }
}
