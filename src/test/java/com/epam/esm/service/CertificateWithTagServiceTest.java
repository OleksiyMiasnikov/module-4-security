package com.epam.esm.service;

import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithListOfTagsDTO;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithListOfTagsRequest;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.CertificateWithTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.CertificateWithTagRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.mapper.CertificateMapper;
import com.epam.esm.service.mapper.CertificateWithListOfTagsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateWithTagServiceTest {
    @Mock
    CertificateWithTagRepository repo;
    @Mock
    TagRepository tagRepo;
    @Mock
    CertificateRepository certificateRepo;
    @InjectMocks
    CertificateWithTagService subject;
    @Mock
    CertificateWithListOfTagsMapper mapper;
    @Mock
    CertificateMapper certificateMapper;

    private CertificateWithTag certificateWithTag;
    private CertificateWithListOfTagsRequest request;
    private CertificateWithListOfTagsDTO certificateWithListOfTagsDTO;
    private Certificate certificate;
    private Tag tag;    
    private String[] tags;
    private Pageable pageable;
    private Page<Certificate> page;
    private Page<CertificateWithListOfTagsDTO> pageCWLOT;


    @BeforeEach
    void setUp() {
        subject = new CertificateWithTagService(repo,
                tagRepo,
                certificateRepo,
                certificateMapper,
                mapper);
        certificateWithTag = CertificateWithTag.builder()
                .tagId(1L)
                .certificateId(1L)
                .build();
        certificateWithListOfTagsDTO = CertificateWithListOfTagsDTO.builder()
                .tags(List.of("tag_name"))
                .name("certificate 1")
                .description("description of certificate 1")
                .price(100.50)
                .duration(7)
                .build();
        request = CertificateWithListOfTagsRequest.builder()
                .tags(new String[] {"tag_name"})
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
        pageCWLOT = new PageImpl<>(List.of(certificateWithListOfTagsDTO));
        page = new PageImpl<>(List.of(certificate));

    }


    @Test
    void create() {

        when(certificateMapper.toCertificate(request)).thenReturn(certificate);
        when(certificateRepo.save(certificate)).thenReturn(certificate);
        when(tagRepo.findByName(tag.getName())).thenReturn(Optional.of(tag));
        when(repo.save(CertificateWithTag.builder()
                .tagId(tag.getId())
                .certificateId(certificate.getId())
                .build())).thenReturn(certificateWithTag);

        when(certificateRepo.findById(certificate.getId())).thenReturn(Optional.of(certificate));
        when(mapper.toDTO(certificate)).thenReturn(certificateWithListOfTagsDTO);

        assertThat(subject.create(request)).isEqualTo(certificateWithListOfTagsDTO);

        verify(certificateMapper).toCertificate(request);
        verify(certificateRepo).save(certificate);
        verify(repo).save(CertificateWithTag.builder()
                .tagId(tag.getId())
                .certificateId(certificate.getId())
                .build());
    }

    @Test
    void getAll() {
        Pageable pageable = Pageable.ofSize(3).withPage(0);
        Page<Certificate> page = new PageImpl<>(List.of(certificate));
        when(certificateRepo.findAll(pageable)).thenReturn(page);
        when(mapper.toDTO(certificate)).thenReturn(certificateWithListOfTagsDTO);

        Page<CertificateWithListOfTagsDTO> result = subject.getAll(pageable);

        assertThat(result.get().count()).isEqualTo(1);
        assertThat(result).isEqualTo(pageCWLOT);
    }

    @Test
    void findById() {
        when(certificateRepo.findById(1L)).thenReturn(Optional.of(certificate));
        when(mapper.toDTO(certificate)).thenReturn(certificateWithListOfTagsDTO);
        CertificateWithListOfTagsDTO result = subject.findById(1L);
        assertThat(result).isEqualTo(certificateWithListOfTagsDTO);
    }

    @Test
    void findByIdThrowException() {
        when(certificateRepo.findById(10L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> subject.findById(10L))
                .isInstanceOf(ApiEntityNotFoundException.class)
                .hasMessageContaining("Requested certificate with tags was not found (id=10)");
    }


    @Test
    void findByPartOfNameOrDescription() {
        String pattern = "pattern";
        when(certificateRepo.findByListOfTagsAndPattern(tags, pattern, pageable))
                .thenReturn(page);
        when(mapper.toDTO(certificate)).thenReturn(certificateWithListOfTagsDTO);

        assertThat(subject.findByPartOfNameOrDescription(pattern, tags, pageable))
                .isEqualTo(pageCWLOT);

        verify(certificateRepo).findByListOfTagsAndPattern(tags, pattern, pageable);
        verify(mapper).toDTO(certificate);
    }

    @Test
    void delete() {
        when(certificateRepo.findById(10L)).thenReturn(Optional.of(certificate));

        assertThat(subject.delete(10L)).isTrue();

        verify(certificateRepo).findById(10L);
        verify(certificateRepo).delete(certificate);
    }

    @Test
    void deleteReturnFalse() {
        when(certificateRepo.findById(10L)).thenReturn(Optional.empty());

        assertThat(subject.delete(10L)).isFalse();

        verify(certificateRepo).findById(10L);
        verify(certificateRepo, never()).delete(certificate);
    }

    @Test
    void update() {
        // todo
    }
}