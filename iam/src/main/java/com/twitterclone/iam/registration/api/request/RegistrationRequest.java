package com.twitterclone.iam.registration.api.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

import static com.twitterclone.iam.common.RegExpr.ALPHANUMERIC;
import static com.twitterclone.iam.common.RegExpr.PERSONAL_NAME;

public record RegistrationRequest(
        @NotNull
        @Email
        String email,

        @NotNull
        @Size(min = 3, max = 15)
        @Pattern(regexp = ALPHANUMERIC)
        String handle,

        @NotNull
        @Size(min = 5, max = 20)
        @Pattern(regexp = ALPHANUMERIC)
        String password,

        @NotNull
        @Size(min = 5, max = 20)
        @Pattern(regexp = ALPHANUMERIC)
        String matchingPassword,

        @Size(min = 1, max = 50)
        @Pattern(regexp = PERSONAL_NAME)
        String firstName,

        @Size(min = 1, max = 50)
        @Pattern(regexp = PERSONAL_NAME)
        String lastName,

        @Past(message = "Birth date must be in the past")
        LocalDate birthday) {

    @AssertTrue
    private boolean isPasswordEqualToMatchingPassword() {
        return password.equals(matchingPassword);
    }
}
