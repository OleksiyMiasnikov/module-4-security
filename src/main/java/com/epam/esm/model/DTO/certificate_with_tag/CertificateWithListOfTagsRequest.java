package com.epam.esm.model.DTO.certificate_with_tag;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateWithListOfTagsRequest {
    //@NotEmpty(message = "Field 'tag' can not be empty!")
    private Long id;
    private String[] tags;
    @NotEmpty(message = "Field 'name' can not be empty!")
    private String name;
    @NotEmpty(message = "Field 'description' can not be empty!")
    private String description;
    @NotNull(message = "Field 'price' can not be empty!")
    @DecimalMin(value = "0.01", message = "Field 'price' should be more then 0!")
    private Double price;
    @NotNull(message = "Field 'duration' can not be empty!")
    @Min(value = 0, message = "Field 'duration' should be equal or more then 0!")
    private Integer duration;
}
