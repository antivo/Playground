package com.drmtx.app;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import sun.applet.Main;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@EnableAutoConfiguration
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(Application.ROOT_PACKAGE)
@EntityScan(Application.ROOT_PACKAGE)
@ComponentScan(Application.ROOT_PACKAGE)
@Configuration
public class Application {
    public static final String ROOT_PACKAGE = "com.drmtx";
    private static final Logger _logger = LogManager.getLogger(Application.class);

    @Bean
    protected ServletContextListener listener() {
        return new ServletContextListener() {

            @Override
            public void contextInitialized(ServletContextEvent sce) {
                _logger.info("NEXT RUN\n\n");
                _logger.info("ServletContext initialized");
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {
                _logger.info("ServletContext destroyed\n\n");
            }

        };
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setRegisterShutdownHook(true);
        ConfigurableApplicationContext applicationContext = app.run(Application.class, args);
    }
}

