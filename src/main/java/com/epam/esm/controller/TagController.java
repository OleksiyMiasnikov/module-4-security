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
    public TagDTO findById(@PathVariable("id") Long id) {
        log.info("Locking for tag by id: {}.", id);
        return mapper.toDTO(service.findById(id));
    }

    @GetMapping()
    public Page<TagDTO> findAll(Pageable pageable) {
        log.info("Getting all tags.");
        Page<Tag> page = service.findAllWithPageable(pageable);
        return page.map(mapper::toDTO);
    }

    @GetMapping("/tag")
    public TagDTO findByName(@Valid @Param("name") String name) {
        log.info("Locking for tag by name: {}.", name);
        return mapper.toDTO(service.findByName(name));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        log.info("Deleting tag by id: {}.", id);
        return service.delete(id);
    }

}
