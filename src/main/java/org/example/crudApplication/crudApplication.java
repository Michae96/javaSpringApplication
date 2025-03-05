package org.example.crudApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = { SessionAutoConfiguration.class })
@EntityScan("org.example.crudApplication.models")
@EnableJpaRepositories("org.example.crudApplication.repository")
public class crudApplication {
    public static void main(String[] args) {
        SpringApplication.run(crudApplication.class, args);
    }
}

