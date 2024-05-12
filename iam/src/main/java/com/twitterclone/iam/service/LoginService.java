package com.twitterclone.iam.service;

import com.twitterclone.iam.api.request.LoginRequest;
import com.twitterclone.iam.api.response.LoginResponse;
import com.twitterclone.iam.repository.UserRepository;
import com.twitterclone.iam.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        final var userEntity = userRepository.findByEmail(request.email());
        if (userEntity.isEmpty() || !passwordEncoder.matches(request.password(), userEntity.get().getPassword())) {
            throw new IllegalStateException("Wrong username or password");
        }
        return new LoginResponse(jwtUtil.generateToken(userEntity.get().getId()));
    }
}
