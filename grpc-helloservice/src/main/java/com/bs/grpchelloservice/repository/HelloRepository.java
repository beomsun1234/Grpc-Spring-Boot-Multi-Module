package com.bs.grpchelloservice.repository;

import com.bs.grpchelloservice.domain.Hello;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelloRepository extends R2dbcRepository<Hello, Long> {
}
