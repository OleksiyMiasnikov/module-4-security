package com.epam.esm.controller;

import com.epam.esm.controller.advice.ApiExceptionHandler;
import com.epam.esm.model.DTO.tag.CreateTagRequest;
import com.epam.esm.model.DTO.tag.TagDTO;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.mapper.TagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TagControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Mock
    TagService service;
    @Mock
    TagMapper mapper;
    @InjectMocks
    TagController subject;

    private Tag tag1;
    private Tag tag2;
    private TagDTO tagDto1;
    private TagDTO tagDto2;
    private List<Tag> listOfTwoTags;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        tag1 = Tag.builder().id(1L).name("first tag").build();
        tag2 = Tag.builder().id(2L).name("second tag").build();
        tagDto1 = new ModelMapper().map(tag1, TagDTO.class);
        tagDto2 = new ModelMapper().map(tag2, TagDTO.class);
        listOfTwoTags = new LinkedList<>(List.of(tag1, tag2));
    }

    @Test
    void createTest() throws Exception {
        String jsonCreateTag = "{\n \"name\": \"second tag\"\n}";

        when(service.create(any(CreateTagRequest.class))).thenReturn(tag2);
        when(mapper.toDTO(tag2)).thenReturn(tagDto2);

        this.mockMvc.perform(post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonCreateTag))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tag2.getId()))
                .andExpect(jsonPath("$.name").value(tag2.getName()));

        verify(service).create(any(CreateTagRequest.class));
        verify(mapper).toDTO(tag2);
    }

    @Test
    void findByIdTest() throws Exception {
        when(service.findById(1L)).thenReturn(tag1);
        when(mapper.toDTO(tag1)).thenReturn(tagDto1);

        this.mockMvc.perform(get("/tags/{id}",1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tag1.getId()))
                .andExpect(jsonPath("$.name").value(tag1.getName()));

        verify(service).findById(1L);
        verify(mapper).toDTO(tag1);
    }

    @Test
    void findByNameTest() throws Exception{
        Page<Tag> page = new PageImpl<>(new LinkedList<>(List.of(tag2)));
        Pageable pageable = Pageable.ofSize(3).withPage(0);

        when(service.findByNameWithPageable("first tag", pageable)).thenReturn(page);
        when(mapper.toDTO(tag2)).thenReturn(tagDto2);

        this.mockMvc.perform(get("/tags/tag")
                        .param("name","first tag")
                        .param("page", "0")
                        .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0]").value(tag2));

        verify(service).findByNameWithPageable("first tag", pageable);
        verify(mapper).toDTO(tag2);
    }

    @Test
    void findAllTest() throws Exception {
        Page<Tag> page = new PageImpl<>(listOfTwoTags);
        Pageable pageable = Pageable.ofSize(3).withPage(0);

        when(service.findAllWithPageable(pageable)).thenReturn(page);
        when(mapper.toDTO(tag1)).thenReturn(tagDto1);
        when(mapper.toDTO(tag2)).thenReturn(tagDto2);

        this.mockMvc.perform(get("/tags")
                        .param("page", "0")
                        .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0]").value(tag1))
                .andExpect(jsonPath("$.content[1]").value(tag2));

        verify(service).findAllWithPageable(pageable);
        verify(mapper).toDTO(tag1);
        verify(mapper).toDTO(tag2);
    }

    @Test
    void deleteTest() throws Exception {
        when(service.delete(any(Long.class))).thenReturn(Boolean.TRUE);

        this.mockMvc.perform(delete("/tags/{id}",1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).delete(1L);
    }
}