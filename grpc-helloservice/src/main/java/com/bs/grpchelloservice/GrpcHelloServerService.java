package com.bs.grpchelloservice;


import io.grpc.stub.StreamObserver;
import org.bs.examples.lib.HelloReply;
import org.bs.examples.lib.HelloRequest;
import org.bs.examples.lib.SimpleGrpc;

public class GrpcHelloServerService extends SimpleGrpc.SimpleImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {

        HelloReply helloReply = HelloReply.newBuilder().setMessage("hi park").build();
        responseObserver.onNext(helloReply);
        responseObserver.onCompleted();
    }
}
