package com.cdtphuhoi.oun_de_de.services.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.exceptions.ConflictException;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.services.auth.dto.SignUpData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        var user = User.builder().id(UUID.randomUUID().toString()).username("test").password("pass").build();
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(user));

        var details = userDetailsService.loadUserByUsername("test");
        assertEquals("test", details.getUsername());
        assertEquals("pass", details.getPassword());
    }

    @Test
    void loadUserByUsername_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("missing"));
    }

    @Test
    void signUp_ShouldSaveUser_WhenValid() {
        var request = SignUpData.builder()
            .username("newuser")
            .password("pw")
            .reEnteredPassword("pw")
            .build();
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("pw")).thenReturn("encodedpw");

        userDetailsService.signUp(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        var saved = captor.getValue();
        assertEquals("newuser", saved.getUsername());
        assertEquals("encodedpw", saved.getPassword());
    }

    @Test
    void signUp_ShouldThrow_WhenPasswordsDoNotMatch() {
        var request = SignUpData.builder()
            .username("user")
            .password("pw1")
            .reEnteredPassword("pw2")
            .build();
        assertThrows(BadRequestException.class, () -> userDetailsService.signUp(request));
    }

    @Test
    void signUp_ShouldThrow_WhenUsernameExists() {
        var request = SignUpData.builder()
            .username("existing")
            .password("pw")
            .reEnteredPassword("pw")
            .build();
        when(userRepository.existsByUsername("existing")).thenReturn(true);
        assertThrows(ConflictException.class, () -> userDetailsService.signUp(request));
    }
}
