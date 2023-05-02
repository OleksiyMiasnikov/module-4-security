package com.epam.esm.model.DTO.user_order;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserOrderDTO {
    private int id;
    private String userName;
    private int CertificateWithTagId;
    private String tagName;
    private String certificateName;
    private String description;
    private Integer duration;
    private Double cost;
    private String createDate;
}
