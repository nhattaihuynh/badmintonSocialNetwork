package com.social.identity_service.flyway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FlywayConfig {
    

    @Value("${flywaypf.active}")
    private String DEFAULT_SCHEMA;

    private static final String DEFAULT_LOCATION = "db/migration";

    private final DataSource dataSource;

    @Bean
    @Profile("!test")
    public Flyway flyway() {
        log.info("Migrating default schema: {} with location: {}", DEFAULT_SCHEMA, DEFAULT_LOCATION);
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(DEFAULT_SCHEMA)
                .locations(DEFAULT_LOCATION)
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        return flyway;
    }
}
