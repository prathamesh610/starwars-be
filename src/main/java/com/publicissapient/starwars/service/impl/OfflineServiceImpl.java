package com.publicissapient.starwars.service.impl;

import com.publicissapient.starwars.constants.SwTypeConstants;
import com.publicissapient.starwars.dto.response.SWResponseDTO;
import com.publicissapient.starwars.exception.ErrorCodes;
import com.publicissapient.starwars.exception.SWException;
import com.publicissapient.starwars.factory.SwComponentModelFactory;
import com.publicissapient.starwars.service.OfflineService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OfflineServiceImpl implements OfflineService {
    private final SwComponentModelFactory swComponentModelFactory;

    public OfflineServiceImpl(SwComponentModelFactory swComponentModelFactory){
        this.swComponentModelFactory = swComponentModelFactory;
    }

    public SWResponseDTO findByName(SwTypeConstants swTypeConstants, String name) throws SWException{
        SWResponseDTO swResponseDTO = new SWResponseDTO();
        swResponseDTO.setType(swTypeConstants.name());
        swResponseDTO.setName(name);

        if(swTypeConstants.equals(SwTypeConstants.Films)){
            swResponseDTO.setFilms(Collections.singletonList(name));
        }else{
            Map<String, List<String>> map = swComponentModelFactory.getSWMap(swTypeConstants);
            swResponseDTO.setFilms(map.get(name));
        }

        if(Objects.isNull(swResponseDTO.getFilms()))
            throw new SWException(ErrorCodes.INVALID_NAME.getErrorMessage());
        swResponseDTO.setCount(swResponseDTO.getFilms().size());

        return swResponseDTO;
    }
}

