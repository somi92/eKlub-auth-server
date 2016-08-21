/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.auth.boot;

import com.github.somi92.auth.config.EKlubServletCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author milos
 */
@ComponentScan(basePackages = "com.github.somi92.auth")
@Configuration
@EnableAutoConfiguration
public class EKlubAuthServerApplication extends SpringBootServletInitializer {

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EKlubServletCustomizer();
    }
    
//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//        registerServletFilter(servletContext, new CORSFilter());
//    }
//    
//    protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
//        String filterName = Conventions.getVariableName(filter);
//        FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);
//        registration.setAsyncSupported(true);
//        registration.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
//        return registration;
//    }

    public static void main(String[] args) {
        SpringApplication.run(EKlubAuthServerApplication.class, args);
    }
}
