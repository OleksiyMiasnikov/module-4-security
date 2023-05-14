package com.epam.esm.service;

import com.epam.esm.model.DTO.tag.CreateTagRequest;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.mapper.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @Mock
    private TagRepository repo;
    @Mock
    private TagMapper mapper;
    private TagService subject;
    private Long id = 1L;
    private final String name = "tag1";
    private Tag tag;
    private Tag tagBeforeSave;
    private CreateTagRequest createTagRequest;

    @BeforeEach
    void setUp(){
        subject = new TagService(repo, mapper);
        tag = Tag.builder()
                .id(id)
                .name(name)
                .build();
        tagBeforeSave = Tag.builder()
                .name(name)
                .build();
        createTagRequest = CreateTagRequest.builder()
                .name(name)
                .build();
    }

    @Test
    void create() {

        when(repo.save(tagBeforeSave)).thenReturn(tag);
        when(mapper.toTag(createTagRequest)).thenReturn(tagBeforeSave);

        Tag result = subject.create(createTagRequest);
        assertThat(result).isEqualTo(tag);
    }

    @Test
    void findAll() {
        Tag tag2 = Tag.builder()
                .id(++id)
                .name("tag2")
                .build();
        when(repo.findAll()).thenReturn(List.of(tag2, tag, tag2));
        List<Tag> result = subject.findByName("");
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).isEqualTo(List.of(tag2, tag, tag2));
    }

    @Test
    void findById() {
        when(repo.findById(id)).thenReturn(Optional.of(tag));
        Tag result = subject.findById(id);
        assertThat(result).isEqualTo(tag);
    }

    @Test
    void findByName() {
        when(repo.findByName(name)).thenReturn(List.of(tag));
        List<Tag> result = subject.findByName(name);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result).isEqualTo(List.of(tag));
    }

    @Test
    void delete() {
        doNothing().when(repo).delete(any(Tag.class));
        when(repo.findById(id)).thenReturn(Optional.of(tag));
        assertThat(subject.delete(id)).isTrue();
    }

    @Test
    void deleteIfTagAbsent() {
        when(repo.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThat(subject.delete(id)).isFalse();
    }
}