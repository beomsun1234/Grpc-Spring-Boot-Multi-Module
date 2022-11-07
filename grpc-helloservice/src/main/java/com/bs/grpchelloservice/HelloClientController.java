package com.bs.grpchelloservice;


import com.bs.grpchelloservice.domain.Hello;
import com.bs.grpchelloservice.service.HelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RequiredArgsConstructor
@RestController
public class HelloClientController {
    private final GrpcClientService grpcClientService;

    private final HelloService helloService;
    @GetMapping()
    public String sayHello(){
        return grpcClientService.sayHello();
    }

    @GetMapping("hello")
    public Flux<Hello> getHellos(){
        return helloService.getHellos();
    }


}
