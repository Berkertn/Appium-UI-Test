package org.api.models.forecast.fiveDaily;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaximumAndMin {
    @JsonProperty("Value")
    private double value;
    @JsonProperty("Unit")
    private String unit;
    @JsonProperty("UnitType")
    private int unitType;
}
