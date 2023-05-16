package com.epam.esm.controller;

import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagDTO;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagRequest;
import com.epam.esm.model.entity.CertificateWithTag;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/certificates_with_tags")
@RequiredArgsConstructor
public class CertificateWithTagController{

    private final CertificateWithTagService service;
    private final CertificateWithTagMapper mapper;

    @PostMapping()
    public CertificateWithTagDTO create(
            @Valid
            @RequestBody CertificateWithTagRequest request) {
        log.info("Creating a new certificate '{}' with tag '{}'.",
                request.getName(),
                request.getTag());
        CertificateWithTagDTO createdDTO = mapper.toDTO(service.create(request));
        createdDTO.add(
                linkTo(methodOn(CertificateWithTagController.class)
                        .findById(createdDTO.getId()))
                        .withSelfRel());
        return createdDTO;
    }

    @GetMapping()
    public Page<CertificateWithTagDTO> findAll(Pageable pageable) {
        log.info("Getting all certificates with tags");
        return service.findAll(pageable).map(mapper::toDTO);
    }

    @GetMapping("/tag")
    public Page<CertificateWithTagDTO> findByTagNames(Pageable pageable,
                                                      @Param("name") String ... name) {
        log.info("Getting all certificates by tag name.");
        return service.findByTagNames(pageable, Arrays.stream(name).toList()).map(mapper::toDTO);
    }

    @GetMapping("/{id}")
    public CertificateWithTagDTO findById(@PathVariable("id") Long id) {
        log.info("Locking for certificate with tag by id: {}.", id);
        return mapper.toDTO(service.findById(id));
    }

    @GetMapping("/search")
    public Page<CertificateWithTagDTO> findByPartOfNameOrDescription(
            Pageable pageable, @Param("pattern") String pattern) {
        log.info("Locking for certificates by part of name or description");
        Page<CertificateWithTag> page = service.findByPartOfNameOrDescription(pattern, pageable);
        return page.map(mapper::toDTO);
    }
}
