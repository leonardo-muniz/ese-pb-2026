package com.exchange.crypto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Habilita o CORS no nível de Segurança (Crucial para o React)
            .cors(Customizer.withDefaults()) 
            // Desabilita o CSRF (pois nossa API é Stateless)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Libera nossos endpoints da API
                .requestMatchers("/api/v1/users/**", "/api/v1/wallets/**", "/api/v1/orders/**").permitAll()
                // Qualquer outra rota exige autenticação
                .anyRequest().authenticated()
            );

        return http.build();
    }

    // Configuração explícita do CORS para o Spring Security
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Libera a origem exata do React (Vite)
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        
        // Libera todos os métodos (GET, POST, etc) e Headers
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica essa regra de CORS para TODAS as rotas da nossa API
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}