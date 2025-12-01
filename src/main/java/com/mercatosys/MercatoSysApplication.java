package com.mercatosys;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class MercatoSysApplication {

    public static void main(String[] args) {

        if (!"test".equals(System.getProperty("spring.profiles.active"))) {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            System.setProperty("DB_URL", dotenv.get("DB_URL"));
            System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
            System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        }

        SpringApplication.run(MercatoSysApplication.class, args);
    }

}
