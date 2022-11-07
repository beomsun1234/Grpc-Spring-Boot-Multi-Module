package com.bs.grpchelloservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import org.springframework.transaction.reactive.TransactionSynchronizationManager;
import reactor.core.publisher.Mono;

@Slf4j
public class MultiTenantRoutingConnectionFactory extends AbstractRoutingConnectionFactory {
    @Override
    protected Mono<Object> determineCurrentLookupKey() {
        return TransactionSynchronizationManager.forCurrentTransaction().map(transactionSynchronizationManager-> {
            log.info("getCurrentTransactionName() : {}", transactionSynchronizationManager.getCurrentTransactionName());
            log.info("isActualTransactionActive() : {}", transactionSynchronizationManager.isActualTransactionActive());
            log.info("isCurrentTransactionReadOnly() : {}", transactionSynchronizationManager.isCurrentTransactionReadOnly());
            if (transactionSynchronizationManager.isActualTransactionActive()) {
                if (transactionSynchronizationManager.isCurrentTransactionReadOnly()) {
                    log.info("slave");
                    return "slavedb";
                } else {
                    log.info("master");
                    return "masterdb";
                }
            } else {
                log.info("master");
                return "masterdb";
            }
        });
    }
}
