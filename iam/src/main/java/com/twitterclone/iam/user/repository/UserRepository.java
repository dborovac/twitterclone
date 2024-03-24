package com.twitterclone.iam.user.repository;

import com.twitterclone.nodes.iam.UserEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);

    @Query("MATCH (u: User)<-[:TWEETED_BY]-(t: Tweet) WHERE t.id = $tweetId RETURN u")
    Optional<UserEntity> findByTweetId(String tweetId);
}
