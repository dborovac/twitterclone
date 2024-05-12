package com.twitterclone.iam.repository;

import com.twitterclone.iam.repository.dto.FollowRecommendationDto;
import com.twitterclone.nodes.iam.UserEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends Neo4jRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);

    @Query("MATCH (u: User)<-[:TWEETED_BY]-(t: Tweet) WHERE t.id = $tweetId RETURN u")
    Optional<UserEntity> findByTweetId(String tweetId);

    List<UserEntity> findByHandleContainsIgnoreCase(String handle);

    @Query("MATCH (u: User)<-[:MENTIONS]-(t: Tweet) WHERE t.id = $tweetId RETURN u")
    List<UserEntity> findMentionedInTweet(String tweetId);

    @Query("""
            MATCH (u: User)
            WHERE u.id <> $myUserId AND toLower(u.handle) CONTAINS toLower($searchQuery)
               OR toLower(u.firstName) CONTAINS toLower($searchQuery)
               OR toLower(u.lastName) CONTAINS toLower($searchQuery)
            RETURN u;
            """)
    List<UserEntity> findByHandleOrFirstNameOrLastNameContainsIgnoreCase(String searchQuery, String myUserId);

    @Query("MATCH (u: User)<-[:LIKED_BY]-(t: Tweet) WHERE t.id = $tweetId RETURN u SKIP $offset LIMIT $first")
    Set<UserEntity> findUsersThatLikeThisTweet(String tweetId, Integer first, Integer offset);

    @Query("MATCH (u: User {id: $userId})<-[r:LIKED_BY]-(t: Tweet {id: $tweetId}) RETURN COUNT(r) > 0")
    Boolean hasUserLikedTweet(String userId, String tweetId);

    @Query("MATCH (u1: User { id: $myUserId })-[f: FOLLOWS]->(u2: User { id: $userId }) DETACH DELETE f")
    void deleteFollowsRelationship(String myUserId, String userId);

    @Query("""
            MATCH (me:User {id: $myUserId})-[:FOLLOWS]->(mutual:User)<-[:FOLLOWS]-(other:User)
            WHERE other.id <> $myUserId AND NOT (me)-[:FOLLOWS]->(other)
            WITH other,
                COUNT(mutual) AS mutualFolloweeCount,
                [(me)-[:FOLLOWS]->(f) WHERE (other)-[:FOLLOWS]->(f) | f][..10] AS mutualFollowees,
                [(me)-[:FOLLOWS]->() | 1] AS myFollowing
            RETURN other as recommendation, toFloat(mutualFolloweeCount) / size(myFollowing) AS relevance, mutualFollowees
            ORDER BY relevance DESC
            LIMIT $numberOfRecommendations
            """)
    List<FollowRecommendationDto> findRecommendedUsersToFollow(String myUserId, Integer numberOfRecommendations);
}
