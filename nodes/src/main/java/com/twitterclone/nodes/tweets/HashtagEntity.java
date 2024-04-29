package com.twitterclone.nodes.tweets;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node("Hashtag")
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class HashtagEntity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;

    @ToString.Include
    private String name;
}
