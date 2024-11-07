package com.wibeechat.missa.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.wibeechat.missa.entity.mysql",
        entityManagerFactoryRef = "db1EntityMgrFactory",
        transactionManagerRef = "db1TransactionMgr"
)
@EnableTransactionManagement
public class MysqlConfig {

    @Primary
    @Bean(name = "datasource1")
    @ConfigurationProperties(prefix = "spring.db1.datasource")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "db1EntityMgrFactory")
    public LocalContainerEntityManagerFactoryBean db1EntityMgrFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("datasource1") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.wibeechat.missa.entity.mysql")
                .persistenceUnit("db1")
                .properties(Map.of(
                        "hibernate.dialect", "org.hibernate.dialect.MySQLDialect"
                ))
                .build();
    }

    @Primary
    @Bean(name = "db1TransactionMgr")
    public PlatformTransactionManager db1TransactionMgr(
            @Qualifier("db1EntityMgrFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}