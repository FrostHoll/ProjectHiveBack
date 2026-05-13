package com.frostholl.projectHiveBack.services;

import com.frostholl.projectHiveBack.exception.auth.UserLoginAlreadyInUseException;
import com.frostholl.projectHiveBack.model.User;
import com.frostholl.projectHiveBack.repository.UserRepository;
import com.frostholl.projectHiveBack.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void whenGetUserByLogin_thenUserReturned() {
        String login = "testLogin";
        User user = new User(1, login, "pass", "fullname", true);

        Mockito.when(userRepository.findUserByLogin(login)).thenReturn(Optional.of(user));

        User result = userService.getUserByLogin(login);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    public void whenAddUserWithExistedLogin_thenThrow() {
        String login = "testLogin";
        User user = new User(1, login, "pass", "fullname", true);
        User user1 = new User(2, login, "pass1", "fullname1", true);

        Mockito.when(userRepository.findUserByLogin(login)).thenReturn((Optional.of(user)));

        assertThrows(UserLoginAlreadyInUseException.class, () -> userService.addNewUser(user1));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void whenPasswordChange_thenPasswordChangedToEncoded() {
        String oldPass = "oldPass";
        String newPass = "newPass";
        String encodedPass = "encodedNewPass";
        User user = new User(1, "login", oldPass, "name", true);

        Mockito.when(passwordEncoder.matches(oldPass, oldPass)).thenReturn(true);
        Mockito.when(passwordEncoder.encode(newPass)).thenReturn(encodedPass);

        assertDoesNotThrow(() -> userService.changeUserPassword(user, oldPass, newPass));
        assertEquals(encodedPass, user.getPassword());
        Mockito.verify(userRepository).save(Mockito.any());
    }
}
