package com.twitterclone.iam.domain;

import java.util.List;

public record FollowRecommendation(User recommendation, Double relevance, List<User> mutualFollowees) {}
