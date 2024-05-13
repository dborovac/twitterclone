package com.twitterclone.iam.api.controller;

import com.twitterclone.iam.common.PageRequest;
import com.twitterclone.iam.domain.FollowRecommendation;
import com.twitterclone.iam.domain.Tweet;
import com.twitterclone.iam.domain.User;
import com.twitterclone.iam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated() and !isAnonymous()")
public class UserController {

    private final UserService userService;

    @QueryMapping
    public User getUserById(@Argument String userId) {
        return userService.getById(userId);
    }

    @QueryMapping
    public List<User> searchUsersByHandle(@Argument String handle) {
        return userService.searchByHandle(handle);
    }

    @QueryMapping
    public List<User> searchUsers(@Argument String searchQuery, @AuthenticationPrincipal Authentication authentication) {
        return userService.search(searchQuery, authentication);
    }

    @QueryMapping
    public User getMyself(@AuthenticationPrincipal Authentication authentication) {
        return userService.getById(authentication.getName());
    }

    @MutationMapping
    public User follow(@Argument String userId, @AuthenticationPrincipal Authentication authentication) {
        return userService.follow(userId, authentication);
    }

    @MutationMapping
    public User unfollow(@Argument String userId, @AuthenticationPrincipal Authentication authentication) {
        return userService.unfollow(userId, authentication);
    }

    @QueryMapping
    public List<FollowRecommendation> followRecommendations(@Argument Integer first, @AuthenticationPrincipal Authentication authentication) {
        return userService.getFollowRecommendationsForUser(first, authentication.getName());
    }

    @SchemaMapping
    public User postedBy(Tweet tweet) {
        return userService.getByTweetId(tweet.id());
    }

    @SchemaMapping
    public List<User> mentions(Tweet tweet) {
        return userService.getMentions(tweet.id());
    }

    @SchemaMapping
    public Set<User> likedBy(Tweet tweet, @Argument PageRequest pageRequest) {
        return userService.getLikes(tweet.id(), pageRequest);
    }

    @SchemaMapping
    public Boolean likedByMe(Tweet tweet, @AuthenticationPrincipal Authentication authentication) {
        return userService.isTweetLikedByUser(tweet.id(), authentication.getName());
    }
}
