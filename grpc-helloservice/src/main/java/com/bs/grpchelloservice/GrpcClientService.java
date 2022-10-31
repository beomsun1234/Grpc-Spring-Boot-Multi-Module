package com.bs.grpchelloservice;


import io.grpc.ManagedChannelBuilder;
import org.bs.examples.lib.HelloRequest;
import org.bs.examples.lib.SimpleGrpc;
import org.springframework.stereotype.Service;

@Service
public class GrpcClientService {
    private final SimpleGrpc.SimpleBlockingStub stub = SimpleGrpc.newBlockingStub(ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build());

    public String sayHello(){
        return stub.sayHello(HelloRequest.newBuilder().build()).getMessage();
    }


}
