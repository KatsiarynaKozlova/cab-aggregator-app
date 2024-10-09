package com.software.modsen.passengerservice.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@Testcontainers
public class DatabaseContainerConfiguration {
    @Container
    public static final MySQLContainer<?> mySQL =
            new MySQLContainer<>(DockerImageName.parse("mysql:latest"))
                    .withDatabaseName("test_db")
                    .withUsername("test")
                    .withPassword("test")
                    .waitingFor(Wait.forListeningPort())
                    .withStartupTimeout(Duration.ofMinutes(6))
                    .withReuse(true);

    static {
        mySQL.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQL::getJdbcUrl);
        registry.add("spring.datasource.username", mySQL::getUsername);
        registry.add("spring.datasource.password", mySQL::getPassword);
        registry.add("spring.datasource.driver-class-name", mySQL::getDriverClassName);
    }
}
