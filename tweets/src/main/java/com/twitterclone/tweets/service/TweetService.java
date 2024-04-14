package com.twitterclone.tweets.service;

import com.twitterclone.nodes.iam.UserEntity;
import com.twitterclone.tweets.common.repository.UserRepository;
import com.twitterclone.nodes.tweets.TweetEntity;
import com.twitterclone.tweets.api.request.PostTweetRequest;
import com.twitterclone.tweets.mapper.TweetMapper;
import com.twitterclone.tweets.model.domain.Tweet;
import com.twitterclone.tweets.repository.TweetRepository;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final TweetMapper tweetMapper;

    public Mono<Tweet> post(PostTweetRequest request, Authentication authentication) {
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
        return tweetRepository.save(tweetEntity).map(tweetMapper::toDomain);
    }

    private List<UserEntity> getMentionedUsers(PostTweetRequest request) {
        final List<String> mentions = Arrays.stream(request.content().split(" "))
            .filter(word -> word.startsWith("@"))
            .filter(word -> word.length() > 1)
            .toList();
        return mentions.stream()
            .map(mention -> userRepository.findByHandle(mention.substring(1)))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }

    public Flux<Tweet> getByUserId(String userId) {
        return tweetRepository.getAllByUserIdOrderByPostedAtDesc(userId)
                .map(tweetMapper::toDomain);
    }

    public Flux<Tweet> getMyTweets(Authentication authentication) {
        return getByUserId(authentication.getName());
    }

    public Flux<Tweet> getFolloweeTweets(Instant cursorTimestamp, Authentication authentication) {
        return tweetRepository.getFromFollowees(cursorTimestamp, authentication.getName())
            .map(tweetMapper::toDomain);
    }
}
