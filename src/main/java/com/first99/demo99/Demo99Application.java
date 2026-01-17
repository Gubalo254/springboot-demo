package com.first99.demo99;

import com.first99.demo99.model.User;
import com.first99.demo99.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class Demo99Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo99Application.class, args);
    }

    @Bean
    CommandLineRunner createFirstAdmin(UserRepository userRepository, BCryptPasswordEncoder passwordencoder) {
        return args -> {
            if (userRepository.count() == 0) { // No users yet
                User admin = new User();
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordencoder.encode("admin123")); // default password
                admin.setName("Super Admin");
                admin.setRoles(Set.of("ADMIN", "USER")); // first admin
                userRepository.save(admin);
                System.out.println("First admin created: admin@example.com / admin123");
            }


        };
    }

}