package com.epam.esm.config;

import com.epam.esm.model.DTO.certificate.CertificateDTO;
import com.epam.esm.model.DTO.certificate.CreateCertificateRequest;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.util.DateUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Configuration
public class MapperConfig {
    private static final String PATTERN_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    @Bean
    public ModelMapper certificateModelMapper(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
                .withZone(ZoneId.systemDefault());
        ModelMapper mapper = new ModelMapper();

        TypeMap<CreateCertificateRequest, Certificate> propertyRequestToCertificate =
                mapper.createTypeMap(CreateCertificateRequest.class, Certificate.class);
        propertyRequestToCertificate.addMappings(
                m -> m.map((certificateRequest) -> DateUtil.getDate(),
                        Certificate::setCreateDate));
        propertyRequestToCertificate.addMappings(
                m -> m.map((certificateRequest) -> DateUtil.getDate(),
                        Certificate::setLastUpdateDate));

        TypeMap<Certificate, CertificateDTO> propertyCertificateToDTO =
                mapper.createTypeMap(Certificate.class, CertificateDTO.class);
        Converter<Instant, String> formatDate = ctx -> ctx.getSource() != null
                ? formatter.format(ctx.getSource())
                : formatter.format(Instant.EPOCH);
        propertyCertificateToDTO.addMappings(
                m -> m.using(formatDate).map(Certificate::getCreateDate, CertificateDTO::setCreateDate));
        propertyCertificateToDTO.addMappings(
                m -> m.using(formatDate).map(Certificate::getLastUpdateDate, CertificateDTO::setLastUpdateDate));

        return mapper;
    }

}
