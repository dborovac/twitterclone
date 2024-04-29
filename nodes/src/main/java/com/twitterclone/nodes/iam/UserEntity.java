package com.twitterclone.nodes.iam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Node("User")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class UserEntity {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String id;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthday;

    @ToString.Include
    private String handle;

    private String password;

    private Instant createdAt;

    @Relationship(type = "FOLLOWS", direction = Direction.INCOMING)
    private Set<UserEntity> followers;

    @Relationship(type = "FOLLOWS", direction = Direction.OUTGOING)
    private Set<UserEntity> followees;
}
