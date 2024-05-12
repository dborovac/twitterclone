package com.twitterclone.tweets.service;

import com.twitterclone.nodes.iam.UserEntity;
import com.twitterclone.nodes.tweets.TweetEntity;
import com.twitterclone.tweets.api.request.PostTweetRequest;
import com.twitterclone.tweets.common.PageRequest;
import com.twitterclone.tweets.repository.UserRepository;
import com.twitterclone.tweets.mapper.TweetMapper;
import com.twitterclone.tweets.domain.Hashtag;
import com.twitterclone.tweets.domain.HashtagTrend;
import com.twitterclone.tweets.domain.Tweet;
import com.twitterclone.tweets.repository.TweetRepository;
import com.twitterclone.tweets.common.utils.StringUtils;
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
        return tweetMapper.toDomain(tweetRepository.save(tweetEntity));
    }

    public Tweet getById(String id) {
        return tweetMapper.toDomain(tweetRepository.findById(id).orElseThrow(() -> new RuntimeException("Tweet with id: " + id + " not found.")));
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
                .map(tweetMapper::toDomain)
                .toList();
    }

    public List<Tweet> getMyTweets(Authentication authentication) {
        return getByUserId(authentication.getName());
    }

    public List<Tweet> getFolloweeTweets(PageRequest pageRequest, Authentication authentication) {
        return tweetRepository.getFromFollowees(pageRequest.first(), pageRequest.offset(), authentication.getName())
                .stream()
                .map(tweetMapper::toDomain)
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
        return tweetMapper.toDomain(tweet);
    }

    public List<Tweet> getAllByHashtag(String hashtag) {
        return tweetRepository.getAllByHashtagsNameIgnoreCase(hashtag)
                .stream()
                .map(tweetMapper::toDomain)
                .toList();
    }

    public HashtagTrend getTrendForHashtag(String hashtagName) {
        final Hashtag hashtag = hashtagService.getHashtag(hashtagName);
        final Integer tweetCount = tweetRepository.countAllByHashtagsNameIgnoreCase(hashtagName);
        return new HashtagTrend(hashtag, tweetCount);
    }

    public Integer getNumberOfLikes(Tweet tweet) {
        return tweetRepository.countNumberOfLikes(tweet.id());
    }
}
