package com.twitterclone.tweets.repository;


import com.twitterclone.nodes.tweets.TweetEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends Neo4jRepository<TweetEntity, String> {

    List<TweetEntity> getAllByUserId(String userId);
}
