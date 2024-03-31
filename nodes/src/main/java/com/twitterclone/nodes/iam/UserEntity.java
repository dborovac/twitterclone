package com.twitterclone.nodes.iam;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

@Node("User")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthday;

    private String handle;

    private String password;

    private Instant createdAt;

    @Relationship(type = "FOLLOWS", direction = Direction.INCOMING)
    private List<UserEntity> followers;

    @Relationship(type = "FOLLOWS", direction = Direction.OUTGOING)
    private List<UserEntity> followees;
}
