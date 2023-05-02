package com.epam.esm.model.DTO.user_order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateUserOrderRequest {
    @NotNull(message = "Field 'userId' can not be empty!")
    @Min(value = 1, message = "Field 'userId' should be more then 0!")
    private int userId;
    @NotNull(message = "Field 'certificateWithTagId' can not be empty!")
    @Min(value = 1, message = "Field 'certificateWithTagId' should be more then 0!")
    private int certificateWithTagId;
}
