package com.twitterclone.iam.user.api.service;

import com.twitterclone.iam.user.mapper.UserMapper;
import com.twitterclone.iam.user.model.domain.User;
import com.twitterclone.iam.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User getById(String userId) {
        final var userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id: " + userId + " not found."));
        return userMapper.toDomain(userEntity);
    }

    public User getByTweetId(String tweetId) {
        final var userEntity = userRepository.findByTweetId(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet does not have a relationship to any user."));
        return userMapper.toDomain(userEntity);
    }

    public List<User> searchByHandle(String handle) {
        return userRepository.findByHandleContainsIgnoreCase(handle).stream().map(userMapper::toDomain).toList();
    }
}
