package com.zyh.easyapplyresume.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Security载体类
 * @author shiningCloud2025
 */
@Data
public class SecurityUser implements UserDetails {
    private Integer userId;

    private String username;

    private String password;

    private String userType;

    private Collection<? extends GrantedAuthority> authorities;

    private boolean enabled;



}
