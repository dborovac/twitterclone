package com.twitterclone.nodes.tweets;

import com.twitterclone.nodes.iam.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.Instant;
import java.util.Set;

@Node("Tweet")
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class TweetEntity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;

    private String content;

    private String userId;

    private Instant postedAt;

    @Relationship(type = "TWEETED_BY", direction = Direction.OUTGOING)
    private UserEntity user;

    @Relationship(type = "MENTIONS", direction = Direction.OUTGOING)
    private Set<UserEntity> mentions;

    @Relationship(type = "LIKED_BY", direction = Direction.OUTGOING)
    private Set<UserEntity> likedBy;

    @Version
    private Long version;
}
