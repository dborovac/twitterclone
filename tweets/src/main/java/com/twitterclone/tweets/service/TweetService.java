package com.twitterclone.tweets.service;

import com.twitterclone.nodes.iam.UserEntity;
import com.twitterclone.nodes.tweets.TweetEntity;
import com.twitterclone.tweets.api.request.PostTweetRequest;
import com.twitterclone.tweets.common.repository.UserRepository;
import com.twitterclone.tweets.mapper.TweetMapper;
import com.twitterclone.tweets.model.domain.Tweet;
import com.twitterclone.tweets.repository.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final TweetMapper tweetMapper;

    public Tweet post(PostTweetRequest request, Authentication authentication) {
        final var userEntity = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User with id: " + authentication.getName() + " not found."));
        final var mentionedUserEntities = getMentionedUsers(request);
        final var tweetEntity = TweetEntity.builder()
                .content(request.content())
                .userId(authentication.getName())
                .user(userEntity)
                .mentions(mentionedUserEntities)
                .postedAt(Instant.now())
                .build();
        return tweetMapper.toDomain(tweetRepository.save(tweetEntity), false);
    }

    private Set<UserEntity> getMentionedUsers(PostTweetRequest request) {
        final List<String> mentions = Arrays.stream(request.content().split(" "))
            .filter(word -> word.startsWith("@"))
            .filter(word -> word.length() > 1)
            .toList();
        return mentions.stream()
            .map(mention -> userRepository.findByHandle(mention.substring(1)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());
    }

    public List<Tweet> getByUserId(String userId) {
        return tweetRepository.getAllByUserIdOrderByPostedAtDesc(userId)
                .stream()
                .map(tweetEntity -> tweetMapper.toDomain(tweetEntity, tweetEntity.getLikedBy().stream().anyMatch(a -> a.getId().equals(userId))))
                .toList();
    }

    public List<Tweet> getMyTweets(Authentication authentication) {
        return getByUserId(authentication.getName());
    }

    public List<Tweet> getFolloweeTweets(Instant cursorTimestamp, Authentication authentication) {
        return tweetRepository.getFromFollowees(cursorTimestamp, authentication.getName())
                .stream()
                .map(tweetEntity -> tweetMapper.toDomain(tweetEntity, tweetEntity.getLikedBy().stream().anyMatch(a -> a.getId().equals(authentication.getName()))))
                .toList();
    }

    public Tweet toggleLike(String tweetId, Authentication authentication) {
        final var myself = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User with id: " + authentication.getName() + " not found."));
        final var tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet with id: " + tweetId + " not found."));
        final Set<UserEntity> likedBy = tweet.getLikedBy();
        if (likedBy.contains(myself)) {
            likedBy.remove(myself);
        } else {
            likedBy.add(myself);
        }
        tweet.setLikedBy(likedBy);
        tweetRepository.save(tweet);
        return tweetMapper.toDomain(tweet, likedBy.contains(myself));
    }
}
