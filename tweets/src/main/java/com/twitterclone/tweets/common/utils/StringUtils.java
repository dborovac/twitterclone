package com.twitterclone.tweets.common.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class StringUtils {

    public List<String> getWordsThatStartWith(String prefix, String content) {
        return Arrays.stream(content.split(" "))
                .filter(word -> word.startsWith(prefix))
                .filter(word -> word.length() > 1)
                .toList();
    }

    public List<String> getWordsThatStartWithPrefixAndMatchRegex(String prefix, String regex, String content) {
        return Arrays.stream(content.split(" "))
                .filter(word -> word.startsWith(prefix))
                .filter(word -> word.length() > 1)
                .filter(word -> word.substring(prefix.length()).matches(regex))
                .toList();
    }
}
