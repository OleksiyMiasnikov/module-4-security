package com.epam.esm.model.DTO.certificate_with_tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateWithTagDTO extends RepresentationModel<CertificateWithTagDTO> {
    private Long id;
    private String tag;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private String createDate;
    private String lastUpdateDate;
}
