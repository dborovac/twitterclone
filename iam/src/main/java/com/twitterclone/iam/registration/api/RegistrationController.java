package com.twitterclone.iam.registration.api;

import com.twitterclone.iam.common.response.GenericResponse;
import com.twitterclone.iam.registration.api.request.RegistrationRequest;
import com.twitterclone.iam.registration.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @MutationMapping
    public GenericResponse register(@Argument @Valid RegistrationRequest request) {
        return registrationService.register(request);
    }
}
