package com.twitterclone.tweets.service;

import com.twitterclone.nodes.iam.UserEntity;
import com.twitterclone.nodes.tweets.TweetEntity;
import com.twitterclone.tweets.api.request.PostTweetRequest;
import com.twitterclone.tweets.common.repository.UserRepository;
import com.twitterclone.tweets.mapper.TweetMapper;
import com.twitterclone.tweets.model.domain.Hashtag;
import com.twitterclone.tweets.model.domain.HashtagTrend;
import com.twitterclone.tweets.model.domain.Tweet;
import com.twitterclone.tweets.repository.TweetRepository;
import com.twitterclone.tweets.utils.StringUtils;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final TweetMapper tweetMapper;
    private final HashtagService hashtagService;

    public Tweet post(PostTweetRequest request, Authentication authentication) {
        final var userEntity = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User with id: " + authentication.getName() + " not found."));
        final var mentionedUserEntities = getMentionedUsers(request);
        final var hashtags = hashtagService.getHashtags(request);
        final var tweetEntity = TweetEntity.builder()
                .content(request.content())
                .userId(authentication.getName())
                .user(userEntity)
                .mentions(mentionedUserEntities)
                .hashtags(hashtags)
                .postedAt(Instant.now())
                .build();
        return tweetMapper.toDomain(tweetRepository.save(tweetEntity), false);
    }

    private Set<UserEntity> getMentionedUsers(PostTweetRequest request) {
        final List<String> mentions = StringUtils.getWordsThatStartWith("@", request.content());
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

    public List<Tweet> getFolloweeTweets(Integer first, Integer offset, Authentication authentication) {
        return tweetRepository.getFromFollowees(first, offset, authentication.getName())
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

    public List<Tweet> getAllByHashtag(String hashtag, Authentication authentication) {
        return tweetRepository.getAllByHashtagsNameIgnoreCase(hashtag)
                .stream()
                .map(tweetEntity -> tweetMapper.toDomain(tweetEntity, tweetEntity.getLikedBy().stream().anyMatch(a -> a.getId().equals(authentication.getName()))))
                .toList();
    }

    public HashtagTrend getTrendForHashtag(String hashtagName) {
        final Hashtag hashtag = hashtagService.getHashtag(hashtagName);
        final long tweetCount = tweetRepository.countAllByHashtagsNameIgnoreCase(hashtagName);
        return new HashtagTrend(hashtag, tweetCount);
    }
}
