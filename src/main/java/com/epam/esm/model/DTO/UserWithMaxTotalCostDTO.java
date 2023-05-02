package com.epam.esm.model.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserWithMaxTotalCostDTO {
    private String user;
    private String tag;
    private Double totalCost;
}
