package com.twitterclone.tweets.model.domain;

import com.twitterclone.tweets.common.model.User;

import java.time.Instant;
import java.util.List;

public record Tweet(String id, String content, User user, List<User> mentions, Instant postedAt) {
}
