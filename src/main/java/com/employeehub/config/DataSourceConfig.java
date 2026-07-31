package com.employeehub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

// Replaces Spring Boot's single auto-configured DataSource with one that
// routes each request to the Bean or Intellan schema based on
// TenantContext. Defining this @Bean makes Spring Boot's own
// DataSourceAutoConfiguration back off (it's @ConditionalOnMissingBean),
// so no exclusion/property juggling is needed beyond this class.
@Configuration
public class DataSourceConfig {

    @Value("${db.url.bean}")
    private String beanUrl;

    @Value("${db.url.intellan}")
    private String intellanUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource beanDataSource = buildDataSource(beanUrl);
        DataSource intellanDataSource = buildDataSource(intellanUrl);

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(TenantContext.BEAN, beanDataSource);
        targetDataSources.put(TenantContext.INTELLAN, intellanDataSource);

        TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(intellanDataSource);
        routingDataSource.afterPropertiesSet();
        return routingDataSource;
    }

    private DataSource buildDataSource(String url) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
