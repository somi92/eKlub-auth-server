/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author milos
 */
@Component
@RestController
public class AuthController {
    
    @Autowired
    private TokenStore tokenStore;
    
    @RequestMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
                SecurityContextHolder.getContext().setAuthentication(null);
            }
            request.getSession().invalidate();
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
            return new ResponseEntity("{ \"status\": " + HttpStatus.OK + " }", HttpStatus.OK);
        } else {
            return new ResponseEntity("{ \"status\": " + HttpStatus.BAD_REQUEST + " }", HttpStatus.BAD_REQUEST);
        }
    }
}
