package com.larffxx.synchronoustelegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entry point for the Telegram side of the Synchronous application.
 * Boots the application context and starts long polling for Telegram updates.
 */
@SpringBootApplication
public class SynchronousTelegramApplication {
    /**
     * Starts the Spring Boot application.
     * @param args command line arguments supplied at startup
     */
    public static void main(String[] args) {
        SpringApplication.run(SynchronousTelegramApplication.class, args);
    }

}
