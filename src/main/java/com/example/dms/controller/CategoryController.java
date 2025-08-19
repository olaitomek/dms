package com.example.dms.controller;

import com.example.dms.dto.CategoryCreateDTO;
import com.example.dms.dto.CategoryDTO;
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
    public CategoryDTO create(@RequestBody CategoryCreateDTO c) {
        Category entity = new Category();
        entity.setName(c.name());
        if (c.parentId() != null) {
            Category p = repo.findById(c.parentId()).orElseThrow();
            entity.setParent(p);
        }
        return toDto(repo.save(entity));
    }

    @GetMapping
    public List<CategoryDTO> list() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    private CategoryDTO toDto(Category c) {
        Long parentId = c.getParent() != null ? c.getParent().getId() : null;
        return new CategoryDTO(c.getId(), c.getName(), parentId);
    }
}
