package com.v1.config.SpringSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private CustomAccessDeineHandler customAccessDeineHandler;
    @Autowired
    private CheckTokenFilter checkTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().headers().frameOptions().disable();
        http.csrf().disable().authorizeRequests()
                .antMatchers("/api/login/login", "/api/login/image")
                .permitAll().anyRequest().authenticated()
                .and().exceptionHandling()
                .authenticationEntryPoint(loginFailureHandler)
                .accessDeniedHandler(customAccessDeineHandler);
        http.addFilterBefore(checkTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
