package com.twitterclone.iam.user.api;

import com.twitterclone.iam.common.model.Tweet;
import com.twitterclone.iam.user.service.UserService;
import com.twitterclone.iam.user.model.domain.User;
import java.util.List;
import java.util.Set;

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
    @PreAuthorize("isAuthenticated() and !isAnonymous()")
    public List<User> searchUsersByHandle(@Argument String handle) {
        return userService.searchByHandle(handle);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated() and !isAnonymous()")
    public List<User> searchUsers(@Argument String searchQuery, @AuthenticationPrincipal Authentication authentication) {
        return userService.search(searchQuery, authentication);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated() and !isAnonymous()")
    public User getMyself(@AuthenticationPrincipal Authentication authentication) {
        return userService.getById(authentication.getName());
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated() and !isAnonymous()")
    public User follow(@Argument String userId, @AuthenticationPrincipal Authentication authentication) {
        return userService.follow(userId, authentication);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated() and !isAnonymous()")
    public User unfollow(@Argument String userId, @AuthenticationPrincipal Authentication authentication) {
        return userService.unfollow(userId, authentication);
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
    public Set<User> likedBy(Tweet tweet) {
        return userService.getLikes(tweet.id());
    }

    @SchemaMapping
    public Boolean likedByMe(Tweet tweet, @AuthenticationPrincipal Authentication authentication) {
        return userService.isTweetLikedByUser(tweet.id(), authentication.getName());
    }
}
