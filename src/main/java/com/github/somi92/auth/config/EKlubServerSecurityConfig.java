/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.auth.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
        http.authorizeRequests()
            .antMatchers("/login", "/oauth/confirm_access", "/oauth/check_token").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll();
        
//        http.csrf().disable();
    }
    
    private String getUserQuery() {
        return "SELECT username, `password`, enabled FROM Employee WHERE username = ?";
    }
    
    private String getAuthoritiesQuery() {
        return "SELECT username, scopeName AS authority \n" +
                "FROM Employee E JOIN EmployeeScope ES ON (E.idEmployee = ES.idEmployee) \n" +
                "	JOIN oauth_eklub_scope S ON (ES.idScope = S.idScope) \n" +
                "WHERE username = ?";
    } 
}
