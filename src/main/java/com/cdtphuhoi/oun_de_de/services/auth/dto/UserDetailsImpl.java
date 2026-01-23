package com.cdtphuhoi.oun_de_de.services.auth.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

@Data
@Builder
public class UserDetailsImpl implements UserDetails {

    private String id;

    private String username;

    private String password;

    private List<GrantedAuthority> authorities;
}
