package com.example.dms.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public record DocumentDTO(Long id,
                          Long bucketId,
                          String contentHash,
                          LocalDateTime uploadedAt,
                          Map<String, String> metadata,
                          Set<Long> categoryIds,
                          Set<Long> tagIds,
                          String textContent) {}
