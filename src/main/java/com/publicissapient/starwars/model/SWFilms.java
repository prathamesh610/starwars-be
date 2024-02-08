package com.publicissapient.starwars.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SWFilms extends SWBaseModel{
    @JsonProperty("title")
    private String title;

    @JsonProperty("episode_id")
    private Integer episodeId;

}
