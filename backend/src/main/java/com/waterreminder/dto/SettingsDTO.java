package com.waterreminder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDTO {
    private Integer height;
    private Integer weight;
    private Integer coefficient;
    private String workStart;
    private String workEnd;
    private String sleepTime;
}
