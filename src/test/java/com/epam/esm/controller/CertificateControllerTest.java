package com.epam.esm.controller;

import com.epam.esm.controller.advice.ApiExceptionHandler;
import com.epam.esm.model.DTO.certificate.CertificateDTO;
import com.epam.esm.model.DTO.certificate.CreateCertificateRequest;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.mapper.CertificateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CertificateControllerTest {

    private MockMvc mockMvc;
    @Mock
    private CertificateMapper mapper;
    @Mock
    CertificateService service;
    @InjectMocks
    CertificateController subject;

    private final List<CertificateDTO> certificateDTOList = new ArrayList<>();
    private List<Certificate> certificateList = new ArrayList<>();
    private String expectedFindAll;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();

        certificateDTOList.add(CertificateDTO.builder()
                .id(1L)
                .name("cert_1 ABC")
                .description("certificate with #1")
                .price(100.0)
                .duration(7)
                .createDate("2023-04-02T14:30:15.000")
                .lastUpdateDate("2023-04-02T14:30:15.000")
                .build());
        certificateDTOList.add(CertificateDTO.builder()
                .id(2L)
                .name("cert_2 DFG")
                .description("certificate with #2 ABC")
                .price(200.0)
                .duration(15)
                .createDate("2023-04-03T14:30:15.000")
                .lastUpdateDate("2023-04-03T14:30:15.000")
                .build());

        expectedFindAll = "[{\"id\":1," +
                "\"name\":\"cert_1 ABC\"," +
                "\"description\":\"certificate with #1\"," +
                "\"price\":100.0," +
                "\"duration\":7," +
                "\"createDate\":\"2023-04-02T14:30:15.000\"," +
                "\"lastUpdateDate\":\"2023-04-02T14:30:15.000\"," +
                "\"links\":[]}," +
                "{\"id\":2," +
                "\"name\":\"cert_2 DFG\"," +
                "\"description\":\"certificate with #2 ABC\"," +
                "\"price\":200.0," +
                "\"duration\":15," +
                "\"createDate\":\"2023-04-03T14:30:15.000\"," +
                "\"lastUpdateDate\":\"2023-04-03T14:30:15.000\"," +
                "\"links\":[]}]";
        certificateList = certificateDTOList.stream()
                .map(dto -> new ModelMapper().map(dto, Certificate.class))
                .toList();
    }

    @Test
    void findAllTest() throws Exception {

        Page<Certificate> page = new PageImpl<>(certificateList);
        Pageable pageable = Pageable.ofSize(4).withPage(0);

        when(service.findAll(pageable)).thenReturn(page);
        when(mapper.toDTO(certificateList.get(0))).thenReturn(certificateDTOList.get(0));
        when(mapper.toDTO(certificateList.get(1))).thenReturn(certificateDTOList.get(1));

        this.mockMvc.perform(get("/certificates")
                        .param("page", "0")
                        .param("size", "4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0]").value(certificateDTOList.get(0)))
                .andExpect(jsonPath("$.content[1]").value(certificateDTOList.get(1)));

        verify(service).findAll(pageable);
    }

    @Test
    void createTest() throws Exception {
        String bodyContent = """
                {
                    "name": "new certificate",
                    "description": "description of new certificate",
                    "price": 153.45,
                    "duration": 8
                }""";
        Certificate expectedCertificate = Certificate.builder()
                .id(5L)
                .name("new certificate")
                .description("description of new certificate")
                .price(153.45)
                .duration(8)
                .build();
        CertificateDTO expectedDTO = CertificateDTO.builder()
                .id(5L)
                .name("new certificate")
                .description("description of new certificate")
                .price(153.45)
                .duration(8)
                .build();

        when(service.create(any(CreateCertificateRequest.class))).thenReturn(expectedCertificate);
        when(mapper.toDTO(any(Certificate.class))).thenReturn(expectedDTO);

        this.mockMvc.perform(post("/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedCertificate.getId()))
                .andExpect(jsonPath("$.name").value(expectedCertificate.getName()))
                .andExpect(jsonPath("$.description").value(expectedCertificate.getDescription()))
                .andExpect(jsonPath("$.price").value(expectedCertificate.getPrice()))
                .andExpect(jsonPath("$.duration").value(expectedCertificate.getDuration()));

        verify(service).create(any(CreateCertificateRequest.class));
    }

    @Test
    void findByIdTest() throws Exception {
        Certificate certificate = certificateList.get(0);

        when(service.findById(1L)).thenReturn(certificate);
        when(mapper.toDTO(certificate)).thenReturn(certificateDTOList.get(0));

        this.mockMvc.perform(get("/certificates/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(certificate.getId()))
                .andExpect(jsonPath("$.name").value(certificate.getName()))
                .andExpect(jsonPath("$.description").value(certificate.getDescription()))
                .andExpect(jsonPath("$.price").value(certificate.getPrice()))
                .andExpect(jsonPath("$.duration").value(certificate.getDuration()));

        verify(service).findById(1L);
    }

    @Test
    void updateTest() throws Exception {
        String bodyContent = """
                {
                    "name": "new name of certificate",
                    "price": 222.45
                }""";
        Certificate expectedCertificate = Certificate.builder()
                .id(1L)
                .name("new name of certificate")
                .description("description of certificate 1")
                .price(222.45)
                .duration(5)
                .build();
        CertificateDTO expectedDTO = CertificateDTO.builder()
                .id(1L)
                .name("new name of certificate")
                .description("description of certificate 1")
                .price(222.45)
                .duration(5)
                .build();
        Certificate updatedCertificate = Certificate.builder()
                .name("new name of certificate")
                .price(222.45)
                .build();

        when(service.update(1L, updatedCertificate)).thenReturn(expectedCertificate);
        when(mapper.toDTO(expectedCertificate)).thenReturn(expectedDTO);

        this.mockMvc.perform(patch("/certificates/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedCertificate.getId()))
                .andExpect(jsonPath("$.name").value(expectedCertificate.getName()))
                .andExpect(jsonPath("$.description").value(expectedCertificate.getDescription()))
                .andExpect(jsonPath("$.price").value(expectedCertificate.getPrice()))
                .andExpect(jsonPath("$.duration").value(expectedCertificate.getDuration()));

        verify(service).update(any(Long.class), any(Certificate.class));
    }

    @Test
    void deleteTest() throws Exception {
        when(service.delete(any(Long.class))).thenReturn(Boolean.TRUE);

        this.mockMvc.perform(delete("/certificates/{id}",1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).delete(1L);
    }
}
