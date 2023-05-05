package com.epam.esm.controller;

import com.epam.esm.model.DTO.tag.CreateTagRequest;
import com.epam.esm.model.DTO.tag.TagDTO;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.TagMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController{
    private final TagService service;
    private final TagMapper mapper;

    @PostMapping()
    public TagDTO create(
            @Valid
            @RequestBody CreateTagRequest request) {
        log.info("Creating tag with name: {}.", request.getName());
        return mapper.toDTO(service.create(request));
    }

    @GetMapping("/{id}")
    public Tag findById(@PathVariable("id") Long id) {
        log.info("Locking for tag by id: {}.", id);
        return service.findById(id);
    }

    @GetMapping()
    public Page<TagDTO> findByName(
            @Valid
            @Param("name") String name,
            Pageable pageable) {
        log.info("Locking for tag by name: {}.", name);
        Page<Tag> page = service.findByNameWithPageable(name, pageable);
        return page.map(mapper::toDTO);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        log.info("Deleting tag by id: {}.", id);
        return service.delete(id);
    }

}
