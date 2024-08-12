package com.wexad.dto;

import com.wexad.domains.screen.Screen;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowDTO {
    private Screen screen;
    private Double price;
    private List<LocalDateTime> showTimes;
}
