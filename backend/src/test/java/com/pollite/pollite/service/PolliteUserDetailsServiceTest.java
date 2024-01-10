package com.pollite.pollite.service;

import com.pollite.pollite.model.auth.User;
import com.pollite.pollite.repository.UserRepository;
import com.pollite.pollite.service.poll.PolliteUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolliteUserDetailsServiceTest {

    private static final String USER_NAME = "user";

    @InjectMocks
    private PolliteUserDetailsService sut;

    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldLoadUserByNameWhenUserExists() {
        //given
        var user = User.builder().username(USER_NAME).password("password").build();
        when(userRepository.findByUsername(USER_NAME)).thenReturn(Optional.of(user));

        //when
        var result = sut.loadUserByUsername(USER_NAME);

        //then
        assertThat(result.getUsername()).isEqualTo(USER_NAME);
    }

    @Test
    public void shouldThrowExceptionWhenUserDoesNotExist() {
        Throwable throwable = assertThrows(UsernameNotFoundException.class, () -> sut.loadUserByUsername(USER_NAME));
        assertThat(throwable.getMessage()).isEqualTo(USER_NAME);
    }
}