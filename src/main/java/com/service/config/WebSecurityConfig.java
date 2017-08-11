package com.service.config;

import com.service.app.security.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Import({WebDatasourceConfig.class})
@ComponentScan(basePackages = "com.service.app.security")
@PropertySource("classpath:security.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${pattern}") String pattern;

    // Form Login
    @Value("${login.page}") String loginPage;
    @Value("${failure.url}") String failureUrl;
    @Value("${username.parameter}") String usernameParameter;
    @Value("${password.parameter}") String passwordParameter;

    // Logout
    @Value("${delete.cookies}") String deleteCookies;
    @Value("${invalidate.http.session}") boolean invalidateHttpSession;
    @Value("${clear.authentication}") boolean clearAuthentication;
    @Value("${logout.request.matcher}") String logoutRequestMatcher;
    @Value("${logout.success.url}") String logoutSuccessUrl;

    // Session Management
    @Value("${invalid.session.url}") String invalidSessionUrl;
    @Value("${maximum.sessions}") int maximumSessions;
    @Value("${expired.url}") String expiredUrl;

    // Authentication Manager
    @Value("${users.by.username.query}") String usersByUsernameQuery;
    @Value("${authorities.by.username.query}") String authoritiesByUsernameQuery;

    // Password Encoder
    @Value("${strength.password.encoder}") int strengthPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(pattern).permitAll()
                .and()
                    .formLogin()
                        .loginPage(loginPage)
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl(failureUrl)
                        .usernameParameter(usernameParameter)
                        .passwordParameter(passwordParameter)
                .and()
                    .logout()
                        .deleteCookies(deleteCookies)
                        .invalidateHttpSession(invalidateHttpSession)
                        .clearAuthentication(clearAuthentication)
                        .logoutRequestMatcher(new AntPathRequestMatcher(logoutRequestMatcher))
                        .logoutSuccessUrl(logoutSuccessUrl)
                .and()
                    .sessionManagement()
                        .invalidSessionUrl(invalidSessionUrl)
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(maximumSessions)
                        .expiredUrl(expiredUrl).and()
                .and()
                    .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(passwordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery(usersByUsernameQuery)
                .authoritiesByUsernameQuery(authoritiesByUsernameQuery);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(strengthPasswordEncoder);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
