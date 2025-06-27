package com.example.pfm.config;

import com.example.pfm.repository.UserRepository;
import com.example.pfm.security.JsonUsernamePasswordFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepo;

    @Bean
    BCryptPasswordEncoder encoder() { return new BCryptPasswordEncoder(); }

    @Bean
    UserDetailsService uds() {
        return email -> userRepo.findByEmail(email)
                .orElseThrow(() -> new org.springframework.security.core.userdetails
                        .UsernameNotFoundException(email));
    }

    @Bean
    DaoAuthenticationProvider daoProvider(UserDetailsService uds) {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setPasswordEncoder(encoder());
        p.setUserDetailsService(uds);
        return p;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http,
                                    AuthenticationConfiguration cfg) throws Exception {

        JsonUsernamePasswordFilter json = new JsonUsernamePasswordFilter();
        json.setAuthenticationManager(cfg.getAuthenticationManager());
        json.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/api/auth/login", "POST"));
        json.setAuthenticationFailureHandler(
                (req, res, ex) -> res.sendError(HttpStatus.UNAUTHORIZED.value(), "Bad credentials"));

        http.formLogin(f -> f.disable())             // make sure default form login is off
                .csrf(c -> c.disable())
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/api/auth/register","/api/auth/login").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req,res,ex) ->
                                res.sendError(HttpStatus.UNAUTHORIZED.value())))
                .addFilterAt(json, UsernamePasswordAuthenticationFilter.class)
                .logout(lo -> lo.logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler((req,res,auth) ->
                                res.getWriter().write("{\"message\":\"Logout successful\"}")));

        return http.build();
    }

}
