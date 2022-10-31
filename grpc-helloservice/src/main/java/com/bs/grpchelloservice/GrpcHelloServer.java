package com.bs.grpchelloservice;


import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrpcHelloServer implements ApplicationRunner {
    private static final int PORT = 9090;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("-- grpc hello server start -----------------");
        Server server = ServerBuilder.forPort(PORT)
                .addService(new GrpcHelloServerService())
                .build();
        server.start();

    }
}
