package com.neml.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class HikariConfigs {

    @Value("${spring.datasource.url}")
    String url;

    @Value("${spring.datasource.username}")
    String userName;

    @Value("${spring.datasource.password}")
    String password;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    int poolSize;

    @Bean
    public DataSource dataSource() {
        if (url == null || userName == null || password == null) {
            throw new RuntimeException("Database connection properties are not set");
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(userName);
        config.setPassword(password);
        config.setMaximumPoolSize(poolSize);
        config.setDriverClassName("org.postgresql.Driver"); // Replace with your database driver class name
        config.setConnectionTimeout(60000); // 30 seconds
        config.setIdleTimeout(60000); // 1 minute
        config.setMaxLifetime(180000); // 3 minutes

        return new HikariDataSource(config);
    }
}