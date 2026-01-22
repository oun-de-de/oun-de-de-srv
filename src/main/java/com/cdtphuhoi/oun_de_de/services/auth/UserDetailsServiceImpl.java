package com.cdtphuhoi.oun_de_de.services.auth;

import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.repository.UserRepository;
import com.cdtphuhoi.oun_de_de.services.auth.dto.SignUpData;
import com.cdtphuhoi.oun_de_de.services.auth.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usr = userRepository.findByUsername(username)
            .orElse(null);
        if (usr == null) {
            throw new UsernameNotFoundException("Account does nto exist");
        }
        return UserDetailsImpl.builder()
            .username(username)
            .build();
    }

    @Transactional
    public void signUp(SignUpData request) {
        if (!request.getPassword().equals(request.getReEnteredPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
