package com.tgt.documentsearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class DocumentSearchApplication {

    @Autowired
    @Qualifier("documentSearchConsole")
    DocumentSearchConsole documentSearchConsole;

    private static final Logger logger = Logger.getLogger(DocumentSearchApplication.class);

    public static void main(String[] args) {
            SpringApplication.run(DocumentSearchApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            documentSearchConsole.run();

        };
    }
}

