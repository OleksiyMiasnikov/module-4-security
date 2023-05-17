package com.epam.esm.service;

import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagRequest;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.CertificateWithTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.CertificateWithTagRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.mapper.CertificateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateWithTagServiceTest {
    @Mock
    CertificateWithTagRepository repo;
    @Mock
    TagRepository tagRepo;
    @Mock
    CertificateRepository certificateRepo;
    @Mock
    CertificateWithTagService subject;
    @Mock
    CertificateMapper certificateMapper;

    private CertificateWithTag certificateWithTag;
    private CertificateWithTagRequest request;
    private Certificate certificate;
    private Tag tag;
    private Pageable pageable;
    private Page<CertificateWithTag> page;

    @BeforeEach
    void setUp() {
        subject = new CertificateWithTagService(repo,
                tagRepo,
                certificateRepo,
                certificateMapper);
        certificateWithTag = CertificateWithTag.builder()
                .tagId(1L)
                .certificateId(1L)
                .build();
        request = CertificateWithTagRequest.builder()
                .tag("tag_name")
                .name("certificate 1")
                .description("description of certificate 1")
                .price(100.50)
                .duration(7)
                .build();
        certificate = Certificate.builder()
                .id(1L)
                .name("certificate 1")
                .description("description of certificate 1")
                .price(100.50)
                .duration(7)
                .build();
        tag = Tag.builder()
                .id(1L)
                .name("tag_name")
                .build();
        pageable = Pageable.ofSize(3).withPage(0);
        page = new PageImpl<>(List.of(certificateWithTag));

    }

    @Test
    void create() {
        when(tagRepo.findByName(request.getTag())).thenReturn(List.of(tag));
        when(certificateRepo.save(any(Certificate.class))).thenReturn(certificate);
        when(repo.save(certificateWithTag)).thenReturn(certificateWithTag);
        when(certificateMapper.toCertificate(any(CertificateWithTagRequest.class)))
                .thenReturn(certificate);

        assertThat(subject.create(request)).isEqualTo(certificateWithTag);

        verify(repo).save(any(CertificateWithTag.class));
    }


    @Test
    void createWithNewTag() {
        Long tagId = 5L;
        Tag tag = Tag.builder()
                .id(tagId)
                .name("new tag")
                .build();

        certificateWithTag.setTagId(tag.getId());

        when(tagRepo.findByName(request.getTag())).thenReturn(new ArrayList<>());
        when(tagRepo.save(any(Tag.class))).thenReturn(tag);
        when(certificateRepo.save(any(Certificate.class))).thenReturn(certificate);
        when(repo.save(certificateWithTag)).thenReturn(certificateWithTag);
        when(certificateMapper.toCertificate(any(CertificateWithTagRequest.class)))
                .thenReturn(certificate);

        assertThat(subject.create(request)).isEqualTo(certificateWithTag);

        verify(repo).save(any(CertificateWithTag.class));
    }

    @Test
    void findAll() {
        Pageable pageable = Pageable.ofSize(3).withPage(0);
        Page<CertificateWithTag> page = new PageImpl<>(List.of(certificateWithTag));

        when(repo.findAll(pageable)).thenReturn(page);

        Page<CertificateWithTag> result = subject.findAll(pageable);

        assertThat(result.get().count()).isEqualTo(1);
        assertThat(result).isEqualTo(page);
    }

    @Test
    void findByTagName() {
        Tag tag2 = Tag.builder()
                .id(4L)
                .name("tag 4")
                .build();

        when(tagRepo.findByName(tag.getName())).thenReturn(List.of(tag));
        when(tagRepo.findByName(tag2.getName())).thenReturn(List.of(tag2));
        when(repo.findByTagIds(List.of(tag.getId(), tag2.getId()), pageable))
                .thenReturn(page);

        List<CertificateWithTag> result = subject
                .findByTagNames(pageable, List.of(tag.getName(), tag2.getName())).stream().toList();

        assertThat(result).isEqualTo(List.of(certificateWithTag));

        verify(tagRepo, times(2)).findByName(any());
        verify(repo).findByTagIds(List.of(tag.getId(), tag2.getId()), pageable);
    }

    @Test
    void findById() {
        when(repo.findById(1L)).thenReturn(Optional.of(certificateWithTag));
        CertificateWithTag result = subject.findById(1L);
        assertThat(result).isEqualTo(certificateWithTag);
    }

    @Test
    void findByIdThrowException() {
        when(repo.findById(10L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> subject.findById(10L))
                .isInstanceOf(ApiEntityNotFoundException.class)
                .hasMessageContaining("Requested certificate with tag was not found (id=10)");
    }

    @Test
    void findByPartOfNameOrDescription() {
        String pattern = "pattern";

        when(certificateRepo.findByNameContaining(pattern)).thenReturn(List.of(certificate));
        when(certificateRepo.findByDescriptionContaining(pattern)).thenReturn(List.of(certificate));
        when(repo.findByCertificateId(List.of(certificate.getId()), pageable)).thenReturn(page);

        assertThat(subject.findByPartOfNameOrDescription(pattern, pageable))
                .isEqualTo(page);

        verify(certificateRepo).findByNameContaining(pattern);
        verify(certificateRepo).findByDescriptionContaining(pattern);
        verify(repo).findByCertificateId(List.of(certificate.getId()), pageable);

    }
}