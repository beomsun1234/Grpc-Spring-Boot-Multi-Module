package com.bs.grpchelloservice;

import com.bs.grpchelloservice.config.MasterDataSourceProperties;
import com.bs.grpchelloservice.config.SlaveDataSourceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@EnableConfigurationProperties({MasterDataSourceProperties.class, SlaveDataSourceProperties.class})
@SpringBootApplication
public class GrpcHelloserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrpcHelloserviceApplication.class, args);
	}

}
