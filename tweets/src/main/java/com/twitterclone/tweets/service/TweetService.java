package com.twitterclone.tweets.service;

import com.twitterclone.tweets.common.repository.UserRepository;
import com.twitterclone.nodes.tweets.TweetEntity;
import com.twitterclone.tweets.api.request.PostTweetRequest;
import com.twitterclone.tweets.mapper.TweetMapper;
import com.twitterclone.tweets.model.domain.Tweet;
import com.twitterclone.tweets.repository.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
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
        final var tweetEntity = TweetEntity.builder()
                .content(request.content())
                .userId(authentication.getName())
                .user(userEntity)
                .postedAt(Instant.now())
                .build();
        tweetRepository.save(tweetEntity);
        return tweetMapper.toDomain(tweetEntity);
    }

    public List<Tweet> getByUserId(String userId) {
        return tweetRepository.getAllByUserId(userId)
                .stream()
                .map(tweetMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Tweet> getMyTweets(Authentication authentication) {
        return getByUserId(authentication.getName());
    }
}
