package com.example.dms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

@Entity
@Indexed
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"content", "categories", "tags"})
public class Document {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="bucket_id")
    @IndexedEmbedded()
    private Bucket bucket;

    @ManyToOne(optional=false)
    @JoinColumn(name="content_hash")
    private BinaryContent content;

    private LocalDateTime uploadedAt;

    @ElementCollection
    @CollectionTable(name="document_metadata", joinColumns=@JoinColumn(name="document_id"))
    @MapKeyColumn(name="meta_key")
    @Column(name="meta_value")
    private Map<String,String> metadata = new HashMap<>();

    @ManyToMany
    @JoinTable(
        name="document_category",
        joinColumns=@JoinColumn(name="document_id"),
        inverseJoinColumns=@JoinColumn(name="category_id")
    )
    @IndexedEmbedded()
    private Set<Category> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name="document_tag",
        joinColumns=@JoinColumn(name="document_id"),
        inverseJoinColumns=@JoinColumn(name="tag_id")
    )
    @IndexedEmbedded()
    private Set<Tag> tags = new HashSet<>();

    @FullTextField(analyzer = "standard")
    @Column(columnDefinition = "TEXT")
    private String textContent;

    // getters & setters
}
