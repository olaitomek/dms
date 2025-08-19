package com.example.dms.controller;

import com.example.dms.model.Category;
import com.example.dms.repository.CategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository repo;

    public CategoryController(CategoryRepository r) {
        this.repo = r;
    }

    @PostMapping
    public Category create(@RequestBody Category c) {
        if (c.getParent() != null && c.getParent().getId() != null) {
            Category p = repo.findById(c.getParent().getId()).orElseThrow();
            c.setParent(p);
        }
        return repo.save(c);
    }

    @GetMapping
    public List<Category> list() {
        return repo.findAll();
    }
}
