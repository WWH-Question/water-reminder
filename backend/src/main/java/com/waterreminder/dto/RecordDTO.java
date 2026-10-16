package com.waterreminder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordDTO {
    private Long id;
    private Integer amount;
    private String mode;
    private String recordTime;
    private String recordDate;
}
