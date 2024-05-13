package com.twitterclone.iam.service;

import com.twitterclone.iam.common.PageRequest;
import com.twitterclone.iam.common.mapping.CycleAvoidingMappingContext;
import com.twitterclone.iam.mapper.UserMapper;
import com.twitterclone.iam.domain.FollowRecommendation;
import com.twitterclone.iam.domain.FollowNotification;
import com.twitterclone.iam.domain.User;
import com.twitterclone.iam.repository.ReactiveUserRepository;
import com.twitterclone.iam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReactiveUserRepository reactiveUserRepository;
    private final UserMapper userMapper;

    public User getById(String userId) {
        final var userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id: " + userId + " not found."));
        return userMapper.toDomain(userEntity, new CycleAvoidingMappingContext());
    }

    public User getByTweetId(String tweetId) {
        final var userEntity = userRepository.findByTweetId(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet does not have a relationship to any recommendation."));
        return userMapper.toDomain(userEntity, new CycleAvoidingMappingContext());
    }

    public List<User> searchByHandle(String handle) {
        return userRepository.findByHandleContainsIgnoreCase(handle)
            .stream()
            .map(user -> userMapper.toDomain(user, new CycleAvoidingMappingContext()))
            .toList();
    }

    public List<User> search(String searchQuery, Authentication authentication) {
        return userRepository.findByHandleOrFirstNameOrLastNameContainsIgnoreCase(searchQuery, authentication.getName())
                .stream()
                .map(user -> userMapper.toDomain(user, new CycleAvoidingMappingContext()))
                .toList();
    }

    public List<User> getMentions(String tweetId) {
        return userRepository.findMentionedInTweet(tweetId)
            .stream()
            .map(user -> userMapper.toDomain(user, new CycleAvoidingMappingContext()))
            .toList();
    }

    public User follow(String userId, Authentication authentication) {
        final var myself = userRepository.findById(authentication.getName())
            .orElseThrow(() -> new RuntimeException("User with id: " + authentication.getName() + " not found."));
        final var followee = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User with id: " + userId + " not found."));
        followee.getFollowers().add(myself);
        myself.getFollowees().add(followee);
        userRepository.save(followee);
        userRepository.save(myself);
        return userMapper.toDomain(followee, new CycleAvoidingMappingContext());
    }

    public User unfollow(String userId, Authentication authentication) {
        final var myself = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User with id: " + authentication.getName() + " not found."));
        final var followee = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id: " + userId + " not found."));
        userRepository.deleteFollowsRelationship(myself.getId(), followee.getId());
        return userMapper.toDomain(followee, new CycleAvoidingMappingContext());
    }

    public Set<User> getLikes(String tweetId, PageRequest pageRequest) {
        return userRepository.findUsersThatLikeThisTweet(tweetId, pageRequest.first(), pageRequest.offset())
                .stream()
                .map(user -> userMapper.toDomain(user, new CycleAvoidingMappingContext()))
                .collect(Collectors.toSet());
    }

    public Boolean isTweetLikedByUser(String tweetId, String userId) {
        return userRepository.hasUserLikedTweet(userId, tweetId);
    }

    public List<FollowRecommendation> getFollowRecommendationsForUser(Integer first, String userId) {
        final var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id: " + userId + " not found."));
        final var result = new ArrayList<FollowRecommendation>();
        userRepository.findRecommendedUsersToFollow(user.getId(), first)
                .forEach(recommendationDto -> result.add(new FollowRecommendation(userMapper.toDomain(recommendationDto.recommendation(), new CycleAvoidingMappingContext()), recommendationDto.relevance(), userMapper.toDomain(recommendationDto.mutualFollowees(), new CycleAvoidingMappingContext()))));
        return result;
    }

    public Flux<FollowNotification> getFollowNotifications(String myUserId) {
        return reactiveUserRepository.findFollowersForUser(myUserId)
                .map(follower -> new FollowNotification(userMapper.toDomain(follower, new CycleAvoidingMappingContext()), Instant.now()));
    }
}
