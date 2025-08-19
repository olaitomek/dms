package com.example.dms.controller;

import com.example.dms.model.Tag;
import com.example.dms.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository repo;

    @PostMapping
    public Tag create(@RequestBody Tag t) {
        return repo.save(t);
    }

    @GetMapping
    public List<Tag> list() {
        return repo.findAll();
    }
}
