package com.hotelsongo.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateDto {

    private Long stateId;

    private String stateCode;

    private String stateDesc;

    private Long countryId;

}
