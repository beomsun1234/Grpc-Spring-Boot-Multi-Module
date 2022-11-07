package com.bs.grpchelloservice.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "HELLO")
public class Hello {

    @Id
    private Long id;

    private String message;

    @Builder
    public Hello(Long id, String message) {
        this.id = id;
        this.message = message;
    }
}
