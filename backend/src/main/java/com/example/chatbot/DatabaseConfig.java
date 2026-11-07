package com.example.chatbot;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;

@Configuration
public class DatabaseConfig {

    @Bean
    CommandLineRunner testDatabaseConnection(
            DataSource dataSource) {

        return args -> {
            try (Connection connection =
                         dataSource.getConnection()) {

                System.out.println(
                    "PostgreSQL Connected Successfully!"
                );

                System.out.println(
                    "Database: " +
                    connection.getCatalog()
                );
            }
        };
    }
}