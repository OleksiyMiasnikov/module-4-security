package com.epam.esm.model.DTO.tag;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTagRequest {
    @NotEmpty(message = "Field 'name' can not be empty!")
    private String name;
}
