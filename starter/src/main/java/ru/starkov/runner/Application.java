package ru.starkov.runner;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "ru.starkov")
@EnableJpaRepositories(basePackages ="ru.starkov.struct.db.dao")
@EntityScan(basePackages = "ru.starkov.struct.db.model")
@EnableAsync(proxyTargetClass = true)
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
