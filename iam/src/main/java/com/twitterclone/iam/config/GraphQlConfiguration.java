package com.twitterclone.iam.config;

import com.apollographql.federation.graphqljava.Federation;
import com.apollographql.federation.graphqljava._Entity;
import com.twitterclone.iam.common.model.Tweet;
import graphql.schema.DataFetcher;
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.ClassNameTypeResolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.twitterclone.iam.common.model.Tweet.TWEET_TYPE;

@Configuration
public class GraphQlConfiguration {

    @Bean
    public GraphQlSourceBuilderCustomizer federationTransform() {
        DataFetcher<?> entityDataFetcher = env -> {
            List<Map<String, Object>> representations = env.getArgument(_Entity.argumentName);
            return representations.stream()
                    .map(representation -> {
                        if (TWEET_TYPE.equals(representation.get("__typename"))) {
                            return new Tweet((String) representation.get("id"));
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
