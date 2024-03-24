package com.twitterclone.iam.user.model.domain;

public record User(String id,
                   String email,
                   String handle,
                   String firstName,
                   String lastName,
                   String birthday) {
}
