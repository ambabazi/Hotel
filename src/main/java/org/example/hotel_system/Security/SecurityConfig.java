package org.example.hotel_system.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        // Swagger UI — allow access without token
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/bookings").hasAnyRole("GUEST", "STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/bookings/guest/**").hasAnyRole("GUEST", "STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/bookings/**").hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/bookings/*/status").hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/rooms/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/rooms").hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/rooms/**").hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/rooms/**").hasAnyRole("STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        .requestMatchers("/api/guests/**").hasAnyRole("STAFF", "ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ↓ This Bean is what AuthService needs — it MUST be here
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Spring Security 7: UserDetailsService goes into the constructor now
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}