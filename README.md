# Grpc Multi Module Spring Boot

## R2DBC 설정 및 master, slave 분리


AbstractRoutingConnectionFactory를 상속받은 MultiTenantRoutingConnectionFactory 클래스를 만들어서 determineCurrentLookupKey()에 Transaction이 read only인지 아닌지에 따라 master와 slave를 구분하는 키값을 리턴해준다.
read only true일 경우 slave로 설정했고 해당 Connection의 키값을 'slavedb'로 설정했고 그 외의 경우 master로 키값은 'masterdb'로 설정했다. 

MultiTenantRoutingConnectionFactory Class

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


R2dbcConfig class에 Map형식으로 master와 slave Connection을 설정해준다. DefaultTargetConnection은 master로 설정해준다.

R2dbcConfig Class

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

설정은 끝났지만 어떻게 동작하는지 궁금해서 확인해 보았다.

MultiTenantRoutingConnectionFactory가 상속한 AbstractRoutingConnectionFactory클래스를 살펴보면 ConnectionFactory를 implements 하고있다. 아래 코드는 AbstractRoutingConnectionFactory클래스 중 determineTargetConnectionFactory() 함수이다. 코드를 보면
우리가 MultiTenantRoutingConnectionFactory 에서 설정한 determineCurrentLookupKey()에 키를 가져와서 R2dbcConfig에 설정한 TargetConnectionFactories에 키에 해당하는 connectionFactory 반환한다.  

        protected Mono<ConnectionFactory> determineTargetConnectionFactory() {

                Assert.state(this.resolvedConnectionFactories != null, "ConnectionFactory router not initialized");
                Mono<Object> lookupKey = determineCurrentLookupKey().defaultIfEmpty(FALLBACK_MARKER);
        
                return lookupKey.handle((key, sink) -> {
                    ConnectionFactory connectionFactory = this.resolvedConnectionFactories.get(key);
                    if (connectionFactory == null && (key == FALLBACK_MARKER || this.lenientFallback)) {
                        connectionFactory = this.resolvedDefaultConnectionFactory;
                    }
                    if (connectionFactory == null) {
                        sink.error(new IllegalStateException(String.format(
                                "Cannot determine target ConnectionFactory for lookup key '%s'", key == FALLBACK_MARKER ? null : key)));
                        return;
                    }
                    sink.next(connectionFactory);
                });
            }
        
            /**
             * Determine the current lookup key. This will typically be implemented to check a
             * subscriber context. Allows for arbitrary keys. The returned key needs to match the
             * stored lookup key type, as resolved by the {@link #resolveSpecifiedLookupKey} method.
             * @return {@link Mono} emitting the lookup key. May complete without emitting a value
             * if no lookup key available
             */
        protected abstract Mono<Object> determineCurrentLookupKey();


아래는 Transaction이 발생했을 때 디버깅한 사진이다.

[사진]