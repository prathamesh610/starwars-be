package com.publicissapient.starwars.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.publicissapient.starwars.constants.SwApiConstants;
import com.publicissapient.starwars.model.*;
import com.publicissapient.starwars.service.InitializerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.publicissapient.starwars.utility.MasterDataLoader.*;

@Service
public class InitializerServiceImpl implements InitializerService {

    Logger logger = LoggerFactory.getLogger(InitializerServiceImpl.class);

    private final RestTemplate restTemplate;


    public InitializerServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void loadAllData() {
        logger.info("Started InitializerService.loadAllData()");
        List<SWFilms> swFilmsList =  getSwFilmsList();
        swFilmsList.forEach(swFilms -> swFilmsMap.put(Integer.parseInt(String.valueOf(swFilms.getUrl().charAt(swFilms.getUrl().length()-2))), swFilms.getTitle()));
        logger.info("Films List Population Completed");

        List<SwGeneric> swPeopleList = getSwPeopleList();
        swPeopleList.forEach(swPeople -> swPeoplesMap.putIfAbsent(swPeople.getName(), getFilmsFromSwObject(swPeople)));
        logger.info("People List Population Completed");

        List<SwGeneric> swPlanetsList = getSwPlanetsList();
        swPlanetsList.forEach(swPlanet -> swPlanetsMap.put(swPlanet.getName(), getFilmsFromSwObject(swPlanet)));
        logger.info("Planets List Population Completed");

        List<SwGeneric> swSpeciesList = getSwSpeciesList();
        swSpeciesList.forEach(swSpecies -> swSpeciesMap.put(swSpecies.getName(), getFilmsFromSwObject(swSpecies)));
        logger.info("Species List Population Completed");

        List<SwGeneric> swStarshipsList = getSwStarshipsList();
        swStarshipsList.forEach(swStarship -> swStarshipMap.put(swStarship.getName(), getFilmsFromSwObject(swStarship)));
        logger.info("Starship List Population Completed");

        List<SwGeneric> swVehiclesList = getSwVehiclesList();
        swVehiclesList.forEach(swVehicle -> swVehicleMap.put(swVehicle.getName(), getFilmsFromSwObject(swVehicle)));
        logger.info("Vehicle List Population Completed");

    }

    private List<String> getFilmsFromSwObject(SwGeneric swGeneric){
        List<String> filmsName = new ArrayList<>();
        swGeneric.getFilms().forEach(filmsUrl -> {
            filmsName.add(swFilmsMap.get(Integer.parseInt(String.valueOf(filmsUrl.charAt(filmsUrl.length()-2)))));
        });
        return filmsName;
    }
    private List<SWFilms> getSwFilmsList(){
        List<Object> responseList = getResponse(SwApiConstants.SW_FILMS_URL);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(responseList, new TypeReference<List<SWFilms>>(){});
    }
    private List<SwGeneric> getSwPeopleList(){
        List<Object> responseList = getResponse(SwApiConstants.SW_PEOPLE_URL);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(responseList, new TypeReference<List<SwGeneric>>(){});
    }
    private List<SwGeneric> getSwPlanetsList(){
        List<Object> responseList = getResponse(SwApiConstants.SW_PLANETS_URL);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(responseList, new TypeReference<List<SwGeneric>>(){});
    }

    private List<SwGeneric> getSwSpeciesList(){
        List<Object> responseList = getResponse(SwApiConstants.SW_SPECIES_URL);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(responseList, new TypeReference<List<SwGeneric>>(){});
    }

    private List<SwGeneric> getSwStarshipsList(){
        List<Object> responseList = getResponse(SwApiConstants.SW_STARSHIP_URL);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(responseList, new TypeReference<List<SwGeneric>>(){});
    }

    private List<SwGeneric> getSwVehiclesList(){
        List<Object> responseList = getResponse(SwApiConstants.SW_STARSHIP_URL);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(responseList, new TypeReference<List<SwGeneric>>(){});
    }

    private <T> List<T> getResponse(SwApiConstants url){

        List<T> returnList = new ArrayList<>();
        ParameterizedTypeReference<SWApiResponse<T>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<SWApiResponse<T>> responseEntity = restTemplate.exchange(url.getSwApi(), HttpMethod.GET, null, responseType);
        SWApiResponse<T> sWApiResponse = responseEntity.getBody();
        if(Objects.nonNull(sWApiResponse))
            returnList = sWApiResponse.getResults();
        else{
            logger.error("EMPTY RESPONSE!!!!");
            return returnList;
        }

        int i = 2;

        while(sWApiResponse.getNext() != null){
            String newUrl = urlConstructor(url.getSwApi(), i++);
            responseEntity = restTemplate.exchange(newUrl, HttpMethod.GET, null, responseType);
            sWApiResponse = responseEntity.getBody();
            if(sWApiResponse==null) return returnList;
            returnList.addAll(sWApiResponse.getResults());
        }



        return returnList;
    }

    private String urlConstructor(String url, int pageNumber){
        return url + "?page=" +pageNumber;
    }
}
