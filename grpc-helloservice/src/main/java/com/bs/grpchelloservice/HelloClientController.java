package com.bs.grpchelloservice;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class HelloClientController {
    private final GrpcClientService grpcClientService;

    @GetMapping()
    public String sayHello(){
        return grpcClientService.sayHello();
    }

}
