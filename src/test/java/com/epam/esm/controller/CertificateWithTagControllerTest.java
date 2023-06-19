package com.epam.esm.controller;

import com.epam.esm.controller.advice.ApiExceptionHandler;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagDTO;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagRequest;
import com.epam.esm.model.entity.CertificateWithTag;
import com.epam.esm.service.CertificateWithTagService;
import com.epam.esm.service.mapper.CertificateWithTagMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CertificateWithTagControllerTest {

    private MockMvc mockMvc;
    @Mock
    CertificateWithTagService service;
    @Mock
    CertificateWithTagMapper mapper;
    @InjectMocks
    CertificateWithTagController subject;
    private Pageable pageable;
    private CertificateWithTag certificate1;
    private CertificateWithTag certificate3;
    private CertificateWithTagDTO certificateDto1;
    private CertificateWithTagDTO certificateDto3;
    Page<CertificateWithTag> page;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        certificate1 = CertificateWithTag.builder()
                .tagId(1L)
                .certificateId(1L)
                .build();
        certificate3 = CertificateWithTag.builder()
                .tagId(2L)
                .certificateId(3L)
                .build();

        PodamFactory factory = new PodamFactoryImpl();

        certificateDto1 = factory.manufacturePojoWithFullData(CertificateWithTagDTO.class);
        certificateDto3 = factory.manufacturePojoWithFullData(CertificateWithTagDTO.class);

        pageable = Pageable.ofSize(3).withPage(0);
        page = new PageImpl<>(List.of(certificate1, certificate3));
    }
    @Test
    void create() throws Exception {
        String bodyContent = """
                {
                    "tag": "new tag",
                    "name": "new certificate",
                    "description": "description of new certificate",
                    "price": 153.45,
                    "duration": 8
                }""";
        CertificateWithTagDTO expectedDTO = CertificateWithTagDTO.builder()
                .tag("new tag")
                .name("new certificate")
                .description("description of new certificate")
                .price(153.45)
                .duration(8)
                .build();

        when(service.create(any(CertificateWithTagRequest.class)))
                .thenReturn(CertificateWithTag.builder().build());
        when(mapper.toDTO(any(CertificateWithTag.class))).thenReturn(expectedDTO);

        this.mockMvc.perform(post("/certificates_with_tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tag").value(expectedDTO.getTag()))
                .andExpect(jsonPath("$.name").value(expectedDTO.getName()))
                .andExpect(jsonPath("$.description").value(expectedDTO.getDescription()))
                .andExpect(jsonPath("$.price").value(expectedDTO.getPrice()))
                .andExpect(jsonPath("$.duration").value(expectedDTO.getDuration()));

        verify(service).create(any(CertificateWithTagRequest.class));
    }

    //todo
//    @Test
//    void findByPartOfNameOrDescription() throws Exception {
//
//        when(service.findByPartOfNameOrDescription("certificate 1", pageable)).thenReturn(page);
//        when(mapper.toDTO(certificate1)).thenReturn(certificateDto1);
//        when(mapper.toDTO(certificate3)).thenReturn(certificateDto3);
//
//        this.mockMvc.perform(get("/certificates_with_tags/search")
//                        .param("page", "0")
//                        .param("size", "3")
//                        .param("pattern","certificate 1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content", hasSize(2)))
//                .andExpect(jsonPath("$.content[0].id").value(certificateDto1.getId()))
//                .andExpect(jsonPath("$.content[1].id").value(certificateDto3.getId()));
//
//        verify(service).findByPartOfNameOrDescription("certificate 1", pageable);
//    }

    @Test
    void findAll() throws Exception {

        when(service.findAll(pageable)).thenReturn(page);
        when(mapper.toDTO(certificate1)).thenReturn(certificateDto1);
        when(mapper.toDTO(certificate3)).thenReturn(certificateDto3);

        this.mockMvc.perform(get("/certificates_with_tags")
                        .param("page", "0")
                        .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(certificateDto1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(certificateDto3.getId()));

        verify(service).findAll(pageable);
    }

    @Test
    void findByTagNames() throws Exception  {

        when(service.findByTagNames(pageable, List.of("pattern"))).thenReturn(page);
        when(mapper.toDTO(certificate1)).thenReturn(certificateDto1);
        when(mapper.toDTO(certificate3)).thenReturn(certificateDto3);

        this.mockMvc.perform(get("/certificates_with_tags/tag")
                        .param("name", "pattern")
                        .param("page", "0")
                        .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id").value(certificateDto1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(certificateDto3.getId()));

        verify(service).findByTagNames(pageable, List.of("pattern"));
    }

    //todo
//    @Test
//    void findById() throws Exception {
//        when(service.findById(1L)).thenReturn(certificate1);
//        when(mapper.toDTO(certificate1)).thenReturn(certificateDto1);
//
//        this.mockMvc.perform(get("/certificates_with_tags/{id}", 1L))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(certificateDto1.getId()));
//
//        verify(service).findById(1L);
//    }
}