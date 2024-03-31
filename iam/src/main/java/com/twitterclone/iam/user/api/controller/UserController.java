package com.twitterclone.iam.user.api.controller;

import com.twitterclone.iam.common.model.Tweet;
import com.twitterclone.iam.common.response.GenericResponse;
import com.twitterclone.iam.user.api.service.UserService;
import com.twitterclone.iam.user.model.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @QueryMapping
    @PreAuthorize("isAuthenticated() and !isAnonymous()")
    public User getUserById(@Argument String userId) {
        return userService.getById(userId);
    }

    @QueryMapping
    public List<User> searchUsersByHandle(@Argument String handle) {
        return userService.searchByHandle(handle);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated() and !isAnonymous()")
    public User getMyself(@AuthenticationPrincipal Authentication authentication) {
        return userService.getById(authentication.getName());
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated() and !isAnonymous()")
    public GenericResponse follow(@Argument String userId, @AuthenticationPrincipal Authentication authentication) {
        return userService.follow(userId, authentication);
    }

    @SchemaMapping
    public User user(Tweet tweet) {
        return userService.getByTweetId(tweet.id());
    }

    @SchemaMapping
    public List<User> mentions(Tweet tweet) {
        return userService.getMentions(tweet.id());
    }
}
