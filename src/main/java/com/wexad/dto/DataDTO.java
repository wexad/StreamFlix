package com.wexad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataDTO {
    private String theaterName;
    private String theaterLocation;
    private String screenName;
    private LocalDateTime showTime;
    private Double showPrice;
    private String seatNumbers;
}
