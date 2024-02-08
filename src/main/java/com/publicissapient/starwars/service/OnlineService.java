package com.publicissapient.starwars.service;

import com.publicissapient.starwars.constants.SwTypeConstants;
import com.publicissapient.starwars.dto.response.SWResponseDTO;
import com.publicissapient.starwars.exception.SWException;
import com.publicissapient.starwars.model.SWBaseModel;

public interface OnlineService {
    SWResponseDTO findByName(SwTypeConstants swTypeConstants, String name) throws SWException;
}
