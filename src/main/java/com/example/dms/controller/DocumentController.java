package com.example.dms.controller;

import com.example.dms.dto.DocumentDTO;
import com.example.dms.model.Document;
import com.example.dms.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/buckets/{bucketId}/documents")
public class DocumentController {
    private final DocumentService svc;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDTO> upload(@PathVariable Long bucketId,
                                              @RequestParam("file") MultipartFile file,
                                              @RequestParam Map<String, String> metadata,
                                              @RequestParam(required = false) Set<Long> categoryIds,
                                              @RequestParam(required = false) Set<Long> tagIds,
                                              @RequestParam String textContent) throws Exception {
        Document d = svc.upload(bucketId, file.getBytes(), file.getOriginalFilename(),
                file.getContentType(), metadata, categoryIds, tagIds, textContent);
        return ResponseEntity.ok(toDto(d));
    }

    @GetMapping
    public Page<DocumentDTO> list(@PathVariable Long bucketId,
                                  @RequestParam(required = false) String metaKey,
                                  @RequestParam(required = false) String metaValue,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        return svc.list(bucketId, metaKey, metaValue, page, size).map(this::toDto);
    }

    @GetMapping("/search")
    public Page<DocumentDTO> search(@PathVariable Long bucketId,
                                    @RequestParam String q,
                                    @RequestParam(required = false) Set<Long> categoryIds,
                                    @RequestParam(required = false) Set<Long> tagIds,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {
        return svc.search(q, bucketId, categoryIds, tagIds, page, size).map(this::toDto);
    }

    @GetMapping("/{docId}/download")
    public ResponseEntity<ByteArrayResource> download(@PathVariable Long bucketId,
                                                      @PathVariable Long docId) throws Exception {
        Document d = svc.list(bucketId, null, null, 0, Integer.MAX_VALUE).getContent().stream()
                .filter(doc -> doc.getId().equals(docId)).findFirst().orElseThrow();
        byte[] data = svc.loadContent(d);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + d.getContent().getFilename() + "\"")
                .contentType(MediaType.parseMediaType(d.getContent().getContentType()))
                .body(new ByteArrayResource(data));
    }

    private DocumentDTO toDto(Document d) {
        Set<Long> catIds = d.getCategories().stream().map(c -> c.getId()).collect(Collectors.toSet());
        Set<Long> tIds = d.getTags().stream().map(t -> t.getId()).collect(Collectors.toSet());
        return new DocumentDTO(d.getId(),
                d.getBucket().getId(),
                d.getContent().getContentHash(),
                d.getUploadedAt(),
                d.getMetadata(),
                catIds,
                tIds,
                d.getTextContent());
    }
}
