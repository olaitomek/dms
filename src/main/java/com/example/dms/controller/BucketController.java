package com.example.dms.controller;

import com.example.dms.dto.BucketCreateDTO;
import com.example.dms.dto.BucketDTO;
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
    public BucketDTO create(@RequestBody BucketCreateDTO dto) {
        Bucket b = new Bucket();
        b.setName(dto.name());
        b.setMetadata(dto.metadata());
        return toDto(repo.save(b));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BucketDTO> get(@PathVariable Long id) {
        return repo.findById(id)
                .map(b -> ResponseEntity.ok(toDto(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    private BucketDTO toDto(Bucket b) {
        return new BucketDTO(b.getId(), b.getName(), b.getMetadata());
    }
}
