package com.pollite.service.user;

import com.pollite.dto.UserDto;
import com.pollite.exception.UserAlreadyExistsException;
import com.pollite.exception.UserDoesNotExistException;
import com.pollite.model.auth.User;
import com.pollite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto registerUser(String userName, String password) {
        if (!userExists(userName)) {
            String encodedPassword = passwordEncoder.encode(password);
            User newUser = new User();
            newUser.setUsername(userName);
            newUser.setPassword(encodedPassword);
            userRepository.save(newUser);

            return UserDto.builder()
                    .username(userName)
                    .password(password)
                    .build();
        } else {
            throw new UserAlreadyExistsException(userName);
        }
    }

    public UserDto authenticateUser(UserDto userDto) {
        var user = findUserByUsername(userDto.getUsername());
        if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            return userDto;
        }
        throw new UserDoesNotExistException(userDto.getUsername());
    }

    public User findUserByUsername(String username) throws UserDoesNotExistException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserDoesNotExistException(username));
    }

    private boolean userExists(String userName) {
        return userRepository.findByUsername(userName).isPresent();
    }
}
