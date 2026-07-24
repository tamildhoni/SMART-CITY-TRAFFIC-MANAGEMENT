package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentDto {
    @NotBlank(message = "Title is required")
    private String title;
    private String incidentType;
    private String severity;
    @NotNull(message = "Zone ID is required")
    private Long zoneId;
    private String description;
}