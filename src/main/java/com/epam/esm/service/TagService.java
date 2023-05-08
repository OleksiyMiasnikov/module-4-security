package com.epam.esm.service;

import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.model.DTO.tag.CreateTagRequest;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *  A service to work with {@link Tag}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagRepository repo;
    private final TagMapper mapper;

    /**
     * Creates a new tag.
     *
     * @param createTagRequest - createTagRequest of new tag.
     * @return {@link Tag} created tag.
     */
    public Tag create(CreateTagRequest createTagRequest) {
        log.info("Creating a new tag.");
        Tag tag = mapper.toTag(createTagRequest);
        return repo.save(tag);
    }

    /**
     * Finds a {@link Tag} by its id.
     *
     * @param id tag id.
     * @return {@link Tag} tag.
     * @throws {@link ApiEntityNotFoundException} if a tag with a given id doesn't exist.
     */
    public Tag findById(Long id) {
        log.info("Getting tag by id: {}.", id);
        Optional<Tag> result = repo.findById(id);
        return result.orElseThrow(() -> new ApiEntityNotFoundException(
                "Requested tag is not found (id=" + id + ")"));
    }

    /**
     * Finds all tags with pageable.
     * Returns all records if name is empty.
     *
     * @return List {@link Tag} List of tags.
     */
    public Page<Tag> findAllWithPageable(Pageable pageable) {
        log.info("Getting all tags.");
        return repo.findAll(pageable);
    }

    /**
     * Finds all {@link Tag} by name with pageable.
     * Returns all records if name is empty.
     *
     * @param name tag name.
     * @return List {@link Tag} List of tags.
     */
    public Page<Tag> findByNameWithPageable(String name, Pageable pageable) {
        log.info("Locking for tag by name: {} with pageable.", name);
        return repo.findByName(name, pageable);
    }

    /**
     * Finds all {@link Tag} by name.
     * Returns all records if name is empty.
     *
     * @param name tag name.
     * @return List {@link Tag} List of tags.
     */
    public List<Tag> findByName(String name) {
        log.info("Locking for tag by name: {}.", name);
        return (name == null || name.isEmpty())
                ? repo.findAll()
                : repo.findByName(name);
    }

    /**
     * Removes a {@link Tag} by its id.
     *
     * @param id tag id
     * @return boolean result of removing tag with appropriate id.
     */
    public boolean delete(Long id) {
        log.info("Deleting tag by id: {}.", id);
        Optional<Tag> deletedTag = repo.findById(id);
        if (deletedTag.isEmpty()) return false;
        repo.delete(deletedTag.get());
        return true;
    }

}
