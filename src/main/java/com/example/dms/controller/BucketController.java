package com.example.dms.controller;

import com.example.dms.model.Bucket;
import com.example.dms.repository.BucketRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buckets")
public class BucketController {
    private final BucketRepository repo;

    public BucketController(BucketRepository r) {
        this.repo = r;
    }

    @PostMapping
    public Bucket create(@RequestBody Bucket b) {
        return repo.save(b);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bucket> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
