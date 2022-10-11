package com.webapp.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.webapp.rating.repository")
@ComponentScan("com.webapp.rating")
public class StudentRatingWebApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StudentRatingWebApplication.class, args);
    }

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        ResourceDatabasePopulator resourceDatabasePopulator =
                new ResourceDatabasePopulator(false, false,
                        "UTF-8", new ClassPathResource("db_data.sql"));
        resourceDatabasePopulator.execute(dataSource);

    }
}
