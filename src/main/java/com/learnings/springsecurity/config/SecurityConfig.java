package com.learnings.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        //This bean is required to encode the password
        //It uses the BCrypt algorithm to encode the password
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //This bean is required to configure the security filter chain, 
        //This makes sure which endpoints need to be authenticated and which ones are public
        //As signup and signin endpoints are public, we are allowing them to be accessed by anyone
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/signup", "/signin", "/css/**").permitAll() //allow all requests to signup, signin and css endpoints
                .anyRequest().authenticated()
            )
                //This
            .formLogin(form -> form
                .loginPage("/signin") //custom login page
                .loginProcessingUrl("/signin") //custom login processing url
                .defaultSuccessUrl("/", true)
                .failureUrl("/signin?error") //custom failure url
                .permitAll() //allow all requests to signin endpoint
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/signin?logout") //custom logout success url
                .permitAll() //allow all requests to logout endpoint
            )
            .build();
    }
    
}
