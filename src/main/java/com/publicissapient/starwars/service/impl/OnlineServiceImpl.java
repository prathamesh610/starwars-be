package com.publicissapient.starwars.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.publicissapient.starwars.constants.SwApiConstants;
import com.publicissapient.starwars.constants.SwTypeConstants;
import com.publicissapient.starwars.dto.response.SWResponseDTO;
import com.publicissapient.starwars.exception.ErrorCodes;
import com.publicissapient.starwars.exception.SWException;
import com.publicissapient.starwars.factory.SwComponentModelFactory;
import com.publicissapient.starwars.model.*;
import com.publicissapient.starwars.service.OnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.publicissapient.starwars.exception.ErrorCodes.SW_API_RESPONSE_NULL;

@Service
public class OnlineServiceImpl implements OnlineService {

    Logger logger = LoggerFactory.getLogger(OnlineServiceImpl.class);

    private final SwComponentModelFactory swComponentModelFactory;
    private final RestTemplate restTemplate;

    public OnlineServiceImpl(SwComponentModelFactory swComponentModelFactory, RestTemplate restTemplate){
        this.swComponentModelFactory = swComponentModelFactory;
        this.restTemplate = restTemplate;
    }

    public SWResponseDTO findByName(SwTypeConstants swTypeConstants, String name) throws SWException {
        SWResponseDTO swResponseDTO;

        SwApiConstants swApi = swComponentModelFactory.getSwApi(swTypeConstants);
        SWBaseModel swBaseModel = swComponentModelFactory.getSwModel(swTypeConstants);

         swResponseDTO = getSwModel(swApi, name, swBaseModel);
         swResponseDTO.setType(swTypeConstants.name());

        return swResponseDTO;
    }

    private SWResponseDTO getSwModel(SwApiConstants swApi, String name, SWBaseModel clazz) throws SWException {
        ObjectMapper objectMapper = new ObjectMapper();
        SWResponseDTO swResponseDTO = new SWResponseDTO();

        Object returnValue = findInSwApi(swApi, name);
        clazz = objectMapper.convertValue(returnValue, clazz.getClass());


        if (!SwApiConstants.SW_FILMS_URL.equals(swApi)) {
            List<String> filmNames = new ArrayList<>();
            ((SwGeneric) clazz).getFilms().forEach(film -> {
                try {
                    filmNames.add(findFilmInSwApi(film));
                } catch (SWException ignored) {
                }
            });
            swResponseDTO.setFilms(filmNames);
        } else {
            swResponseDTO.setFilms(Collections.singletonList(((SWFilms) clazz).getTitle()));
        }

        swResponseDTO.setName(name);
        swResponseDTO.setCount(Objects.isNull(swResponseDTO.getFilms()) ? 0 : swResponseDTO.getFilms().size());

        return swResponseDTO;
    }

    private <T> Object findInSwApi(SwApiConstants swApiConstants, String name) throws SWException {
        String url = swApiConstants.getSwApi() + "/?search=" + name;
        Object returnModel;
        ParameterizedTypeReference<SWApiResponse<T>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<SWApiResponse<T>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        SWApiResponse<T> sWApiResponse = responseEntity.getBody();
        if(Objects.nonNull(sWApiResponse) ) {
            if(!sWApiResponse.getResults().isEmpty())
                returnModel = sWApiResponse.getResults().get(0);
            else{
                logger.error("Invalid name entered!!!!");
                throw new SWException(ErrorCodes.INVALID_NAME.getErrorMessage());
            }
        }
        else{
            logger.error("EMPTY RESPONSE!!!!");
           throw new SWException(SW_API_RESPONSE_NULL.getErrorMessage());
        }

        return  returnModel;
    }

    private String findFilmInSwApi(String filmUrl) throws SWException{
        ParameterizedTypeReference<SWFilms> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<SWFilms> swFilmsResponseEntity =  restTemplate.exchange(filmUrl, HttpMethod.GET, null, responseType);
        SWFilms swFilms = swFilmsResponseEntity.getBody();
        if(Objects.nonNull(swFilms))
            return swFilms.getTitle();
        throw new SWException(SW_API_RESPONSE_NULL.getErrorMessage());
    }
}
