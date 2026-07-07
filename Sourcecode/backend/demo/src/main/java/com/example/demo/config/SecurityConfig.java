
package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/error").permitAll()
                .anyRequest().authenticated()   // /data, /ws, sab login ke baad hi
            )
            .formLogin(Customizer.withDefaults())   // default login page mil jaayega
            .httpBasic(Customizer.withDefaults())   // API/fetch ke liye basic auth bhi
            .csrf(csrf -> csrf.disable());          // demo API + WebSocket ke liye off
        return http.build();
    }

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.withUsername("admin")
                .password("$2a$10$IWLUYWFXNhVR00HQmm.BKOjgM71Ku2cpsq08X5roIC8z9cx1M8T.a") // admin123
                .roles("ADMIN")
                .build();

        UserDetails viewer = User.withUsername("viewer")
                .password("$2a$10$Zr6fb92tKon.S69kJzA8yeKXd406OgjfqhG7EjEUxx3TFbJfS/ccG") // viewer123
                .roles("VIEWER")
                .build();

        return new InMemoryUserDetailsManager(admin, viewer);
    }

}