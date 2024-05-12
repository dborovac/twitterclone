package com.twitterclone.iam.api.controller;

import com.twitterclone.iam.api.request.LoginRequest;
import com.twitterclone.iam.api.response.LoginResponse;
import com.twitterclone.iam.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @MutationMapping
    public LoginResponse login(@Argument @Valid LoginRequest request) {
        return loginService.login(request);
    }
}
