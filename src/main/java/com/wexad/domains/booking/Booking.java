package com.wexad.domains.booking;

import com.wexad.domains.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Booking extends BaseDomain {
    private Integer seatNumber;
    private Long cardNumber;
    private UUID userId;
    private UUID showId;
}
