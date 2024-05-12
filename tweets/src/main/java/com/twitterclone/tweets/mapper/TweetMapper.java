package com.twitterclone.tweets.mapper;

import com.twitterclone.nodes.tweets.TweetEntity;
import com.twitterclone.tweets.domain.Tweet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TweetMapper {

    @Mapping(target = "user", expression = "java(new User(tweetEntity.getUserId()))")
    Tweet toDomain(TweetEntity tweetEntity);
}
