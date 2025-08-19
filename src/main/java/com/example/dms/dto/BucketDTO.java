package com.example.dms.dto;

import java.util.Map;

public record BucketDTO(Long id, String name, Map<String, String> metadata) {}
