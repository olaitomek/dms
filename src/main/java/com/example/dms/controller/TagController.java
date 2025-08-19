package com.example.dms.controller;

import com.example.dms.dto.TagCreateDTO;
import com.example.dms.dto.TagDTO;
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
    public TagDTO create(@RequestBody TagCreateDTO t) {
        Tag entity = new Tag();
        entity.setName(t.name());
        return toDto(repo.save(entity));
    }

    @GetMapping
    public List<TagDTO> list() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    private TagDTO toDto(Tag t) {
        return new TagDTO(t.getId(), t.getName());
    }
}
