package com.epam.esm;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.advice.ApiExceptionHandler;
import com.epam.esm.model.DTO.certificate.CertificateDTO;
import com.epam.esm.model.DTO.certificate.CreateCertificateRequest;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.mapper.CertificateMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
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
    private final Certificate certificate1;
    private final Certificate certificate2;
    private final Certificate certificate3;
    private List<Certificate> certificateList = new ArrayList<>();

    {
        certificate1 = Certificate.builder()
                .id(1L)
                .name("certificate 1")
                .description("description of certificate 1")
                .price(15.5)
                .duration(5)
                .build();
        certificate2 = Certificate.builder()
                .id(2L)
                .name("certificate 2")
                .description("description of certificate 2")
                .price(21.0)
                .duration(10)
                .build();
        certificate3 = Certificate.builder()
                .id(3L)
                .name("certificate 3")
                .description("description of certificate 3")
                .price(150d)
                .duration(14)
                .build();

//        URL res = getClass().getClassLoader().getResource("list_of_certificates.json");
//        File file = null;
//        try {
//            file = Paths.get(res.toURI()).toFile();
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//        String absolutePath = file.getAbsolutePath();
//
//        String filePath = getClass()
//                .getClassLoader()
//                .getResourceAsStream("list_of_certificates.json").toString();
//        Gson gson = new Gson();
//        try (Reader reader = new FileReader(absolutePath)) {
//            Type listType = new TypeToken<ArrayList<Certificate>>(){}.getType();
//            certificateList = gson.fromJson(reader, listType);
//
//            System.out.println(certificateList);
//            System.out.println(certificateList.get(0).getDescription());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subject)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    void findAllTest() throws Exception {
        List<Certificate> list = new LinkedList<>(List.of(certificate1, certificate2, certificate3));
        String expected1 = "{\"id\":1," +
                "\"name\":\"certificate 1\"," +
                "\"description\":\"description of certificate 1\"," +
                "\"price\":15.5," +
                "\"duration\":5,";
        String expected2 = "{\"id\":2," +
                "\"name\":\"certificate 2\"," +
                "\"description\":\"description of certificate 2\"," +
                "\"price\":21.0," +
                "\"duration\":10,";
        String expected3 = "{\"id\":3," +
                "\"name\":\"certificate 3\"," +
                "\"description\":\"description of certificate 3\"," +
                "\"price\":150.0," +
                "\"duration\":14,";
        CertificateDTO certificateDto1 = CertificateDTO.builder()
                .id(1L)
                .name("certificate 1")
                .description("description of certificate 1")
                .price(15.5)
                .duration(5)
                .build();
        CertificateDTO certificateDto2 = CertificateDTO.builder()
                .id(2L)
                .name("certificate 2")
                .description("description of certificate 2")
                .price(21.0)
                .duration(10)
                .build();
        CertificateDTO certificateDto3 = CertificateDTO.builder()
                .id(3L)
                .name("certificate 3")
                .description("description of certificate 3")
                .price(150d)
                .duration(14)
                .build();
        Page<Certificate> page = new PageImpl<>(list);
        Pageable pageable = Pageable.ofSize(3).withPage(0);

        when(service.findAll(pageable)).thenReturn(page);
        when(mapper.toDTO(certificate1)).thenReturn(certificateDto1);
        when(mapper.toDTO(certificate2)).thenReturn(certificateDto2);
        when(mapper.toDTO(certificate3)).thenReturn(certificateDto3);

        this.mockMvc.perform(get("/certificates")
                        .param("page", "0")
                        .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expected1)))
                .andExpect(content().string(containsString(expected2)))
                .andExpect(content().string(containsString(expected3)));
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
        CertificateDTO expectedDTO = CertificateDTO.builder()
                .id(1L)
                .name("certificate 1")
                .description("description of certificate 1")
                .price(15.5)
                .duration(5)
                .build();

        when(service.findById(1L)).thenReturn(certificate1);
        when(mapper.toDTO(certificate1)).thenReturn(expectedDTO);

        this.mockMvc.perform(get("/certificates/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(certificate1.getId()))
                .andExpect(jsonPath("$.name").value(certificate1.getName()))
                .andExpect(jsonPath("$.description").value(certificate1.getDescription()))
                .andExpect(jsonPath("$.price").value(certificate1.getPrice()))
                .andExpect(jsonPath("$.duration").value(certificate1.getDuration()));

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
