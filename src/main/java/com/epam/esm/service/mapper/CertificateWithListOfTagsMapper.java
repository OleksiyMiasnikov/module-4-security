package com.epam.esm.service.mapper;

import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithListOfTagsDTO;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.CertificateWithTagRepository;
import com.epam.esm.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CertificateWithListOfTagsMapper {

    private final CertificateWithTagRepository certificateWithTagRepository;
    private final TagRepository tagRepository;

    private static final String PATTERN_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public CertificateWithListOfTagsDTO toDTO(Certificate certificate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault());
        List<Long> tagsIds = certificateWithTagRepository.findByCertificateId(certificate.getId());

        return CertificateWithListOfTagsDTO.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .tags(tagRepository.findByIds(tagsIds))
                .description(certificate.getDescription())
                .price(certificate.getPrice())
                .duration(certificate.getDuration())
                .createDate(formatter.format(certificate.getCreateDate()))
                .lastUpdateDate(formatter.format(certificate.getLastUpdateDate()))
                .build();
    }
}
