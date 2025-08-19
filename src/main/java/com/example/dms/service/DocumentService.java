package com.example.dms.service;
import com.example.dms.model.*;
import com.example.dms.repository.*;
import com.example.dms.storage.StorageService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.engine.search.query.SearchResult;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DocumentService {
    private final BucketRepository bucketRepo;
    private final DocumentRepository docRepo;
    private final BinaryContentRepository binRepo;
    private final CategoryRepository catRepo;
    private final TagRepository tagRepo;
    private final StorageService storage;
    private final EntityManager entityManager;

    public DocumentService(BucketRepository bucketRepo,
                           DocumentRepository docRepo,
                           BinaryContentRepository binRepo,
                           CategoryRepository catRepo,
                           TagRepository tagRepo,
                           StorageService storage,
                           EntityManager entityManager) {
        this.bucketRepo = bucketRepo;
        this.docRepo = docRepo;
        this.binRepo = binRepo;
        this.catRepo = catRepo;
        this.tagRepo = tagRepo;
        this.storage = storage;
        this.entityManager = entityManager;
    }

    public Document upload(Long bucketId, byte[] data, String fname, String contentType,
                           Map<String,String> metadata, Set<Long> catIds, Set<Long> tagIds,
                           String textContent) throws Exception {
        Bucket bucket = bucketRepo.findById(bucketId).orElseThrow();
        String hash = storage.store(data, fname, contentType);
        BinaryContent bin = binRepo.findById(hash)
                .orElseThrow();

        Document doc = new Document();
        doc.setBucket(bucket);
        doc.setContent(bin);
        doc.setUploadedAt(LocalDateTime.now());
        doc.setMetadata(metadata);
        if (catIds != null && !catIds.isEmpty()) doc.setCategories(new HashSet<>(catRepo.findAllById(catIds)));
        if (tagIds != null && !tagIds.isEmpty()) doc.setTags(new HashSet<>(tagRepo.findAllById(tagIds)));

        doc.setTextContent(textContent);
        return docRepo.save(doc);
    }

    public Page<Document> list(Long bucketId, String key, String value, int page, int size) {
        Pageable p = PageRequest.of(page, size);
        if (key != null || value != null) return docRepo.findByBucketAndMetadata(bucketId, key, value, p);
        return docRepo.findByBucketId(bucketId, p);
    }

    public Page<Document> search(String query, Long bucketId, Set<Long> catIds, Set<Long> tagIds,
                                 int page, int size) {
        var scope = Search.session(entityManager).search(Document.class);
        var bool = scope.where(f -> f.bool(b -> {
            b.must(f.match().fields("textContent").matching(query));
            b.must(f.match().field("bucket.id").matching(bucketId));
            if (catIds != null && !catIds.isEmpty()) {
                b.must(f.terms().field("categories.id").matchingAny(catIds));
            }
            if (tagIds != null && !tagIds.isEmpty()) {
                b.must(f.terms().field("tags.id").matchingAny(tagIds));
            }
        }));
        SearchResult<Document> result = bool.fetch(page * size, size);
        return new PageImpl<>(result.hits(), PageRequest.of(page, size), result.total().hitCount());
    }

    public byte[] loadContent(Document doc) throws Exception {
        return storage.load(doc.getContent().getContentHash());
    }
}
