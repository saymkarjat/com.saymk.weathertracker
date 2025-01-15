package com.example.pogodaspring.config;

import com.example.pogodaspring.filter.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.*;
import org.testcontainers.containers.PostgreSQLContainer;
import javax.sql.DataSource;
import java.util.Properties;


@Profile("test")
@Configuration
@ComponentScan("com.example.pogodaspring")
@EnableTransactionManagement
public class TestSpringConfig implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;



    @Autowired
    public TestSpringConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

    }

    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.3")
                .withDatabaseName("test_db")
                .withUsername("user")
                .withPassword("1");
        container.start();
        return container;
    }


    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> testContainer) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(testContainer.getJdbcUrl());
        dataSource.setUsername(testContainer.getUsername());
        dataSource.setPassword(testContainer.getPassword());
        return dataSource;
    }


    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        return properties;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.example.pogodaspring.model");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager(LocalSessionFactoryBean sessionFactoryBean) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactoryBean.getObject());

        return transactionManager;
    }

    @Bean
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**");
    }

}
