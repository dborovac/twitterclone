package com.twitterclone.tweets.api.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostTweetRequest(

        @NotNull
        @Size(min = 1, max = 255, message = "Content length must be between 1 and 255 characters")
        String content) {
}
