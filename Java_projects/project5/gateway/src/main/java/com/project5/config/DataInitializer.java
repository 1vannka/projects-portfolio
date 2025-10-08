package com.project5.config; 

import com.project5.services.MyUserDetailService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration 
public class DataInitializer {
    private final MyUserDetailService myUserDetailService;

    public DataInitializer(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }

    @Bean
    public CommandLineRunner runDataInitialization() {
        return args -> {
            myUserDetailService.initAdmin();
            System.out.println("Администратор и роли инициализированы (если не существовали).");
        };
    }
}