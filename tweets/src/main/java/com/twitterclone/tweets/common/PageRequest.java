package com.twitterclone.tweets.common;

import jakarta.validation.constraints.Min;

public record PageRequest(

        @Min(value = 0, message = "First must be a positive number")
        Integer first,

        @Min(value = 0, message = "Offset must be a positive number")
        Integer offset) {
}
