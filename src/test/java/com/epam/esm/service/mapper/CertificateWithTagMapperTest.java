package com.epam.esm.service.mapper;

import com.epam.esm.exception.ApiEntityNotFoundException;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagDTO;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.CertificateWithTag;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateWithTagMapperTest {
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private TagRepository tagRepository;
    @InjectMocks
    private CertificateWithTagMapper subject;

    @BeforeEach
    void setUp() {
    }

    @Test
    void toDTO() {
        Double price = 10d;
        String tagName = "tag";
        String certificateName = "certificate";
        String description = "";
        int duration = 5;

        CertificateWithTag cwt = CertificateWithTag.builder()
                .id(1L)
                .tagId(2L)
                .certificateId(3L)
                .build();
        Tag tag = Tag.builder()
                .id(2L)
                .name(tagName)
                .build();
        Certificate certificate = Certificate.builder()
                .id(3L)
                .name(certificateName)
                .price(price)
                .description(description)
                .lastUpdateDate(Instant.EPOCH)
                .createDate(Instant.EPOCH)
                .build();
        CertificateWithTagDTO expected = CertificateWithTagDTO.builder()
                .id(1L)
                .price(price)
                .tag(tagName)
                .name(certificateName)
                .description(description)
                .duration(duration)
                .build();

        when(certificateRepository.findById(cwt.getCertificateId()))
                .thenReturn(Optional.of(certificate));
        when(tagRepository.findById(cwt.getTagId()))
                .thenReturn(Optional.of(tag));

        assertThat(subject.toDTO(cwt)).isEqualTo(expected);

        verify(certificateRepository).findById(cwt.getCertificateId());
        verify(tagRepository).findById(cwt.getTagId());
    }

    @Test
    void toDTOThrowException() {
        CertificateWithTag cwt = CertificateWithTag.builder().build();

        when(certificateRepository.findById(cwt.getCertificateId()))
                .thenReturn(Optional.empty());
        when(tagRepository.findById(cwt.getTagId()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> subject.toDTO(cwt))
                .isInstanceOf(ApiEntityNotFoundException.class)
                .hasMessage("Entity is absent");

        verify(certificateRepository).findById(cwt.getCertificateId());
        verify(tagRepository).findById(cwt.getTagId());
    }
}