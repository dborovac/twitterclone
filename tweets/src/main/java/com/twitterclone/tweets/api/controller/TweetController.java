package com.twitterclone.tweets.api.controller;

import com.twitterclone.tweets.api.request.PostTweetRequest;
import com.twitterclone.tweets.common.PageRequest;
import com.twitterclone.tweets.domain.User;
import com.twitterclone.tweets.domain.HashtagTrend;
import com.twitterclone.tweets.domain.Tweet;
import com.twitterclone.tweets.repository.HashtagOccurrences;
import com.twitterclone.tweets.service.HashtagService;
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
@PreAuthorize("isAuthenticated() and !isAnonymous()")
public class TweetController {

    private final TweetService tweetService;
    private final HashtagService hashtagService;

    @QueryMapping
    public Tweet tweetById(@Argument String id) {
        return tweetService.getById(id);
    }

    @QueryMapping
    public List<Tweet> followeeTweets(@Argument PageRequest pageRequest, @AuthenticationPrincipal Authentication authentication) {
        return tweetService.getFolloweeTweets(pageRequest, authentication);
    }

    @MutationMapping
    public Tweet postTweet(
            @Argument @Valid PostTweetRequest request,
            @AuthenticationPrincipal Authentication authentication) {
        return tweetService.post(request, authentication);
    }

    @QueryMapping
    public List<Tweet> myTweets(@AuthenticationPrincipal Authentication authentication) {
        return tweetService.getMyTweets(authentication);
    }

    @MutationMapping
    public Tweet toggleLike(@Argument String tweetId, @AuthenticationPrincipal Authentication authentication) {
        return tweetService.toggleLike(tweetId, authentication);
    }

    @QueryMapping
    public List<Tweet> taggedWith(@Argument String hashtag) {
        return tweetService.getAllByHashtag(hashtag);
    }

    // TODO: instead of a separate endpoint, return the trend (total number of tweets tagged with) as a totalCount in the TweetConnection
    @QueryMapping
    public HashtagTrend trendForHashtag(@Argument String hashtag) {
        return tweetService.getTrendForHashtag(hashtag);
    }

    @QueryMapping
    public List<HashtagOccurrences> topHashtagOccurrences(@Argument Integer top) {
        return hashtagService.getTopHashtagOccurrences(top);
    }

    @SchemaMapping
    public List<Tweet> tweets(User user) {
        return tweetService.getByUserId(user.id());
    }

    @SchemaMapping
    public Integer numberOfLikes(Tweet tweet) {
        return tweetService.getNumberOfLikes(tweet);
    }
}
