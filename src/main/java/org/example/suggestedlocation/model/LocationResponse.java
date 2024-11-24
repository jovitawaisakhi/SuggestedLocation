package org.example.suggestedlocation.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationResponse {
    @NotNull
    private String name;
    private Double latitude;
    private Double longitude;
    private Double score;
}
