package com.twitterclone.tweets.model.domain;

import com.twitterclone.tweets.common.model.User;

import java.time.Instant;

public record Tweet(String id, String content, User user, Instant postedAt) {
}
