package com.twitterclone.iam.service;

import com.twitterclone.iam.api.response.GenericResponse;
import com.twitterclone.iam.api.request.RegistrationRequest;
import com.twitterclone.iam.repository.UserRepository;
import com.twitterclone.nodes.iam.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public GenericResponse register(RegistrationRequest request) {
        final var user = new UserEntity();
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setBirthday(request.birthday());
        user.setHandle(request.handle());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
        return new GenericResponse(true);
    }
}
