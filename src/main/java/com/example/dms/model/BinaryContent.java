package com.example.dms.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BinaryContent {
    @Id
    @EqualsAndHashCode.Include
    private String contentHash;
    private String filename;
    private String contentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;
}
