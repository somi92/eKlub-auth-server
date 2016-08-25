/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.somi92.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
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
    public ResponseEntity logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null) {
            request.getSession().invalidate();
            SecurityContextHolder.getContext().setAuthentication(null);
            
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
            return new ResponseEntity("logout " + HttpStatus.OK, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
    }
}
