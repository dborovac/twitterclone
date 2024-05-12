package com.twitterclone.tweets.repository;

import com.twitterclone.nodes.tweets.HashtagEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepository extends Neo4jRepository<HashtagEntity, String> {

    Optional<HashtagEntity> findByName(String name);

    @Query("""
            MATCH (tweet:Tweet)-[:CONTAINS_HASHTAG]->(hashtag:Hashtag)
            WHERE datetime(tweet.postedAt) >= datetime() - duration({days: 30})
            WITH toLower(hashtag.name) AS hashtagName, COUNT(tweet) AS occurrences
            ORDER BY occurrences DESC
            RETURN hashtagName, occurrences
            LIMIT 10
            """)
    List<HashtagOccurrences> findTopHashtagOccurrences(Integer top);
}
