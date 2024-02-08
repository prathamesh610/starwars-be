package com.publicissapient.starwars.constants;

import lombok.Getter;

@Getter
public enum SwApiConstants {

    SW_FILMS_URL("https://swapi.dev/api/films"),
    SW_PEOPLE_URL("https://swapi.dev/api/people"),
    SW_PLANETS_URL("https://swapi.dev/api/planets"),
    SW_SPECIES_URL("https://swapi.dev/api/species"),
    SW_STARSHIP_URL("https://swapi.dev/api/starships"),
    SW_VEHICLE_URL("https://swapi.dev/api/vehicles"),
    ;

    private final String swApi;

    SwApiConstants(String swApi){
        this.swApi = swApi;
    }

}
