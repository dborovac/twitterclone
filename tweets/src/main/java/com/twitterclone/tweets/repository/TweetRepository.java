package com.twitterclone.tweets.repository;


import com.twitterclone.nodes.tweets.TweetEntity;
import com.twitterclone.tweets.common.PageRequest;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TweetRepository extends Neo4jRepository<TweetEntity, String> {

    @Query("""
            MATCH (tweet: Tweet {userId: $userId})
            RETURN tweet
            ORDER BY tweet.postedAt DESC
            SKIP $offset
            LIMIT $first
            """)
    List<TweetEntity> getAllByUserIdOrderByPostedAtDesc(Integer first, Integer offset, String userId);

    @Query("""
            MATCH (user: User {id: $userId})-[:FOLLOWS]->(anotherUser: User)<-[:TWEETED_BY]-(tweet: Tweet)
            WHERE NOT (user)<-[:TWEETED_BY]-(tweet)
            WITH tweet
            OPTIONAL MATCH (tweet)-[containsHashtag: CONTAINS_HASHTAG]->(hashtag: Hashtag)
            RETURN tweet, collect(containsHashtag), collect(hashtag)
            ORDER BY tweet.postedAt DESC
            SKIP $offset
            LIMIT $first
            """)
    List<TweetEntity> getFromFollowees(Integer first, Integer offset, String userId);

    List<TweetEntity> getAllByHashtagsNameIgnoreCase(String hashtag);

    Integer countAllByHashtagsNameIgnoreCase(String hashtag);

    @Query("MATCH (:Tweet{id: $tweetId})-[r:LIKED_BY]->() RETURN count(r)")
    Integer countNumberOfLikes(String tweetId);
}
