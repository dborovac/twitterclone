package com.twitterclone.tweets.common.repository;

import com.twitterclone.nodes.iam.UserEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends Neo4jRepository<UserEntity, String> {
}
