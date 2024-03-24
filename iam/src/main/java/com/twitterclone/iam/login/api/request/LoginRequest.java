package com.twitterclone.iam.login.api.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        @Email
        String email,

        @NotBlank
        String password) {
}
