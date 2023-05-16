package com.epam.esm.model.DTO.user_order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserOrderRequest {
    @NotNull(message = "Field 'userId' can not be empty!")
    @Min(value = 1, message = "Field 'userId' should be more then 0!")
    private Long userId;
    @NotNull(message = "Field 'certificateWithTagId' can not be empty!")
    @Min(value = 1, message = "Field 'certificateWithTagId' should be more then 0!")
    private Long certificateWithTagId;
}
