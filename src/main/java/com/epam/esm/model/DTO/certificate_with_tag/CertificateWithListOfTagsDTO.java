package com.epam.esm.model.DTO.certificate_with_tag;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateWithListOfTagsDTO extends RepresentationModel<CertificateWithListOfTagsDTO> {
    private Long id;
    private List<String> tags;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private String createDate;
    private String lastUpdateDate;
}
