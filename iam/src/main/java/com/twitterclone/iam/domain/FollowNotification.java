package com.twitterclone.iam.domain;

import java.time.Instant;

public record FollowNotification(User user, Instant followedAt) {
}
