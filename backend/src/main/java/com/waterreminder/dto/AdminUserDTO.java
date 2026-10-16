package com.waterreminder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDTO {
    private Long id;
    private String username;
    private Integer height;
    private Integer weight;
    private Integer coefficient;
    private String workStart;
    private String workEnd;
    private String sleepTime;
}
