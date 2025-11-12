package com.saborgourmet.restaurante.config;

import com.saborgourmet.restaurante.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (sin autenticación)
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()

                        // Dashboard - accesible para todos los autenticados
                        .requestMatchers("/dashboard").authenticated()

                        // ADMIN - Acceso total a administración, inventario, auditoría
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/inventario/**").hasRole("ADMIN")
                        .requestMatchers("/auditoria/**", "/api/auditoria/**").hasRole("ADMIN")

                        // ADMIN y MOZO - Gestión de clientes, mesas y asignaciones
                        .requestMatchers("/clientes/**", "/api/clientes/**").hasAnyRole("ADMIN", "MOZO")
                        .requestMatchers("/mesas/**", "/api/mesas/**").hasAnyRole("ADMIN", "MOZO")
                        .requestMatchers("/asignaciones/**", "/api/asignaciones/**").hasAnyRole("ADMIN", "MOZO")

                        // MOZO y COCINERO - Gestión de pedidos
                        .requestMatchers("/pedidos/**").hasAnyRole("ADMIN", "MOZO", "COCINERO")

                        // CAJERO y ADMIN - Gestión de ventas y facturación
                        .requestMatchers("/ventas/**").hasAnyRole("ADMIN", "CAJERO")

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());  // Desactivar CSRF solo para desarrollo

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
