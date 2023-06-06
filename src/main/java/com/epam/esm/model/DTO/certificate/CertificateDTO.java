package com.epam.esm.model.DTO.certificate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO{
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private String createDate;
    private String lastUpdateDate;

}
