package com.example.pogodaspring.dto;

import java.time.Instant;
import java.util.UUID;

public record SessionDTO(UUID id, String userLogin, Instant expiresAt) {

}
