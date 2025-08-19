package com.example.dms.storage;

import com.example.dms.model.BinaryContent;
import com.example.dms.repository.BinaryContentRepository;
import org.springframework.stereotype.Service;

@Service
public class DatabaseStorageService implements StorageService {
    private final BinaryContentRepository binaryRepo;

    public DatabaseStorageService(BinaryContentRepository binaryRepo) {
        this.binaryRepo = binaryRepo;
    }

    @Override
    public String store(byte[] data, String originalFilename, String contentType) throws Exception {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(data);
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        String hash = sb.toString();
        binaryRepo.findById(hash).orElseGet(() ->
            binaryRepo.save(new BinaryContent(hash, originalFilename, contentType, data))
        );
        return hash;
    }

    @Override
    public byte[] load(String contentHash) throws Exception {
        return binaryRepo.findById(contentHash)
            .map(BinaryContent::getData)
            .orElseThrow(() -> new IllegalArgumentException("Not found"));
    }
}
