package com.epam.esm.model.DTO.certificate_with_tag;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Builder
public class CertificateWithTagDTO extends RepresentationModel<CertificateWithTagDTO> {
    private int id;
    private String tag;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private String createDate;
    private String lastUpdateDate;
}
