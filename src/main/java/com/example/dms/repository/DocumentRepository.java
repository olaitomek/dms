package com.example.dms.repository;
import com.example.dms.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    @Query("SELECT d FROM Document d JOIN d.metadata m WHERE d.bucket.id=:bucketId " +
           "AND (:key IS NULL OR KEY(m)=:key) " +
           "AND (:value IS NULL OR m=:value)")
    Page<Document> findByBucketAndMetadata(@Param("bucketId")Long bucketId,
                                           @Param("key")String key,
                                           @Param("value")String value,
                                           Pageable pageable);
    Page<Document> findByBucketId(Long bucketId, Pageable pageable);
}
