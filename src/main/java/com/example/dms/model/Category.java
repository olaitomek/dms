package com.example.dms.model;

import jakarta.persistence.*;

import java.util.*;

import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"documents"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FullTextField
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Category> subcategories = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    private Set<Document> documents = new HashSet<>();
}
