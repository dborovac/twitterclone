package com.twitterclone.tweets.api.controller;

import com.twitterclone.tweets.model.domain.Tweet;
import com.twitterclone.tweets.common.model.User;
import com.twitterclone.tweets.api.request.PostTweetRequest;
import com.twitterclone.tweets.service.TweetService;
import jakarta.validation.Valid;
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

@Controller
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @MutationMapping
    @PreAuthorize("isAuthenticated() and !isAnonymous()")
    public Tweet postTweet(
            @Argument @Valid PostTweetRequest request,
            @AuthenticationPrincipal Authentication authentication) {
        return tweetService.post(request, authentication);
    }

    @QueryMapping
    @PreAuthorize("isAuthenticated() and !isAnonymous()")
    public List<Tweet> getMyTweets(@AuthenticationPrincipal Authentication authentication) {
        return tweetService.getMyTweets(authentication);
    }

    @SchemaMapping
    public List<Tweet> tweets(User user) {
        return tweetService.getByUserId(user.id());
    }
}
