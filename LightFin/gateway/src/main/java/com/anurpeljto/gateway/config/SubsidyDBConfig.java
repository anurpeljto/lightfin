package com.anurpeljto.gateway.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "subsidyEntityManagerFactory",
        transactionManagerRef = "subsidyTransactionManager",
        basePackages = {"com.anurpeljto.gateway.repositories.subsidies"}
)
public class SubsidyDBConfig {

    @Bean
    @ConfigurationProperties("spring.subsidy")
    public DataSourceProperties subsidyDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "subsidyDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://subsidy-db:5432/subsidies")
                .driverClassName("org.postgresql.Driver")
                .username("user")
                .password("userpass")
                .build();
    }

    @Bean(name = "subsidyEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean subsidyEntityManagerFactory(
            @Qualifier("subsidyDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.anurpeljto.gateway.domain.subsidy");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        return factoryBean;
    }

    @Bean(name = "subsidyTransactionManager")
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("subsidyEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
