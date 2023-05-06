package com.epam.esm.service.mapper;

import com.epam.esm.model.DTO.certificate.CertificateDTO;
import com.epam.esm.model.DTO.certificate.CreateCertificateRequest;
import com.epam.esm.model.DTO.certificate_with_tag.CertificateWithTagRequest;
import com.epam.esm.model.entity.Certificate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CertificateMapper {
    private final ModelMapper apiMapper;

    public Certificate toCertificate(CertificateWithTagRequest request) {
        return apiMapper.map(request, Certificate.class);
    }

    public Certificate toCertificate(CreateCertificateRequest request) {
        return apiMapper.map(request,Certificate.class);
    }

    public CertificateDTO toDTO(Certificate certificate) {
        return apiMapper.map(certificate, CertificateDTO.class);
    }
}
