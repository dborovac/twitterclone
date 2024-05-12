package com.twitterclone.tweets.repository;

import com.twitterclone.nodes.iam.UserEntity;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends Neo4jRepository<UserEntity, String> {

  Optional<UserEntity> findByHandle(String handle);
}
