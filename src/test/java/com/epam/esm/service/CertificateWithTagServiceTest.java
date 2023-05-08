package com.epam.esm.service;

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

import static org.assertj.core.api.Assertions.assertThat;
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

    CertificateWithTag certificateWithTag;
    CertificateWithTagRequest request;

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
    }

    @Test
    void create() {
        Tag tag = Tag.builder()
                .id(1L)
                .name("tag_name")
                .build();
        Certificate certificate = Certificate.builder()
                .id(1L)
                .name("certificate 1")
                .description("description of certificate 1")
                .price(100.50)
                .duration(7)
                .build();

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
        Certificate certificate = Certificate.builder()
                .id(1L)
                .name("certificate 1")
                .description("description of certificate 1")
                .price(100.50)
                .duration(7)
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
        Tag tag1 = Tag.builder()
                .id(2L)
                .name("tag")
                .build();
        Tag tag2 = Tag.builder()
                .id(4L)
                .name("tag 4")
                .build();
        Pageable pageable = Pageable.ofSize(3).withPage(0);
        Page<CertificateWithTag> page = new PageImpl<>(List.of(certificateWithTag));

        when(tagRepo.findByName(tag1.getName())).thenReturn(List.of(tag1));
        when(tagRepo.findByName(tag2.getName())).thenReturn(List.of(tag2));
        when(repo.findByTagIds(List.of(tag1.getId(), tag2.getId()), pageable))
                .thenReturn(page);

        List<CertificateWithTag> result = subject
                .findByTagNames(pageable, List.of(tag1.getName(), tag2.getName())).stream().toList();

        assertThat(result).isEqualTo(List.of(certificateWithTag));

        verify(tagRepo, times(2)).findByName(any());
        verify(repo).findByTagIds(List.of(tag1.getId(), tag2.getId()), pageable);
    }

}