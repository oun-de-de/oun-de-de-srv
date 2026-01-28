package com.cdtphuhoi.oun_de_de.common;

public class Constants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN_TYPE = "Bearer";

    public static final String USERNAME_REGEX = "^[a-zA-Z][a-zA-Z0-9_.]{7,35}$";
    public static final String USERNAME_ERROR_MSG = "Username must be 8–36 characters, start with a letter, and contain only letters, numbers, '_' or '.'";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()_+=\\-])[A-Za-z\\d@$!%*?&#^()_+=\\-]{8,}$";
    public static final String PASSWORD_ERROR_MSG = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character (e.g. @, #, $, !). Spaces are not allowed.";

    public static final String GIT_SHA = "GIT_SHA";
    public static final String SWAGGER_SECURITY_SCHEME_NAME = "Authorization";

    public static final int DEFAULT_STRING_FIELD_LENGTH = 255;
    public static final int DEFAULT_DESCRIPTION_FIELD_LENGTH = 1000;
    public static final int DEFAULT_URL_FIELD_LENGTH = 2000;

    public static final String ORG_FILTER_NAME = "orgFilter";
    public static final String ORG_FILTER_PARAM = "orgId";
    public static final String ORG_ID_COLUMN_NAME = "org_id";
    public static final String ORG_MANAGED_INDEX_NAME = "org_index";
}
