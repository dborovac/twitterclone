package com.twitterclone.nodes.tweets;

import com.twitterclone.nodes.iam.UserEntity;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node("Tweet")
@Data
@Builder
public class TweetEntity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String content;

    private String userId;

    private Instant postedAt;

    @Relationship(type = "TWEETED_BY", direction = Direction.OUTGOING)
    private UserEntity user;

    @Relationship(type = "MENTIONS", direction = Direction.OUTGOING)
    private List<UserEntity> mentions;
}
