package com.epam.esm;

import com.epam.esm.controller.CertificateWithTagController;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CertificateWithTagControllerTest {

    private MockMvc mockMvc;

    @Mock
    CertificateWithTagService service;
    @Mock
    CertificateWithTagMapper mapper;

    @InjectMocks
    CertificateWithTagController subject;

    private final CertificateWithTag certificate1;
    private final CertificateWithTag certificate3;

    {
        certificate1 = CertificateWithTag.builder()
                .tagId(1L)
                .certificateId(1L)
                .build();
        certificate3 = CertificateWithTag.builder()
                .tagId(2L)
                .certificateId(3L)
                .build();
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
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

    @Test
    void findByPartOfNameOrDescription() throws Exception {
        List<CertificateWithTag> list = new LinkedList<>(List.of(certificate1, certificate3));
        String expected1 = "\"tag\":\"tag_1\"," +
                "\"name\":\"certificate 1\"," +
                "\"description\":\"description of certificate 1\"," +
                "\"price\":15.5," +
                "\"duration\":5,";
        String expected2 = "\"tag\":\"tag_2\"," +
                "\"name\":\"certificate 3\"," +
                "\"description\":\"description of certificate 1\"," +
                "\"price\":150.0," +
                "\"duration\":14,";
        CertificateWithTagDTO certificateDto1 = CertificateWithTagDTO.builder()
                .tag("tag_1")
                .name("certificate 1")
                .description("description of certificate 1")
                .price(15.5)
                .duration(5)
                .build();
        CertificateWithTagDTO certificateDto3 = CertificateWithTagDTO.builder()
                .tag("tag_2")
                .name("certificate 3")
                .description("description of certificate 1")
                .price(150d)
                .duration(14)
                .build();

        when(service.findByPartOfNameOrDescription("certificate 1")).thenReturn(list);
        when(mapper.toDTO(certificate1)).thenReturn(certificateDto1);
        when(mapper.toDTO(certificate3)).thenReturn(certificateDto3);

        this.mockMvc.perform(get("/certificates_with_tags//search/{pattern}",
                        "certificate 1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expected1)))
                .andExpect(content().string(containsString(expected2)));

        verify(service).findByPartOfNameOrDescription("certificate 1");
    }
}