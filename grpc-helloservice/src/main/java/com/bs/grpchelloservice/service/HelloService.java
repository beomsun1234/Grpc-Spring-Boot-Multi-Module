package com.bs.grpchelloservice.service;

import com.bs.grpchelloservice.domain.Hello;
import com.bs.grpchelloservice.repository.HelloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HelloService {
    private final HelloRepository helloRepository;


    @Transactional(readOnly = true)
    public Flux<Hello> getHellos(){
        return helloRepository.findAll();
    }

}
