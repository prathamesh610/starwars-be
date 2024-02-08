package com.publicissapient.starwars.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SWResponseDTO {
    private String type;
    private Integer count;
    private String name;
    private List<String> films;
}
