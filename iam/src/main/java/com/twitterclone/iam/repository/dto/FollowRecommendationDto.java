package com.twitterclone.iam.repository.dto;

import com.twitterclone.nodes.iam.UserEntity;

import java.util.List;

public record FollowRecommendationDto(UserEntity recommendation, Double relevance, List<UserEntity> mutualFollowees) {
}
