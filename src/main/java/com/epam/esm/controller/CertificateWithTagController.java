package com.epam.esm.controller;

import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithListOfTagsDTO;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithListOfTagsRequest;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagDTO;
import com.epam.esm.service.CertificateWithTagService;
import com.epam.esm.service.mapper.CertificateWithTagMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/certificates_with_tags")
@RequiredArgsConstructor
public class CertificateWithTagController {

    private final CertificateWithTagService service;
    private final CertificateWithTagMapper mapper;

    @PostMapping()
    public CertificateWithListOfTagsDTO create(
            @Valid
            @RequestBody CertificateWithListOfTagsRequest request) {
        log.info("Creating a new certificate '{}.",
                request.getName());
        return service.create(request);
    }

    @GetMapping()
    public Page<CertificateWithListOfTagsDTO> findAll(Pageable pageable) {
        log.info("Getting all certificates with tags");
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public CertificateWithListOfTagsDTO findById(@PathVariable("id") Long id) {
        log.info("Locking for certificate with tag by id: {}.", id);
        return service.findById(id);
    }

    @PatchMapping("/{id}")
    public CertificateWithListOfTagsDTO update(@PathVariable("id") Long id,
                                               @RequestBody CertificateWithListOfTagsRequest request) {
        log.info("Updating certificate with tags by id: {}.", id);
        return service.update(id, request);
    }

    @GetMapping("/search")
    public Page<CertificateWithListOfTagsDTO> findByPartOfNameOrDescription(
            Pageable pageable,
            @Param("pattern") String pattern,
            @Param("tags") String... tags) {
        log.info("Locking for certificates by part of name or description");
        if (tags == null) {
            tags = new String[0];
        }
        return service.findByPartOfNameOrDescription(pattern, tags, pageable);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        log.info("Deleting certificate by id: {}.", id);
        return service.delete(id);
    }

}
