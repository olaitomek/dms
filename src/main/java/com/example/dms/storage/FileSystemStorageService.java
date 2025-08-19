package com.example.dms.storage;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

public class FileSystemStorageService implements StorageService {
    @Value("${dms.storage.location}") private String storageLocation;
    private Path rootLocation;

    @PostConstruct
    public void init() throws IOException {
        rootLocation = Paths.get(storageLocation);
        Files.createDirectories(rootLocation);
    }

    @Override
    public String store(byte[] data, String originalFilename, String contentType) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(data);
        StringBuilder sb = new StringBuilder();
        for(byte b : digest) sb.append(String.format("%02x", b));
        String hash = sb.toString();
        Path dest = rootLocation.resolve(hash);
        if(!Files.exists(dest)) Files.write(dest, data);
        return hash;
    }

    @Override
    public byte[] load(String contentHash) throws IOException {
        return Files.readAllBytes(rootLocation.resolve(contentHash));
    }
}
