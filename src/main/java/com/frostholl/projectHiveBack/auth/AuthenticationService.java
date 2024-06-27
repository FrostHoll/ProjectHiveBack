package com.frostholl.projectHiveBack.auth;


import com.frostholl.projectHiveBack.config.JwtService;
import com.frostholl.projectHiveBack.exception.auth.IncorrectUserDataException;
import com.frostholl.projectHiveBack.exception.auth.WeakPasswordException;
import com.frostholl.projectHiveBack.model.User;
import com.frostholl.projectHiveBack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService service;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (!service.isUserDataValid(request.getLogin(), request.getFullName()))
            throw new IncorrectUserDataException("Incorrect user data.");
        if (!service.isPasswordValid(request.getPassword()))
            throw new WeakPasswordException("Weak password.");
        var user = User.builder()
                .fullName(request.getFullName())
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .build();

        service.addNewUser(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(), request.getPassword()
                )
        );

        var user = service.getUserByLogin(request.getLogin());

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
