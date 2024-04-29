package com.twitterclone.tweets.repository;

import com.twitterclone.nodes.tweets.HashtagEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends Neo4jRepository<HashtagEntity, String> {

    Optional<HashtagEntity> findByName(String name);
}
