package com.twitterclone.tweets.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegExpr {

    /**
     * Allow letters (uppercase and lowercase).
     * Allow numbers 0 through 9.
     */
    public static final String ALPHANUMERIC = "^[a-zA-Z0-9]*$";
}
