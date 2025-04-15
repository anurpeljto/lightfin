package com.anurpeljto.gateway.config;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "secondEntityManagerFactory",
        transactionManagerRef = "secondTransactionManager",
        basePackages = "com.anurpeljto.gateway.repositories.loan")
public class LoanDBConfig {

   @Bean
   @ConfigurationProperties("spring.loan")
   public DataSourceProperties loanDataSourceProperties() {
       return new DataSourceProperties();
   }

    @Bean(name = "loanDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://loan-db:5432/loans")
                .driverClassName("org.postgresql.Driver")
                .username("user")
                .password("userpass")
                .build();
    }

   @Bean(name = "secondEntityManagerFactory")
   public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
           @Qualifier("loanDataSource") DataSource dataSource) {
           LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
           factoryBean.setDataSource(dataSource);
           factoryBean.setPackagesToScan("com.anurpeljto.gateway.domain.loan");
           HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
           factoryBean.setJpaVendorAdapter(vendorAdapter);

           return factoryBean;
   }

    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("secondEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
