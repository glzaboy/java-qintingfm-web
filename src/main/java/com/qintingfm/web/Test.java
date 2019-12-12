package com.qintingfm.web;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class Test {
    private static AuthenticationManager am = new SampleAuthenticationManager();

    public static void main(String[] args) {
        try{
        Authentication request = new UsernamePasswordAuthenticationToken("fs", "fs");
        Authentication authenticate = am.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
    } catch(AuthenticationException e) {
        System.out.println("Authentication failed: " + e.getMessage());
    }
        System.out.println("Successfully authenticated. Security context contains: " +
                SecurityContextHolder.getContext().getAuthentication());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.printf(SecurityContextHolder.getContext().getAuthentication().toString());

//        System.out.printf(authenticate.getName());

    }
}
