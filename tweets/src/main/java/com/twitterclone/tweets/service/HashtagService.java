package com.twitterclone.tweets.service;

import com.twitterclone.nodes.tweets.HashtagEntity;
import com.twitterclone.tweets.api.request.PostTweetRequest;
import com.twitterclone.tweets.common.RegExpr;
import com.twitterclone.tweets.domain.Hashtag;
import com.twitterclone.tweets.repository.HashtagOccurrences;
import com.twitterclone.tweets.repository.HashtagRepository;
import com.twitterclone.tweets.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public Set<HashtagEntity> getHashtags(PostTweetRequest request) {
        final List<String> hashtags = StringUtils.getWordsThatStartWithPrefixAndMatchRegex("#", RegExpr.ALPHANUMERIC, request.content());
        return hashtags.stream()
                .map(hashtagString -> {
                    final Optional<HashtagEntity> optionalHashtag = hashtagRepository.findByName(hashtagString);
                    return optionalHashtag.orElseGet(() -> hashtagRepository.save(HashtagEntity.builder().name(hashtagString).build()));
                })
                .collect(Collectors.toSet());
    }

    public Hashtag getHashtag(String hashtag) {
        final Optional<HashtagEntity> optionalHashtag = hashtagRepository.findByName(hashtag);
        return optionalHashtag
            .map(hashtagEntity -> new Hashtag(hashtagEntity.getId(), hashtagEntity.getName()))
            .orElseThrow(() -> new RuntimeException("Hashtag " + hashtag + " not found."));
    }

    public List<HashtagOccurrences> getTopHashtagOccurrences(Integer top) {
        return hashtagRepository.findTopHashtagOccurrences(top);
    }
}
