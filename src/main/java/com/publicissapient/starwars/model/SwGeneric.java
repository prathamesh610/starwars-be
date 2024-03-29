package com.publicissapient.starwars.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SwGeneric extends SWBaseModel{
    @JsonProperty("name")
    private String name;

    @JsonProperty("films")
    private List<String> films;
}
