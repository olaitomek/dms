package com.example.dms.dto;

import java.util.Map;

public record BucketCreateDTO(String name, Map<String, String> metadata) {}
