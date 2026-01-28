package com.cdtphuhoi.oun_de_de.services.auth;

import static com.cdtphuhoi.oun_de_de.utils.Utils.randomNumericString;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.exceptions.ConflictException;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
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

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usr = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return UserDetailsImpl.builder()
            .id(usr.getId())
            .username(username)
            .password(usr.getPassword())
            .orgId(usr.getOrgId())
            .build();
    }

    @Transactional
    public void signUp(SignUpData request) {
        if (!request.getPassword().equals(request.getReEnteredPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username already exists");
        }

        var user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .build();
        user.setOrgId(String.format("DAN-%s", randomNumericString(15)));
        userRepository.save(user);
    }
}
