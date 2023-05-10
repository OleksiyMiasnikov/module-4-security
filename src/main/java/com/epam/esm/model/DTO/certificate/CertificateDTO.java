package com.epam.esm.model.DTO.certificate;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO extends RepresentationModel<CertificateDTO> {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private String createDate;
    private String lastUpdateDate;

}
