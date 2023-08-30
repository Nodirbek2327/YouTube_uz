package com.example.util;


import com.example.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {

     public static Integer getProfileId(){
            CustomUserDetails customUserDetails = getCurrentUser();
            return customUserDetails.getProfile().getId();
     }
    public static String getCurrentUsername(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName= authentication.getName();
        return currentPrincipalName;
    }
    public static CustomUserDetails getCurrentUser(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }
}
