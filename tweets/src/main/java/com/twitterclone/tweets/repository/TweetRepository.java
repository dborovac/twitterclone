package com.twitterclone.tweets.repository;


import com.twitterclone.nodes.tweets.TweetEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends Neo4jRepository<TweetEntity, String> {

    List<TweetEntity> getAllByUserIdOrderByPostedAtDesc(String userId);

    @Query("MATCH (user: User {id: $userId})-[:FOLLOWS]->(anotherUser: User)<-[:TWEETED_BY]-(tweet: Tweet) "
        + "WHERE NOT (user)<-[:TWEETED_BY]-(tweet) RETURN tweet ORDER BY tweet.postedAt DESC LIMIT 10")
    List<TweetEntity> getFromFollowees(String userId);
}
