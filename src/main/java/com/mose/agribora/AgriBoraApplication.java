package com.mose.agribora;

import com.mose.agribora.entity.Role;
import com.mose.agribora.entity.User;
import com.mose.agribora.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AgriBoraApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgriBoraApplication.class, args);
    }

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "mosezachary198@gmail.com";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setUsername("Mose Zachary");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("Mose@198")); // You can change this
                admin.setRole(Role.ADMIN);
                admin.setPhoneNumber("0705728240");
                userRepository.save(admin);
                System.out.println("✅ Admin user created.");
            } else {
                System.out.println("Admin already exists.");
            }
        };
    }
}
