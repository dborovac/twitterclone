package com.twitterclone.iam.repository;

import com.twitterclone.nodes.iam.UserEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import reactor.core.publisher.Flux;

public interface ReactiveUserRepository extends ReactiveNeo4jRepository<UserEntity, String> {

    @Query("MATCH (followee: User {id: $userId})<-[:FOLLOWS]-(follower: User) RETURN follower")
    Flux<UserEntity> findFollowersForUser(String userId);
}
