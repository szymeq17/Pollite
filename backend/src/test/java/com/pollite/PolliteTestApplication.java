package com.pollite;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@SpringBootApplication
@EntityScan("com.pollite.*")
@ComponentScan(basePackages = "com.pollite.*")
public class PolliteTestApplication {

    private static final MySQLContainer DB_CONTAINER;

    static {
        DB_CONTAINER = (MySQLContainer) new MySQLContainer("mysql:8.0") //NOSONAR
                .withCommand("mysqld --character-set-server=utf8 --collation-server=utf8_general_ci --thread-stack=256k --skip-ssl");
        DB_CONTAINER.start();
        String rootJdbcUrl = String.format("%s?allowPublicKeyRetrieval=true&user=%s&password=%s", DB_CONTAINER.getJdbcUrl(), "root", DB_CONTAINER.getPassword());
        try {
            Connection rootCon = DriverManager.getConnection(rootJdbcUrl);
            rootCon.createStatement().executeUpdate("create database pollite");
            rootCon.createStatement().executeUpdate("grant all privileges on pollite.* to 'test'@'%'");
            rootCon.close();
        } catch (SQLException e) {
            throw new RuntimeException("Błąd inicjalizacji ustawień kontenera MySQL testów ", e);
        }
    }

    @Primary
    @Bean
    @LiquibaseDataSource
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(DB_CONTAINER.getDriverClassName())
                .username(DB_CONTAINER.getUsername())
                .password(DB_CONTAINER.getPassword())
                .url(DB_CONTAINER.getJdbcUrl() + "?allowPublicKeyRetrieval=true&useSSL=false")
                .build();
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }


}
