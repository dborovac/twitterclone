package com.twitterclone.iam.user.api.service;

import com.twitterclone.iam.common.mapping.CycleAvoidingMappingContext;
import com.twitterclone.iam.user.mapper.UserMapper;
import com.twitterclone.iam.user.model.domain.User;
import com.twitterclone.iam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getById(String userId) {
        final var userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id: " + userId + " not found."));
        return userMapper.toDomain(userEntity, new CycleAvoidingMappingContext());
    }

    public User getByTweetId(String tweetId) {
        final var userEntity = userRepository.findByTweetId(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet does not have a relationship to any user."));
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
//        if (!followee.getFollowers().remove(myself) || !myself.getFollowees().remove(followee)) {
//            throw new RuntimeException("User with id: " + authentication.getName() + " does not follow user with id: " + userId + ". Because of this, failed to unfollow.");
//        }
        followee.getFollowers().remove(myself);
        userRepository.save(followee);
        myself.getFollowees().remove(followee);
        userRepository.save(myself);
        return userMapper.toDomain(followee, new CycleAvoidingMappingContext());
    }

    public Set<User> getLikes(String tweetId) {
        return userRepository.findUsersThatLikeThisTweet(tweetId)
                .stream()
                .map(user -> userMapper.toDomain(user, new CycleAvoidingMappingContext()))
                .collect(Collectors.toSet());
    }

    public Boolean isTweetLikedByUser(String tweetId, String userId) {
        return userRepository.hasUserLikedTweet(userId, tweetId);
    }
}
