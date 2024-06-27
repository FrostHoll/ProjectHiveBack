package com.frostholl.projectHiveBack.service;

import com.frostholl.projectHiveBack.exception.auth.IncorrectUserDataException;
import com.frostholl.projectHiveBack.exception.auth.UserLoginAlreadyInUseException;
import com.frostholl.projectHiveBack.exception.auth.UserNotFoundException;
import com.frostholl.projectHiveBack.model.User;
import com.frostholl.projectHiveBack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private static final String LOGIN_REGEX = "^[a-zA-Z][a-zA-Z0-9_]{5,19}$";

    private static final String PASSWORD_REGEX = "^[a-zA-Z0-9!@#$%^&*()_+]{8,16}$";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public boolean isUserDataValid(String login, String fullName) {
        return isLoginValid(login) && isFullNameValid(fullName);
    }

    public boolean isLoginValid(String login) {
        return login != null && login.matches(LOGIN_REGEX);
    }

    public boolean isFullNameValid(String fullName) {
        return fullName != null && !fullName.isEmpty();
    }

    public boolean isPasswordValid(String password) {
        return password != null && password.matches(PASSWORD_REGEX);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserByLogin(String login) {
        Optional<User> userOptional = userRepository.findUserByLogin(login);
        return userOptional.orElseThrow(() -> new UserNotFoundException("User was not found."));
    }

    public boolean isUserWithLoginExist(String login) {
        Optional<User> userOptional = userRepository.findUserByLogin(login);
        return userOptional.isPresent();
    }

    public void addNewUser(User user) {
        if (isUserWithLoginExist(user.getLogin())) {
            throw new UserLoginAlreadyInUseException("Specified login is already in use.");
        }
        userRepository.save(user);
    }

    public void updateUser(User user) {
        Optional<User> userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            userRepository.save(user);
        }
        else throw new UserNotFoundException("User was not found.");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        var user = userRepository.findUserByLogin(username);
        return user.orElseThrow(() ->  new UserNotFoundException("User was not found."));
    }

    public void changeUserPassword(User user, String pass, String newPass) {
        if (passwordEncoder.matches(pass, user.getPassword())) {
            throw new IncorrectUserDataException("Wrong password!");
        }

        user.setPassword(passwordEncoder.encode(newPass));

        userRepository.save(user);
    }
}
