package com.epam.esm.service;

import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.model.DTO.certificate.CreateCertificateRequest;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.service.mapper.CertificateMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private CertificateRepository repo;
    @Mock
    private CertificateMapper mapper;

    private CertificateService subject;
    Certificate certificate;
    CreateCertificateRequest certificateRequest;
    Long id = 1L;
    @BeforeEach
    void setUp(){
        subject = new CertificateService(repo, mapper);
        certificate = Certificate.builder()
                .id(id)
                .name("certificate 1")
                .description("description of certificate 1")
                .price(15.5)
                .duration(5)
                .build();
        certificateRequest = CreateCertificateRequest.builder()
                .name("certificate 1")
                .description("description of certificate 1")
                .price(15.5)
                .duration(5)
                .build();
    }

    @Test
    void create() {
        when(repo.save(certificate)).thenReturn(certificate);
        when(mapper.toCertificate(any(CreateCertificateRequest.class))).thenReturn(certificate);

        Certificate result = subject.create(certificateRequest);
        assertThat(result).isEqualTo(certificate);

        verify(mapper).toCertificate(certificateRequest);
    }

    @Test
    void findAll() {
        Certificate certificate2 = Certificate.builder()
                .id(++id)
                .name("certificate 2")
                .description("description of certificate 2")
                .price(25.5)
                .duration(15)
                .build();
        Pageable pageable = Pageable.ofSize(3).withPage(0);
        Page<Certificate> page = new PageImpl<>(List.of(certificate, certificate2));

        when(repo.findAll(pageable)).thenReturn(page);

        Page<Certificate> result = subject.findAll(pageable);

        assertThat(result.get().count()).isEqualTo(2);
        assertThat(result).isEqualTo(page);
    }

    @Test
    void findById() {
        when(repo.findById(id)).thenReturn(Optional.of(certificate));

        Certificate result = subject.findById(id);

        assertThat(result).isEqualTo(certificate);
    }

    @Test
    void findByIdThrowException() {
        Long wrongId = 100L;

        when(repo.findById(wrongId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subject.findById(wrongId))
                .isInstanceOf(ApiEntityNotFoundException.class)
                .hasMessageContaining("Requested certificate was not found (id=" + wrongId + ")");
    }

    @Test
    void updateSomeFields() {
        Certificate certificate2 = Certificate.builder()
                .id(++id)
                .name("certificate 2")
                .price(25.5)
                .build();
        Certificate certificate3 = Certificate.builder()
                .id(id)
                .name("certificate 2")
                .description("description of certificate 1")
                .price(25.5)
                .duration(5)
                .build();

        when(repo.findById(id)).thenReturn(Optional.of(certificate));
        when(repo.save(any(Certificate.class))).thenReturn(certificate3);

        Certificate result = subject.update(id, certificate2);

        verify(repo).save(any(Certificate.class));
        verify(repo).findById(id);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(result.getId()).isEqualTo(certificate3.getId());
        softAssertions.assertThat(result.getName()).isEqualTo(certificate3.getName());
        softAssertions.assertThat(result.getDescription()).isEqualTo(certificate3.getDescription());
        softAssertions.assertThat(result.getPrice()).isEqualTo(certificate3.getPrice());
        softAssertions.assertThat(result.getDuration()).isEqualTo(certificate3.getDuration());
        softAssertions.assertAll();
    }

    @Test
    void updateAnotherFields() {
        Certificate certificate2 = Certificate.builder()
                .id(++id)
                .description("description of certificate 2")
                .duration(15)
                .build();
        Certificate certificate3 = Certificate.builder()
                .id(id)
                .name("certificate 1")
                .description("description of certificate 2")
                .price(15.5)
                .duration(15)
                .build();

        when(repo.findById(id)).thenReturn(Optional.of(certificate));
        when(repo.save(any(Certificate.class))).thenReturn(certificate3);

        Certificate result = subject.update(id, certificate2);

        verify(repo).save(any(Certificate.class));
        verify(repo).findById(id);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(result.getId()).isEqualTo(certificate3.getId());
        softAssertions.assertThat(result.getName()).isEqualTo(certificate3.getName());
        softAssertions.assertThat(result.getDescription()).isEqualTo(certificate3.getDescription());
        softAssertions.assertThat(result.getPrice()).isEqualTo(certificate3.getPrice());
        softAssertions.assertThat(result.getDuration()).isEqualTo(certificate3.getDuration());
        softAssertions.assertAll();
    }

    @Test
    void deleteIfPresent() {
        when(repo.findById(certificate.getId())).thenReturn(Optional.of(certificate));

        assertThat(subject.delete(id)).isTrue();

        verify(repo).delete(certificate);
    }

    @Test
    void deleteIfAbsent() {
        when(repo.findById(certificate.getId())).thenReturn(Optional.empty());

        assertThat(subject.delete(id)).isFalse();

        verify(repo, never()).delete(certificate);
    }
}