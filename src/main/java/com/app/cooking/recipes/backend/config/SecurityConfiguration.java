package com.app.cooking.recipes.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

    @Autowired
    public void configureAuthenticationManager(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            UserDetailsService userDetailsService
    ) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/recipe", "/category")
                        .hasAnyRole("READ", "EDIT", "ADMIN")
                        .requestMatchers("/recipe", "/category")
                        .hasAnyRole("EDIT", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/user/data")
                        .hasAnyRole("EDIT", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/user/role")
                        .hasRole("ADMIN")
                        .requestMatchers("/user")
                        .hasRole("ADMIN")
                        .requestMatchers("/login")
                        .permitAll()
                        .anyRequest()
                        .denyAll())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
