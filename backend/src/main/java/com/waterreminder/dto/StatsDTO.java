package com.waterreminder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsDTO {
    private Integer drank;
    private Integer target;
    private Integer remaining;
    private Integer progress;
    private Integer suggestedAmount;
    private String mode;
    private Integer countdownSeconds;
}
