package com.example.pfm.config;

import com.example.pfm.repository.UserRepository;
import com.example.pfm.security.JsonUsernamePasswordFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepo;

    @Bean
    BCryptPasswordEncoder encoder() { return new BCryptPasswordEncoder(); }

    @Bean
    UserDetailsService uds() {
        return email -> userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new org.springframework.security.core.userdetails.UsernameNotFoundException(email));
    }

    @Bean
    DaoAuthenticationProvider daoProvider(UserDetailsService uds) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setPasswordEncoder(encoder());
        p.setUserDetailsService(uds);
        return p;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        RequestMatcher loginMatcher = new AntPathRequestMatcher("/api/auth/login", "POST");

        JsonUsernamePasswordFilter json = new JsonUsernamePasswordFilter();
        json.setAuthenticationManager(http.getSharedObject(
                org.springframework.security.authentication.AuthenticationManager.class));
        json.setRequiresAuthenticationRequestMatcher(loginMatcher);
        json.setAuthenticationFailureHandler(
                (req,res,ex) -> res.sendError(HttpStatus.UNAUTHORIZED.value(), "Bad credentials"));

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/health", "/api/auth/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (req,res,ex2) -> res.sendError(HttpStatus.UNAUTHORIZED.value())))
                .addFilterAt(json, UsernamePasswordAuthenticationFilter.class)
                .logout(lo -> lo
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((req,res,auth) ->
                                res.getWriter().write("{\"message\":\"Logout successful\"}")))
                .build();
    }
}
