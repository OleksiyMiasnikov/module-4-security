package com.epam.esm.model.DTO.tag;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private int id;
    private String name;
}
