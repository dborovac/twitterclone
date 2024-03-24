package com.twitterclone.iam.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegExpr {

    /**
     * Allow letters (uppercase and lowercase).
     * Allow numbers 0 through 9.
     */
    public static final String ALPHANUMERIC = "^[a-zA-Z0-9]*$";

    /**
     * Allow letters (uppercase and lowercase).
     * Optionally allow spaces and hyphens for names like "Mary Jane" or "John-Smith".
     * Do not allow numbers or special characters (such as @, #, $, etc.).
     */
    public static final String PERSONAL_NAME = "^[A-Za-z]+(?:[-\\s][A-Za-z]+)*$";
}
