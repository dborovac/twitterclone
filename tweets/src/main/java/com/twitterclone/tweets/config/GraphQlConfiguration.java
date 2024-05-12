package com.twitterclone.tweets.config;

import com.apollographql.federation.graphqljava.Federation;
import com.apollographql.federation.graphqljava._Entity;
import com.twitterclone.tweets.domain.User;
import graphql.schema.DataFetcher;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.ClassNameTypeResolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class GraphQlConfiguration {

    @Bean
    public GraphQlSourceBuilderCustomizer federationTransform() {
        DataFetcher<?> entityDataFetcher = env -> {
            List<Map<String, Object>> representations = env.getArgument(_Entity.argumentName);
            return representations.stream()
                    .map(representation -> {
                        if (User.USER_TYPE.equals(representation.get("__typename"))) {
                            return new User((String) representation.get("id"));
                        }
                        return null;
                    })
                    .collect(Collectors.toList());
        };

        return builder ->
                builder.schemaFactory((registry, wiring)->
                        Federation.transform(registry, wiring)
                                .fetchEntities(entityDataFetcher)
                                .resolveEntityType(new ClassNameTypeResolver())
                                .build()
                );
    }
}
