package com.twitterclone.tweets.domain;

import java.time.Instant;
import java.util.Set;

public record Tweet(String id,
                    String content,
                    User user,
                    Set<User> mentions,
                    Set<User> likedBy,
                    Set<Hashtag> hashtags,
                    Instant postedAt,
                    Boolean likedByMe) {
}
