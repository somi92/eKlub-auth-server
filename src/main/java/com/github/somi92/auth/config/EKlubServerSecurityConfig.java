/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.auth.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 *
 * @author milos
 */
@Configuration
@EnableWebSecurity
public class EKlubServerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(getUserQuery())
                .authoritiesByUsernameQuery(getAuthoritiesQuery());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new Filter() {

            @Override
            public void init(FilterConfig fc) throws ServletException { }

            @Override
            public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
                HttpServletResponse response = (HttpServletResponse) res;
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                response.setHeader("Access-Control-Max-Age", "3600");
                response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers: Origin, X-Requested-With, Content-Type, Accept, Authorization");
                chain.doFilter(req, res);
            }

            @Override
            public void destroy() { }
        }, AbstractPreAuthenticatedProcessingFilter.class);
        http.authorizeRequests()
                .antMatchers("/login", "/oauth/confirm_access", "/oauth/check_token", "/logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();
    }

    private String getUserQuery() {
        return "SELECT username, `password`, enabled FROM Employee WHERE username = ?";
    }

    private String getAuthoritiesQuery() {
        return "SELECT username, authorityName AS authority \n"
                + "FROM Employee E JOIN EmployeeAuthority EA ON (E.idEmployee = EA.idEmployee) \n"
                + "	JOIN oauth_eklub_authority A ON (EA.idAuthority = A.idAuthority) \n"
                + "WHERE username = ?";
    }
}
