package com.epam.esm.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
//    @Mock
//    private TagRepository repo;
//    @Mock
//    private TagMapper mapper;
//    private TagService subject;
//    Tag tag;
//    int id = 1;
//    private final String name = "tag1";
//    @BeforeEach
//    void setUp(){
//        subject = new TagService(repo, mapper);
//        tag = Tag.builder()
//                .id(id)
//                .name(name)
//                .build();
//    }
//
//    @Test
//    void create() {
//
//        when(repo.create(any(Tag.class))).thenReturn(id);
//        when(repo.findById(id)).thenReturn(Optional.of(tag));
//        when(mapper.toTag(any(CreateTagRequest.class))).thenReturn(Tag.builder().name(name).build());
//
//        Tag result = subject.create(CreateTagRequest.builder().name(name).build());
//        assertThat(result).isEqualTo(tag);
//    }
//
//    @Test
//    void findAll() {
//        Tag tag2 = Tag.builder()
//                .id(++id)
//                .name("tag2")
//                .build();
//        when(repo.findAll()).thenReturn(List.of(tag2, tag, tag2));
//        List<Tag> result = subject.findByName("");
//        assertThat(result.size()).isEqualTo(3);
//        assertThat(result).isEqualTo(List.of(tag2, tag, tag2));
//    }
//
//    @Test
//    void findById() {
//        when(repo.findById(id)).thenReturn(Optional.of(tag));
//        Tag result = subject.findById(id);
//        assertThat(result).isEqualTo(tag);
//    }
//
//    @Test
//    void findByName() {
//        when(repo.findByName(name)).thenReturn(List.of(tag));
//        List<Tag> result = subject.findByName(name);
//        assertThat(result.size()).isEqualTo(1);
//        assertThat(result).isEqualTo(List.of(tag));
//    }
//
//    @Test
//    void delete() {
//        when(repo.delete(id)).thenReturn(true);
//        assertThat(subject.delete(id)).isTrue();
//    }
}