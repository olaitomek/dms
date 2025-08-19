package com.example.dms.storage;
public interface StorageService {
    String store(byte[] data, String originalFilename, String contentType) throws Exception;
    byte[] load(String contentHash) throws Exception;
}
