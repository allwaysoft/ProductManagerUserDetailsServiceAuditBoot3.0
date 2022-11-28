package com.example;

import java.util.Arrays;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomLoginFailureHandler loginFailureHandler;
    @Autowired
    private CustomLoginSuccessHandler loginSuccessHandler;

    @Autowired

    private CustomUserDetailsService customUserDetailsService;

//    @Autowired
//    public void configAuthentication(AuthenticationManagerBuilder authBuilder) throws Exception {
//        authBuilder.userDetailsService(customUserDetailsService)
//                .passwordEncoder(new BCryptPasswordEncoder());
//
//    }
//requestMatchers("/**").
    @Bean
    VerifyCodeAuthenticationProvider authenticationProvider() {
        VerifyCodeAuthenticationProvider authenticationProvider = new VerifyCodeAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        return authenticationProvider;
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(authenticationProvider()));
    }

    @Autowired
    CustomAuthorizationManager customAuthorizationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests()
                .requestMatchers("/403").permitAll()
                .requestMatchers("/captcha_image").permitAll()
                .requestMatchers("/header").permitAll()
                .requestMatchers("/footer").permitAll()
                .requestMatchers("/sidebar").permitAll()
                .requestMatchers("/index2").permitAll()
                .requestMatchers("/index3").permitAll()
                .requestMatchers("/pages/**").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/assets/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                .requestMatchers("/common/**").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/logout").permitAll()
                .requestMatchers("/verify").permitAll()
                .requestMatchers("/home").authenticated()
                .requestMatchers("/user/info").authenticated()
                .requestMatchers("/change/password").authenticated()
                .requestMatchers("/new/password").authenticated()
                .requestMatchers("/").authenticated()
                .anyRequest()
                //                .authenticated()
                .access(customAuthorizationManager)
                .and()
                .formLogin().loginPage("/login")
                .permitAll()
                .failureHandler(loginFailureHandler)
                .successHandler(loginSuccessHandler)
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/403");
        http
                .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/")
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry());
        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    // Register HttpSessionEventPublisher
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
