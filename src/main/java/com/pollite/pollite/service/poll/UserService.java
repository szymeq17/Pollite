package com.pollite.pollite.service.poll;

import com.pollite.pollite.dto.UserDto;
import com.pollite.pollite.exception.UserDoesNotExistException;
import com.pollite.pollite.model.auth.User;
import com.pollite.pollite.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(String userName, String password) {
        if (!userExists(userName)) {
            String encodedPassword = passwordEncoder.encode(password);
            User newUser = new User();
            newUser.setUsername(userName);
            newUser.setPassword(encodedPassword);
            userRepository.save(newUser);
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
