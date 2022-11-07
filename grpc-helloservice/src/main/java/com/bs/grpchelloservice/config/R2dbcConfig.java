package com.bs.grpchelloservice.config;


import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

import java.util.HashMap;

@Configuration
@EnableR2dbcRepositories
@Slf4j
@RequiredArgsConstructor
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    private final MasterDataSourceProperties masterDataSourceProperties;
    private final SlaveDataSourceProperties slaveDataSourceProperties;

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        MultiTenantRoutingConnectionFactory multiTenantRoutingConnectionFactory = new MultiTenantRoutingConnectionFactory();
        HashMap<Object, Object> factories  = new HashMap<>();
        factories.put("masterdb", masterConnectionFactory());
        factories.put("slavedb", slaveConnectionFactory());
        multiTenantRoutingConnectionFactory.setDefaultTargetConnectionFactory(masterConnectionFactory());
        multiTenantRoutingConnectionFactory.setTargetConnectionFactories(factories);
        return multiTenantRoutingConnectionFactory;
    }



    @Bean("MasterConnectionFactory")
    public ConnectionFactory masterConnectionFactory(){
        MariadbConnectionConfiguration conf = MariadbConnectionConfiguration.builder()
                .host(masterDataSourceProperties.getAddress())
                .port(masterDataSourceProperties.getPort())
                .username(masterDataSourceProperties.getUsername())
                .password(masterDataSourceProperties.getPassword())
                .database(masterDataSourceProperties.getDb())
                .build();

        MariadbConnectionFactory factory = new MariadbConnectionFactory(conf);
        return factory;

    }

    @Bean("SlaveConnectionFactory")
    public ConnectionFactory slaveConnectionFactory(){
        MariadbConnectionConfiguration conf = MariadbConnectionConfiguration.builder()
                .host(slaveDataSourceProperties.getAddress())
                .port(slaveDataSourceProperties.getPort())
                .username(slaveDataSourceProperties.getUsername())
                .password(slaveDataSourceProperties.getPassword())
                .database(slaveDataSourceProperties.getDb())
                .build();
        MariadbConnectionFactory factory = new MariadbConnectionFactory(conf);
        return  factory;
    }

    @Bean
    @Primary
    public ReactiveTransactionManager masterTransactionManager(@Qualifier("MasterConnectionFactory")ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public ReactiveTransactionManager slaveTransactionManager(@Qualifier("SlaveConnectionFactory")ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }



}
