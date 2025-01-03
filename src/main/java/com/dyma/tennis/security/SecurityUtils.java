package com.dyma.tennis.security;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;

public class SecurityUtils {

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public static final String AUTHORITIES_CLAIM_KEY = "auth";
}
