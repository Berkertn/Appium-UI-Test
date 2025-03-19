package org.api.models.conditions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Imperial {
    @JsonProperty("Value")
    private int value;
    @JsonProperty("Unit")
    private String unit;
    @JsonProperty("UnitType")
    private int unitType;
}