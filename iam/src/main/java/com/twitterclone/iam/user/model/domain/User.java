package com.twitterclone.iam.user.model.domain;

import java.util.List;

public record User(String id,
                   String email,
                   String handle,
                   String firstName,
                   String lastName,
                   String birthday,
                   List<User> followers,
                   List<User> followees) {
}
