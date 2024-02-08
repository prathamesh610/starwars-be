package com.publicissapient.starwars.factory;

import com.publicissapient.starwars.constants.SwApiConstants;
import com.publicissapient.starwars.constants.SwTypeConstants;
import com.publicissapient.starwars.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.publicissapient.starwars.utility.MasterDataLoader.*;


@Component
public class SwComponentModelFactory {

    public Map<String, List<String>> getSWMap(SwTypeConstants swTypeConstants){
        return switch (swTypeConstants) {
            case Vehicle ->swVehicleMap;
            case People -> swPeoplesMap;
            case Planets -> swPlanetsMap;
            case Species -> swSpeciesMap;
            case Spaceships -> swStarshipMap;
            case Films -> null;
        };
    }

    public SwApiConstants getSwApi(SwTypeConstants swTypeConstants){
        return switch (swTypeConstants) {
            case Films -> SwApiConstants.SW_FILMS_URL;
            case Vehicle ->SwApiConstants.SW_VEHICLE_URL;
            case People -> SwApiConstants.SW_PEOPLE_URL;
            case Planets -> SwApiConstants.SW_PLANETS_URL;
            case Species -> SwApiConstants.SW_SPECIES_URL;
            case Spaceships -> SwApiConstants.SW_STARSHIP_URL;
        };
    }

    public SWBaseModel getSwModel(SwTypeConstants swTypeConstants){
        return  swTypeConstants.equals(SwTypeConstants.Films) ?
             new SWFilms() : new SwGeneric();

    }

}
