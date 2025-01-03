package com.dyma.tennis.model;

public record UserAuthentication(
        String login,
        String token
) {
}