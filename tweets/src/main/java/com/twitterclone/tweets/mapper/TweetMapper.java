package com.twitterclone.tweets.mapper;

import com.twitterclone.nodes.tweets.TweetEntity;
import com.twitterclone.tweets.model.domain.Tweet;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TweetMapper {

    @Mapping(target = "user", expression = "java(new User(tweetEntity.getUserId()))")
    @Mapping(target = "likedByMe", expression = "java(likedByMe)")
    Tweet toDomain(TweetEntity tweetEntity, @Context Boolean likedByMe);
}
