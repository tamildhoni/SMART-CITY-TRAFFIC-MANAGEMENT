package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutageDto {
    private String outageType;
    private String gridType;
    private String severity;
    @NotNull(message = "Grid ID is required")
    private Long gridId;
    private String affectedArea;
    private String description;
}