package com.wibeechat.missa.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.wibeechat.missa.repository.postgres",
        entityManagerFactoryRef = "db2EntityMgrFactory",
        transactionManagerRef = "db2TransactionMgr"
)
@EnableTransactionManagement
public class PostgresConfig {

    @Bean(name = "datasource2")
    @ConfigurationProperties(prefix = "spring.db2.datasource")
    public DataSource postgresDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "db2EntityMgrFactory")
    public LocalContainerEntityManagerFactoryBean db2EntityMgrFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("datasource2") DataSource dataSource
    ) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.default_schema", "mydb2");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        // 스키마 자동 생성 설정
        properties.put("hibernate.connection.init_sql", "CREATE SCHEMA IF NOT EXISTS mydb2");

        return builder
                .dataSource(dataSource)
                .packages("com.wibeechat.missa.entity.postgresql")
                .persistenceUnit("db2")
                .properties(properties)
                .build();
    }

    @Bean(name = "db2TransactionMgr")
    public PlatformTransactionManager db2TransactionMgr(
            @Qualifier("db2EntityMgrFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}