package com.twitterclone.tweets.repository;


import com.twitterclone.nodes.tweets.TweetEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Instant;

@Repository
public interface TweetRepository extends ReactiveNeo4jRepository<TweetEntity, String> {

    Flux<TweetEntity> getAllByUserIdOrderByPostedAtDesc(String userId);

    @Query("""
            MATCH (user: User {id: $userId})-[:FOLLOWS]->(anotherUser: User)<-[:TWEETED_BY]-(tweet: Tweet)
            WHERE NOT (user)<-[:TWEETED_BY]-(tweet) AND ($cursorTimestamp IS NULL OR tweet.postedAt < $cursorTimestamp)
            RETURN tweet ORDER BY tweet.postedAt DESC LIMIT 2
            """)
    Flux<TweetEntity> getFromFollowees(Instant cursorTimestamp, String userId);
}
